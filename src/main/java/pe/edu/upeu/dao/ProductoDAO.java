package pe.edu.upeu.dao;

import pe.edu.upeu.config.ConexionBD;
import pe.edu.upeu.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Constructor del DAO
    // Cuando se crea un ProductoDAO,
    // automáticamente verifica si existe la tabla.
    public ProductoDAO() {
        crearTabla();
    }

    // Método que crea la tabla producto
    // solamente si todavía no existe.
    private void crearTabla() {

        String sql = """
                CREATE TABLE IF NOT EXISTS producto(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100),
                    descripcion VARCHAR(255),
                    precio DOUBLE,
                    stock INT
                )
                """;

        try (
                // Abrimos conexión a la BD
                Connection cn = ConexionBD.getConnection();

                // Statement ejecuta SQL simple
                Statement st = cn.createStatement()
        ) {

            // Ejecuta el CREATE TABLE
            st.execute(sql);

            System.out.println(
                    "Tabla producto verificada"
            );

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    // ==================================================
    // GUARDAR PRODUCTO
    // ==================================================
    public void guardar(Producto p) {

        // Consulta SQL para insertar datos
        String sql = """
                INSERT INTO producto
                (nombre, descripcion, precio, stock)
                VALUES (?, ?, ?, ?)
                """;

        try (
                // Abrimos conexión
                Connection cn = ConexionBD.getConnection();

                // PreparedStatement permite
                // enviar valores dinámicos
                PreparedStatement ps =
                        cn.prepareStatement(sql)
        ) {

            // Reemplaza el primer ?
            ps.setString(
                    1,
                    p.getNombre()
            );

            // Reemplaza el segundo ?
            ps.setString(
                    2,
                    p.getDescripcion()
            );

            // Reemplaza el tercer ?
            ps.setDouble(
                    3,
                    p.getPrecio()
            );

            // Reemplaza el cuarto ?
            ps.setInt(
                    4,
                    p.getStock()
            );

            // Ejecuta INSERT
            ps.executeUpdate();

            System.out.println(
                    "Producto guardado correctamente"
            );

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    // ==================================================
    // LISTAR PRODUCTOS
    // ==================================================
    public List<Producto> listar() {

        // Lista donde almacenaremos
        // los productos encontrados
        List<Producto> lista =
                new ArrayList<>();

        // Consulta SQL
        String sql =
                "SELECT * FROM producto";

        try (
                // Conexión
                Connection cn =
                        ConexionBD.getConnection();

                // Ejecuta consulta SQL
                Statement st =
                        cn.createStatement();

                // ResultSet contiene
                // los registros obtenidos
                ResultSet rs =
                        st.executeQuery(sql)
        ) {

            // Recorre cada fila encontrada
            while (rs.next()) {

                // Crear objeto producto
                Producto p =
                        new Producto();

                // Leer columna id
                p.setId(
                        rs.getInt("id")
                );

                // Leer columna nombre
                p.setNombre(
                        rs.getString("nombre")
                );

                // Leer columna descripcion
                p.setDescripcion(
                        rs.getString("descripcion")
                );

                // Leer columna precio
                p.setPrecio(
                        rs.getDouble("precio")
                );

                // Leer columna stock
                p.setStock(
                        rs.getInt("stock")
                );

                // Agregar producto a la lista
                lista.add(p);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        // Retornar todos los productos encontrados
        return lista;
    }

    // ==================================================
    // ACTUALIZAR PRODUCTO
    // ==================================================
    public void actualizar(Producto p) {

        String sql = """
                UPDATE producto
                SET nombre=?,
                    descripcion=?,
                    precio=?,
                    stock=?
                WHERE id=?
                """;

        try (
                Connection cn =
                        ConexionBD.getConnection();

                PreparedStatement ps =
                        cn.prepareStatement(sql)
        ) {

            // Nuevos datos
            ps.setString(
                    1,
                    p.getNombre()
            );

            ps.setString(
                    2,
                    p.getDescripcion()
            );

            ps.setDouble(
                    3,
                    p.getPrecio()
            );

            ps.setInt(
                    4,
                    p.getStock()
            );

            // ID que se actualizará
            ps.setInt(
                    5,
                    p.getId()
            );

            // Ejecuta UPDATE
            ps.executeUpdate();

            System.out.println(
                    "Producto actualizado"
            );

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    // ==================================================
    // ELIMINAR PRODUCTO
    // ==================================================
    public void eliminar(int id) {

        String sql =
                "DELETE FROM producto WHERE id=?";

        try (
                Connection cn =
                        ConexionBD.getConnection();

                PreparedStatement ps =
                        cn.prepareStatement(sql)
        ) {

            // ID a eliminar
            ps.setInt(1, id);

            // Ejecuta DELETE
            ps.executeUpdate();

            System.out.println(
                    "Producto eliminado"
            );

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}