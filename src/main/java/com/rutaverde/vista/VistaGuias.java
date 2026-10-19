package com.rutaverde.vista;

import com.rutaverde.modelo.dominio.Guia;
import javafx.scene.control.TextField;

public class VistaGuias extends VistaCrud<Guia> {
    private final TextField txtId = Componentes.campo("Identificación");
    private final TextField txtNombre = Componentes.campo("Nombre completo");
    private final TextField txtIdiomas = Componentes.campo("Separados por coma: Español, Inglés");
    private final TextField txtTelefono = Componentes.campo("Teléfono");
    private final TextField txtTarifa = Componentes.campo("Tarifa por día en pesos");

    public VistaGuias() {
        super("Guías", "Guías turísticos, sus idiomas y su tarifa diaria");
        agregarCampo("Identificación", txtId);
        agregarCampo("Nombre", txtNombre);
        agregarCampo("Idiomas", txtIdiomas);
        agregarCampo("Teléfono", txtTelefono);
        agregarCampo("Tarifa por día", txtTarifa);

        columna("Identificación", Guia::getIdentificacion);
        columna("Nombre", Guia::getNombre);
        columna("Idiomas", g -> String.join(", ", g.getIdiomas()));
        columna("Teléfono", Guia::getTelefono);
        columna("Tarifa/día", g -> Formatos.pesos(g.getTarifaPorDia()));
    }

    public String getIdentificacion() { return txtId.getText(); }
    public String getNombre() { return txtNombre.getText(); }
    public String getIdiomas() { return txtIdiomas.getText(); }
    public String getTelefono() { return txtTelefono.getText(); }
    public String getTarifa() { return txtTarifa.getText(); }

    @Override
    public void mostrar(Guia g) {
        txtId.setText(g.getIdentificacion());
        txtId.setDisable(true);
        txtNombre.setText(g.getNombre());
        txtIdiomas.setText(String.join(", ", g.getIdiomas()));
        txtTelefono.setText(g.getTelefono());
        txtTarifa.setText(g.getTarifaPorDia().toPlainString());
    }

    @Override
    public void limpiar() {
        for (TextField t : new TextField[]{txtId, txtNombre, txtIdiomas, txtTelefono, txtTarifa}) {
            t.clear();
        }
        txtId.setDisable(false);
        getTabla().getSelectionModel().clearSelection();
    }
}
