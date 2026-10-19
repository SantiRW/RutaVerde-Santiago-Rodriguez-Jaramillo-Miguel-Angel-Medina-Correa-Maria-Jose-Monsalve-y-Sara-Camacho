package com.rutaverde.modelo.dominio;

import java.math.BigDecimal;
import java.util.List;

/** PATRÓN COMPOSITE - Hoja. */
public class Actividad implements Producto {
    private final String codigo;
    private String nombre;
    private String municipio;
    private int duracionHoras;
    private BigDecimal precioPorPersona;
    private int cupoMaximoPorDia;
    private Dificultad dificultad;
    private EstadoActividad estado;

    public Actividad(String codigo, String nombre, String municipio, int duracionHoras,
                     BigDecimal precioPorPersona, int cupoMaximoPorDia,
                     Dificultad dificultad, EstadoActividad estado) {
        this.codigo = Validaciones.texto(codigo, "código");
        setNombre(nombre);
        setMunicipio(municipio);
        setDuracionHoras(duracionHoras);
        setPrecioPorPersona(precioPorPersona);
        setCupoMaximoPorDia(cupoMaximoPorDia);
        setDificultad(dificultad);
        setEstado(estado);
    }

    @Override public String getCodigo() { return codigo; }
    @Override public String getNombre() { return nombre; }
    @Override public BigDecimal getPrecioPorPersona() { return precioPorPersona; }
    @Override public int getDuracionHoras() { return duracionHoras; }
    @Override public List<Actividad> getActividades() { return List.of(this); }
    @Override public boolean contiene(Producto otro) { return otro == this; }

    public String getMunicipio() { return municipio; }
    public int getCupoMaximoPorDia() { return cupoMaximoPorDia; }
    public Dificultad getDificultad() { return dificultad; }
    public EstadoActividad getEstado() { return estado; }
    public boolean estaSuspendida() { return estado == EstadoActividad.SUSPENDIDA; }

    public void setNombre(String nombre) { this.nombre = Validaciones.texto(nombre, "nombre"); }
    public void setMunicipio(String municipio) { this.municipio = Validaciones.texto(municipio, "municipio"); }
    public void setDuracionHoras(int horas) { this.duracionHoras = Validaciones.enteroPositivo(horas, "duración (horas)"); }
    public void setPrecioPorPersona(BigDecimal precio) { this.precioPorPersona = Validaciones.dineroNoNegativo(precio, "precio por persona"); }
    public void setCupoMaximoPorDia(int cupo) { this.cupoMaximoPorDia = Validaciones.enteroPositivo(cupo, "cupo máximo por día"); }
    public void setDificultad(Dificultad dificultad) {
        if (dificultad == null) throw new com.rutaverde.modelo.excepcion.ReglaNegocioException("La dificultad es obligatoria");
        this.dificultad = dificultad;
    }
    public void setEstado(EstadoActividad estado) {
        if (estado == null) throw new com.rutaverde.modelo.excepcion.ReglaNegocioException("El estado es obligatorio");
        this.estado = estado;
    }

    @Override public String toString() { return "Actividad · " + codigo + " - " + nombre; }
}
