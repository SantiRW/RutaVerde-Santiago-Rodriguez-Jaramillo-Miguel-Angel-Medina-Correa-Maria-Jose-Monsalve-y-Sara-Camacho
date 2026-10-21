package com.rutaverde.controlador;

import com.rutaverde.modelo.dominio.Agencia;
import com.rutaverde.modelo.dominio.Cliente;
import com.rutaverde.modelo.excepcion.ReglaNegocioException;
import com.rutaverde.vista.Mensajes;
import com.rutaverde.vista.VistaClientes;

import java.util.Optional;

public class ControladorClientes implements Refrescable {
    private final VistaClientes vista;
    private final Agencia agencia;

    public ControladorClientes(VistaClientes vista, Agencia agencia) {
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
    public void refrescar() { vista.setItems(agencia.getClientes().listar()); }

    private void guardar() {
        try {
            // Se construye primero para validar todos los campos antes de modificar nada.
            Cliente nuevo = new Cliente(vista.getDocumento(), vista.getNombre(), vista.getTelefono(),
                    vista.getCorreo(), vista.getPais(), vista.getIdioma());
            // Si hay una fila seleccionada se está editando (el documento está bloqueado);
            // si no, es un cliente nuevo y un documento repetido se rechaza en vez de pisar al anterior.
            Cliente c = vista.getTabla().getSelectionModel().getSelectedItem();
            if (c != null) {
                c.setNombreCompleto(nuevo.getNombreCompleto());
                c.setTelefono(nuevo.getTelefono());
                c.setCorreo(nuevo.getCorreo());
                c.setPaisOrigen(nuevo.getPaisOrigen());
                c.setIdiomaPreferido(nuevo.getIdiomaPreferido());
            } else {
                agencia.getClientes().agregar(nuevo);
            }
            refrescar();
            vista.limpiar();
            vista.exito("Cliente guardado correctamente.");
        } catch (ReglaNegocioException e) {
            Mensajes.error(e.getMessage());
        }
    }

    private void eliminar() {
        Cliente c = vista.getTabla().getSelectionModel().getSelectedItem();
        if (c == null) {
            Mensajes.error("Seleccione un cliente de la tabla.");
            return;
        }
        boolean conReservas = agencia.getReservas().listar().stream().anyMatch(r -> r.getCliente() == c);
        if (conReservas) {
            Mensajes.error("El cliente tiene reservas registradas y no se puede eliminar.");
            return;
        }
        if (Mensajes.confirmar("¿Eliminar al cliente " + c.getNombreCompleto() + "?")) {
            agencia.getClientes().eliminar(c.getDocumento());
            vista.limpiar();
            refrescar();
            vista.exito("Cliente eliminado.");
        }
    }
}