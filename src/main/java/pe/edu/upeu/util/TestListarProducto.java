package pe.edu.upeu.util;

import pe.edu.upeu.dao.ProductoDAO;
import pe.edu.upeu.model.Producto;

import java.util.List;

public class TestListarProducto {

    public static void main(String[] args) {

        // Crear DAO
        ProductoDAO dao =
                new ProductoDAO();

        // Obtener todos los productos
        List<Producto> productos =
                dao.listar();

        // Recorrer lista e imprimir datos
        for (Producto p : productos) {

            System.out.println(
                    "ID: " + p.getId()
            );

            System.out.println(
                    "Nombre: " + p.getNombre()
            );

            System.out.println(
                    "Descripción: " + p.getDescripcion()
            );

            System.out.println(
                    "Precio: " + p.getPrecio()
            );

            System.out.println(
                    "Stock: " + p.getStock()
            );

            System.out.println(
                    "---------------------"
            );
        }
    }
}