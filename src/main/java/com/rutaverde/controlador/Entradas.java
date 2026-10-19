package com.rutaverde.controlador;

import com.rutaverde.modelo.excepcion.ReglaNegocioException;

import java.math.BigDecimal;

/** Conversión de los textos de los formularios a números, con mensajes claros. */
final class Entradas {
    private Entradas() { }

    static int entero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (RuntimeException e) {
            throw new ReglaNegocioException("El campo '" + campo + "' debe ser un número entero");
        }
    }

    static BigDecimal decimal(String texto, String campo) {
        try {
            return new BigDecimal(texto.trim());
        } catch (RuntimeException e) {
            throw new ReglaNegocioException("El campo '" + campo + "' debe ser un número válido");
        }
    }
}
