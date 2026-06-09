package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.TransaccionDAO;
import pe.edu.upeu.model.Transaccion;
import pe.edu.upeu.service.TicketService;
import pe.edu.upeu.util.Sesion;

public class MisComprasController {

    @FXML private TableView<Transaccion>            tablaCompras;
    @FXML private TableColumn<Transaccion, Integer> colId;
    @FXML private TableColumn<Transaccion, String>  colProducto;
    @FXML private TableColumn<Transaccion, Double>  colPrecio;
    @FXML private TableColumn<Transaccion, String>  colFecha;

    private final TransaccionDAO dao     = new TransaccionDAO();
    private final TicketService  ticket  = new TicketService();

    // =====================================================
    // INICIALIZAR
    // Carga el historial de compras del usuario logueado
    // =====================================================
    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        // Solo muestra las compras del usuario activo en sesión
        int idUsuario = Sesion.getUsuario().getId();
        tablaCompras.setItems(
                FXCollections.observableArrayList(dao.listarPorComprador(idUsuario))
        );
    }

    // =====================================================
    // IMPRIMIR TICKET
    // Toma la compra seleccionada en la tabla y genera el PDF.
    // Si no hay nada seleccionado, muestra un aviso.
    // =====================================================
    @FXML
    public void imprimirTicket() {

        Transaccion seleccionada = tablaCompras.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            // Avisa al usuario que debe seleccionar una fila
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText(null);
            alert.setContentText("Selecciona una compra de la tabla para imprimir su ticket.");
            alert.showAndWait();
            return;
        }

        // Genera y abre el ticket PDF con los datos de la compra
        ticket.mostrarTicket(seleccionada, Sesion.getUsuario().getUsername());
    }
}