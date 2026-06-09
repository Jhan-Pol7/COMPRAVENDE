package pe.edu.upeu.util;

import pe.edu.upeu.model.Usuario;

/**
 * Clase Sesión
 * Guarda el usuario logueado en memoria
 * (accesible desde todo el sistema)
 */
public class Sesion {

    private static Usuario usuario;

    // Guardar usuario logueado
    public static void setUsuario(Usuario u) {
        usuario = u;
    }

    // Obtener usuario actual
    public static Usuario getUsuario() {
        return usuario;
    }

    // Cerrar sesión
    public static void cerrar() {
        usuario = null;
    }
}