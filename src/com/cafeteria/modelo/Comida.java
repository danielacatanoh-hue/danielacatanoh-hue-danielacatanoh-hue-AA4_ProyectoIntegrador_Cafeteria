package com.cafeteria.modelo;

/** Comida de la cafetería: si es para llevar se cobra el empaque. */
public class Comida extends Producto {

    public static final double COSTO_EMPAQUE = 0.30;

    private final boolean paraLlevar;

    public Comida(String nombre, double precioBase, boolean paraLlevar) {
        super(nombre, precioBase);
        this.paraLlevar = paraLlevar;
    }

    public boolean isParaLlevar() {
        return paraLlevar;
    }

    @Override
    public double calcularPrecio() {
        return paraLlevar ? getPrecioBase() + COSTO_EMPAQUE : getPrecioBase();
    }

    @Override
    public String getCategoria() {
        return "Comida";
    }

    @Override
    protected String getDetalle() {
        return paraLlevar ? "para llevar" : "para comer aquí";
    }
}
