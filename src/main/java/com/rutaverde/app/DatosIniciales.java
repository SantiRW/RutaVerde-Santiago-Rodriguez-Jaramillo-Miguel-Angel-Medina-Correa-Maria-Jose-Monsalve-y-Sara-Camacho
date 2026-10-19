package com.rutaverde.app;

import com.rutaverde.modelo.dominio.*;

import java.math.BigDecimal;
import java.util.List;


final class DatosIniciales {
    private DatosIniciales() { }

    static void cargar(Agencia agencia) {
        Actividad cocora = new Actividad("A1", "Valle del Cocora", "Salento", 5, BigDecimal.valueOf(80_000), 10, Dificultad.MEDIA, EstadoActividad.DISPONIBLE);
        Actividad finca = new Actividad("A2", "Tour de café", "Salento", 3, BigDecimal.valueOf(60_000), 10, Dificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad parque = new Actividad("A3", "Parque del Café", "Montenegro", 6, BigDecimal.valueOf(120_000), 4, Dificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad aves = new Actividad("A4", "Avistamiento de aves", "Filandia", 4, BigDecimal.valueOf(70_000), 10, Dificultad.ALTA, EstadoActividad.DISPONIBLE);
        Actividad rafting = new Actividad("A5", "Rafting", "Armenia", 2, BigDecimal.valueOf(90_000), 10, Dificultad.ALTA, EstadoActividad.SUSPENDIDA);
        for (Actividad a : List.of(cocora, finca, parque, aves, rafting)) {
            agencia.getActividades().agregar(a);
        }

        Paquete salento = new Paquete("P1", "Paquete Salento", BigDecimal.TEN);
        salento.agregar(cocora);
        salento.agregar(finca);
        Paquete parques = new Paquete("P2", "Paquete Parques", BigDecimal.ZERO);
        parques.agregar(parque);
        Paquete total = new Paquete("P3", "Quindío Total", BigDecimal.valueOf(5));
        total.agregar(salento);
        total.agregar(parques);
        total.agregar(aves);
        agencia.getPaquetes().agregar(salento);
        agencia.getPaquetes().agregar(parques);
        agencia.getPaquetes().agregar(total);

        agencia.getClientes().agregar(new Cliente("1094", "Ana Gómez", "3115550000", "ana@correo.com", "Colombia", "Español"));
        agencia.getClientes().agregar(new Cliente("P-77", "John Smith", "+15550001", "john@mail.com", "EE. UU.", "Inglés"));

        agencia.getGuias().agregar(new Guia("G1", "Carlos Ríos", List.of("Español"), "3100000000", BigDecimal.valueOf(150_000)));
        agencia.getGuias().agregar(new Guia("G2", "Laura Mesa", List.of("Español", "Inglés"), "3200000000", BigDecimal.valueOf(180_000)));
    }
}
