package com.rutaverde.modelo.servicio;

import com.rutaverde.modelo.dominio.Actividad;
import com.rutaverde.modelo.dominio.Reserva;
import com.rutaverde.modelo.pago.PasarelaPago;
import com.rutaverde.modelo.pago.ResultadoPago;
import com.rutaverde.modelo.pago.ValidadorTarjeta;
import com.rutaverde.modelo.repositorio.GeneradorConsecutivo;
import com.rutaverde.modelo.repositorio.Repositorio;
import com.rutaverde.modelo.voucher.EmisorVoucher;

/**
 * PATRÓN FACADE (estructural): una sola entrada para el proceso de confirmación.
 * La pantalla del asesor, el kiosco y la app móvil llaman a {@link #confirmar} y no
 * conocen ni repiten los pasos. Todas las dependencias son abstracciones (DIP).
 */
public class ConfirmadorReservas {
    private final ControlCupos cupos;
    private final ValidadorTarjeta validadorTarjeta;
    private final PasarelaPago pasarela;
    private final Repositorio<Reserva, Integer> reservas;
    private final GeneradorConsecutivo consecutivo;
    private final ServicioCorreo correo;

    public ConfirmadorReservas(ControlCupos cupos, ValidadorTarjeta validadorTarjeta,
                               PasarelaPago pasarela, Repositorio<Reserva, Integer> reservas,
                               GeneradorConsecutivo consecutivo, ServicioCorreo correo) {
        this.cupos = cupos;
        this.validadorTarjeta = validadorTarjeta;
        this.pasarela = pasarela;
        this.reservas = reservas;
        this.consecutivo = consecutivo;
        this.correo = correo;
    }

    /** Pasos en orden exacto; si uno falla se avisa al cliente y no se descuenta ningún cupo. */
    public synchronized ResultadoConfirmacion confirmar(Reserva reserva, String numeroTarjeta,
                                                        EmisorVoucher emisorVoucher) {
        try {
            if (reserva.estaConfirmada()) {
                return rechazar(reserva, "La reserva ya fue confirmada");
            }
            // 1. Verificar cupo (y que ninguna actividad haya sido suspendida entretanto)
            for (Actividad a : reserva.getProducto().getActividades()) {
                if (a.estaSuspendida()) {
                    return rechazar(reserva, "La actividad '" + a.getNombre() + "' está suspendida");
                }
            }
            if (!cupos.hayCupo(reserva.getProducto(), reserva.getFechaViaje(), reserva.getViajeros())) {
                return rechazar(reserva, "No hay cupo suficiente en la fecha del viaje");
            }
            // 2. Validar tarjeta
            if (!validadorTarjeta.esValida(numeroTarjeta)) {
                return rechazar(reserva, "El número de tarjeta no es válido");
            }
            // 3. Cobrar en la pasarela
            String referencia = "RES-" + reserva.getCliente().getDocumento() + "-" + reserva.getFechaViaje();
            ResultadoPago pago = pasarela.cobrar(numeroTarjeta, reserva.getValor(), referencia);
            if (!pago.aprobado()) {
                return rechazar(reserva, "Pago rechazado: " + pago.motivo().getMensaje());
            }
            // 4. Descontar cupos
            cupos.descontar(reserva.getProducto(), reserva.getFechaViaje(), reserva.getViajeros());
            // 5. Registrar con número consecutivo
            reserva.confirmar(consecutivo.siguiente(), pago.autorizacion());
            reservas.agregar(reserva);
            // 6. Emitir voucher
            String voucher = emisorVoucher.emitir(reserva);
            // 7. Enviar por correo
            correo.enviar(reserva.getCliente().getCorreo(),
                    "Voucher de su reserva No. " + reserva.getNumero(), voucher);
            return new ResultadoConfirmacion(true, "Reserva confirmada No. " + reserva.getNumero(), reserva, voucher);
        } catch (RuntimeException e) {
            return rechazar(reserva, "Error inesperado: " + e.getMessage());
        }
    }

    private ResultadoConfirmacion rechazar(Reserva reserva, String motivo) {
        correo.enviar(reserva.getCliente().getCorreo(),
                "No fue posible confirmar su reserva", "Motivo: " + motivo);
        return new ResultadoConfirmacion(false, motivo, reserva, null);
    }
}
