package com.rutaverde.controlador;

import com.rutaverde.modelo.dominio.Agencia;
import com.rutaverde.modelo.dominio.Guia;
import com.rutaverde.modelo.excepcion.ReglaNegocioException;
import com.rutaverde.vista.Mensajes;
import com.rutaverde.vista.VistaGuias;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ControladorGuias implements Refrescable {
    private final VistaGuias vista;
    private final Agencia agencia;

    public ControladorGuias(VistaGuias vista, Agencia agencia) {
        this.vista = vista;
        this.agencia = agencia;
        vista.getTabla().getSelectionModel().selectedItemProperty().addListener((o, antes, ahora) -> {
            if (ahora != null) vista.mostrar(ahora);
        });
        vista.getBtnGuardar().setOnAction(e -> guardar());
        vista.getBtnNuevo().setOnAction(e -> vista.limpiar());
        vista.getBtnEliminar().setOnAction(e -> eliminar());
        refrescar();
    }

    @Override
    public void refrescar() { vista.setItems(agencia.getGuias().listar()); }

    private void guardar() {
        try {
            List<String> idiomas = Arrays.stream(vista.getIdiomas().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).toList();
            Guia nuevo = new Guia(vista.getIdentificacion(), vista.getNombre(), idiomas, vista.getTelefono(),
                    Entradas.decimal(vista.getTarifa(), "tarifa por día"));
            Optional<Guia> existente = agencia.getGuias().buscar(nuevo.getIdentificacion());
            if (existente.isPresent()) {
                Guia g = existente.get();
                g.setNombre(nuevo.getNombre());
                g.setIdiomas(nuevo.getIdiomas());
                g.setTelefono(nuevo.getTelefono());
                g.setTarifaPorDia(nuevo.getTarifaPorDia());
            } else {
                agencia.getGuias().agregar(nuevo);
            }
            refrescar();
            vista.limpiar();
            vista.exito("Guía guardado correctamente.");
        } catch (ReglaNegocioException e) {
            Mensajes.error(e.getMessage());
        }
    }

    private void eliminar() {
        Guia g = vista.getTabla().getSelectionModel().getSelectedItem();
        if (g == null) {
            Mensajes.error("Seleccione un guía de la tabla.");
            return;
        }
        boolean enReservas = agencia.getReservas().listar().stream()
                .anyMatch(r -> r.getGuia().filter(x -> x == g).isPresent());
        if (enReservas) {
            Mensajes.error("El guía está asignado a reservas y no se puede eliminar.");
            return;
        }
        if (Mensajes.confirmar("¿Eliminar al guía " + g.getNombre() + "?")) {
            agencia.getGuias().eliminar(g.getIdentificacion());
            vista.limpiar();
            refrescar();
            vista.exito("Guía eliminado.");
        }
    }
}
