package com.rutaverde.vista;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class Formatos {
    private Formatos() { }

    public static String pesos(BigDecimal valor) {
        NumberFormat f = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"));
        f.setMaximumFractionDigits(0);
        return f.format(valor);
    }
}
