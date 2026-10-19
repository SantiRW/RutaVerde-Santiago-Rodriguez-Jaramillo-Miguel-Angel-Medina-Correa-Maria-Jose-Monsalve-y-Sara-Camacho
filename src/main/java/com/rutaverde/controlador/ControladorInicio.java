package com.rutaverde.controlador;

import com.rutaverde.modelo.dominio.Agencia;
import com.rutaverde.vista.VistaInicio;

public class ControladorInicio implements Refrescable {
    private final VistaInicio vista;
    private final Agencia agencia;

    public ControladorInicio(VistaInicio vista, Agencia agencia) {
        this.vista = vista;
        this.agencia = agencia;
        refrescar();
    }

    @Override
    public void refrescar() {
        vista.actualizar(agencia.getClientes().listar().size(), agencia.getGuias().listar().size(),
                agencia.getActividades().listar().size(), agencia.getPaquetes().listar().size(),
                agencia.getReservas().listar().size());
    }
}
