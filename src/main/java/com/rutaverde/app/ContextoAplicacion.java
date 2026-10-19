package com.rutaverde.app;

import com.rutaverde.modelo.dominio.*;
import com.rutaverde.modelo.pago.LuhnValidador;
import com.rutaverde.modelo.pago.PagoAndinoAPI;
import com.rutaverde.modelo.pago.PagoAndinoAdapter;
import com.rutaverde.modelo.repositorio.ConsecutivoMemoria;
import com.rutaverde.modelo.repositorio.RepositorioMemoria;
import com.rutaverde.modelo.servicio.*;
import com.rutaverde.modelo.voucher.EmisorVoucher;
import com.rutaverde.modelo.voucher.EmisorVoucherAsesor;
import com.rutaverde.modelo.voucher.EmisorVoucherKiosco;

import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.Map;


public class ContextoAplicacion {
    public final Agencia agencia;
    public final ConfirmadorReservas confirmador;
    public final ReporteIngresos reporte;
    public final CalculadoraValorReserva calculadora = new CalculadoraValorReserva();
    public final Map<String, EmisorVoucher> canales = new LinkedHashMap<>();
    private final Clock reloj = Clock.systemDefaultZone();

    public ContextoAplicacion() {
        agencia = new Agencia("RutaVerde Quindío", "900.123.456-7", "12345",
                "Cra 14 # 20-30, Armenia, Quindío", "3001234567", "info@rutaverde.co", "www.rutaverde.co",
                new RepositorioMemoria<>(Cliente::getDocumento), new RepositorioMemoria<>(Guia::getIdentificacion),
                new RepositorioMemoria<>(Actividad::getCodigo), new RepositorioMemoria<>(Paquete::getCodigo),
                new RepositorioMemoria<>(Reserva::getNumero));
        confirmador = new ConfirmadorReservas(new CuposEnMemoria(), new LuhnValidador(),
                new PagoAndinoAdapter(new PagoAndinoAPI()), agencia.getReservas(),
                new ConsecutivoMemoria(1), new CorreoConsola());
        reporte = new ReporteIngresos(agencia.getReservas());
        canales.put("Pantalla del asesor (voucher detallado)", new EmisorVoucherAsesor(agencia));
        canales.put("Kiosco de la terminal (tiquete corto)", new EmisorVoucherKiosco(agencia));
    }

    public ReservaBuilder nuevaReserva() { return new ReservaBuilder(calculadora, reloj); }
}
