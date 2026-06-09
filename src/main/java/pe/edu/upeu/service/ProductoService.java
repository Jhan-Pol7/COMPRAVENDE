package pe.edu.upeu.service;

import pe.edu.upeu.dao.ProductoDAO;
import pe.edu.upeu.model.Producto;

import java.util.List;

/**
 * Capa SERVICE
 * Aquí van las reglas del sistema (NO SQL)
 */
public class ProductoService {

    private ProductoDAO dao = new ProductoDAO();

    // ================= GUARDAR =================
    public void guardar(Producto p) {

        // Validaciones del negocio
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new RuntimeException("Nombre obligatorio");
        }

        if (p.getPrecio() <= 0) {
            throw new RuntimeException("Precio inválido");
        }

        if (p.getStock() < 0) {
            throw new RuntimeException("Stock inválido");
        }

        dao.guardar(p);
    }

    // ================= LISTAR =================
    public List<Producto> listar() {
        return dao.listar();
    }

    // ================= ACTUALIZAR =================
    public void actualizar(Producto p) {
        dao.actualizar(p);
    }

    // ================= ELIMINAR =================
    public void eliminar(int id) {
        dao.eliminar(id);
    }
}