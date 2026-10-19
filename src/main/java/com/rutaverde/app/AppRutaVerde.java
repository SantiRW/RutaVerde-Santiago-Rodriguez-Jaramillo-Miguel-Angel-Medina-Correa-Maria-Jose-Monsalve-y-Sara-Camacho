package com.rutaverde.app;

import com.rutaverde.controlador.*;
import com.rutaverde.vista.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;


public class AppRutaVerde extends Application {

    @Override
    public void start(Stage escenario) {
        // Modelo
        ContextoAplicacion modelo = new ContextoAplicacion();
        DatosIniciales.cargar(modelo.agencia);

        // Vistas
        VistaInicio vInicio = new VistaInicio();
        VistaClientes vClientes = new VistaClientes();
        VistaGuias vGuias = new VistaGuias();
        VistaActividades vActividades = new VistaActividades();
        VistaPaquetes vPaquetes = new VistaPaquetes();
        VistaReservas vReservas = new VistaReservas();
        VistaReportes vReportes = new VistaReportes();
        VistaPrincipal vPrincipal = new VistaPrincipal();

        // Controladores
        List<Seccion> secciones = List.of(
                new Seccion("Inicio", vInicio, new ControladorInicio(vInicio, modelo.agencia)),
                new Seccion("Clientes", vClientes, new ControladorClientes(vClientes, modelo.agencia)),
                new Seccion("Guías", vGuias, new ControladorGuias(vGuias, modelo.agencia)),
                new Seccion("Actividades", vActividades, new ControladorActividades(vActividades, modelo.agencia)),
                new Seccion("Paquetes", vPaquetes, new ControladorPaquetes(vPaquetes, modelo.agencia)),
                new Seccion("Reservas", vReservas, new ControladorReservas(vReservas, modelo.agencia,
                        modelo.confirmador, modelo.calculadora, modelo::nuevaReserva, modelo.canales)),
                new Seccion("Reportes", vReportes, new ControladorReportes(vReportes, modelo.reporte)));
        new ControladorPrincipal(vPrincipal, secciones);

        Scene escena = new Scene(vPrincipal, 1280, 800);
        escena.getStylesheets().add(Estilos.HOJA);
        escenario.setTitle("RutaVerde Quindío - Gestión de turismo");
        escenario.setMinWidth(1150);
        escenario.setMinHeight(720);
        escenario.setScene(escena);
        escenario.show();
    }
}
