package pe.edu.upeu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de inicio del sistema
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Carga pantalla de login
        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/pe/edu/upeu/view/LoginView.fxml"
                )
        );

        stage.setTitle("Login - CompraVende");
        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}