package com.rutaverde.modelo.servicio;

import com.rutaverde.modelo.dominio.Actividad;
import com.rutaverde.modelo.dominio.Producto;

import java.time.LocalDate;

public interface ControlCupos {
    /** Verifica que TODAS las actividades del producto tengan cupo para los viajeros en esa fecha. */
    boolean hayCupo(Producto producto, LocalDate fecha, int viajeros);

    void descontar(Producto producto, LocalDate fecha, int viajeros);

    int disponibles(Actividad actividad, LocalDate fecha);
}
