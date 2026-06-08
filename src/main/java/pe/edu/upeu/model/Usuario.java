package pe.edu.upeu.model;

/**
 * Modelo Usuario
 * Representa la tabla usuario en la base de datos
 */
public class Usuario {

    private int id;             // Identificador único
    private String username;    // Usuario de login
    private String password;    // Contraseña
    private String rol;         // Rol: ADMIN o USER

    public Usuario() {
        // Constructor vacío necesario para frameworks y DAO
    }

    public Usuario(int id, String username, String password, String rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // ================= GETTERS Y SETTERS =================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}