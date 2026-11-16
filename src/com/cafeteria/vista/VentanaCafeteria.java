package com.cafeteria.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import com.cafeteria.descuento.ReglasDescuento;
import com.cafeteria.descuento.ResultadoDescuento;
import com.cafeteria.modelo.Producto;
import com.cafeteria.servicio.ServicioCafeteria;

/**
 * Ventana principal (vista). Recoge los datos del usuario y muestra los resultados;
 * los cálculos y el acceso a la base de datos los hace {@link ServicioCafeteria}.
 */
public final class VentanaCafeteria extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Color COLOR_OK = new Color(0x1B7F3B);
    private static final Color COLOR_ERROR = new Color(0xC62828);
    private static final Color COLOR_NEUTRO = new Color(0x555555);
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final transient ServicioCafeteria servicio;
    private final String urlBaseDatos;
    private boolean baseDatosDisponible;

    private final JComboBox<Producto> comboProductos;
    private final JButton botonAgregar = new JButton("Agregar al pedido");
    private final JTextField campoMonto = new JTextField(12);
    private final JButton botonCalcular = new JButton("Calcular Descuento");
    private final JButton botonNuevo = new JButton("Nuevo pedido");
    private final JLabel etiquetaEstadoBD = new JLabel("Base de datos: conectando...");
    private final JLabel etiquetaTotal = new JLabel("Total a pagar: $0.00");
    private final JTextArea areaResultado = new JTextArea(13, 56);

    public VentanaCafeteria(ServicioCafeteria servicio, List<Producto> menu, String urlBaseDatos) {
        super("Cafetería Campus - Calculadora de descuentos");
        this.servicio = servicio;
        this.urlBaseDatos = urlBaseDatos;
        this.comboProductos = new JComboBox<>(menu.toArray(new Producto[0]));
        construirInterfaz();
        registrarEventos();
    }

    // ---------------------------------------------------------------------
    // Construcción de la interfaz
    // ---------------------------------------------------------------------

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.add(crearFormulario(), BorderLayout.NORTH);
        panelCentral.add(crearAreaResultado(), BorderLayout.CENTER);

        panelPrincipal.add(crearEncabezado(), BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(crearBarraTotal(), BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        getRootPane().setDefaultButton(botonCalcular);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("Calculadora de descuentos de la cafetería");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        JLabel regla = new JLabel(String.format(Locale.US,
                "Regla vigente: pedidos mayores a $%.2f tienen %d %% de descuento.",
                ReglasDescuento.MONTO_MINIMO, Math.round(ReglasDescuento.PORCENTAJE * 100)));
        regla.setForeground(COLOR_NEUTRO);
        etiquetaEstadoBD.setForeground(COLOR_NEUTRO);

        JPanel encabezado = new JPanel(new GridLayout(3, 1, 0, 2));
        encabezado.add(titulo);
        encabezado.add(regla);
        encabezado.add(etiquetaEstadoBD);
        return encabezado;
    }

    private JPanel crearFormulario() {
        // Tres columnas (etiquetas, campos y botones), cada una con GridLayout, unidas con BorderLayout.
        JPanel etiquetas = new JPanel(new GridLayout(2, 1, 0, 8));
        JLabel etiquetaProducto = new JLabel("Producto:");
        etiquetaProducto.setLabelFor(comboProductos);
        JLabel etiquetaMonto = new JLabel("Monto total del pedido ($):");
        etiquetaMonto.setLabelFor(campoMonto);
        etiquetas.add(etiquetaProducto);
        etiquetas.add(etiquetaMonto);

        JPanel campos = new JPanel(new GridLayout(2, 1, 0, 8));
        campos.add(comboProductos);
        campos.add(campoMonto);

        botonCalcular.setFont(botonCalcular.getFont().deriveFont(Font.BOLD));
        JPanel botones = new JPanel(new GridLayout(2, 1, 0, 8));
        botones.add(botonAgregar);
        botones.add(botonCalcular);

        JPanel formulario = new JPanel(new BorderLayout(10, 0));
        formulario.setBorder(BorderFactory.createTitledBorder("Pedido"));
        formulario.add(etiquetas, BorderLayout.WEST);
        formulario.add(campos, BorderLayout.CENTER);
        formulario.add(botones, BorderLayout.EAST);
        return formulario;
    }

    private JScrollPane crearAreaResultado() {
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaResultado.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane desplazamiento = new JScrollPane(areaResultado);
        desplazamiento.setBorder(BorderFactory.createTitledBorder("Resultado y mensajes de conexión"));
        return desplazamiento;
    }

    private JPanel crearBarraTotal() {
        etiquetaTotal.setFont(etiquetaTotal.getFont().deriveFont(Font.BOLD, 20f));
        etiquetaTotal.setForeground(COLOR_OK);
        JPanel barra = new JPanel(new BorderLayout(10, 0));
        barra.add(etiquetaTotal, BorderLayout.CENTER);
        barra.add(botonNuevo, BorderLayout.EAST);
        return barra;
    }

    // ---------------------------------------------------------------------
    // Eventos
    // ---------------------------------------------------------------------

    private void registrarEventos() {
        botonCalcular.addActionListener(evento -> calcularDescuento());
        botonAgregar.addActionListener(evento -> agregarProducto());
        botonNuevo.addActionListener(evento -> nuevoPedido());
    }

    /** ActionListener del botón "Calcular Descuento". */
    private void calcularDescuento() {
        // 1. Obtener el monto del JTextField
        double monto;
        try {
            monto = leerMonto();
        } catch (NumberFormatException e) {
            escribir("ERROR: \"" + campoMonto.getText() + "\" no es un monto válido. "
                    + "Escriba un número mayor o igual a 0, por ejemplo 12.50");
            campoMonto.requestFocusInWindow();
            campoMonto.selectAll();
            return;
        }

        // 2. Aplicar la lógica lambda (ReglasDescuento.POR_MONTO, a través del servicio)
        ResultadoDescuento resultado = servicio.calcularDescuento(monto);

        // 3. Mostrar el monto final en la GUI
        etiquetaTotal.setText(String.format(Locale.US, "Total a pagar: $%.2f", resultado.totalFinal()));
        StringBuilder texto = new StringBuilder("Pedido calculado")
                .append(String.format(Locale.US, "%n   Subtotal:              $%8.2f", resultado.subtotal()))
                .append(String.format(Locale.US, "%n   Descuento (%2.0f %%):       -$%8.2f",
                        resultado.porcentajeAplicado(), resultado.descuento()))
                .append(String.format(Locale.US, "%n   TOTAL A PAGAR:         $%8.2f", resultado.totalFinal()));
        if (!resultado.tieneDescuento()) {
            texto.append(String.format(Locale.US, "%n   (Sin descuento: el pedido no supera $%.2f)",
                    ReglasDescuento.MONTO_MINIMO));
        }
        texto.append(System.lineSeparator()).append("   ").append(registrarEnBaseDatos(resultado));
        escribir(texto.toString());
    }

    private String registrarEnBaseDatos(ResultadoDescuento resultado) {
        if (!baseDatosDisponible) {
            return "(El pedido no se guardó: no hay conexión con la base de datos.)";
        }
        try {
            long id = servicio.registrarPedido(resultado);
            return "Pedido #" + id + " guardado en la base de datos.";
        } catch (ClassNotFoundException e) {
            return "ERROR al guardar: falta el driver JDBC (" + e.getMessage() + ").";
        } catch (SQLException e) {
            return "ERROR al guardar el pedido: " + e.getMessage();
        }
    }

    private void agregarProducto() {
        Producto producto = (Producto) comboProductos.getSelectedItem();
        if (producto == null) {
            return;
        }
        double actual;
        try {
            actual = campoMonto.getText().isBlank() ? 0 : leerMonto();
        } catch (NumberFormatException e) {
            actual = 0;
        }
        double nuevoTotal = actual + producto.calcularPrecio();
        campoMonto.setText(String.format(Locale.US, "%.2f", nuevoTotal));
        escribir(String.format(Locale.US, "Agregado: %s%n   Monto del pedido: $%.2f", producto, nuevoTotal));
    }

    private void nuevoPedido() {
        campoMonto.setText("");
        etiquetaTotal.setText("Total a pagar: $0.00");
        escribir("Nuevo pedido: monto en $0.00");
        campoMonto.requestFocusInWindow();
    }

    private double leerMonto() {
        String texto = campoMonto.getText().trim().replace("$", "").replace(",", ".").trim();
        double monto = Double.parseDouble(texto);
        if (monto < 0 || Double.isNaN(monto) || Double.isInfinite(monto)) {
            throw new NumberFormatException("monto fuera de rango");
        }
        return monto;
    }

    // ---------------------------------------------------------------------
    // Conexión a la base de datos al iniciar
    // ---------------------------------------------------------------------

    /** Intenta conectarse a la base de datos en segundo plano, sin congelar la ventana. */
    public void iniciarConexion() {
        escribir("Iniciando aplicación. Conectando con " + urlBaseDatos + " ...");
        new SwingWorker<String, Void>() {
            private boolean exito;

            @Override
            protected String doInBackground() {
                try {
                    String informacion = servicio.iniciarBaseDatos();
                    exito = true;
                    return "Conexión exitosa con la base de datos." + System.lineSeparator() + "   " + informacion;
                } catch (ClassNotFoundException e) {
                    return "ERROR: no se encontró el driver JDBC de SQLite (" + e.getMessage() + ")."
                            + System.lineSeparator() + "   Verifique que lib/sqlite-jdbc-3.46.1.3.jar esté en el classpath."
                            + System.lineSeparator() + "   La calculadora funciona, pero los pedidos no se guardarán.";
                } catch (SQLException e) {
                    String codigo = e.getSQLState() == null ? "" : " (SQLState " + e.getSQLState() + ")";
                    return "ERROR al conectar con la base de datos" + codigo + ": " + e.getMessage()
                            + System.lineSeparator() + "   La calculadora funciona, pero los pedidos no se guardarán.";
                }
            }

            @Override
            protected void done() {
                try {
                    escribir(get());
                } catch (InterruptedException | ExecutionException e) {
                    escribir("ERROR inesperado al conectar: " + e.getMessage());
                }
                baseDatosDisponible = exito;
                etiquetaEstadoBD.setText(exito
                        ? "Base de datos: conectada (" + urlBaseDatos + ")"
                        : "Base de datos: sin conexión - vea el detalle abajo");
                etiquetaEstadoBD.setForeground(exito ? COLOR_OK : COLOR_ERROR);
            }
        }.execute();
    }

    private void escribir(String mensaje) {
        if (!areaResultado.getText().isEmpty()) {
            areaResultado.append(System.lineSeparator());
        }
        areaResultado.append("[" + LocalTime.now().format(FORMATO_HORA) + "] " + mensaje + System.lineSeparator());
        areaResultado.setCaretPosition(areaResultado.getDocument().getLength());
    }
}
