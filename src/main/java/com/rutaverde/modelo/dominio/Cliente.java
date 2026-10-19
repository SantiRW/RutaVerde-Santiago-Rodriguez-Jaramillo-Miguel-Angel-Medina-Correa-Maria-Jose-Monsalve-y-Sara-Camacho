package com.rutaverde.modelo.dominio;

public class Cliente {
    private final String documento;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String paisOrigen;
    private String idiomaPreferido;

    public Cliente(String documento, String nombreCompleto, String telefono, String correo,
                   String paisOrigen, String idiomaPreferido) {
        this.documento = Validaciones.texto(documento, "documento o pasaporte");
        setNombreCompleto(nombreCompleto);
        setTelefono(telefono);
        setCorreo(correo);
        setPaisOrigen(paisOrigen);
        setIdiomaPreferido(idiomaPreferido);
    }

    public String getDocumento() { return documento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getPaisOrigen() { return paisOrigen; }
    public String getIdiomaPreferido() { return idiomaPreferido; }

    public void setNombreCompleto(String v) { this.nombreCompleto = Validaciones.texto(v, "nombre completo"); }
    public void setTelefono(String v) { this.telefono = Validaciones.texto(v, "teléfono"); }
    public void setCorreo(String v) { this.correo = Validaciones.correo(v); }
    public void setPaisOrigen(String v) { this.paisOrigen = Validaciones.texto(v, "país de origen"); }
    public void setIdiomaPreferido(String v) { this.idiomaPreferido = Validaciones.texto(v, "idioma preferido"); }

    @Override public String toString() { return nombreCompleto + " (" + documento + ")"; }
}
