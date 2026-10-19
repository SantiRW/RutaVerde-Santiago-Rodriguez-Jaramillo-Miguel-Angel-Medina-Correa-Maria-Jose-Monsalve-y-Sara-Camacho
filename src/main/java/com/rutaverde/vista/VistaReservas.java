package com.rutaverde.vista;

import com.rutaverde.modelo.dominio.Cliente;
import com.rutaverde.modelo.dominio.Guia;
import com.rutaverde.modelo.dominio.Producto;
import com.rutaverde.modelo.dominio.Reserva;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

/** Nueva reserva, cotización, pago, voucher y listado de reservas confirmadas. */
public class VistaReservas extends VBox {
    private final ComboBox<Cliente> cmbCliente = Componentes.combo("Seleccione un cliente");
    private final ComboBox<Producto> cmbProducto = Componentes.combo("Actividad o paquete");
    private final DatePicker dpFecha = new DatePicker(LocalDate.now().plusDays(1));
    private final Spinner<Integer> spnViajeros = new Spinner<>(1, 15, 2);
    private final CheckBox chkGuia = new CheckBox("Asignar guía");
    private final ComboBox<Guia> cmbGuia = Componentes.combo("Seleccione un guía");
    private final CheckBox chkTransporte = new CheckBox("Transporte desde el hotel ($35.000 por viajero)");
    private final CheckBox chkSeguro = new CheckBox("Seguro de viaje ($15.000 por viajero)");
    private final TextArea txtObservaciones = new TextArea();
    private final TextField txtTarjeta = Componentes.campo("Ej.: 4539 1488 0343 6467");
    private final ComboBox<String> cmbCanal = Componentes.combo("Canal de confirmación");
    private final Button btnCotizar = Componentes.secundario("Calcular valor");
    private final Button btnConfirmar = Componentes.primario("Confirmar y pagar");
    private final Label lblValor = new Label("$ 0");
    private final Label lblInfo = new Label("Elija un producto y calcule el valor.");
    private final TextArea txtVoucher = new TextArea();
    private final TableView<Reserva> tabla = new TableView<>();

    public VistaReservas() {
        setSpacing(18);
        setPadding(new Insets(28));
        getStyleClass().add("contenido");

        dpFecha.setMaxWidth(Double.MAX_VALUE);
        dpFecha.setEditable(false);
        dpFecha.setDayCellFactory(p -> new DateCell() {
            @Override
            public void updateItem(LocalDate fecha, boolean vacia) {
                super.updateItem(fecha, vacia);
                setDisable(vacia || fecha.isBefore(LocalDate.now()));
            }
        });
        spnViajeros.setEditable(true);
        spnViajeros.setMaxWidth(Double.MAX_VALUE);
        cmbGuia.disableProperty().bind(chkGuia.selectedProperty().not());
        txtObservaciones.setPromptText("Alergias, movilidad reducida, alimentación...");
        txtObservaciones.setPrefRowCount(3);
        txtObservaciones.setWrapText(true);

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);
        ColumnConstraints c0 = new ColumnConstraints();
        c0.setMinWidth(95);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(c0, c1);
        int f = 0;
        form.addRow(f++, Componentes.etiqueta("Cliente"), cmbCliente);
        form.addRow(f++, Componentes.etiqueta("Producto"), cmbProducto);
        form.addRow(f++, Componentes.etiqueta("Fecha del viaje"), dpFecha);
        form.addRow(f++, Componentes.etiqueta("Viajeros (1-15)"), spnViajeros);
        form.addRow(f++, chkGuia, cmbGuia);
        form.add(chkTransporte, 0, f++, 2, 1);
        form.add(chkSeguro, 0, f++, 2, 1);
        form.addRow(f++, Componentes.etiqueta("Observaciones"), txtObservaciones);
        form.addRow(f++, Componentes.etiqueta("Tarjeta"), txtTarjeta);
        form.addRow(f, Componentes.etiqueta("Canal"), cmbCanal);

        VBox tarjetaForm = Componentes.tarjeta(Componentes.tituloSeccion("Nueva reserva"), form,
                new HBox(10, btnCotizar, btnConfirmar));
        tarjetaForm.setPrefWidth(500);
        tarjetaForm.setMinWidth(460);

        lblValor.getStyleClass().add("valor-grande");
        lblInfo.getStyleClass().add("subtitulo");
        lblInfo.setWrapText(true);
        txtVoucher.setEditable(false);
        txtVoucher.getStyleClass().add("voucher-area");
        txtVoucher.setPromptText("El voucher aparecerá aquí cuando la reserva se confirme.");
        txtVoucher.setPrefRowCount(12);
        VBox.setVgrow(txtVoucher, Priority.ALWAYS);
        VBox tarjetaResumen = Componentes.tarjeta(Componentes.tituloSeccion("Valor de la reserva"), lblValor, lblInfo,
                Componentes.tituloSeccion("Voucher emitido"), txtVoucher);
        HBox.setHgrow(tarjetaResumen, Priority.ALWAYS);

        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("Aún no hay reservas confirmadas"));
        tabla.setPrefHeight(210);
        tabla.getColumns().add(Componentes.<Reserva>columna("No.", r -> String.valueOf(r.getNumero())));
        tabla.getColumns().add(Componentes.<Reserva>columna("Cliente", r -> r.getCliente().getNombreCompleto()));
        tabla.getColumns().add(Componentes.<Reserva>columna("Producto", r -> r.getProducto().getNombre()));
        tabla.getColumns().add(Componentes.<Reserva>columna("Fecha", r -> r.getFechaViaje().toString()));
        tabla.getColumns().add(Componentes.<Reserva>columna("Viajeros", r -> String.valueOf(r.getViajeros())));
        tabla.getColumns().add(Componentes.<Reserva>columna("Valor", r -> Formatos.pesos(r.getValor())));
        tabla.getColumns().add(Componentes.<Reserva>columna("Estado", r -> r.getEstado().name()));

        HBox arriba = new HBox(18, tarjetaForm, tarjetaResumen);
        getChildren().addAll(
                Componentes.encabezado("Reservas", "Cotice, cobre con tarjeta y emita el voucher del viaje"),
                arriba, Componentes.tituloSeccion("Reservas confirmadas"), tabla);
    }

    // ---- lectura del formulario ----
    public Cliente getCliente() { return cmbCliente.getValue(); }
    public Producto getProducto() { return cmbProducto.getValue(); }
    public LocalDate getFecha() { return dpFecha.getValue(); }
    public boolean isGuiaMarcada() { return chkGuia.isSelected(); }
    public Guia getGuia() { return chkGuia.isSelected() ? cmbGuia.getValue() : null; }
    public boolean isTransporte() { return chkTransporte.isSelected(); }
    public boolean isSeguro() { return chkSeguro.isSelected(); }
    public String getObservaciones() { return txtObservaciones.getText(); }
    public String getTarjeta() { return txtTarjeta.getText(); }
    public String getCanal() { return cmbCanal.getValue(); }

    public int getViajeros() {
        try {
            spnViajeros.increment(0); // confirma lo que se haya escrito a mano
        } catch (RuntimeException ignorada) {
            // si el texto no es válido se conserva el último valor
        }
        return spnViajeros.getValue();
    }

    public Button getBtnCotizar() { return btnCotizar; }
    public Button getBtnConfirmar() { return btnConfirmar; }

    // ---- datos que muestra ----
    public void setClientes(List<Cliente> l) { Componentes.reponer(cmbCliente, l); }
    public void setProductos(List<Producto> l) { Componentes.reponer(cmbProducto, l); }
    public void setGuias(List<Guia> l) { Componentes.reponer(cmbGuia, l); }

    public void setCanales(List<String> canales) {
        cmbCanal.getItems().setAll(canales);
        if (cmbCanal.getValue() == null && !canales.isEmpty()) {
            cmbCanal.setValue(canales.get(0));
        }
    }

    public void setReservas(List<Reserva> l) { tabla.getItems().setAll(l); }

    public void mostrarValor(String valor, String detalle) {
        lblValor.setText(valor);
        lblInfo.setText(detalle);
    }

    public void mostrarVoucher(String voucher) { txtVoucher.setText(voucher); }

    public void limpiarPago() {
        txtTarjeta.clear();
        txtObservaciones.clear();
    }
}
