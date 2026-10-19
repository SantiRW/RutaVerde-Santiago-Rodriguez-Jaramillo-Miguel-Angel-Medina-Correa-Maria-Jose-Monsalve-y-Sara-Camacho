package com.rutaverde.modelo.dominio;

import com.rutaverde.modelo.excepcion.ReglaNegocioException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * PATRÓN COMPOSITE - Compuesto: contiene actividades y otros paquetes.
 * Reglas: descuento 0-20 %, sin ciclos y sin actividades repetidas en ningún nivel.
 */
public class Paquete implements Producto {
    public static final BigDecimal DESCUENTO_MAXIMO = BigDecimal.valueOf(20);

    private final String codigo;
    private String nombre;
    private BigDecimal descuentoPorcentaje;
    private final List<Producto> contenido = new ArrayList<>();
    private final List<Paquete> padres = new ArrayList<>();

    public Paquete(String codigo, String nombre, BigDecimal descuentoPorcentaje) {
        this.codigo = Validaciones.texto(codigo, "código");
        setNombre(nombre);
        setDescuentoPorcentaje(descuentoPorcentaje);
    }

    @Override public String getCodigo() { return codigo; }
    @Override public String getNombre() { return nombre; }
    public BigDecimal getDescuentoPorcentaje() { return descuentoPorcentaje; }
    public List<Producto> getContenido() { return Collections.unmodifiableList(contenido); }

    public void setNombre(String nombre) { this.nombre = Validaciones.texto(nombre, "nombre"); }

    public void setDescuentoPorcentaje(BigDecimal descuento) {
        if (descuento == null || descuento.signum() < 0 || descuento.compareTo(DESCUENTO_MAXIMO) > 0) {
            throw new ReglaNegocioException("El descuento del paquete debe estar entre 0 % y 20 %");
        }
        this.descuentoPorcentaje = descuento;
    }

    /** Suma de lo que contiene, con el descuento propio aplicado. */
    @Override
    public BigDecimal getPrecioPorPersona() {
        BigDecimal suma = BigDecimal.ZERO;
        for (Producto p : contenido) {
            suma = suma.add(p.getPrecioPorPersona());
        }
        BigDecimal factor = BigDecimal.valueOf(100).subtract(descuentoPorcentaje)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return suma.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int getDuracionHoras() {
        int total = 0;
        for (Producto p : contenido) {
            total += p.getDuracionHoras();
        }
        return total;
    }

    @Override
    public List<Actividad> getActividades() {
        List<Actividad> todas = new ArrayList<>();
        for (Producto p : contenido) {
            todas.addAll(p.getActividades());
        }
        return todas;
    }

    @Override
    public boolean contiene(Producto otro) {
        if (otro == this) return true;
        for (Producto p : contenido) {
            if (p.contiene(otro)) return true;
        }
        return false;
    }

    public void agregar(Producto hijo) {
        Objects.requireNonNull(hijo, "El producto a agregar es obligatorio");
        if (hijo == this || hijo.contiene(this)) {
            throw new ReglaNegocioException("Un paquete no puede contenerse a sí mismo (ni directa ni indirectamente)");
        }
        if (contenido.contains(hijo)) {
            throw new ReglaNegocioException("El paquete ya contiene '" + hijo.getNombre() + "'");
        }
        // Se revisa este paquete y todos los paquetes que lo contienen.
        Set<Paquete> afectados = new HashSet<>();
        afectados.add(this);
        recolectarAncestros(afectados);
        Set<Actividad> nuevas = new HashSet<>(hijo.getActividades());
        for (Paquete paquete : afectados) {
            for (Actividad existente : paquete.getActividades()) {
                if (nuevas.contains(existente)) {
                    throw new ReglaNegocioException("La actividad '" + existente.getNombre()
                            + "' ya está incluida en el plan '" + paquete.getNombre() + "'");
                }
            }
        }
        contenido.add(hijo);
        hijo.agregadoA(this);
    }

    public void remover(Producto hijo) {
        if (contenido.remove(hijo)) {
            hijo.quitadoDe(this);
        }
    }

    @Override public void agregadoA(Paquete padre) { padres.add(padre); }

    @Override public void quitadoDe(Paquete padre) { padres.remove(padre); }

    private void recolectarAncestros(Set<Paquete> acumulado) {
        for (Paquete padre : padres) {
            if (acumulado.add(padre)) {
                padre.recolectarAncestros(acumulado);
            }
        }
    }

    @Override public String toString() { return "Paquete · " + codigo + " - " + nombre; }
}
