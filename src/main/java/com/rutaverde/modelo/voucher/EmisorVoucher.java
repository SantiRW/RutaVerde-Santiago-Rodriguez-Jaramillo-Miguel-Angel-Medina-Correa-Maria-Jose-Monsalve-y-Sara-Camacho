package com.rutaverde.modelo.voucher;

import com.rutaverde.modelo.dominio.Agencia;
import com.rutaverde.modelo.dominio.Reserva;


public abstract class EmisorVoucher {
    private static final String POLITICA =
            "Política de cancelación: sin costo hasta 48 horas antes del viaje.";

    private final Agencia agencia;

    protected EmisorVoucher(Agencia agencia) { this.agencia = agencia; }

    public final String emitir(Reserva reserva) {
        return encabezado() + System.lineSeparator()
                + crearDetalle().generar(reserva) + System.lineSeparator()
                + POLITICA;
    }

    private String encabezado() {
        return agencia.getNombreComercial() + " - RNT " + agencia.getRnt();
    }

    /** Factory Method. */
    protected abstract DetalleVoucher crearDetalle();
}
