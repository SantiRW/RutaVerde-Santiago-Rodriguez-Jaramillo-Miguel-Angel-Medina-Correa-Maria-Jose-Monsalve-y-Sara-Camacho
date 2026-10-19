package com.rutaverde.modelo.servicio;

import com.rutaverde.modelo.dominio.Reserva;

/** Resultado de confirmar una reserva; la vista solo lo muestra. */
public record ResultadoConfirmacion(boolean exitoso, String mensaje, Reserva reserva, String voucher) { }
