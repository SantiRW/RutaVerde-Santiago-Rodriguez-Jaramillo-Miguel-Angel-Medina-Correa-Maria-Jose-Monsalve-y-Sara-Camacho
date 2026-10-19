package com.rutaverde.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** Pantalla de bienvenida con indicadores y un paisaje de palmas de cera. */
public class VistaInicio extends StackPane {
    private final Label lblClientes = new Label("0");
    private final Label lblGuias = new Label("0");
    private final Label lblActividades = new Label("0");
    private final Label lblPaquetes = new Label("0");
    private final Label lblReservas = new Label("0");

    public VistaInicio() {
        getStyleClass().add("contenido");

        PaisajePalmas palmas = new PaisajePalmas(560, 210, 340, 270, 180, 300);
        palmas.setOpacity(0.95);
        StackPane.setAlignment(palmas, Pos.BOTTOM_RIGHT);

        Label titulo = new Label("Bienvenido a RutaVerde Quindío");
        titulo.getStyleClass().add("hero-titulo");
        Label sub = new Label("Gestione clientes, guías, actividades, paquetes y reservas del Eje Cafetero en un solo lugar.");
        sub.getStyleClass().add("subtitulo");
        sub.setWrapText(true);

        HBox tarjetas = new HBox(14,
                estadistica("Clientes", lblClientes), estadistica("Guías", lblGuias),
                estadistica("Actividades", lblActividades), estadistica("Paquetes", lblPaquetes),
                estadistica("Reservas", lblReservas));

        VBox texto = new VBox(16, titulo, sub, tarjetas);
        texto.setPadding(new Insets(40));
        texto.setMaxWidth(820);
        texto.setMaxHeight(VBox.USE_PREF_SIZE);
        StackPane.setAlignment(texto, Pos.TOP_LEFT);

        getChildren().addAll(palmas, texto);
    }

    private VBox estadistica(String titulo, Label valor) {
        valor.getStyleClass().add("estadistica-numero");
        VBox caja = Componentes.tarjeta(valor, Componentes.etiqueta(titulo));
        caja.getStyleClass().add("tarjeta-estadistica");
        caja.setSpacing(2);
        return caja;
    }

    public void actualizar(int clientes, int guias, int actividades, int paquetes, int reservas) {
        lblClientes.setText(String.valueOf(clientes));
        lblGuias.setText(String.valueOf(guias));
        lblActividades.setText(String.valueOf(actividades));
        lblPaquetes.setText(String.valueOf(paquetes));
        lblReservas.setText(String.valueOf(reservas));
    }
}
