package com.rutaverde.modelo.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/** Se crea únicamente mediante {@link ReservaBuilder}, que valida las reglas de negocio. */
public class Reserva {
    public static final int MIN_VIAJEROS = 1;
    public static final int MAX_VIAJEROS = 15;

    private final Cliente cliente;
    private final Producto producto;
    private final LocalDate fechaViaje;
    private final int viajeros;
    private final Guia guia;
    private final boolean transporte;
    private final boolean seguro;
    private final String observaciones;
    private final BigDecimal valor;

    private Integer numero;
    private String autorizacion;
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    Reserva(ReservaBuilder b, BigDecimal valor) {
        this.cliente = b.cliente;
        this.producto = b.producto;
        this.fechaViaje = b.fechaViaje;
        this.viajeros = b.viajeros;
        this.guia = b.guia;
        this.transporte = b.transporte;
        this.seguro = b.seguro;
        this.observaciones = b.observaciones;
        this.valor = valor;
    }

    public void confirmar(int numero, String autorizacion) {
        this.numero = numero;
        this.autorizacion = autorizacion;
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public Cliente getCliente() { return cliente; }
    public Producto getProducto() { return producto; }
    public LocalDate getFechaViaje() { return fechaViaje; }
    public int getViajeros() { return viajeros; }
    public Optional<Guia> getGuia() { return Optional.ofNullable(guia); }
    public boolean incluyeTransporte() { return transporte; }
    public boolean incluyeSeguro() { return seguro; }
    public String getObservaciones() { return observaciones; }
    public BigDecimal getValor() { return valor; }
    public Integer getNumero() { return numero; }
    public String getAutorizacion() { return autorizacion; }
    public EstadoReserva getEstado() { return estado; }
    public boolean estaConfirmada() { return estado == EstadoReserva.CONFIRMADA; }
}
