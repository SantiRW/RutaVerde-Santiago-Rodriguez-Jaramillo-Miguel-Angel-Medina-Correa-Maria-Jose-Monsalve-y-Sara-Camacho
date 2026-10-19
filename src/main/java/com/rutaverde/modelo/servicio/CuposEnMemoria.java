package com.rutaverde.modelo.servicio;

import com.rutaverde.modelo.dominio.Actividad;
import com.rutaverde.modelo.dominio.Producto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CuposEnMemoria implements ControlCupos {
    private final Map<String, Integer> usados = new HashMap<>();

    @Override
    public boolean hayCupo(Producto producto, LocalDate fecha, int viajeros) {
        for (Actividad a : producto.getActividades()) {
            if (disponibles(a, fecha) < viajeros) return false;
        }
        return true;
    }

    @Override
    public void descontar(Producto producto, LocalDate fecha, int viajeros) {
        for (Actividad a : producto.getActividades()) {
            usados.merge(clave(a, fecha), viajeros, Integer::sum);
        }
    }

    @Override
    public int disponibles(Actividad actividad, LocalDate fecha) {
        return actividad.getCupoMaximoPorDia() - usados.getOrDefault(clave(actividad, fecha), 0);
    }

    private String clave(Actividad a, LocalDate fecha) { return a.getCodigo() + "|" + fecha; }
}
