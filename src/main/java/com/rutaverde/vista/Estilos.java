package com.rutaverde.vista;

/** Ruta de la hoja de estilos única de la aplicación. */
public final class Estilos {
    public static final String HOJA = Estilos.class.getResource("/estilos.css").toExternalForm();

    private Estilos() { }
}
