package com.rutaverde.controlador;

import com.rutaverde.modelo.excepcion.ReglaNegocioException;
import com.rutaverde.modelo.servicio.ReporteIngresos;
import com.rutaverde.vista.Formatos;
import com.rutaverde.vista.Mensajes;
import com.rutaverde.vista.VistaReportes;

public class ControladorReportes implements Refrescable {
    private final VistaReportes vista;
    private final ReporteIngresos reporte;

    public ControladorReportes(VistaReportes vista, ReporteIngresos reporte) {
        this.vista = vista;
        this.reporte = reporte;
        vista.getBtnIngresos().setOnAction(e -> calcularIngresos());
        vista.getBtnTop().setOnAction(e -> consultarTop());
    }

    @Override
    public void refrescar() {
        // Los reportes se calculan cuando el usuario los pide.
    }

    private void calcularIngresos() {
        try {
            var total = reporte.ingresosEnPeriodo(vista.getInicio(), vista.getFin());
            vista.mostrarIngresos(Formatos.pesos(total),
                    "Del " + vista.getInicio() + " al " + vista.getFin() + " (ambas fechas incluidas).");
        } catch (ReglaNegocioException e) {
            Mensajes.error(e.getMessage());
        }
    }

    private void consultarTop() {
        reporte.productoConMasViajeros().ifPresentOrElse(
                r -> vista.mostrarTop(r.producto().getNombre(), r.viajeros() + " viajeros en reservas confirmadas."),
                () -> vista.mostrarTop("-", "Todavía no hay reservas confirmadas."));
    }
}
