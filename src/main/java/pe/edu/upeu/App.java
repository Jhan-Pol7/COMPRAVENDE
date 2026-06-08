package pe.edu.upeu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // FXMLLoader lee el archivo FXML
        // y construye automáticamente la interfaz gráfica.
        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/pe/edu/upeu/view/ProductoView.fxml"
                )
        );

        // Scene contiene todos los elementos visuales
        Scene scene = new Scene(
                root,
                900,
                600
        );

        // Configuración de la ventana principal
        stage.setTitle("CompraVende");

        // Asignar la escena a la ventana
        stage.setScene(scene);

        // Mostrar la ventana
        stage.show();
    }

    public static void main(String[] args) {

        // Inicia JavaFX.
        // Java llamará automáticamente al método start().
        launch(args);
    }
}

/*
Conceptos:

Application
→ Clase base de JavaFX.

Stage
→ Ventana principal del programa.

Scene
→ Contenido de la ventana.

FXMLLoader
→ Lee el archivo FXML y construye la interfaz.

ProductoView.fxml
→ Diseño visual de la pantalla.
*/
        //Stage = Marco de la casa
        //Scene = Interior de la casa


/*
main-view.fxml=Plano de la casa
FXMLLoader=Constructor que lee el plano
Stage=la casa terminada*/
