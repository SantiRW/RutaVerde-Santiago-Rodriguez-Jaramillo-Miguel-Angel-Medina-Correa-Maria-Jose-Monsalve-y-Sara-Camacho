package com.rutaverde.vista;

import com.rutaverde.modelo.dominio.Actividad;
import com.rutaverde.modelo.dominio.Dificultad;
import com.rutaverde.modelo.dominio.EstadoActividad;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class VistaActividades extends VistaCrud<Actividad> {
    private final TextField txtCodigo = Componentes.campo("Código");
    private final TextField txtNombre = Componentes.campo("Nombre de la actividad");
    private final TextField txtMunicipio = Componentes.campo("Municipio");
    private final TextField txtDuracion = Componentes.campo("Horas");
    private final TextField txtPrecio = Componentes.campo("Precio por persona en pesos");
    private final TextField txtCupo = Componentes.campo("Cupo máximo por día");
    private final ComboBox<Dificultad> cmbDificultad = Componentes.combo("Seleccione");
    private final ComboBox<EstadoActividad> cmbEstado = Componentes.combo("Seleccione");

    public VistaActividades() {
        super("Actividades", "Caminatas, fincas cafeteras, aves y parques temáticos");
        cmbDificultad.getItems().addAll(Dificultad.values());
        cmbEstado.getItems().addAll(EstadoActividad.values());

        agregarCampo("Código", txtCodigo);
        agregarCampo("Nombre", txtNombre);
        agregarCampo("Municipio", txtMunicipio);
        agregarCampo("Duración (h)", txtDuracion);
        agregarCampo("Precio p/p", txtPrecio);
        agregarCampo("Cupo por día", txtCupo);
        agregarCampo("Dificultad", cmbDificultad);
        agregarCampo("Estado", cmbEstado);

        columna("Código", Actividad::getCodigo);
        columna("Nombre", Actividad::getNombre);
        columna("Municipio", Actividad::getMunicipio);
        columna("Horas", a -> String.valueOf(a.getDuracionHoras()));
        columna("Precio p/p", a -> Formatos.pesos(a.getPrecioPorPersona()));
        columna("Cupo", a -> String.valueOf(a.getCupoMaximoPorDia()));
        columna("Dificultad", a -> a.getDificultad().toString());
        columna("Estado", a -> a.getEstado().toString());
    }

    public String getCodigo() { return txtCodigo.getText(); }
    public String getNombre() { return txtNombre.getText(); }
    public String getMunicipio() { return txtMunicipio.getText(); }
    public String getDuracion() { return txtDuracion.getText(); }
    public String getPrecio() { return txtPrecio.getText(); }
    public String getCupo() { return txtCupo.getText(); }
    public Dificultad getDificultad() { return cmbDificultad.getValue(); }
    public EstadoActividad getEstado() { return cmbEstado.getValue(); }

    @Override
    public void mostrar(Actividad a) {
        txtCodigo.setText(a.getCodigo());
        txtCodigo.setDisable(true);
        txtNombre.setText(a.getNombre());
        txtMunicipio.setText(a.getMunicipio());
        txtDuracion.setText(String.valueOf(a.getDuracionHoras()));
        txtPrecio.setText(a.getPrecioPorPersona().toPlainString());
        txtCupo.setText(String.valueOf(a.getCupoMaximoPorDia()));
        cmbDificultad.setValue(a.getDificultad());
        cmbEstado.setValue(a.getEstado());
    }

    @Override
    public void limpiar() {
        for (TextField t : new TextField[]{txtCodigo, txtNombre, txtMunicipio, txtDuracion, txtPrecio, txtCupo}) {
            t.clear();
        }
        cmbDificultad.setValue(null);
        cmbEstado.setValue(null);
        txtCodigo.setDisable(false);
        getTabla().getSelectionModel().clearSelection();
    }
}
