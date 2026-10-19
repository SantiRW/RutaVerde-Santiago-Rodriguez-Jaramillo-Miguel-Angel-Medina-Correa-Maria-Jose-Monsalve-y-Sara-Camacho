package com.rutaverde.modelo.repositorio;

public class ConsecutivoMemoria implements GeneradorConsecutivo {
    private int actual;

    public ConsecutivoMemoria(int inicio) { this.actual = inicio - 1; }

    @Override public synchronized int siguiente() { return ++actual; }
}
