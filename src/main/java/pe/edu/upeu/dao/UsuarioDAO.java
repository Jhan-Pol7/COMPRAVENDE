package pe.edu.upeu.dao;

import pe.edu.upeu.config.ConexionBD;
import pe.edu.upeu.model.Usuario;

import java.sql.*;

/**
 * DAO de Usuario
 * Maneja creación de tabla y login
 */
public class UsuarioDAO {

    public UsuarioDAO() {
        crearTabla();
        insertarUsuarioDefault();
    }

    // ================= CREAR TABLA =================
    private void crearTabla() {

        String sql = """
            CREATE TABLE IF NOT EXISTS usuario(
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50),
                password VARCHAR(50),
                rol VARCHAR(20)
            )
        """;

        try (Connection cn = ConexionBD.getConnection();
             Statement st = cn.createStatement()) {

            st.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= USUARIO POR DEFECTO =================
    private void insertarUsuarioDefault() {

        // Usuario admin inicial del sistema
        String sql = """
            INSERT INTO usuario(username, password, rol)
            VALUES('admin','123','ADMIN')
        """;

        try (Connection cn = ConexionBD.getConnection();
             Statement st = cn.createStatement()) {

            st.execute(sql);

        } catch (Exception e) {
            // Si ya existe, se ignora el error
        }
    }

    // ================= LOGIN =================
    public Usuario login(String user, String pass) {

        String sql = """
            SELECT * FROM usuario
            WHERE username=? AND password=?
        """;

        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            // Reemplaza parámetros del query
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            // Si existe usuario
            if (rs.next()) {

                Usuario u = new Usuario();

                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));

                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // login fallido
    }
}