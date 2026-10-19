package com.rutaverde.vista;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Function;


public abstract class VistaCrud<T> extends VBox {
    private final TableView<T> tabla = new TableView<>();
    private final GridPane formulario = new GridPane();
    private final Label lblEstado = new Label();
    private final Button btnGuardar = Componentes.primario("Guardar");
    private final Button btnNuevo = Componentes.secundario("Nuevo");
    private final Button btnEliminar = Componentes.peligro("Eliminar");
    protected final VBox panelFormulario;
    private int fila = 0;

    protected VistaCrud(String titulo, String subtitulo) {
        setSpacing(18);
        setPadding(new Insets(28));
        getStyleClass().add("contenido");

        formulario.setHgap(12);
        formulario.setVgap(10);
        ColumnConstraints etiquetas = new ColumnConstraints();
        etiquetas.setMinWidth(105);
        ColumnConstraints controles = new ColumnConstraints();
        controles.setHgrow(Priority.ALWAYS);
        formulario.getColumnConstraints().addAll(etiquetas, controles);

        lblEstado.getStyleClass().add("estado-ok");
        panelFormulario = Componentes.tarjeta(formulario, new HBox(10, btnGuardar, btnNuevo, btnEliminar), lblEstado);

        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("Aún no hay registros"));

        ScrollPane lateral = new ScrollPane(panelFormulario);
        lateral.setFitToWidth(true);
        lateral.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        lateral.setPrefWidth(440);
        lateral.setMinWidth(420);

        HBox cuerpo = new HBox(18, tabla, lateral);
        HBox.setHgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(cuerpo, Priority.ALWAYS);
        getChildren().addAll(Componentes.encabezado(titulo, subtitulo), cuerpo);
    }

    protected void agregarCampo(String etiqueta, Node control) {
        if (control instanceof Region r) {
            r.setMaxWidth(Double.MAX_VALUE);
        }
        formulario.add(Componentes.etiqueta(etiqueta), 0, fila);
        formulario.add(control, 1, fila);
        fila++;
    }

    protected void columna(String titulo, Function<T, String> extractor) {
        tabla.getColumns().add(Componentes.<T>columna(titulo, extractor));
    }

    /** Actualiza las filas conservando la selección actual. */
    public void setItems(List<T> datos) {
        T seleccionado = tabla.getSelectionModel().getSelectedItem();
        tabla.getItems().setAll(datos);
        if (seleccionado != null && datos.contains(seleccionado)) {
            tabla.getSelectionModel().select(seleccionado);
        }
    }

    public void exito(String mensaje) { lblEstado.setText(mensaje); }

    public TableView<T> getTabla() { return tabla; }
    public Button getBtnGuardar() { return btnGuardar; }
    public Button getBtnNuevo() { return btnNuevo; }
    public Button getBtnEliminar() { return btnEliminar; }

    public abstract void mostrar(T elemento);

    public abstract void limpiar();
}
