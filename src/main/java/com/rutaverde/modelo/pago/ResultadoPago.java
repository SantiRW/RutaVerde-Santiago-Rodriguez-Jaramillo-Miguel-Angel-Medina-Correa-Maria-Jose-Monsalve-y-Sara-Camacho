package com.rutaverde.modelo.pago;

public record ResultadoPago(MotivoPago motivo, String autorizacion) {
    public boolean aprobado() { return motivo == MotivoPago.APROBADO; }
}
