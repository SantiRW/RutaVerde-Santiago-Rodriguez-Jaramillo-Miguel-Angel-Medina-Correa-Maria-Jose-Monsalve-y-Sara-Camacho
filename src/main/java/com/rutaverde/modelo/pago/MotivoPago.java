package com.rutaverde.modelo.pago;

/** Resultado del cobro expresado en los términos de la agencia (no de la pasarela). */
public enum MotivoPago {
    APROBADO("Pago aprobado"),
    FONDOS_INSUFICIENTES("Fondos insuficientes"),
    TARJETA_INVALIDA("Tarjeta inválida"),
    PASARELA_NO_DISPONIBLE("Pasarela de pagos no disponible"),
    ERROR_DESCONOCIDO("Error desconocido de la pasarela");

    private final String mensaje;

    MotivoPago(String mensaje) { this.mensaje = mensaje; }

    public String getMensaje() { return mensaje; }
}
