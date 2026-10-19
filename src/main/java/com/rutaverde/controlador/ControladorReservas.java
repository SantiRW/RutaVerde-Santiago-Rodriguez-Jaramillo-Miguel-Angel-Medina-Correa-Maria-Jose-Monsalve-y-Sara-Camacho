package com.rutaverde.controlador;

import com.rutaverde.modelo.dominio.Agencia;
import com.rutaverde.modelo.dominio.CalculadoraValorReserva;
import com.rutaverde.modelo.dominio.Producto;
import com.rutaverde.modelo.dominio.Reserva;
import com.rutaverde.modelo.dominio.ReservaBuilder;
import com.rutaverde.modelo.excepcion.ReglaNegocioException;
import com.rutaverde.modelo.servicio.ConfirmadorReservas;
import com.rutaverde.modelo.servicio.ResultadoConfirmacion;
import com.rutaverde.modelo.voucher.EmisorVoucher;
import com.rutaverde.vista.Formatos;
import com.rutaverde.vista.Mensajes;
import com.rutaverde.vista.VistaReservas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/** Arma la reserva con los datos de la pantalla y delega todo el proceso en la fachada del modelo. */
public class ControladorReservas implements Refrescable {
    private final VistaReservas vista;
    private final Agencia agencia;
    private final ConfirmadorReservas confirmador;
    private final CalculadoraValorReserva calculadora;
    private final Supplier<ReservaBuilder> fabricaBuilder;
    private final Map<String, EmisorVoucher> canales;

    public ControladorReservas(VistaReservas vista, Agencia agencia, ConfirmadorReservas confirmador,
                               CalculadoraValorReserva calculadora, Supplier<ReservaBuilder> fabricaBuilder,
                               Map<String, EmisorVoucher> canales) {
        this.vista = vista;
        this.agencia = agencia;
        this.confirmador = confirmador;
        this.calculadora = calculadora;
        this.fabricaBuilder = fabricaBuilder;
        this.canales = canales;
        vista.getBtnCotizar().setOnAction(e -> cotizar());
        vista.getBtnConfirmar().setOnAction(e -> confirmar());
        refrescar();
    }

    @Override
    public void refrescar() {
        vista.setClientes(agencia.getClientes().listar());
        vista.setGuias(agencia.getGuias().listar());
        List<Producto> productos = new ArrayList<>(agencia.getActividades().listar());
        productos.addAll(agencia.getPaquetes().listar());
        vista.setProductos(productos);
        vista.setCanales(new ArrayList<>(canales.keySet()));
        vista.setReservas(agencia.getReservas().listar());
    }

    private Reserva construir() {
        if (vista.isGuiaMarcada() && vista.getGuia() == null) {
            throw new ReglaNegocioException("Seleccione el guía o desmarque la opción 'Asignar guía'");
        }
        return fabricaBuilder.get()
                .cliente(vista.getCliente())
                .producto(vista.getProducto())
                .fechaViaje(vista.getFecha())
                .viajeros(vista.getViajeros())
                .guia(vista.getGuia())
                .conTransporte(vista.isTransporte())
                .conSeguro(vista.isSeguro())
                .observaciones(vista.getObservaciones())
                .build();
    }

    private void cotizar() {
        try {
            Reserva r = construir();
            Producto p = r.getProducto();
            String detalle = "Precio por persona: " + Formatos.pesos(p.getPrecioPorPersona())
                    + "  ·  Duración: " + p.getDuracionHoras() + " h"
                    + "  ·  Actividades: " + p.getActividades().size()
                    + (r.getGuia().isPresent() ? "  ·  Días de guía: " + calculadora.diasDeGuia(p) : "");
            vista.mostrarValor(Formatos.pesos(r.getValor()), detalle);
        } catch (ReglaNegocioException e) {
            Mensajes.error(e.getMessage());
        }
    }

    private void confirmar() {
        try {
            EmisorVoucher emisor = canales.get(vista.getCanal());
            if (emisor == null) {
                throw new ReglaNegocioException("Seleccione el canal de confirmación");
            }
            Reserva reserva = construir();
            vista.mostrarValor(Formatos.pesos(reserva.getValor()), "Procesando la confirmación...");
            ResultadoConfirmacion resultado = confirmador.confirmar(reserva, vista.getTarjeta(), emisor);
            if (resultado.exitoso()) {
                vista.mostrarVoucher(resultado.voucher());
                vista.mostrarValor(Formatos.pesos(reserva.getValor()), resultado.mensaje());
                vista.limpiarPago();
                vista.setReservas(agencia.getReservas().listar());
                Mensajes.info("Reserva confirmada", resultado.mensaje()
                        + "\nSe envió el voucher a " + reserva.getCliente().getCorreo() + ".");
            } else {
                vista.mostrarVoucher("");
                Mensajes.error(resultado.mensaje()
                        + "\n\nSe envió un correo al cliente con el motivo y no se descontó ningún cupo.");
            }
        } catch (ReglaNegocioException e) {
            Mensajes.error(e.getMessage());
        }
    }
}
