package pe.edu.upeu.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // URL de la base H2
    private static final String URL =
            "jdbc:h2:./data/compravende";

    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Devuelve una conexión abierta
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}