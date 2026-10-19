package com.rutaverde.modelo.dominio;

public enum Dificultad {
    BAJA("Baja"), MEDIA("Media"), ALTA("Alta");

    private final String etiqueta;

    Dificultad(String etiqueta) { this.etiqueta = etiqueta; }

    @Override public String toString() { return etiqueta; }
}
