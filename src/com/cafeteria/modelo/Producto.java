package com.cafeteria.modelo;

import java.util.Locale;

/**
 * Clase base de todo lo que se vende en la cafetería.
 * Cada subclase define cómo se calcula su precio (polimorfismo).
 */
public abstract class Producto {

    private final String nombre;
    private final double precioBase;

    protected Producto(String nombre, double precioBase) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El producto debe tener nombre.");
        }
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    /** Precio que se cobra por el producto; cada tipo de producto lo calcula a su manera. */
    public abstract double calcularPrecio();

    /** Categoría del producto (Bebida, Comida...). */
    public abstract String getCategoria();

    /** Detalle propio de cada subclase (tamaño, empaque...). */
    protected abstract String getDetalle();

    @Override
    public String toString() {
        return String.format(Locale.US, "%s (%s, %s) - $%.2f",
                nombre, getCategoria(), getDetalle(), calcularPrecio());
    }
}
