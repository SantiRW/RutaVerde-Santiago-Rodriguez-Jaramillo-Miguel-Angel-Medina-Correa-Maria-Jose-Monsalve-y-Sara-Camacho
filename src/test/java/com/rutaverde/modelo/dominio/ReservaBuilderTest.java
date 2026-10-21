package com.rutaverde.modelo.dominio;

import com.rutaverde.modelo.excepcion.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservaBuilderTest {

    private static final Clock HOY = Clock.fixed(Instant.parse("2026-10-20T00:00:00Z"), ZoneOffset.UTC);

    private ReservaBuilder nuevoBuilder() {
        return new ReservaBuilder(new CalculadoraValorReserva(), HOY);
    }

    private Cliente cliente(String idioma) {
        return new Cliente("1094", "Ana Gómez", "3115550000", "ana@correo.com", "Colombia", idioma);
    }

    private Actividad actividadDisponible() {
        return new Actividad("A1", "Cocora", "Salento", 5, BigDecimal.valueOf(80_000), 10,
                Dificultad.MEDIA, EstadoActividad.DISPONIBLE);
    }

    @Test
    void noPuedeExistirUnaReservaSinCliente() {
        assertThrows(ReglaNegocioException.class, () -> nuevoBuilder()
                .producto(actividadDisponible())
                .fechaViaje(LocalDate.of(2026, 11, 1))
                .viajeros(2)
                .build());
    }

    @Test
    void noPuedeExistirUnaReservaSinProducto() {
        assertThrows(ReglaNegocioException.class, () -> nuevoBuilder()
                .cliente(cliente("Español"))
                .fechaViaje(LocalDate.of(2026, 11, 1))
                .viajeros(2)
                .build());
    }

    @Test
    void laFechaDelViajeNoPuedeEstarEnElPasado() {
        assertThrows(ReglaNegocioException.class, () -> nuevoBuilder()
                .cliente(cliente("Español"))
                .producto(actividadDisponible())
                .fechaViaje(LocalDate.of(2026, 10, 19))
                .viajeros(2)
                .build());
    }

    @Test
    void elGrupoDebeTenerEntreUnoYQuinceViajeros() {
        assertThrows(ReglaNegocioException.class, () -> nuevoBuilder()
                .cliente(cliente("Español"))
                .producto(actividadDisponible())
                .fechaViaje(LocalDate.of(2026, 11, 1))
                .viajeros(16)
                .build());
    }

    @Test
    void noSePuedeReservarUnProductoConUnaActividadSuspendida() {
        Actividad suspendida = new Actividad("A5", "Rafting", "Armenia", 2, BigDecimal.valueOf(90_000), 10,
                Dificultad.ALTA, EstadoActividad.SUSPENDIDA);
        assertThrows(ReglaNegocioException.class, () -> nuevoBuilder()
                .cliente(cliente("Español"))
                .producto(suspendida)
                .fechaViaje(LocalDate.of(2026, 11, 1))
                .viajeros(2)
                .build());
    }

    @Test
    void elGuiaAsignadoDebeHablarElIdiomaPreferidoDelCliente() {
        Guia guia = new Guia("G1", "Carlos Ríos", List.of("Inglés"), "3100000000", BigDecimal.valueOf(150_000));
        assertThrows(ReglaNegocioException.class, () -> nuevoBuilder()
                .cliente(cliente("Español"))
                .producto(actividadDisponible())
                .fechaViaje(LocalDate.of(2026, 11, 1))
                .viajeros(2)
                .guia(guia)
                .build());
    }
}
