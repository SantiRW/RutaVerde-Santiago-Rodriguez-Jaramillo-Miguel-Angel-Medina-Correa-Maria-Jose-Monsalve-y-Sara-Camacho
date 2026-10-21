package com.rutaverde.modelo.dominio;

import com.rutaverde.modelo.excepcion.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaqueteCompositeTest {

    private Actividad actividad(String codigo, BigDecimal precio, int horas) {
        return new Actividad(codigo, "Actividad " + codigo, "Salento", horas, precio, 10,
                Dificultad.BAJA, EstadoActividad.DISPONIBLE);
    }

    @Test
    void precioYDuracionSumanElContenidoConElDescuentoPropio() {
        Paquete paquete = new Paquete("P1", "Paquete", BigDecimal.TEN);
        paquete.agregar(actividad("A1", BigDecimal.valueOf(100_000), 3));
        paquete.agregar(actividad("A2", BigDecimal.valueOf(50_000), 2));

        assertEquals(0, new BigDecimal("135000.00").compareTo(paquete.getPrecioPorPersona()));
        assertEquals(5, paquete.getDuracionHoras());
    }

    @Test
    void unPaqueteNoPuedeContenerseASiMismoDirectamente() {
        Paquete paquete = new Paquete("P1", "Paquete", BigDecimal.ZERO);
        assertThrows(ReglaNegocioException.class, () -> paquete.agregar(paquete));
    }

    @Test
    void unPaqueteNoPuedeContenerseASiMismoIndirectamente() {
        Paquete abuelo = new Paquete("P1", "Abuelo", BigDecimal.ZERO);
        Paquete padre = new Paquete("P2", "Padre", BigDecimal.ZERO);
        abuelo.agregar(padre);

        assertThrows(ReglaNegocioException.class, () -> padre.agregar(abuelo));
    }

    @Test
    void unMismoPlanNoPuedeIncluirDosVecesLaMismaActividad() {
        Actividad aves = actividad("A4", BigDecimal.valueOf(70_000), 4);
        Paquete salento = new Paquete("P1", "Salento", BigDecimal.ZERO);
        salento.agregar(aves);

        Paquete total = new Paquete("P3", "Total", BigDecimal.ZERO);
        total.agregar(salento);

        assertThrows(ReglaNegocioException.class, () -> total.agregar(aves));
    }
}
