package com.cafeteria.modelo;

/** Bebida de la cafetería: su precio depende del tamaño. */
public class Bebida extends Producto {

    /** Tamaños disponibles y el recargo que suma cada uno al precio base. */
    public enum Tamano {
        PEQUENO("pequeño", 0.00),
        MEDIANO("mediano", 0.50),
        GRANDE("grande", 1.00);

        private final String etiqueta;
        private final double recargo;

        Tamano(String etiqueta, double recargo) {
            this.etiqueta = etiqueta;
            this.recargo = recargo;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public double getRecargo() {
            return recargo;
        }
    }

    private final Tamano tamano;

    public Bebida(String nombre, double precioBase, Tamano tamano) {
        super(nombre, precioBase);
        this.tamano = tamano;
    }

    public Tamano getTamano() {
        return tamano;
    }

    @Override
    public double calcularPrecio() {
        return getPrecioBase() + tamano.getRecargo();
    }

    @Override
    public String getCategoria() {
        return "Bebida";
    }

    @Override
    protected String getDetalle() {
        return tamano.getEtiqueta();
    }
}
