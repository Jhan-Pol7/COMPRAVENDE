package pe.edu.upeu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application{
    @Override
    public void start(Stage stage){
        Label mensaje = new Label("Sea bienvenido a CompraVende");

        StackPane root = new StackPane();
        root.getChildren().add(mensaje);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Compravende");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args){
        launch(args);
        //Stage = Marco de la casa
        //Scene = Interior de la casa
    }
}