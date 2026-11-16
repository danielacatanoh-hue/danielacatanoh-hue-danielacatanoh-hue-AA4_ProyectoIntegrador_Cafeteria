package com.cafeteria.descuento;

import java.util.List;

/**
 * Aplica las reglas de descuento recibidas a un total.
 * Está cerrada a modificaciones y abierta a extensión: las reglas llegan por el constructor,
 * así que se pueden agregar reglas nuevas sin cambiar esta clase.
 */
public class CalculadoraDescuento {

    private final List<ReglaDescuento> reglas;

    public CalculadoraDescuento(List<ReglaDescuento> reglas) {
        this.reglas = List.copyOf(reglas);
    }

    /**
     * Calcula el descuento del pedido. Si varias reglas aplican, se usa la de mayor descuento,
     * y el descuento nunca puede ser mayor que el total.
     */
    public ResultadoDescuento calcular(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo.");
        }
        double descuento = reglas.stream()
                .mapToDouble(regla -> regla.calcularDescuento(total))
                .max()
                .orElse(0.0);
        descuento = redondear(Math.min(descuento, total));
        return new ResultadoDescuento(redondear(total), descuento, redondear(total - descuento));
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
