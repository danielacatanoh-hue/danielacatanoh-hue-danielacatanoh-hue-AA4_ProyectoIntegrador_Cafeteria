package com.cafeteria.modelo;

import java.util.List;

/** Productos disponibles en la cafetería. */
public final class Menu {

    private Menu() {
        // Clase de utilidad: no se instancia.
    }

    public static List<Producto> productosDisponibles() {
        return List.of(
                new Bebida("Café americano", 2.00, Bebida.Tamano.PEQUENO),
                new Bebida("Capuchino", 2.50, Bebida.Tamano.MEDIANO),
                new Bebida("Chocolate caliente", 2.50, Bebida.Tamano.GRANDE),
                new Bebida("Jugo natural", 3.00, Bebida.Tamano.MEDIANO),
                new Comida("Croissant", 2.20, false),
                new Comida("Sándwich de pollo", 4.50, true),
                new Comida("Empanada", 1.80, false),
                new Comida("Porción de torta", 3.20, true));
    }
}
