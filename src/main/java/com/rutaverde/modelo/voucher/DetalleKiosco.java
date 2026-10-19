package com.rutaverde.modelo.voucher;

import com.rutaverde.modelo.dominio.Reserva;

import java.time.format.DateTimeFormatter;


public class DetalleKiosco implements DetalleVoucher {
    @Override
    public String generar(Reserva r) {
        String codigo = "RV" + r.getNumero() + "-" + r.getFechaViaje().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "Reserva " + r.getNumero() + " | " + r.getCliente().getNombreCompleto()
                + " | " + r.getFechaViaje() + " | " + r.getViajeros() + " viajeros | Código: " + codigo;
    }
}
