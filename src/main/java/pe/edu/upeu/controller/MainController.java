package pe.edu.upeu.controller;
import javafx.fxml.FXML;

public class MainController {
    public MainController(){
        System.out.println("MainController cargado");
    }
    @FXML
    private void saludar(){
        System.out.println("Hola desde java fx");
    }
}
