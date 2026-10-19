package com.rutaverde.vista;

import com.rutaverde.modelo.dominio.Cliente;
import javafx.scene.control.TextField;

public class VistaClientes extends VistaCrud<Cliente> {
    private final TextField txtDocumento = Componentes.campo("Cédula o pasaporte");
    private final TextField txtNombre = Componentes.campo("Nombre completo");
    private final TextField txtTelefono = Componentes.campo("Teléfono");
    private final TextField txtCorreo = Componentes.campo("correo@ejemplo.com");
    private final TextField txtPais = Componentes.campo("País de origen");
    private final TextField txtIdioma = Componentes.campo("Ej.: Español");

    public VistaClientes() {
        super("Clientes", "Viajeros que realizan reservas en la agencia");
        agregarCampo("Documento", txtDocumento);
        agregarCampo("Nombre", txtNombre);
        agregarCampo("Teléfono", txtTelefono);
        agregarCampo("Correo", txtCorreo);
        agregarCampo("País", txtPais);
        agregarCampo("Idioma", txtIdioma);

        columna("Documento", Cliente::getDocumento);
        columna("Nombre", Cliente::getNombreCompleto);
        columna("Teléfono", Cliente::getTelefono);
        columna("Correo", Cliente::getCorreo);
        columna("País", Cliente::getPaisOrigen);
        columna("Idioma", Cliente::getIdiomaPreferido);
    }

    public String getDocumento() { return txtDocumento.getText(); }
    public String getNombre() { return txtNombre.getText(); }
    public String getTelefono() { return txtTelefono.getText(); }
    public String getCorreo() { return txtCorreo.getText(); }
    public String getPais() { return txtPais.getText(); }
    public String getIdioma() { return txtIdioma.getText(); }

    @Override
    public void mostrar(Cliente c) {
        txtDocumento.setText(c.getDocumento());
        txtDocumento.setDisable(true);
        txtNombre.setText(c.getNombreCompleto());
        txtTelefono.setText(c.getTelefono());
        txtCorreo.setText(c.getCorreo());
        txtPais.setText(c.getPaisOrigen());
        txtIdioma.setText(c.getIdiomaPreferido());
    }

    @Override
    public void limpiar() {
        for (TextField t : new TextField[]{txtDocumento, txtNombre, txtTelefono, txtCorreo, txtPais, txtIdioma}) {
            t.clear();
        }
        txtDocumento.setDisable(false);
        getTabla().getSelectionModel().clearSelection();
    }
}
