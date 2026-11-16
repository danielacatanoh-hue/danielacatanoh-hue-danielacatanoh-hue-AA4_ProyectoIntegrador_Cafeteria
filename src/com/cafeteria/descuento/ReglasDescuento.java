package com.cafeteria.descuento;

/**
 * Reglas de descuento de la cafetería, escritas como expresiones lambda.
 * Para agregar una regla nueva basta con crear otra lambda: no hay que modificar
 * CalculadoraDescuento (principio abierto/cerrado).
 */
public final class ReglasDescuento {

    public static final double MONTO_MINIMO = 10.00;
    public static final double PORCENTAJE = 0.15;

    /** Regla del caso: si el total es mayor a $10 se aplica el 15 %; si no, el 0 %. */
    public static final ReglaDescuento POR_MONTO =
            total -> total > MONTO_MINIMO ? total * PORCENTAJE : 0.0;

    /** Regla que nunca aplica descuento. */
    public static final ReglaDescuento SIN_DESCUENTO = total -> 0.0;

    private ReglasDescuento() {
        // Clase de utilidad: no se instancia.
    }

    /** Crea una regla con otro monto mínimo y otro porcentaje, por ejemplo para una promoción. */
    public static ReglaDescuento porMontoMinimo(double montoMinimo, double porcentaje) {
        return total -> total > montoMinimo ? total * porcentaje : 0.0;
    }
}
