package com.rutaverde.modelo.dominio;

import com.rutaverde.modelo.excepcion.ReglaNegocioException;

import java.time.Clock;
import java.time.LocalDate;

/**
 * PATRÓN BUILDER (creacional): la reserva tiene datos obligatorios y varios opcionales
 * (guía, transporte, seguro, observaciones); el builder los arma y valida al construir.
 */
public class ReservaBuilder {

    //Atributos
    Cliente cliente;
    Producto producto;
    LocalDate fechaViaje;
    int viajeros;
    Guia guia;
    boolean transporte;
    boolean seguro;
    String observaciones = "";

    private final CalculadoraValorReserva calculadora;
    private final Clock reloj;

    public ReservaBuilder(CalculadoraValorReserva calculadora, Clock reloj) {
        this.calculadora = calculadora;
        this.reloj = reloj;
    }

    public ReservaBuilder cliente(Cliente cliente) { this.cliente = cliente; return this; }
    public ReservaBuilder producto(Producto producto) { this.producto = producto; return this; }
    public ReservaBuilder fechaViaje(LocalDate fecha) { this.fechaViaje = fecha; return this; }
    public ReservaBuilder viajeros(int viajeros) { this.viajeros = viajeros; return this; }
    public ReservaBuilder guia(Guia guia) { this.guia = guia; return this; }
    public ReservaBuilder conTransporte(boolean transporte) { this.transporte = transporte; return this; }
    public ReservaBuilder conSeguro(boolean seguro) { this.seguro = seguro; return this; }
    public ReservaBuilder observaciones(String texto) { this.observaciones = texto == null ? "" : texto.trim(); return this; }

    public Reserva build() {
        if (cliente == null) throw new ReglaNegocioException("No puede existir una reserva sin cliente");
        if (producto == null) throw new ReglaNegocioException("No puede existir una reserva sin producto");
        if (producto.getActividades().isEmpty()) {
            throw new ReglaNegocioException("El producto '" + producto.getNombre() + "' no contiene actividades");
        }
        if (fechaViaje == null) throw new ReglaNegocioException("La fecha del viaje es obligatoria");
        if (fechaViaje.isBefore(LocalDate.now(reloj))) {
            throw new ReglaNegocioException("La fecha del viaje no puede estar en el pasado");
        }
        if (viajeros < Reserva.MIN_VIAJEROS || viajeros > Reserva.MAX_VIAJEROS) {
            throw new ReglaNegocioException("El grupo debe tener entre 1 y 15 viajeros");
        }
        for (Actividad a : producto.getActividades()) {
            if (a.estaSuspendida()) {
                throw new ReglaNegocioException("No se puede reservar: la actividad '" + a.getNombre() + "' está suspendida");
            }
        }
        if (guia != null && !guia.hablaIdioma(cliente.getIdiomaPreferido())) {
            throw new ReglaNegocioException("El guía debe hablar el idioma preferido del cliente ("
                    + cliente.getIdiomaPreferido() + ")");
        }
        return new Reserva(this, calculadora.calcular(producto, viajeros, guia, transporte, seguro));
    }
}
