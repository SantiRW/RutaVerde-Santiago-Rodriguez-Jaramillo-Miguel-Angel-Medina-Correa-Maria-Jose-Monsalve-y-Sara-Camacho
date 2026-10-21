# RutaVerde Quindío

Sistema de gestión de clientes, guías, actividades, paquetes turísticos y reservas para la agencia de turismo RutaVerde Quindío, desarrollado en Java con interfaz gráfica JavaFX bajo arquitectura MVC.

## Integrantes

- Santiago Rodríguez Jaramillo
- Miguel Ángel Medina Correa
- Sara Camacho Rodríguez
- María José Monsalve

## Funcionalidades principales

- Registro y administración de clientes, guías, actividades, paquetes y reservas.
- Composición de paquetes turísticos a partir de actividades y otros paquetes (precio y duración calculados recursivamente).
- Confirmación de reservas con validación de cupos, cobro mediante pasarela de pago (PagoAndino) y validación de tarjeta con el algoritmo de Luhn.
- Emisión de voucher según el canal de confirmación (asesor, kiosco).
- Reportes de ingresos y de producto con más viajeros en un periodo determinado.

## Arquitectura

El proyecto sigue una organización en paquetes correspondiente al patrón MVC:

- `com.rutaverde.modelo`: entidades de dominio, servicios, repositorios, pagos y vouchers.
- `com.rutaverde.vista`: componentes y pantallas JavaFX.
- `com.rutaverde.controlador`: controladores que conectan la vista con el modelo.
- `com.rutaverde.app`: arranque y configuración de la aplicación.

## Ejecución

Requisitos: JDK 25 y Maven.

```
mvn javafx:run
```

## Pruebas

El proyecto incluye pruebas unitarias sobre las reglas de negocio más sensibles (algoritmo de Luhn, composición de paquetes y cálculo del valor de una reserva):

```
mvn test
```
