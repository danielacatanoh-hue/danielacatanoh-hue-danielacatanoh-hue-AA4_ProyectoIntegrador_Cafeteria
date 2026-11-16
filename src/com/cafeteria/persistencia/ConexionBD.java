package com.cafeteria.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abre conexiones JDBC con la base de datos local (SQLite).
 * Quien recibe la conexión debe usarla dentro de un try-with-resources para que se cierre sola.
 */
public class ConexionBD {

    public static final String DRIVER_SQLITE = "org.sqlite.JDBC";

    private final String url;

    public ConexionBD(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    /**
     * @throws ClassNotFoundException si el driver de SQLite no está en el classpath
     * @throws SQLException si no se puede abrir la base de datos
     */
    public Connection abrir() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_SQLITE);
        return DriverManager.getConnection(url);
    }
}
