package pe.edu.upeu.dao;

import pe.edu.upeu.config.ConexionBD;
import pe.edu.upeu.model.ItemVenta;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Maneja ventas completas (transacción real)
 */
public class VentaDAO {

    public void guardarVenta(List<ItemVenta> carrito, double total) {

        Connection cn = null;

        try {

            cn = ConexionBD.getConnection();
            cn.setAutoCommit(false); // 🔥 inicio transacción

            // ================= VENTA =================
            String sqlVenta =
                    "INSERT INTO venta(fecha, total) VALUES(?, ?)";

            PreparedStatement psVenta =
                    cn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);

            psVenta.setString(1, LocalDate.now().toString());
            psVenta.setDouble(2, total);

            psVenta.executeUpdate();

            ResultSet rs = psVenta.getGeneratedKeys();

            int idVenta = 0;

            if (rs.next()) {
                idVenta = rs.getInt(1);
            }

            // ================= DETALLE =================
            String sqlDetalle =
                    "INSERT INTO detalle_venta(id_venta, id_producto, cantidad, subtotal) VALUES(?,?,?,?)";

            PreparedStatement psDetalle =
                    cn.prepareStatement(sqlDetalle);

            for (ItemVenta i : carrito) {

                psDetalle.setInt(1, idVenta);
                psDetalle.setInt(2, i.getIdProducto());
                psDetalle.setInt(3, i.getCantidad());
                psDetalle.setDouble(4, i.getSubtotal());

                psDetalle.addBatch();
            }

            psDetalle.executeBatch();

            cn.commit(); // 🔥 confirmar todo

            System.out.println("✔ Venta registrada correctamente");

        } catch (Exception e) {

            try {
                if (cn != null) cn.rollback(); // 🔥 rollback si falla
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
        }
    }
}