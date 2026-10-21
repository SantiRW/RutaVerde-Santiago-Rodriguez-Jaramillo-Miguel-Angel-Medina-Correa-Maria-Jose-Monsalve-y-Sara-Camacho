package com.rutaverde.modelo.dominio;

import com.rutaverde.modelo.excepcion.ReglaNegocioException;

import java.math.BigDecimal;
import java.text.Normalizer;
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
        // se valida en un conjunto aparte, si nuevos es una vista de este mismo guía
        // o si algún idioma es inválido no se pierde lo que ya tenía.
        Set<String> normalizados = new LinkedHashSet<>();
        for (String idioma : nuevos) {
            normalizados.add(Validaciones.texto(idioma, "idioma").toLowerCase(Locale.ROOT));
        }
        idiomas.clear();
        idiomas.addAll(normalizados);
    }

    /** Compara sin importar mayúsculas ni tildes como ingles e inglés */
    public boolean hablaIdioma(String idioma) {
        if (idioma == null || idioma.isBlank()) return false;
        String buscado = sinTildes(idioma);
        for (String propio : idiomas) {
            if (sinTildes(propio).equals(buscado)) return true;
        }
        return false;
    }

    private static String sinTildes(String texto) {
        return Normalizer.normalize(texto.trim().toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    @Override public String toString() { return nombre + " (" + identificacion + ")"; }
}
