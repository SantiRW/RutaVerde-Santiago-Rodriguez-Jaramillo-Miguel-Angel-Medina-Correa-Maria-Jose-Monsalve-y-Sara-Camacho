package com.rutaverde.modelo.pago;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * PATRÓN ADAPTER (estructural): adapta PagoAndinoAPI (centavos, códigos numéricos)
 * a PasarelaPago (pesos, MotivoPago) sin modificar la clase del proveedor.
 */
public class PagoAndinoAdapter implements PasarelaPago {
    private final PagoAndinoAPI api;

    public PagoAndinoAdapter(PagoAndinoAPI api) { this.api = api; }

    /**
     * la API del proveedor devuelve la última autorización, así que cobrar y
     * leerla debe ser una sola operación para que dos cobros no se crucen.
     */
    @Override
    public synchronized ResultadoPago cobrar(String numeroTarjeta, BigDecimal valorPesos, String referencia) {
        long centavos = valorPesos.setScale(0, RoundingMode.HALF_UP).movePointRight(2).longValueExact();
        try {
            int codigo = api.procesarCobro(numeroTarjeta, centavos, referencia);
            MotivoPago motivo = traducir(codigo);
            String autorizacion = motivo == MotivoPago.APROBADO ? api.ultimaAutorizacion() : null;
            return new ResultadoPago(motivo, autorizacion);
        } catch (RuntimeException e) {
            // Un fallo de red o del proveedor no debe salir como excepción del dominio.
            return new ResultadoPago(MotivoPago.PASARELA_NO_DISPONIBLE, null);
        }
    }

    private MotivoPago traducir(int codigo) {
        return switch (codigo) {
            case 0 -> MotivoPago.APROBADO;
            case 51 -> MotivoPago.FONDOS_INSUFICIENTES;
            case 14 -> MotivoPago.TARJETA_INVALIDA;
            case 91 -> MotivoPago.PASARELA_NO_DISPONIBLE;
            default -> MotivoPago.ERROR_DESCONOCIDO;
        };
    }
}
