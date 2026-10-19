package com.rutaverde.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Ventana principal: barra lateral con navegación y área de contenido. */
public class VistaPrincipal extends BorderPane {
    private final VBox menu = new VBox(6);
    private final ToggleGroup grupo = new ToggleGroup();
    private final ScrollPane area = new ScrollPane();

    public VistaPrincipal() {
        PaisajePalmas logo = new PaisajePalmas(34, 34);
        Label nombre = new Label("RutaVerde");
        nombre.getStyleClass().add("logo-texto");
        Label sub = new Label("Quindío · Eje Cafetero");
        sub.getStyleClass().add("logo-sub");
        HBox marca = new HBox(10, logo, new VBox(0, nombre, sub));
        marca.setAlignment(Pos.CENTER_LEFT);

        Region espacio = new Region();
        VBox.setVgrow(espacio, Priority.ALWAYS);
        PaisajePalmas palmas = new PaisajePalmas(208, 95, 150, 110, 70);

        VBox barra = new VBox(24, marca, menu, espacio, palmas);
        barra.setPadding(new Insets(24, 16, 0, 16));
        barra.setPrefWidth(240);
        barra.setMinWidth(240);
        barra.getStyleClass().add("barra-lateral");

        area.setFitToWidth(true);
        area.setFitToHeight(true);
        setLeft(barra);
        setCenter(area);
    }

    public ToggleButton agregarBoton(String texto) {
        ToggleButton b = new ToggleButton(texto);
        b.setToggleGroup(grupo);
        b.setMaxWidth(Double.MAX_VALUE);
        b.getStyleClass().add("nav-button");
        menu.getChildren().add(b);
        return b;
    }

    public void setContenido(Node contenido) { area.setContent(contenido); }
}
