package com.rutaverde.modelo.dominio;

import java.math.BigDecimal;
import java.util.List;

/**
 * PATRÓN COMPOSITE (estructural) - Componente.
 * Actividad (hoja) y Paquete (compuesto) se consultan de la misma manera.
 */
public interface Producto {
    String getCodigo();

    String getNombre();

    BigDecimal getPrecioPorPersona();

    int getDuracionHoras();

    /** Lista plana de todas las actividades, sin importar cuántos niveles de paquetes existan. */
    List<Actividad> getActividades();

    /** Indica si este producto es {@code otro} o lo contiene en cualquier nivel. */
    boolean contiene(Producto otro);

    /** Notificaciones para que un paquete conozca a sus padres (evita actividades repetidas). */
    default void agregadoA(Paquete padre) { }

    default void quitadoDe(Paquete padre) { }
}
