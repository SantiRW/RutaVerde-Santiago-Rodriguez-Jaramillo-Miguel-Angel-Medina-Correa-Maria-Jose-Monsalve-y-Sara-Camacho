package com.rutaverde.modelo.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraValorReservaTest {

    private final CalculadoraValorReserva calculadora = new CalculadoraValorReserva();

    @Test
    void unDiaDeGuiaCubreHastaOchoHoras() {
        Actividad actividad = new Actividad("A1", "Cocora", "Salento", 8, BigDecimal.valueOf(80_000), 10,
                Dificultad.MEDIA, EstadoActividad.DISPONIBLE);
        assertEquals(1, calculadora.diasDeGuia(actividad));
    }

    @Test
    void siElProductoDuraMasDeOchoHorasSeCobranDiasAdicionales() {
        Actividad actividad = new Actividad("A1", "Cocora", "Salento", 9, BigDecimal.valueOf(80_000), 10,
                Dificultad.MEDIA, EstadoActividad.DISPONIBLE);
        assertEquals(2, calculadora.diasDeGuia(actividad));
    }

    @Test
    void elValorIncluyePrecioPorPersonaPorViajerosMasServiciosOpcionales() {
        Actividad actividad = new Actividad("A1", "Cocora", "Salento", 5, BigDecimal.valueOf(80_000), 10,
                Dificultad.MEDIA, EstadoActividad.DISPONIBLE);
        Guia guia = new Guia("G1", "Carlos Ríos", List.of("Español"), "3100000000", BigDecimal.valueOf(150_000));

        BigDecimal valor = calculadora.calcular(actividad, 2, guia, true, true);

        // 2 viajeros * 80.000 + transporte(2*35.000) + seguro(2*15.000) + 1 día de guía (150.000)
        assertEquals(0, new BigDecimal("410000").compareTo(valor));
    }
}
