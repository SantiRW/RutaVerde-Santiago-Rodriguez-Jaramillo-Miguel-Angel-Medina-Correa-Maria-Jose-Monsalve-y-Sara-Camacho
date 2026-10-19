package com.rutaverde.modelo.voucher;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

final class Formato {
    private Formato() { }

    static String pesos(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).format(valor);
    }
}
