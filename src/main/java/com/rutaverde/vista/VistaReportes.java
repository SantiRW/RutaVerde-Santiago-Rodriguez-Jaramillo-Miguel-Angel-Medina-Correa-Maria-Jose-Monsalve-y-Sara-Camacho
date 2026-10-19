package com.rutaverde.vista;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class VistaReportes extends VBox {
    private final DatePicker dpInicio = new DatePicker(LocalDate.now());
    private final DatePicker dpFin = new DatePicker(LocalDate.now().plusDays(30));
    private final Button btnIngresos = Componentes.primario("Calcular ingresos");
    private final Label lblIngresos = new Label("$ 0");
    private final Label lblPeriodo = new Label("Seleccione un periodo (fechas incluidas).");
    private final Button btnTop = Componentes.primario("Consultar");
    private final Label lblTopProducto = new Label("-");
    private final Label lblTopViajeros = new Label("Aún sin consultar.");

    public VistaReportes() {
        setSpacing(18);
        setPadding(new Insets(28));
        getStyleClass().add("contenido");

        dpInicio.setEditable(false);
        dpFin.setEditable(false);
        lblIngresos.getStyleClass().add("valor-grande");
        lblPeriodo.getStyleClass().add("subtitulo");
        lblTopProducto.getStyleClass().add("valor-grande");
        lblTopProducto.setWrapText(true);
        lblTopViajeros.getStyleClass().add("subtitulo");

        HBox fechas = new HBox(12, Componentes.etiqueta("Desde"), dpInicio, Componentes.etiqueta("Hasta"), dpFin, btnIngresos);
        fechas.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        VBox ingresos = Componentes.tarjeta(Componentes.tituloSeccion("Ingresos por periodo"),
                new Label("Reservas confirmadas cuyo viaje se realiza en el periodo."), fechas, lblIngresos, lblPeriodo);
        VBox top = Componentes.tarjeta(Componentes.tituloSeccion("Producto con más viajeros"),
                new Label("Entre todas las reservas confirmadas."), btnTop, lblTopProducto, lblTopViajeros);
        HBox.setHgrow(ingresos, Priority.ALWAYS);
        HBox.setHgrow(top, Priority.ALWAYS);

        getChildren().addAll(Componentes.encabezado("Reportes", "Indicadores del negocio"),
                new HBox(18, ingresos, top));
    }

    public LocalDate getInicio() { return dpInicio.getValue(); }
    public LocalDate getFin() { return dpFin.getValue(); }
    public Button getBtnIngresos() { return btnIngresos; }
    public Button getBtnTop() { return btnTop; }

    public void mostrarIngresos(String valor, String periodo) {
        lblIngresos.setText(valor);
        lblPeriodo.setText(periodo);
    }

    public void mostrarTop(String producto, String detalle) {
        lblTopProducto.setText(producto);
        lblTopViajeros.setText(detalle);
    }
}
