package com.rutaverde.modelo.pago;

/** Algoritmo de Luhn: 13 a 19 dígitos, espacios ignorados. */
public class LuhnValidador implements ValidadorTarjeta {
    @Override
    public boolean esValida(String numeroTarjeta) {
        if (numeroTarjeta == null) return false;
        String numero = numeroTarjeta.replace(" ", "");
        if (numero.length() < 13 || numero.length() > 19) return false;
        int suma = 0;
        boolean duplicar = false;
        for (int i = numero.length() - 1; i >= 0; i--) {
            char c = numero.charAt(i);
            if (c < '0' || c > '9') return false;
            int digito = c - '0';
            if (duplicar) {
                digito *= 2;
                if (digito > 9) digito -= 9;
            }
            suma += digito;
            duplicar = !duplicar;
        }
        return suma % 10 == 0;
    }
}
