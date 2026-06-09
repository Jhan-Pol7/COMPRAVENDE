package pe.edu.upeu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(
                getClass().getResource("/pe/edu/upeu/view/LoginView.fxml")
        );

        Scene scene = new Scene(root, 300, 200);

        // Aplica el CSS global a toda la aplicación desde la escena raíz
        scene.getStylesheets().add(
                getClass().getResource("/pe/edu/upeu/style.css").toExternalForm()
        );

        stage.setTitle("Login - CompraVende");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}