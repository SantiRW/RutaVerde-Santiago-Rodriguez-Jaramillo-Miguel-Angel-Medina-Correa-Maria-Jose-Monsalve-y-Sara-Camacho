package com.rutaverde.modelo.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Única responsabilidad: calcular el valor (en pesos) de una reserva. */
public class CalculadoraValorReserva {
    public static final BigDecimal TRANSPORTE_POR_VIAJERO = BigDecimal.valueOf(35_000);
    public static final BigDecimal SEGURO_POR_VIAJERO = BigDecimal.valueOf(15_000);
    public static final int HORAS_POR_DIA_GUIA = 8;

    public BigDecimal calcular(Producto producto, int viajeros, Guia guia,
                               boolean transporte, boolean seguro) {
        BigDecimal n = BigDecimal.valueOf(viajeros);
        BigDecimal total = producto.getPrecioPorPersona().multiply(n);
        if (transporte) total = total.add(TRANSPORTE_POR_VIAJERO.multiply(n));
        if (seguro) total = total.add(SEGURO_POR_VIAJERO.multiply(n));
        if (guia != null) {
            total = total.add(guia.getTarifaPorDia().multiply(BigDecimal.valueOf(diasDeGuia(producto))));
        }
        return total.setScale(0, RoundingMode.HALF_UP);
    }

    /** Un día cubre hasta 8 horas; si dura más se cobran los días adicionales necesarios. */
    public int diasDeGuia(Producto producto) {
        int horas = producto.getDuracionHoras();
        return Math.max(1, (horas + HORAS_POR_DIA_GUIA - 1) / HORAS_POR_DIA_GUIA);
    }
}
