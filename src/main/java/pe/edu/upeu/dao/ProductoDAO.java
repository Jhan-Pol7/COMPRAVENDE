package pe.edu.upeu.dao;

import pe.edu.upeu.config.ConexionBD;
import pe.edu.upeu.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Al crear el DAO, verifica/crea la tabla automáticamente
    public ProductoDAO() {
        crearTabla();
    }

    // ==================================================
    // CREAR TABLA
    // Crea la tabla si no existe, incluyendo columna imagen.
    // Si ya existía sin imagen, la agrega sin borrar datos.
    // ==================================================
    private void crearTabla() {

        // Crea la tabla completa con imagen desde el inicio
        String sqlCrear = """
                CREATE TABLE IF NOT EXISTS producto(
                    id          INT AUTO_INCREMENT PRIMARY KEY,
                    nombre      VARCHAR(100),
                    descripcion VARCHAR(255),
                    precio      DOUBLE,
                    stock       INT,
                    imagen      BLOB
                )
                """;

        // Si la tabla ya existía sin imagen, la agrega de forma segura
        String sqlAgregar = """
                ALTER TABLE producto
                ADD COLUMN IF NOT EXISTS imagen BLOB
                """;

        try (
                Connection cn = ConexionBD.getConnection();
                Statement st = cn.createStatement()
        ) {
            st.execute(sqlCrear);
            st.execute(sqlAgregar);
            System.out.println("Tabla producto verificada con columna imagen");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================
    // GUARDAR PRODUCTO
    // Inserta un nuevo producto incluyendo su imagen en bytes.
    // Si no tiene foto, imagen llega null y se guarda null.
    // ==================================================
    public void guardar(Producto p) {

        String sql = """
                INSERT INTO producto
                (nombre, descripcion, precio, stock, imagen)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection cn = ConexionBD.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());

            // Guarda los bytes de la imagen como BLOB en la BD
            ps.setBytes(5, p.getImagen());

            ps.executeUpdate();
            System.out.println("Producto guardado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================
    // LISTAR PRODUCTOS
    // Recupera todos los productos incluyendo sus bytes de imagen.
    // Si un producto no tiene foto, getBytes devuelve null.
    // ==================================================
    public List<Producto> listar() {

        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto";

        try (
                Connection cn = ConexionBD.getConnection();
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) {

                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));

                // Recupera los bytes de la imagen almacenada en la BD
                p.setImagen(rs.getBytes("imagen"));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ==================================================
    // ACTUALIZAR PRODUCTO
    // Actualiza todos los campos incluyendo imagen.
    // Si no se seleccionó foto nueva, imagen puede ser null.
    // ==================================================
    public void actualizar(Producto p) {

        String sql = """
                UPDATE producto
                SET nombre=?,
                    descripcion=?,
                    precio=?,
                    stock=?,
                    imagen=?
                WHERE id=?
                """;

        try (
                Connection cn = ConexionBD.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());

            // Actualiza los bytes de la imagen en la BD
            ps.setBytes(5, p.getImagen());

            // Identifica qué fila actualizar por su ID
            ps.setInt(6, p.getId());

            ps.executeUpdate();
            System.out.println("Producto actualizado");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================
    // ELIMINAR PRODUCTO
    // Borra el registro por ID, incluyendo su imagen BLOB.
    // ==================================================
    public void eliminar(int id) {

        String sql = "DELETE FROM producto WHERE id=?";

        try (
                Connection cn = ConexionBD.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {
            // ID del producto a eliminar
            ps.setInt(1, id);

            ps.executeUpdate();
            System.out.println("Producto eliminado");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}