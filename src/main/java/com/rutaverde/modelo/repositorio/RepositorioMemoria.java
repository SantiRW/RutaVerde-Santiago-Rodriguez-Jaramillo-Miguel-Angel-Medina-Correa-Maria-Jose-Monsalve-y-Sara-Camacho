package com.rutaverde.modelo.repositorio;

import com.rutaverde.modelo.excepcion.ReglaNegocioException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class RepositorioMemoria<T, K> implements Repositorio<T, K> {
    private final Map<K, T> datos = new LinkedHashMap<>();
    private final Function<T, K> extractorClave;

    public RepositorioMemoria(Function<T, K> extractorClave) {
        this.extractorClave = extractorClave;
    }

    @Override
    public void agregar(T elemento) {
        K clave = extractorClave.apply(elemento);
        if (datos.containsKey(clave)) {
            throw new ReglaNegocioException("Ya existe un registro con la clave '" + clave + "'");
        }
        datos.put(clave, elemento);
    }

    @Override public Optional<T> buscar(K clave) { return Optional.ofNullable(datos.get(clave)); }

    @Override public List<T> listar() { return new ArrayList<>(datos.values()); }

    @Override public void eliminar(K clave) { datos.remove(clave); }
}
