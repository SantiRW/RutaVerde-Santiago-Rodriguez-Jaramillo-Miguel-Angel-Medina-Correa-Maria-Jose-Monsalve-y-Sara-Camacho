package com.rutaverde.vista;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/** Cuadros de diálogo con el estilo de la aplicación. */
public final class Mensajes {
    private Mensajes() { }

    public static void info(String titulo, String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, titulo, mensaje);
    }

    public static void error(String mensaje) {
        mostrar(Alert.AlertType.WARNING, "No se pudo completar la acción", mensaje);
    }

    public static boolean confirmar(String mensaje) {
        Alert a = crear(Alert.AlertType.CONFIRMATION, "Confirmación", mensaje);
        return a.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        crear(tipo, titulo, mensaje).showAndWait();
    }

    private static Alert crear(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert a = new Alert(tipo, mensaje);
        a.setTitle("RutaVerde Quindío");
        a.setHeaderText(titulo);
        a.getDialogPane().getStylesheets().add(Estilos.HOJA);
        return a;
    }
}
