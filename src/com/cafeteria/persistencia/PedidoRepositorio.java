package com.cafeteria.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.cafeteria.descuento.ResultadoDescuento;

/**
 * Guarda y consulta los pedidos en la tabla "pedidos".
 * Todas las conexiones, sentencias y resultados se abren con try-with-resources.
 */
public class PedidoRepositorio {

    private static final String CREAR_TABLA = """
            CREATE TABLE IF NOT EXISTS pedidos (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                fecha     TEXT NOT NULL,
                subtotal  REAL NOT NULL,
                descuento REAL NOT NULL,
                total     REAL NOT NULL
            )""";
    private static final String INSERTAR =
            "INSERT INTO pedidos (fecha, subtotal, descuento, total) VALUES (?, ?, ?, ?)";
    private static final String CONTAR = "SELECT COUNT(*) FROM pedidos";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ConexionBD conexionBD;

    public PedidoRepositorio(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Verifica la conexión, crea la tabla si no existe y consulta la versión de SQLite.
     *
     * @return texto con la versión del motor y el número de pedidos guardados
     */
    public String inicializar() throws ClassNotFoundException, SQLException {
        try (Connection conexion = conexionBD.abrir();
             Statement sentencia = conexion.createStatement()) {
            sentencia.execute(CREAR_TABLA);
            try (ResultSet version = sentencia.executeQuery("SELECT sqlite_version()")) {
                version.next();
                return "SQLite " + version.getString(1) + " - pedidos registrados: " + contar(conexion);
            }
        }
    }

    /** Guarda el pedido y devuelve el número (id) que le asignó la base de datos. */
    public long guardar(ResultadoDescuento resultado) throws ClassNotFoundException, SQLException {
        try (Connection conexion = conexionBD.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, LocalDateTime.now().format(FORMATO_FECHA));
            sentencia.setDouble(2, resultado.subtotal());
            sentencia.setDouble(3, resultado.descuento());
            sentencia.setDouble(4, resultado.totalFinal());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                return claves.next() ? claves.getLong(1) : -1;
            }
        }
    }

    private long contar(Connection conexion) throws SQLException {
        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(CONTAR)) {
            return resultado.next() ? resultado.getLong(1) : 0;
        }
    }
}
