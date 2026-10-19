package com.rutaverde.controlador;

import com.rutaverde.modelo.dominio.Actividad;
import com.rutaverde.modelo.dominio.Agencia;
import com.rutaverde.modelo.dominio.Paquete;
import com.rutaverde.modelo.excepcion.ReglaNegocioException;
import com.rutaverde.vista.Mensajes;
import com.rutaverde.vista.VistaActividades;

import java.util.Optional;

public class ControladorActividades implements Refrescable {
    private final VistaActividades vista;
    private final Agencia agencia;

    public ControladorActividades(VistaActividades vista, Agencia agencia) {
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
    public void refrescar() { vista.setItems(agencia.getActividades().listar()); }

    private void guardar() {
        try {
            Actividad nueva = new Actividad(vista.getCodigo(), vista.getNombre(), vista.getMunicipio(),
                    Entradas.entero(vista.getDuracion(), "duración (horas)"),
                    Entradas.decimal(vista.getPrecio(), "precio por persona"),
                    Entradas.entero(vista.getCupo(), "cupo máximo por día"),
                    vista.getDificultad(), vista.getEstado());
            Optional<Actividad> existente = agencia.getActividades().buscar(nueva.getCodigo());
            if (existente.isPresent()) {
                Actividad a = existente.get();
                a.setNombre(nueva.getNombre());
                a.setMunicipio(nueva.getMunicipio());
                a.setDuracionHoras(nueva.getDuracionHoras());
                a.setPrecioPorPersona(nueva.getPrecioPorPersona());
                a.setCupoMaximoPorDia(nueva.getCupoMaximoPorDia());
                a.setDificultad(nueva.getDificultad());
                a.setEstado(nueva.getEstado());
            } else {
                agencia.getActividades().agregar(nueva);
            }
            refrescar();
            vista.limpiar();
            vista.exito("Actividad guardada correctamente.");
        } catch (ReglaNegocioException e) {
            Mensajes.error(e.getMessage());
        }
    }

    private void eliminar() {
        Actividad a = vista.getTabla().getSelectionModel().getSelectedItem();
        if (a == null) {
            Mensajes.error("Seleccione una actividad de la tabla.");
            return;
        }
        boolean enPaquete = agencia.getPaquetes().listar().stream().anyMatch((Paquete p) -> p.contiene(a));
        boolean enReserva = agencia.getReservas().listar().stream().anyMatch(r -> r.getProducto().contiene(a));
        if (enPaquete || enReserva) {
            Mensajes.error("La actividad hace parte de paquetes o reservas. Puede suspenderla en lugar de eliminarla.");
            return;
        }
        if (Mensajes.confirmar("¿Eliminar la actividad " + a.getNombre() + "?")) {
            agencia.getActividades().eliminar(a.getCodigo());
            vista.limpiar();
            refrescar();
            vista.exito("Actividad eliminada.");
        }
    }
}
