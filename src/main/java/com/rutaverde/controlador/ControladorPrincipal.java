package com.rutaverde.controlador;

import com.rutaverde.vista.VistaPrincipal;
import javafx.scene.control.ToggleButton;

import java.util.List;

/** Navegación entre secciones: al elegir una, se muestra su vista y se recargan sus datos. */
public class ControladorPrincipal {
    public ControladorPrincipal(VistaPrincipal vista, List<Seccion> secciones) {
        ToggleButton primero = null;
        for (Seccion seccion : secciones) {
            ToggleButton boton = vista.agregarBoton(seccion.titulo());
            boton.setOnAction(e -> {
                boton.setSelected(true); // evita que un segundo clic deje el menú sin selección
                seccion.controlador().refrescar();
                vista.setContenido(seccion.vista());
            });
            if (primero == null) primero = boton;
        }
        if (primero != null) primero.fire();
    }
}
