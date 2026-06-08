package pe.edu.upeu.util;

import pe.edu.upeu.config.ConexionBD;

import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {

        try {

            Connection conn =
                    ConexionBD.getConnection();

            System.out.println(
                    "Conexion exitosa con H2"
            );

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}