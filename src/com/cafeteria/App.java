package com.cafeteria;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.cafeteria.descuento.CalculadoraDescuento;
import com.cafeteria.descuento.ReglasDescuento;
import com.cafeteria.modelo.Menu;
import com.cafeteria.persistencia.ConexionBD;
import com.cafeteria.persistencia.PedidoRepositorio;
import com.cafeteria.servicio.ServicioCafeteria;
import com.cafeteria.vista.VentanaCafeteria;

/**
 * Punto de entrada: crea los objetos de cada capa, los conecta entre sí y abre la ventana.
 * La URL de la base de datos se puede cambiar con -Dbd.url=... (útil para probar errores).
 */
public final class App {

    private static final String URL_POR_DEFECTO = "jdbc:sqlite:cafeteria.db";

    private App() {
    }

    public static void main(String[] args) {
        String url = System.getProperty("bd.url", URL_POR_DEFECTO);

        // Para agregar otra regla basta con añadirla a esta lista (principio abierto/cerrado).
        CalculadoraDescuento calculadora = new CalculadoraDescuento(List.of(ReglasDescuento.POR_MONTO));
        PedidoRepositorio repositorio = new PedidoRepositorio(new ConexionBD(url));
        ServicioCafeteria servicio = new ServicioCafeteria(calculadora, repositorio);

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Si no se puede aplicar el estilo del sistema, se usa el de Java.
            }
            VentanaCafeteria ventana = new VentanaCafeteria(servicio, Menu.productosDisponibles(), url);
            ventana.setVisible(true);
            ventana.iniciarConexion();
        });
    }
}
