package com.rutaverde.modelo.voucher;

import com.rutaverde.modelo.dominio.Agencia;

public class EmisorVoucherKiosco extends EmisorVoucher {
    public EmisorVoucherKiosco(Agencia agencia) { super(agencia); }

    @Override protected DetalleVoucher crearDetalle() { return new DetalleKiosco(); }
}
