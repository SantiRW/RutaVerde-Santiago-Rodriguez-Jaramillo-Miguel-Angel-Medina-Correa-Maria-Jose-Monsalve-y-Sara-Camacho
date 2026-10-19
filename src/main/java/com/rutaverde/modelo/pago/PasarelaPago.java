package com.rutaverde.modelo.pago;

import java.math.BigDecimal;

/**
 * Contrato propio de la agencia: valores en pesos y resultado en sus términos.
 * La segunda pasarela del próximo semestre solo necesitará otro adaptador.
 */
public interface PasarelaPago {
    ResultadoPago cobrar(String numeroTarjeta, BigDecimal valorPesos, String referencia);
}
