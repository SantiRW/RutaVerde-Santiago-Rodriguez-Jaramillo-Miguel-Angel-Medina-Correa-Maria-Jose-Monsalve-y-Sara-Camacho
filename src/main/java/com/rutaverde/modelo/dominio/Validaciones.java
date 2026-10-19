package com.rutaverde.modelo.dominio;

import com.rutaverde.modelo.excepcion.ReglaNegocioException;

import java.math.BigDecimal;

/** Utilidades de validación compartidas por las entidades (evita repetir código). */
final class Validaciones {
    private Validaciones() { }

    static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ReglaNegocioException("El campo '" + campo + "' es obligatorio");
        }
        return valor.trim();
    }

    static String correo(String valor) {
        String correo = texto(valor, "correo electrónico");
        if (!correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ReglaNegocioException("El correo electrónico no es válido");
        }
        return correo;
    }

    static int enteroPositivo(int valor, String campo) {
        if (valor <= 0) {
            throw new ReglaNegocioException("El campo '" + campo + "' debe ser mayor que cero");
        }
        return valor;
    }

    static BigDecimal dineroNoNegativo(BigDecimal valor, String campo) {
        if (valor == null || valor.signum() < 0) {
            throw new ReglaNegocioException("El campo '" + campo + "' no puede ser negativo");
        }
        return valor;
    }
}
