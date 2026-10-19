package com.rutaverde.modelo.voucher;

import com.rutaverde.modelo.dominio.Reserva;


public interface DetalleVoucher {
    String generar(Reserva reserva);
}
