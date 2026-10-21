package com.rutaverde.controlador;

import com.rutaverde.modelo.dominio.Agencia;
import com.rutaverde.modelo.dominio.Paquete;
import com.rutaverde.modelo.dominio.Producto;
import com.rutaverde.modelo.excepcion.ReglaNegocioException;
import com.rutaverde.vista.Formatos;
import com.rutaverde.vista.Mensajes;
import com.rutaverde.vista.VistaPaquetes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControladorPaquetes implements Refrescable {
    private final VistaPaquetes vista;
    private final Agencia agencia;

    public ControladorPaquetes(VistaPaquetes vista, Agencia agencia) {
        this.vista = vista;
        this.agencia = agencia;
        vista.getTabla().getSelectionModel().selectedItemProperty().addListener((o, antes, ahora) -> {
            if (ahora != null) {
                vista.mostrar(ahora);
                cargarContenido(ahora);
            }
        });
        vista.getBtnGuardar().setOnAction(e -> guardar());
        vista.getBtnNuevo().setOnAction(e -> vista.limpiar());
        vista.getBtnEliminar().setOnAction(e -> eliminar());
        vista.getBtnAgregar().setOnAction(e -> agregarContenido());
        vista.getBtnQuitar().setOnAction(e -> quitarContenido());
        refrescar();
    }

    @Override
    public void refrescar() {
        vista.setItems(agencia.getPaquetes().listar());
        Paquete p = vista.getTabla().getSelectionModel().getSelectedItem();
        if (p != null) cargarContenido(p);
    }

    private void cargarContenido(Paquete p) {
        vista.setContenido(p.getContenido());
        List<Producto> candidatos = new ArrayList<>(agencia.getActividades().listar());
        for (Paquete otro : agencia.getPaquetes().listar()) {
            if (otro != p && !otro.contiene(p)) candidatos.add(otro);
        }
        candidatos.removeAll(p.getContenido());
        vista.setDisponibles(candidatos);
        vista.setResumen(Formatos.pesos(p.getPrecioPorPersona()), p.getDuracionHoras() + " h",
                String.valueOf(p.getActividades().size()));
    }

    private void guardar() {
        try {
            Paquete nuevo = new Paquete(vista.getCodigo(), vista.getNombre(),
                    Entradas.decimal(vista.getDescuento(), "descuento (%)"));
            // Fila seleccionada = edición; sin selección = paquete nuevo (un código repetido se rechaza).
            Paquete guardado = vista.getTabla().getSelectionModel().getSelectedItem();
            if (guardado != null) {
                guardado.setNombre(nuevo.getNombre());
                guardado.setDescuentoPorcentaje(nuevo.getDescuentoPorcentaje());
            } else {
                agencia.getPaquetes().agregar(nuevo);
                guardado = nuevo;
            }
            refrescar();
            vista.getTabla().getSelectionModel().select(guardado); // para poder armar su contenido
            cargarContenido(guardado);
            vista.exito("Paquete guardado. Ya puede agregarle contenido.");
        } catch (ReglaNegocioException e) {
            Mensajes.error(e.getMessage());
        }
    }

    private void agregarContenido() {
        Paquete p = vista.getTabla().getSelectionModel().getSelectedItem();
        Producto hijo = vista.getProductoAAgregar();
        if (p == null) {
            Mensajes.error("Guarde o seleccione un paquete antes de agregarle contenido.");
            return;
        }
        if (hijo == null) {
            Mensajes.error("Elija la actividad o el paquete que desea agregar.");
            return;
        }
        try {
            p.agregar(hijo);
            refrescar();
            cargarContenido(p);
            vista.exito("Contenido agregado.");
        } catch (ReglaNegocioException e) {
            Mensajes.error(e.getMessage());
        }
    }

    private void quitarContenido() {
        Paquete p = vista.getTabla().getSelectionModel().getSelectedItem();
        Producto hijo = vista.getContenidoSeleccionado();
        if (p == null || hijo == null) {
            Mensajes.error("Seleccione un paquete y el elemento de su contenido que desea quitar.");
            return;
        }
        p.remover(hijo);
        refrescar();
        cargarContenido(p);
        vista.exito("Contenido quitado.");
    }

    private void eliminar() {
        Paquete p = vista.getTabla().getSelectionModel().getSelectedItem();
        if (p == null) {
            Mensajes.error("Seleccione un paquete de la tabla.");
            return;
        }
        boolean enOtroPaquete = agencia.getPaquetes().listar().stream().anyMatch(q -> q != p && q.contiene(p));
        boolean enReserva = agencia.getReservas().listar().stream().anyMatch(r -> r.getProducto().contiene(p));
        if (enOtroPaquete || enReserva) {
            Mensajes.error("El paquete hace parte de otro paquete o de reservas y no se puede eliminar.");
            return;
        }
        if (Mensajes.confirmar("¿Eliminar el paquete " + p.getNombre() + "?")) {
            // Se desvincula el contenido para que sus hijos no queden con un padre inexistente.
            for (Producto hijo : new ArrayList<>(p.getContenido())) {
                p.remover(hijo);
            }
            agencia.getPaquetes().eliminar(p.getCodigo());
            vista.limpiar();
            refrescar();
            vista.exito("Paquete eliminado.");
        }
    }
}
