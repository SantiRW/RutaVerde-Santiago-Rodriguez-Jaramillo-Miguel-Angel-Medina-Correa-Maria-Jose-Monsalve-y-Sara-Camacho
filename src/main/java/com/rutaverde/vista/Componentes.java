package com.rutaverde.vista;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Function;


public final class Componentes {
    private Componentes() { }

    public static Button primario(String texto) { return boton(texto, "boton-primario"); }

    public static Button secundario(String texto) { return boton(texto, "boton-secundario"); }

    public static Button peligro(String texto) { return boton(texto, "boton-peligro"); }

    private static Button boton(String texto, String clase) {
        Button b = new Button(texto);
        b.getStyleClass().add(clase);
        return b;
    }

    public static Label etiqueta(String texto) {
        Label l = new Label(texto);
        l.getStyleClass().add("etiqueta-campo");
        return l;
    }

    public static Label tituloSeccion(String texto) {
        Label l = new Label(texto);
        l.getStyleClass().add("titulo-seccion");
        return l;
    }

    public static VBox encabezado(String titulo, String subtitulo) {
        Label t = new Label(titulo);
        t.getStyleClass().add("titulo-pagina");
        Label s = new Label(subtitulo);
        s.getStyleClass().add("subtitulo");
        return new VBox(4, t, s);
    }

    public static VBox tarjeta(Node... hijos) {
        VBox v = new VBox(14, hijos);
        v.getStyleClass().add("tarjeta");
        return v;
    }

    public static TextField campo(String sugerencia) {
        TextField t = new TextField();
        t.setPromptText(sugerencia);
        return t;
    }

    public static <T> ComboBox<T> combo(String sugerencia) {
        ComboBox<T> c = new ComboBox<>();
        c.setPromptText(sugerencia);
        c.setMaxWidth(Double.MAX_VALUE);
        return c;
    }

    public static <S> TableColumn<S, String> columna(String titulo, Function<S, String> extractor) {
        TableColumn<S, String> c = new TableColumn<>(titulo);
        c.setCellValueFactory(d -> new SimpleStringProperty(extractor.apply(d.getValue())));
        return c;
    }

    /** Reemplaza los elementos de un combo conservando la selección si todavía existe. */
    public static <T> void reponer(ComboBox<T> combo, List<T> elementos) {
        T seleccionado = combo.getValue();
        combo.getItems().setAll(elementos);
        if (seleccionado != null && elementos.contains(seleccionado)) {
            combo.setValue(seleccionado);
        }
    }
}
