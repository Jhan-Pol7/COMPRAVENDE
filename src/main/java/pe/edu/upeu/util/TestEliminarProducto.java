package pe.edu.upeu.util;

import pe.edu.upeu.dao.ProductoDAO;

public class TestEliminarProducto {

    public static void main(String[] args) {

        // Crear DAO
        ProductoDAO dao =
                new ProductoDAO();

        // Eliminar producto con ID 1
        dao.eliminar(1);
    }
}