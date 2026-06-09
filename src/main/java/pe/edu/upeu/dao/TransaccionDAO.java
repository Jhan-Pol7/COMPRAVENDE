package pe.edu.upeu.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.edu.upeu.config.ConexionBD;
import pe.edu.upeu.model.Transaccion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDAO {

    // Logger de Logback — registra eventos en consola y en archivo .log
    private static final Logger log = LoggerFactory.getLogger(TransaccionDAO.class);

    public TransaccionDAO() {
        crearTabla();
    }

    private void crearTabla() {

        String sql = """
                CREATE TABLE IF NOT EXISTS transaccion(
                    id              INT AUTO_INCREMENT PRIMARY KEY,
                    id_comprador    INT NOT NULL,
                    id_producto     INT NOT NULL,
                    nombre_producto VARCHAR(100),
                    precio          DOUBLE,
                    fecha           VARCHAR(20)
                )
                """;

        try (Connection cn = ConexionBD.getConnection();
             Statement st = cn.createStatement()) {

            st.execute(sql);
            log.info("Tabla transaccion verificada");

        } catch (Exception e) {
            log.error("Error al crear tabla transaccion", e);
        }
    }

    // ==================================================
    // REGISTRAR COMPRA
    // Inserta la transacción y marca el producto VENDIDO.
    // Si algo falla, hace rollback completo.
    // ==================================================
    public void registrarCompra(Transaccion t) {

        Connection cn = null;

        try {
            cn = ConexionBD.getConnection();
            cn.setAutoCommit(false);

            String sqlT = """
                    INSERT INTO transaccion
                    (id_comprador, id_producto, nombre_producto, precio, fecha)
                    VALUES (?, ?, ?, ?, ?)
                    """;

            PreparedStatement ps = cn.prepareStatement(sqlT);
            ps.setInt(1, t.getIdComprador());
            ps.setInt(2, t.getIdProducto());
            ps.setString(3, t.getNombreProducto());
            ps.setDouble(4, t.getPrecio());
            ps.setString(5, LocalDate.now().toString());
            ps.executeUpdate();

            // Marca el producto como VENDIDO en la misma transacción
            String sqlP = "UPDATE producto SET estado='VENDIDO' WHERE id=?";
            PreparedStatement psP = cn.prepareStatement(sqlP);
            psP.setInt(1, t.getIdProducto());
            psP.executeUpdate();

            cn.commit();
            log.info("Compra registrada: producto={} comprador={}", t.getNombreProducto(), t.getIdComprador());

        } catch (Exception e) {
            try { if (cn != null) cn.rollback(); } catch (Exception ex) { log.error("Error en rollback", ex); }
            log.error("Error al registrar compra", e);
        }
    }

    // ==================================================
    // LISTAR COMPRAS POR COMPRADOR
    // ==================================================
    public List<Transaccion> listarPorComprador(int idComprador) {

        List<Transaccion> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM transaccion WHERE id_comprador=? ORDER BY id DESC")) {

            ps.setInt(1, idComprador);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) lista.add(mapear(rs));

            log.info("Compras listadas para comprador id={}", idComprador);

        } catch (Exception e) {
            log.error("Error al listar compras", e);
        }

        return lista;
    }

    // ==================================================
    // LISTAR VENTAS POR VENDEDOR
    // ==================================================
    public List<Transaccion> listarPorVendedor(int idVendedor) {

        List<Transaccion> lista = new ArrayList<>();

        String sql = """
                SELECT t.* FROM transaccion t
                JOIN producto p ON t.id_producto = p.id
                WHERE p.id_vendedor = ?
                ORDER BY t.id DESC
                """;

        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idVendedor);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) lista.add(mapear(rs));

        } catch (Exception e) {
            log.error("Error al listar ventas", e);
        }

        return lista;
    }

    private Transaccion mapear(ResultSet rs) throws SQLException {

        Transaccion t = new Transaccion();
        t.setId(rs.getInt("id"));
        t.setIdComprador(rs.getInt("id_comprador"));
        t.setIdProducto(rs.getInt("id_producto"));
        t.setNombreProducto(rs.getString("nombre_producto"));
        t.setPrecio(rs.getDouble("precio"));
        t.setFecha(rs.getString("fecha"));
        return t;
    }
}