package com.cafeteria.descuento;

/**
 * Interfaz funcional que define cómo se calcula un descuento.
 * Al tener un único método abstracto, cada regla puede escribirse como una expresión lambda.
 */
@FunctionalInterface
public interface ReglaDescuento {

    /**
     * @param total valor del pedido antes del descuento
     * @return valor del descuento que se resta al total (0 si la regla no aplica)
     */
    double calcularDescuento(double total);
}
