package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pe.edu.upeu.dao.TransaccionDAO;
import pe.edu.upeu.model.Producto;
import pe.edu.upeu.model.Transaccion;
import pe.edu.upeu.util.Sesion;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class TarjetaController {

    @FXML private ImageView imgProducto;
    @FXML private Label lblNombre;
    @FXML private Label lblPrecio;

    private Producto producto;
    private final TransaccionDAO transaccionDAO = new TransaccionDAO();

    // =====================================================
    // CARGAR DATOS DEL PRODUCTO EN LA TARJETA
    // =====================================================
    public void setProducto(Producto p) {

        this.producto = p;

        lblNombre.setText(p.getNombre());
        lblPrecio.setText("S/ " + String.format("%.2f", p.getPrecio()));

        if (p.getImagen() != null) {
            // Muestra la foto del producto almacenada en BD
            imgProducto.setImage(new Image(new ByteArrayInputStream(p.getImagen())));
        } else {
            // Muestra imagen por defecto si no tiene foto
            imgProducto.setImage(new Image(
                    getClass().getResourceAsStream("/pe/edu/upeu/img/sin-foto.png")
            ));
        }
    }

    // =====================================================
    // VER DETALLE / COMPRAR
    // Muestra Alert con datos del producto y pide confirmación.
    // Si el usuario confirma, registra la transacción en BD
    // y el producto queda marcado como VENDIDO.
    // =====================================================
    @FXML
    public void verDetalle() {

        // Alert de confirmación con información del producto
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar compra");
        alert.setHeaderText("¿Deseas comprar este producto?");
        alert.setContentText(
                "Producto : " + producto.getNombre() + "\n" +
                        "Precio   : S/ " + String.format("%.2f", producto.getPrecio()) + "\n" +
                        "Descripción: " + producto.getDescripcion()
        );

        // Espera la respuesta del usuario
        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            // Construye la transacción con el usuario activo
            Transaccion t = new Transaccion();
            t.setIdComprador(Sesion.getUsuario().getId());
            t.setIdProducto(producto.getId());
            t.setNombreProducto(producto.getNombre());
            t.setPrecio(producto.getPrecio());

            // Registra la compra y marca el producto como VENDIDO
            transaccionDAO.registrarCompra(t);

            // Muestra confirmación de éxito
            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Compra exitosa");
            ok.setHeaderText(null);
            ok.setContentText("¡Compra realizada! Revisa 'Mis Compras' para ver tu historial.");
            ok.showAndWait();
        }
    }
}