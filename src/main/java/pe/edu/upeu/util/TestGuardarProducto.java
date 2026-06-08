package pe.edu.upeu.util;

import pe.edu.upeu.dao.ProductoDAO;
import pe.edu.upeu.model.Producto;

public class TestGuardarProducto {

    public static void main(String[] args) {

        // Crear objeto DAO
        // Al crearse, verifica que exista la tabla
        ProductoDAO dao = new ProductoDAO();

        // Crear producto nuevo
        Producto producto = new Producto();

        // Asignar datos
        producto.setNombre("Laptop Lenovo");
        producto.setDescripcion("Core i5 16GB RAM");
        producto.setPrecio(2500.00);
        producto.setStock(10);

        // Guardar en la base de datos
        dao.guardar(producto);

        System.out.println(
                "Fin del programa"
        );
    }
}