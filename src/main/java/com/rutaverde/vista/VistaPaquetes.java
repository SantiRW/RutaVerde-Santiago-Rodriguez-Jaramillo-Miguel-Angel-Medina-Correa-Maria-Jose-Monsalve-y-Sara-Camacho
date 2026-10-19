package com.rutaverde.vista;

import com.rutaverde.modelo.dominio.Paquete;
import com.rutaverde.modelo.dominio.Producto;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

/** Registro de paquetes y armado de su contenido (actividades y otros paquetes). */
public class VistaPaquetes extends VistaCrud<Paquete> {
    private final TextField txtCodigo = Componentes.campo("Código");
    private final TextField txtNombre = Componentes.campo("Nombre del paquete");
    private final TextField txtDescuento = Componentes.campo("0 a 20");

    private final ListView<Producto> lstContenido = new ListView<>();
    private final ComboBox<Producto> cmbDisponibles = Componentes.combo("Agregar actividad o paquete");
    private final Button btnAgregar = Componentes.primario("Agregar");
    private final Button btnQuitar = Componentes.peligro("Quitar seleccionado");
    private final Label lblPrecio = new Label("-");
    private final Label lblDuracion = new Label("-");
    private final Label lblActividades = new Label("-");

    public VistaPaquetes() {
        super("Paquetes", "Agrupe actividades y otros paquetes con un descuento propio");
        agregarCampo("Código", txtCodigo);
        agregarCampo("Nombre", txtNombre);
        agregarCampo("Descuento (%)", txtDescuento);

        columna("Código", Paquete::getCodigo);
        columna("Nombre", Paquete::getNombre);
        columna("Desc. %", p -> p.getDescuentoPorcentaje().stripTrailingZeros().toPlainString());
        columna("Precio p/p", p -> Formatos.pesos(p.getPrecioPorPersona()));
        columna("Horas", p -> String.valueOf(p.getDuracionHoras()));
        columna("Actividades", p -> String.valueOf(p.getActividades().size()));

        lstContenido.setPrefHeight(150);
        cmbDisponibles.setPrefWidth(260);
        HBox.setHgrow(cmbDisponibles, Priority.ALWAYS);
        panelFormulario.getChildren().addAll(
                new Separator(),
                Componentes.tituloSeccion("Contenido del paquete"),
                lstContenido,
                new HBox(10, cmbDisponibles, btnAgregar),
                btnQuitar,
                new HBox(8, Componentes.etiqueta("Precio p/p:"), lblPrecio),
                new HBox(8, Componentes.etiqueta("Duración:"), lblDuracion),
                new HBox(8, Componentes.etiqueta("Total actividades:"), lblActividades));
    }

    public String getCodigo() { return txtCodigo.getText(); }
    public String getNombre() { return txtNombre.getText(); }
    public String getDescuento() { return txtDescuento.getText(); }

    public Button getBtnAgregar() { return btnAgregar; }
    public Button getBtnQuitar() { return btnQuitar; }
    public Producto getProductoAAgregar() { return cmbDisponibles.getValue(); }
    public Producto getContenidoSeleccionado() { return lstContenido.getSelectionModel().getSelectedItem(); }

    public void setContenido(List<Producto> contenido) { lstContenido.getItems().setAll(contenido); }

    public void setDisponibles(List<Producto> disponibles) {
        cmbDisponibles.getItems().setAll(disponibles);
        cmbDisponibles.setValue(null);
    }

    public void setResumen(String precio, String duracion, String actividades) {
        lblPrecio.setText(precio);
        lblDuracion.setText(duracion);
        lblActividades.setText(actividades);
    }

    @Override
    public void mostrar(Paquete p) {
        txtCodigo.setText(p.getCodigo());
        txtCodigo.setDisable(true);
        txtNombre.setText(p.getNombre());
        txtDescuento.setText(p.getDescuentoPorcentaje().stripTrailingZeros().toPlainString());
    }

    @Override
    public void limpiar() {
        txtCodigo.clear();
        txtNombre.clear();
        txtDescuento.clear();
        txtCodigo.setDisable(false);
        lstContenido.getItems().clear();
        cmbDisponibles.getItems().clear();
        setResumen("-", "-", "-");
        getTabla().getSelectionModel().clearSelection();
    }
}
