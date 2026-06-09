package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.util.Sesion;

public class MainController {

    // ===== ETIQUETAS DEL MENÚ =====
    @FXML private Label lblUsuario;  // muestra el nombre del usuario
    @FXML private Label lblRol;      // muestra el rol

    // ===== BOTONES DEL MENÚ =====
    @FXML private Button btnProductos;
    @FXML private Button btnCatalogo;
    @FXML private Button btnMisCompras;
    @FXML private Button btnMisVentas;

    // ===== PANEL CENTRAL =====
    // Aquí se cargan las sub-vistas dinámicamente
    @FXML private StackPane contenedor;

    // =====================================================
    // INICIALIZAR
    // Al abrir la ventana, muestra datos del usuario
    // y ajusta el menú según su rol
    // =====================================================
    @FXML
    public void initialize() {

        Usuario u = Sesion.getUsuario();

        if (u == null) return;

        // Muestra nombre y rol en el menú lateral
        lblUsuario.setText(u.getUsername());
        lblRol.setText(u.getRol());

        // Aplica visibilidad de botones según el rol
        aplicarPermisos(u.getRol());

        // Carga la vista de productos por defecto al abrir
        irProductos();
    }

    // =====================================================
    // PERMISOS POR ROL
    // Muestra u oculta botones del menú según el rol
    // =====================================================
    private void aplicarPermisos(String rol) {

        // VENDEDOR: solo ve Productos y Mis Ventas
        if (rol.equals("VENDEDOR")) {
            btnCatalogo.setVisible(false);
            btnMisCompras.setVisible(false);
        }

        // COMPRADOR: ve Catálogo y Mis Compras, no gestiona productos directamente
        if (rol.equals("COMPRADOR")) {
            btnMisVentas.setVisible(false);
        }

        // ADMIN: ve todo, no se oculta nada
    }

    // =====================================================
    // CARGAR VISTA EN EL PANEL CENTRAL
    // Método reutilizable para cargar cualquier FXML
    // dentro del contenedor central sin abrir nueva ventana
    // =====================================================
    private void cargarVista(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pe/edu/upeu/view/" + fxml)
            );
            Parent vista = loader.load();

            // Limpia el panel y carga la nueva vista
            contenedor.getChildren().setAll(vista);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // NAVEGACIÓN — cada botón carga su vista
    // =====================================================

    @FXML
    public void irProductos() {
        // Carga el CRUD de productos en el panel central
        cargarVista("ProductoView.fxml");
    }
    @FXML
    public void irCatalogo() {
        // El catálogo ahora solo muestra productos DISPONIBLES
        cargarVista("CatalogoView.fxml");
    }

    @FXML
    public void irMisCompras() {
        // Carga el historial de compras del usuario
        cargarVista("MisComprasView.fxml");
    }

    @FXML
    public void irMisVentas() {
        // Carga el historial de ventas (reutiliza MisCompras por ahora)
        cargarVista("MisComprasView.fxml");
    }
    // =====================================================
    // CERRAR SESIÓN
    // Limpia la sesión global y regresa a la pantalla de login
    // =====================================================
    @FXML
    public void cerrarSesion() {
        try {
            // Borra el usuario de la sesión global
            Sesion.setUsuario(null);

            // Carga el login de nuevo
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pe/edu/upeu/view/LoginView.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login - CompraVende");
            stage.setScene(new Scene(root, 300, 200));
            stage.show();

            // Cierra la ventana principal actual
            lblUsuario.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}