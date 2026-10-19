package com.rutaverde.modelo.dominio;

import com.rutaverde.modelo.repositorio.Repositorio;

/** Datos básicos de la agencia y acceso a los registros que administra. */
public class Agencia {

    private final String nombreComercial;
    private final String nit;
    private final String rnt;
    private final String direccion;
    private final String telefono;
    private final String correo;
    private final String paginaWeb;

    private final Repositorio<Cliente, String> clientes;
    private final Repositorio<Guia, String> guias;
    private final Repositorio<Actividad, String> actividades;
    private final Repositorio<Paquete, String> paquetes;
    private final Repositorio<Reserva, Integer> reservas;

    public Agencia(String nombreComercial, String nit, String rnt, String direccion,
                   String telefono, String correo, String paginaWeb,
                   Repositorio<Cliente, String> clientes, Repositorio<Guia, String> guias,
                   Repositorio<Actividad, String> actividades, Repositorio<Paquete, String> paquetes,
                   Repositorio<Reserva, Integer> reservas) {
        this.nombreComercial = Validaciones.texto(nombreComercial, "nombre comercial");
        this.nit = Validaciones.texto(nit, "NIT");
        this.rnt = Validaciones.texto(rnt, "RNT");
        this.direccion = Validaciones.texto(direccion, "dirección");
        this.telefono = Validaciones.texto(telefono, "teléfono");
        this.correo = Validaciones.correo(correo);
        this.paginaWeb = Validaciones.texto(paginaWeb, "página web");
        this.clientes = clientes;
        this.guias = guias;
        this.actividades = actividades;
        this.paquetes = paquetes;
        this.reservas = reservas;
    }

    public String getNombreComercial() { return nombreComercial; }
    public String getNit() { return nit; }
    public String getRnt() { return rnt; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getPaginaWeb() { return paginaWeb; }

    public Repositorio<Cliente, String> getClientes() { return clientes; }
    public Repositorio<Guia, String> getGuias() { return guias; }
    public Repositorio<Actividad, String> getActividades() { return actividades; }
    public Repositorio<Paquete, String> getPaquetes() { return paquetes; }
    public Repositorio<Reserva, Integer> getReservas() { return reservas; }
}
