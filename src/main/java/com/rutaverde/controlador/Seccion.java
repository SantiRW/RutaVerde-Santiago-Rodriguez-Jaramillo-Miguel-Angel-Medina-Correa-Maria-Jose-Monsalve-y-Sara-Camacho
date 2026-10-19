package com.rutaverde.controlador;

import javafx.scene.Node;

/** Una opción del menú: su título, su vista y el controlador que la mantiene al día. */
public record Seccion(String titulo, Node vista, Refrescable controlador) { }
