package com.rutaverde.modelo.pago;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LuhnValidadorTest {

    private final LuhnValidador validador = new LuhnValidador();

    @Test
    void numeroValidoSegunElEjemploDelEnunciado() {
        assertTrue(validador.esValida("4539 1488 0343 6467"));
    }

    @Test
    void numeroInvalidoSegunElEjemploDelEnunciado() {
        assertFalse(validador.esValida("4539 1488 0343 6468"));
    }

    @Test
    void rechazaNumerosConMenosDeTreceDigitos() {
        assertFalse(validador.esValida("123456789012"));
    }

    @Test
    void rechazaNumerosConMasDeDiecinueveDigitos() {
        assertFalse(validador.esValida("12345678901234567890"));
    }

    @Test
    void rechazaCaracteresNoNumericos() {
        assertFalse(validador.esValida("4539 1488 0343 64X7"));
    }

    @Test
    void rechazaNulo() {
        assertFalse(validador.esValida(null));
    }
}
