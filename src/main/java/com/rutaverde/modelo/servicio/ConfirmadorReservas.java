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

    /**
     * Pasos en orden exacto. Hay tres fases con reglas distintas:
     * <ol>
     *   <li>Antes del cobro (cupo, tarjeta, pasarela): si algo falla se avisa al cliente y no se
     *       cobra ni se descuenta ningún cupo.</li>
     *   <li>Cobro aprobado: descontar cupos y registrar. Ya hay plata cobrada, así que si esto falla
     *       el mensaje lo dice claramente en vez de aparentar que no pasó nada.</li>
     *   <li>Voucher y correo: la reserva ya existe; un correo caído no la deshace.</li>
     * </ol>
     */
    public synchronized ResultadoConfirmacion confirmar(Reserva reserva, String numeroTarjeta,
                                                        EmisorVoucher emisorVoucher) {
        if (reserva.estaConfirmada()) {
            // No se escribe al cliente: su reserva sigue vigente.
            return new ResultadoConfirmacion(false, "La reserva ya fue confirmada", reserva, null, false);
        }

        // ---- Fase 1: validar y cobrar ----
        String tarjeta = numeroTarjeta == null ? null : numeroTarjeta.replace(" ", "");
        ResultadoPago pago;
        try {
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
            if (!validadorTarjeta.esValida(tarjeta)) {
                return rechazar(reserva, "El número de tarjeta no es válido");
            }
            // 3. Cobrar en la pasarela
            String referencia = "RES-" + reserva.getCliente().getDocumento()
                    + "-" + reserva.getProducto().getCodigo() + "-" + reserva.getFechaViaje();
            pago = pasarela.cobrar(tarjeta, reserva.getValor(), referencia);
        } catch (RuntimeException e) {
            return rechazar(reserva, "Error inesperado: " + e.getMessage());
        }
        if (!pago.aprobado()) {
            return rechazar(reserva, "Pago rechazado: " + pago.motivo().getMensaje());
        }

        // ---- Fase 2: el cobro ya se hizo ----
        try {
            // 4. Descontar cupos
            cupos.descontar(reserva.getProducto(), reserva.getFechaViaje(), reserva.getViajeros());
            // 5. Registrar con número consecutivo
            reserva.confirmar(consecutivo.siguiente(), pago.autorizacion());
            reservas.agregar(reserva);
        } catch (RuntimeException e) {
            return new ResultadoConfirmacion(false, "El pago fue aprobado (autorización " + pago.autorizacion()
                    + ") pero no se pudo registrar la reserva: " + e.getMessage()
                    + ". Comuníquese con un asesor para resolver el cobro.", reserva, null, false);
        }

        // ---- Fase 3: voucher y correo (la reserva ya quedó registrada) ----
        String voucher = emisorVoucher.emitir(reserva);
        String mensaje = "Reserva confirmada No. " + reserva.getNumero();
        boolean enviado = enviarSinFallar(reserva.getCliente().getCorreo(),
                "Voucher de su reserva No. " + reserva.getNumero(), voucher);
        return new ResultadoConfirmacion(true, mensaje, reserva, voucher, enviado);
    }

    private ResultadoConfirmacion rechazar(Reserva reserva, String motivo) {
        boolean enviado = enviarSinFallar(reserva.getCliente().getCorreo(),
                "No fue posible confirmar su reserva", "Motivo: " + motivo);
        return new ResultadoConfirmacion(false, motivo, reserva, null, enviado);
    }

    /** Un correo que no sale nunca debe tumbar el proceso ni ocultar el resultado real. */
    private boolean enviarSinFallar(String destinatario, String asunto, String cuerpo) {
        try {
            correo.enviar(destinatario, asunto, cuerpo);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
