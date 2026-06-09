package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.ProductoDAO;
import pe.edu.upeu.dao.VentaDAO;
import pe.edu.upeu.model.ItemVenta;
import pe.edu.upeu.model.Producto;

public class VentaController {

    // ================= PRODUCTOS =================
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    // ================= CARRITO =================
    @FXML private TableView<ItemVenta> tablaCarrito;
    @FXML private TableColumn<ItemVenta, String> cNombre;
    @FXML private TableColumn<ItemVenta, Integer> cCantidad;
    @FXML private TableColumn<ItemVenta, Double> cSubtotal;

    // ================= UI =================
    @FXML private TextField txtCantidad;
    @FXML private Label lblTotal;

    // ================= DAO =================
    private ProductoDAO productoDAO = new ProductoDAO();
    private VentaDAO ventaDAO = new VentaDAO();

    // ================= LISTAS =================
    private ObservableList<Producto> productos;
    private ObservableList<ItemVenta> carrito = FXCollections.observableArrayList();

    private double total = 0;

    // =====================================================
    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        cNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        cSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        cargarProductos();
        tablaCarrito.setItems(carrito);
    }

    // ================= CARGAR PRODUCTOS =================
    private void cargarProductos() {
        productos = FXCollections.observableArrayList(productoDAO.listar());
        tablaProductos.setItems(productos);
    }

    // ================= AGREGAR AL CARRITO =================
    @FXML
    public void agregarCarrito() {

        Producto p = tablaProductos.getSelectionModel().getSelectedItem();

        if (p == null) return;

        int cantidad = Integer.parseInt(txtCantidad.getText());

        if (cantidad <= 0 || cantidad > p.getStock()) {
            System.out.println("Cantidad inválida");
            return;
        }

        ItemVenta item = new ItemVenta(
                p.getId(),
                p.getNombre(),
                cantidad,
                p.getPrecio()
        );

        carrito.add(item);

        total += item.getSubtotal();
        lblTotal.setText("Total: " + total);
    }

    // ================= FINALIZAR VENTA =================
    @FXML
    public void finalizarVenta() {

        if (carrito.isEmpty()) return;

        ventaDAO.guardarVenta(carrito, total);

        carrito.clear();
        total = 0;
        lblTotal.setText("Total: 0.0");

        cargarProductos();
    }
}