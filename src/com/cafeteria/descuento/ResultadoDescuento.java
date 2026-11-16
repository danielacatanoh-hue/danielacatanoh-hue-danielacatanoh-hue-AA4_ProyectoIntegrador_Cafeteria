package com.cafeteria.descuento;

/** Resultado de aplicar las reglas de descuento a un pedido. */
public record ResultadoDescuento(double subtotal, double descuento, double totalFinal) {

    /** Porcentaje de descuento aplicado sobre el subtotal (0 a 100). */
    public double porcentajeAplicado() {
        return subtotal == 0 ? 0 : descuento * 100 / subtotal;
    }

    public boolean tieneDescuento() {
        return descuento > 0;
    }
}
