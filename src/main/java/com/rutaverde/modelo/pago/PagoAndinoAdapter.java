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

    @Override
    public ResultadoPago cobrar(String numeroTarjeta, BigDecimal valorPesos, String referencia) {
        long centavos = valorPesos.setScale(0, RoundingMode.HALF_UP).movePointRight(2).longValueExact();
        int codigo = api.procesarCobro(numeroTarjeta, centavos, referencia);
        MotivoPago motivo = traducir(codigo);
        String autorizacion = motivo == MotivoPago.APROBADO ? api.ultimaAutorizacion() : null;
        return new ResultadoPago(motivo, autorizacion);
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
