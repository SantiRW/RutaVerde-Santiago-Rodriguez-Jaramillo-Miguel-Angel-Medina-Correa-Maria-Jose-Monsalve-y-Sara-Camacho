package com.rutaverde.modelo.servicio;

public interface ServicioCorreo {
    void enviar(String destinatario, String asunto, String cuerpo);
}
