package com.rutaverde.modelo.repositorio;

import java.util.List;
import java.util.Optional;

/** Abstracción de almacenamiento (DIP): hoy en memoria, mañana base de datos o archivos. */
public interface Repositorio<T, K> {
    void agregar(T elemento);

    Optional<T> buscar(K clave);

    List<T> listar();

    void eliminar(K clave);
}
