package com.rutaverde.modelo.dominio;

import com.rutaverde.modelo.excepcion.ReglaNegocioException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class Guia {

    //Atributos
    private final String identificacion;
    private String nombre;
    private final Set<String> idiomas = new LinkedHashSet<>();
    private String telefono;
    private BigDecimal tarifaPorDia;


    //Constructor
    public Guia(String identificacion, String nombre, Collection<String> idiomas,
                String telefono, BigDecimal tarifaPorDia) {
        this.identificacion = Validaciones.texto(identificacion, "identificación");
        setNombre(nombre);
        setIdiomas(idiomas);
        setTelefono(telefono);
        setTarifaPorDia(tarifaPorDia);
    }

    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public Set<String> getIdiomas() { return Collections.unmodifiableSet(idiomas); }
    public String getTelefono() { return telefono; }
    public BigDecimal getTarifaPorDia() { return tarifaPorDia; }

    public void setNombre(String v) { this.nombre = Validaciones.texto(v, "nombre"); }
    public void setTelefono(String v) { this.telefono = Validaciones.texto(v, "teléfono"); }
    public void setTarifaPorDia(BigDecimal v) { this.tarifaPorDia = Validaciones.dineroNoNegativo(v, "tarifa por día"); }

    public void setIdiomas(Collection<String> nuevos) {
        if (nuevos == null || nuevos.isEmpty()) {
            throw new ReglaNegocioException("El guía debe hablar al menos un idioma");
        }
        idiomas.clear();
        for (String idioma : nuevos) {
            idiomas.add(Validaciones.texto(idioma, "idioma").toLowerCase(Locale.ROOT));
        }
    }

    public boolean hablaIdioma(String idioma) {
        return idioma != null && idiomas.contains(idioma.trim().toLowerCase(Locale.ROOT));
    }

    @Override public String toString() { return nombre + " (" + identificacion + ")"; }
}
