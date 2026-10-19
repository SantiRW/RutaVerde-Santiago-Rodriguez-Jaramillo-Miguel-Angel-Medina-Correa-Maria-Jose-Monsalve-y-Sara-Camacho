package com.rutaverde.demo;

import com.rutaverde.modelo.dominio.*;
import com.rutaverde.modelo.excepcion.ReglaNegocioException;
import com.rutaverde.modelo.pago.*;
import com.rutaverde.modelo.repositorio.*;
import com.rutaverde.modelo.servicio.*;
import com.rutaverde.modelo.voucher.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/** Verificación del modelo por consola (la interfaz JavaFX vendrá después). */
public class DemoConsola {
    private static int fallos = 0;

    public static void main(String[] args) {
        // ---- Composición de dependencias ----
        Agencia agencia = new Agencia("RutaVerde Quindío", "900.123.456-7", "12345",
                "Cra 14 # 20-30, Armenia", "3001234567", "info@rutaverde.co", "www.rutaverde.co",
                new RepositorioMemoria<>(Cliente::getDocumento), new RepositorioMemoria<>(Guia::getIdentificacion),
                new RepositorioMemoria<>(Actividad::getCodigo), new RepositorioMemoria<>(Paquete::getCodigo),
                new RepositorioMemoria<>(Reserva::getNumero));
        ControlCupos cupos = new CuposEnMemoria();
        ServicioCorreo correo = new CorreoConsola();
        ConfirmadorReservas confirmador = new ConfirmadorReservas(cupos, new LuhnValidador(),
                new PagoAndinoAdapter(new PagoAndinoAPI()), agencia.getReservas(),
                new ConsecutivoMemoria(1), correo);
        EmisorVoucher voucherAsesor = new EmisorVoucherAsesor(agencia);
        EmisorVoucher voucherKiosco = new EmisorVoucherKiosco(agencia);
        ReservaBuilderFactory fabrica = new ReservaBuilderFactory();

        // ---- Datos ----
        Actividad cocora = new Actividad("A1", "Valle del Cocora", "Salento", 5, BigDecimal.valueOf(80_000), 10, Dificultad.MEDIA, EstadoActividad.DISPONIBLE);
        Actividad finca = new Actividad("A2", "Tour de café", "Salento", 3, BigDecimal.valueOf(60_000), 10, Dificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad parque = new Actividad("A3", "Parque del Café", "Montenegro", 6, BigDecimal.valueOf(120_000), 4, Dificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad aves = new Actividad("A4", "Avistamiento de aves", "Filandia", 4, BigDecimal.valueOf(70_000), 10, Dificultad.ALTA, EstadoActividad.DISPONIBLE);
        Actividad suspendida = new Actividad("A5", "Rafting", "Armenia", 2, BigDecimal.valueOf(90_000), 10, Dificultad.ALTA, EstadoActividad.SUSPENDIDA);
        for (Actividad a : List.of(cocora, finca, parque, aves, suspendida)) agencia.getActividades().agregar(a);

        Paquete salento = new Paquete("P1", "Paquete Salento", BigDecimal.TEN);
        salento.agregar(cocora); salento.agregar(finca);
        Paquete parques = new Paquete("P2", "Paquete Parques", BigDecimal.ZERO);
        parques.agregar(parque);
        Paquete total = new Paquete("P3", "Quindío Total", BigDecimal.valueOf(5));
        total.agregar(salento); total.agregar(parques); total.agregar(aves);
        agencia.getPaquetes().agregar(salento); agencia.getPaquetes().agregar(parques); agencia.getPaquetes().agregar(total);

        Cliente ana = new Cliente("1094", "Ana Gómez", "3115550000", "ana@correo.com", "Colombia", "Español");
        Cliente john = new Cliente("P-77", "John Smith", "+1555000", "john@mail.com", "EE. UU.", "Inglés");
        agencia.getClientes().agregar(ana); agencia.getClientes().agregar(john);
        Guia guiaEs = new Guia("G1", "Carlos Ríos", List.of("Español"), "3100000000", BigDecimal.valueOf(150_000));
        Guia guiaEn = new Guia("G2", "Laura Mesa", List.of("Español", "Inglés"), "3200000000", BigDecimal.valueOf(180_000));
        agencia.getGuias().agregar(guiaEs); agencia.getGuias().agregar(guiaEn);
        LocalDate viaje = LocalDate.now().plusDays(10);

        // ---- Pruebas ----
        System.out.println("== Luhn ==");
        LuhnValidador luhn = new LuhnValidador();
        verificar(luhn.esValida("4539 1488 0343 6467"), "Tarjeta válida aceptada");
        verificar(!luhn.esValida("4539 1488 0343 6468"), "Tarjeta inválida rechazada");

        System.out.println("== Composite ==");
        // Salento: (80000+60000)*0.90 = 126000 ; Parques: 120000 ; Total: (126000+120000+70000)*0.95 = 300200
        verificar(salento.getPrecioPorPersona().intValue() == 126_000, "Precio paquete Salento = 126.000");
        verificar(total.getPrecioPorPersona().intValue() == 300_200, "Precio Quindío Total = 300.200");
        verificar(total.getDuracionHoras() == 18, "Duración Quindío Total = 18 h");
        verificar(total.getActividades().size() == 4, "Quindío Total tiene 4 actividades");
        verificar(falla(() -> salento.agregar(total)), "Ciclo indirecto rechazado");
        verificar(falla(() -> salento.agregar(salento)), "Paquete no puede contenerse a sí mismo");
        verificar(falla(() -> parques.agregar(cocora)), "Actividad repetida (vía plan padre) rechazada");
        verificar(falla(() -> new Paquete("PX", "Malo", BigDecimal.valueOf(25))), "Descuento > 20 % rechazado");

        System.out.println("== Builder / reglas de reserva ==");
        verificar(falla(() -> fabrica.nueva().cliente(ana).producto(cocora).fechaViaje(viaje).viajeros(16).build()), "16 viajeros rechazado");
        verificar(falla(() -> fabrica.nueva().cliente(ana).producto(cocora).fechaViaje(LocalDate.now().minusDays(1)).viajeros(2).build()), "Fecha pasada rechazada");
        verificar(falla(() -> fabrica.nueva().cliente(ana).producto(suspendida).fechaViaje(viaje).viajeros(2).build()), "Actividad suspendida rechazada");
        verificar(falla(() -> fabrica.nueva().cliente(john).producto(cocora).fechaViaje(viaje).viajeros(2).guia(guiaEs).build()), "Guía sin idioma del cliente rechazado");
        verificar(falla(() -> fabrica.nueva().producto(cocora).fechaViaje(viaje).viajeros(2).build()), "Reserva sin cliente rechazada");
        // 3 viajeros x 300.200 = 900.600 + transporte 105.000 + seguro 45.000 + guía 150.000 x 3 días(18 h) = 450.000
        Reserva r1 = fabrica.nueva().cliente(ana).producto(total).fechaViaje(viaje).viajeros(3)
                .guia(guiaEs).conTransporte(true).conSeguro(true).observaciones("Alergia a las nueces").build();
        verificar(r1.getValor().intValue() == 1_500_600, "Valor de la reserva = 1.500.600");

        System.out.println("== Facade: confirmación ==");
        ResultadoConfirmacion ok = confirmador.confirmar(r1, "4539 1488 0343 6467", voucherAsesor);
        verificar(ok.exitoso() && r1.getNumero() == 1, "Reserva confirmada con consecutivo 1");
        System.out.println(ok.voucher());
        verificar(cupos.disponibles(parque, viaje) == 1, "Cupo del parque descontado (4 - 3 = 1)");

        Reserva r2 = fabrica.nueva().cliente(john).producto(parques).fechaViaje(viaje).viajeros(2).build();
        ResultadoConfirmacion sinCupo = confirmador.confirmar(r2, "4539 1488 0343 6467", voucherKiosco);
        verificar(!sinCupo.exitoso() && agencia.getReservas().listar().size() == 1, "Sobreventa evitada");

        Reserva r3 = fabrica.nueva().cliente(john).producto(cocora).fechaViaje(viaje).viajeros(2).build();
        verificar(!confirmador.confirmar(r3, "4539 1488 0343 6468", voucherKiosco).exitoso(), "Tarjeta inválida: no cobra");
        verificar(cupos.disponibles(cocora, viaje) == 7, "Sin descuento de cupos tras fallo");

        Reserva r4 = fabrica.nueva().cliente(john).producto(cocora).fechaViaje(viaje).viajeros(2).build();
        ResultadoConfirmacion kiosco = confirmador.confirmar(r4, "4539 1488 0343 6467", voucherKiosco);
        verificar(kiosco.exitoso(), "Confirmación desde el kiosco");
        System.out.println(kiosco.voucher());

        System.out.println("== Reportes ==");
        ReporteIngresos reporte = new ReporteIngresos(agencia.getReservas());
        BigDecimal ingresos = reporte.ingresosEnPeriodo(viaje, viaje);
        verificar(ingresos.equals(r1.getValor().add(r4.getValor())), "Ingresos del periodo (fechas incluidas)");
        verificar(reporte.productoConMasViajeros().get().producto() == total, "Producto con más viajeros = Quindío Total (3)");

        System.out.println(fallos == 0 ? "\nTODAS LAS PRUEBAS PASARON" : "\nFALLARON " + fallos + " PRUEBAS");
        if (fallos > 0) System.exit(1);
    }

    /** Fábrica auxiliar solo para la demo: reloj del sistema y calculadora por defecto. */
    static class ReservaBuilderFactory {
        private final CalculadoraValorReserva calculadora = new CalculadoraValorReserva();
        ReservaBuilder nueva() { return new ReservaBuilder(calculadora, Clock.systemDefaultZone()); }
    }

    private static boolean falla(Runnable accion) {
        try { accion.run(); return false; } catch (ReglaNegocioException e) { return true; }
    }

    private static void verificar(boolean condicion, String descripcion) {
        System.out.println((condicion ? "  OK   " : "  FALLA ") + descripcion);
        if (!condicion) fallos++;
    }
}
