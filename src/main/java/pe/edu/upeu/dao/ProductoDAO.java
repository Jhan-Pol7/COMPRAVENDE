package pe.edu.upeu.dao;

import pe.edu.upeu.config.ConexionBD;
import pe.edu.upeu.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public ProductoDAO() {
        crearTabla();
    }

    private void crearTabla() {

        String sqlCrear = """
                CREATE TABLE IF NOT EXISTS producto(
                    id          INT AUTO_INCREMENT PRIMARY KEY,
                    nombre      VARCHAR(100),
                    descripcion VARCHAR(255),
                    precio      DOUBLE,
                    stock       INT,
                    imagen      BLOB,
                    estado      VARCHAR(20) DEFAULT 'DISPONIBLE',
                    id_vendedor INT DEFAULT 0
                )
                """;

        String sqlImagen   = "ALTER TABLE producto ADD COLUMN IF NOT EXISTS imagen      BLOB";
        String sqlEstado   = "ALTER TABLE producto ADD COLUMN IF NOT EXISTS estado      VARCHAR(20) DEFAULT 'DISPONIBLE'";
        String sqlVendedor = "ALTER TABLE producto ADD COLUMN IF NOT EXISTS id_vendedor INT DEFAULT 0";

        try (Connection cn = ConexionBD.getConnection();
             Statement st = cn.createStatement()) {

            st.execute(sqlCrear);
            st.execute(sqlImagen);
            st.execute(sqlEstado);
            st.execute(sqlVendedor);
            System.out.println("Tabla producto verificada");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardar(Producto p) {

        String sql = """
                INSERT INTO producto
                (nombre, descripcion, precio, stock, imagen, estado, id_vendedor)
                VALUES (?, ?, ?, ?, ?, 'DISPONIBLE', ?)
                """;

        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setBytes(5, p.getImagen());
            ps.setInt(6, p.getIdVendedor());
            ps.executeUpdate();
            System.out.println("Producto guardado");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Producto> listar() {

        List<Producto> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM producto")) {

            while (rs.next()) lista.add(mapear(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Producto> listarDisponibles() {

        List<Producto> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM producto WHERE estado='DISPONIBLE'")) {

            while (rs.next()) lista.add(mapear(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void marcarVendido(int idProducto) {

        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE producto SET estado='VENDIDO' WHERE id=?")) {

            ps.setInt(1, idProducto);
            ps.executeUpdate();
            System.out.println("Producto marcado como VENDIDO");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Producto p) {

        String sql = """
                UPDATE producto
                SET nombre=?, descripcion=?, precio=?, stock=?, imagen=?
                WHERE id=?
                """;

        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setBytes(5, p.getImagen());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
            System.out.println("Producto actualizado");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {

        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "DELETE FROM producto WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Producto eliminado");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {

        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setImagen(rs.getBytes("imagen"));
        p.setEstado(rs.getString("estado"));
        p.setIdVendedor(rs.getInt("id_vendedor"));
        return p;
    }
}