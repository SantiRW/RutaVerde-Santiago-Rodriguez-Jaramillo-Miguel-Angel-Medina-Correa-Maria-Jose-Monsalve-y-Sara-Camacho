package com.rutaverde.modelo.servicio;

import com.rutaverde.modelo.dominio.Producto;
import com.rutaverde.modelo.dominio.Reserva;
import com.rutaverde.modelo.excepcion.ReglaNegocioException;
import com.rutaverde.modelo.repositorio.Repositorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Reportes sobre reservas confirmadas (recorrer, acumular, identificar el mayor). */
public class ReporteIngresos {
    private final Repositorio<Reserva, Integer> reservas;

    public ReporteIngresos(Repositorio<Reserva, Integer> reservas) { this.reservas = reservas; }

    /** Ingresos de reservas confirmadas con viaje entre inicio y fin (ambos incluidos). */
    public BigDecimal ingresosEnPeriodo(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null || fin.isBefore(inicio)) {
            throw new ReglaNegocioException("El periodo es inválido: la fecha final no puede ser anterior a la inicial");
        }
        BigDecimal total = BigDecimal.ZERO;
        for (Reserva r : reservas.listar()) {
            boolean enPeriodo = !r.getFechaViaje().isBefore(inicio) && !r.getFechaViaje().isAfter(fin);
            if (r.estaConfirmada() && enPeriodo) {
                total = total.add(r.getValor());
            }
        }
        return total;
    }

    /** Producto que más viajeros ha movido entre las reservas confirmadas. */
    public Optional<ResumenProducto> productoConMasViajeros() {
        Map<String, ResumenProducto> acumulado = new LinkedHashMap<>();
        for (Reserva r : reservas.listar()) {
            if (!r.estaConfirmada()) continue;
            Producto p = r.getProducto();
            ResumenProducto previo = acumulado.get(p.getCodigo());
            int viajeros = (previo == null ? 0 : previo.viajeros()) + r.getViajeros();
            acumulado.put(p.getCodigo(), new ResumenProducto(p, viajeros));
        }
        ResumenProducto mayor = null;
        for (ResumenProducto resumen : acumulado.values()) {
            if (mayor == null || resumen.viajeros() > mayor.viajeros()) {
                mayor = resumen;
            }
        }
        return Optional.ofNullable(mayor);
    }
}
