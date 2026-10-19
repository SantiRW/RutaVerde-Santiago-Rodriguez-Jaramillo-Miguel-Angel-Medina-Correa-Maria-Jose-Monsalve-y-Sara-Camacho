package com.rutaverde.modelo.voucher;

import com.rutaverde.modelo.dominio.Agencia;

public class EmisorVoucherAsesor extends EmisorVoucher {
    public EmisorVoucherAsesor(Agencia agencia) { super(agencia); }

    @Override protected DetalleVoucher crearDetalle() { return new DetalleAsesor(); }
}
