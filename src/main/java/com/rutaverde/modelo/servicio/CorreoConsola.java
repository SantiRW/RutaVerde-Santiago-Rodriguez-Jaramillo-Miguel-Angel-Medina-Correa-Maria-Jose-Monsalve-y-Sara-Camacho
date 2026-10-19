package com.rutaverde.modelo.servicio;

/** Simulación del envío: imprime el correo. Se puede reemplazar por SMTP real. */
public class CorreoConsola implements ServicioCorreo {
    @Override
    public void enviar(String destinatario, String asunto, String cuerpo) {
        System.out.println("[CORREO] Para: " + destinatario + " | Asunto: " + asunto);
        System.out.println(cuerpo);
        System.out.println("[/CORREO]");
    }
}
