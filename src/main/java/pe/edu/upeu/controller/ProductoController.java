package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dao.ProductoDAO;
import pe.edu.upeu.model.Producto;

public class ProductoController {

    // ================= FORMULARIO =================
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    // ================= TABLA =================
    @FXML private TableView<Producto> tablaProductos;

    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    // ================= DAO =================
    private final ProductoDAO dao = new ProductoDAO();

    // ================= LISTA OBSERVABLE =================
    private ObservableList<Producto> lista;

    // =====================================================
    // INICIALIZA AUTOMÁTICAMENTE AL ABRIR LA VISTA
    // =====================================================
    @FXML
    public void initialize() {

        // Conectar columnas con atributos del modelo
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Cargar datos iniciales
        cargarDatos();

        // Evento: seleccionar fila en tabla
        tablaProductos.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {

                    if (newSel != null) {
                        cargarFormulario(newSel);
                    }
                });
    }

    // =====================================================
    // CARGAR DATOS EN TABLA
    // =====================================================
    private void cargarDatos() {

        lista = FXCollections.observableArrayList(dao.listar());

        tablaProductos.setItems(lista);
    }

    // =====================================================
    // CARGAR DATOS EN FORMULARIO
    // =====================================================
    private void cargarFormulario(Producto p) {

        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));
    }

    // =====================================================
    // VALIDACIÓN DE CAMPOS
    // =====================================================
    private boolean validar() {

        if (txtNombre.getText().isEmpty() ||
                txtDescripcion.getText().isEmpty() ||
                txtPrecio.getText().isEmpty() ||
                txtStock.getText().isEmpty()) {

            System.out.println("❌ Campos vacíos");
            return false;
        }

        return true;
    }

    // =====================================================
    // GUARDAR
    // =====================================================
    @FXML
    public void guardar() {

        if (!validar()) return;

        try {

            Producto p = new Producto();

            p.setNombre(txtNombre.getText());
            p.setDescripcion(txtDescripcion.getText());
            p.setPrecio(Double.parseDouble(txtPrecio.getText()));
            p.setStock(Integer.parseInt(txtStock.getText()));

            dao.guardar(p);

            System.out.println("✔ Producto guardado");

            limpiar();
            cargarDatos();

        } catch (NumberFormatException e) {

            System.out.println("❌ Error en precio o stock");
        }
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================
    @FXML
    public void actualizar() {

        Producto seleccionado =
                tablaProductos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            System.out.println("❌ Selecciona un producto");
            return;
        }

        if (!validar()) return;

        try {

            seleccionado.setNombre(txtNombre.getText());
            seleccionado.setDescripcion(txtDescripcion.getText());
            seleccionado.setPrecio(Double.parseDouble(txtPrecio.getText()));
            seleccionado.setStock(Integer.parseInt(txtStock.getText()));

            dao.actualizar(seleccionado);

            System.out.println("✔ Producto actualizado");

            limpiar();
            cargarDatos();

        } catch (NumberFormatException e) {

            System.out.println("❌ Error en datos numéricos");
        }
    }

    // =====================================================
    // ELIMINAR
    // =====================================================
    @FXML
    public void eliminar() {

        Producto seleccionado =
                tablaProductos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            System.out.println("❌ Selecciona un producto");
            return;
        }

        dao.eliminar(seleccionado.getId());

        System.out.println("✔ Producto eliminado");

        limpiar();
        cargarDatos();
    }

    // =====================================================
    // LIMPIAR FORMULARIO
    // =====================================================
    private void limpiar() {

        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtStock.clear();
    }
}