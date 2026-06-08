package pe.edu.upeu.util;

import pe.edu.upeu.dao.ProductoDAO;
import pe.edu.upeu.model.Producto;

public class TestActualizarProducto {

    public static void main(String[] args) {

        // Crear DAO
        ProductoDAO dao =
                new ProductoDAO();

        // Crear producto con ID existente
        Producto producto =
                new Producto();

        // IMPORTANTE:
        // Debe existir ese ID en la BD
        producto.setId(1);

        producto.setNombre(
                "Laptop Lenovo Actualizada"
        );

        producto.setDescripcion(
                "Core i7 32GB RAM"
        );

        producto.setPrecio(
                4200
        );

        producto.setStock(
                15
        );

        // Ejecutar UPDATE
        dao.actualizar(producto);
    }
}