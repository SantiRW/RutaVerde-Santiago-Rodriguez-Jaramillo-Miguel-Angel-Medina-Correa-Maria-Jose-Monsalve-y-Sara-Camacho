package com.rutaverde.modelo.servicio;

import com.rutaverde.modelo.dominio.Reserva;

/**
 * Resultado de confirmar una reserva; la vista solo lo muestra.
 * {@code correoEnviado} indica si el cliente recibió el correo (voucher o motivo del rechazo).
 */

public record ResultadoConfirmacion(boolean exitoso, String mensaje, Reserva reserva, String voucher,
                                    boolean correoEnviado) { }
