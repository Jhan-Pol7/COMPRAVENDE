package pe.edu.upeu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pe.edu.upeu.dao.UsuarioDAO;
import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.util.Sesion;

/**
 * Controlador Login
 */
public class LoginController {

    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    @FXML private Label lblMensaje;

    private UsuarioDAO dao = new UsuarioDAO();

    @FXML
    public void login() {

        String user = txtUser.getText();
        String pass = txtPass.getText();

        Usuario u = dao.login(user, pass);

        if (u != null) {

            // 🔥 GUARDAMOS SESIÓN GLOBAL
            Sesion.setUsuario(u);

            abrirSistema();

        } else {
            lblMensaje.setText("Credenciales incorrectas");
        }
    }

    private void abrirSistema() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(getClass()
                            .getResource("/pe/edu/upeu/view/ProductoView.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("CompraVende");

            stage.setScene(new Scene(root));
            stage.show();

            // cerrar login
            txtUser.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}