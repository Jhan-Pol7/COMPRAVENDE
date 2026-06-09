package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import pe.edu.upeu.dao.ProductoDAO;
import pe.edu.upeu.model.Producto;
import pe.edu.upeu.model.Usuario;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

public class ProductoController {

    // ================= FORMULARIO =================
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    // ImageView que muestra la foto del producto en el formulario
    @FXML private ImageView imgProducto;

    // ================= BOTONES =================
    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;

    // ================= TABLA =================
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String>  colNombre;
    @FXML private TableColumn<Producto, String>  colDescripcion;
    @FXML private TableColumn<Producto, Double>  colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    // ================= DEPENDENCIAS =================
    private final ProductoDAO dao = new ProductoDAO();
    private ObservableList<Producto> lista;

    // Guarda temporalmente los bytes de la imagen seleccionada
    private byte[] imagenSeleccionada = null;

    // Usuario logueado recibido desde el Login
    private Usuario usuarioActual;

    // =====================================================
    // INICIALIZAR VISTA
    // Configura columnas de la tabla y listener de selección
    // =====================================================
    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        cargarDatos();

        // Al seleccionar una fila, carga sus datos en el formulario
        tablaProductos.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    if (newSel != null) cargarFormulario(newSel);
                });
    }

    // =====================================================
    // CARGAR TABLA
    // Obtiene todos los productos desde la BD y los muestra
    // =====================================================
    private void cargarDatos() {
        lista = FXCollections.observableArrayList(dao.listar());
        tablaProductos.setItems(lista);
    }

    // =====================================================
    // CARGAR FORMULARIO
    // Rellena los campos con los datos del producto seleccionado.
    // Si tiene imagen en BD la muestra; si no, muestra sin-foto.png
    // =====================================================
    private void cargarFormulario(Producto p) {

        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));

        if (p.getImagen() != null) {
            // Convierte los bytes de la BD a Image y la muestra
            Image img = new Image(new ByteArrayInputStream(p.getImagen()));
            imgProducto.setImage(img);
            imagenSeleccionada = p.getImagen();
        } else {
            // Si no tiene foto, muestra la imagen por defecto desde resources
            Image sinFoto = new Image(
                    getClass().getResourceAsStream(
                            "/pe/edu/upeu/img/sin-foto.png"
                    )
            );
            imgProducto.setImage(sinFoto);
            imagenSeleccionada = null;
        }
    }

    // =====================================================
    // SELECCIONAR FOTO
    // Abre explorador de archivos, muestra la imagen al instante
    // y guarda sus bytes en imagenSeleccionada para persistirla
    // =====================================================
    @FXML
    public void seleccionarFoto() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar imagen del producto");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"
                )
        );

        File archivo = chooser.showOpenDialog(null);

        if (archivo != null) {
            try {
                // Lee los bytes del archivo para guardarlos en la BD
                imagenSeleccionada = Files.readAllBytes(archivo.toPath());

                // Muestra la imagen inmediatamente en el formulario
                Image img = new Image(archivo.toURI().toString());
                imgProducto.setImage(img);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =====================================================
    // VALIDACIÓN
    // Verifica que los campos obligatorios no estén vacíos
    // =====================================================
    private boolean validar() {
        return !txtNombre.getText().isEmpty()
                && !txtDescripcion.getText().isEmpty()
                && !txtPrecio.getText().isEmpty()
                && !txtStock.getText().isEmpty();
    }

    // =====================================================
    // GUARDAR
    // Crea un nuevo producto con todos sus campos + imagen
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

            // Asigna los bytes de la imagen; null si no se eligió foto
            p.setImagen(imagenSeleccionada);

            dao.guardar(p);
            limpiar();
            cargarDatos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // ACTUALIZAR
    // Modifica el producto seleccionado incluyendo su imagen
    // =====================================================
    @FXML
    public void actualizar() {

        Producto p = tablaProductos.getSelectionModel().getSelectedItem();
        if (p == null) return;

        try {
            p.setNombre(txtNombre.getText());
            p.setDescripcion(txtDescripcion.getText());
            p.setPrecio(Double.parseDouble(txtPrecio.getText()));
            p.setStock(Integer.parseInt(txtStock.getText()));

            // Actualiza con la foto nueva o mantiene la anterior
            p.setImagen(imagenSeleccionada);

            dao.actualizar(p);
            limpiar();
            cargarDatos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // ELIMINAR
    // Borra el producto seleccionado y su imagen BLOB
    // =====================================================
    @FXML
    public void eliminar() {

        Producto p = tablaProductos.getSelectionModel().getSelectedItem();
        if (p == null) return;

        dao.eliminar(p.getId());
        limpiar();
        cargarDatos();
    }

    // =====================================================
    // LIMPIAR
    // Resetea formulario, ImageView y bytes temporales
    // =====================================================
    private void limpiar() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtStock.clear();
        imgProducto.setImage(null);
        imagenSeleccionada = null;
    }

    // =====================================================
    // RECIBIR USUARIO DESDE LOGIN
    // =====================================================
    public void setUsuario(Usuario u) {
        this.usuarioActual = u;
        aplicarPermisos();
    }

    // =====================================================
    // PERMISOS POR ROL
    // ADMIN puede todo, USER solo puede ver
    // =====================================================
    private void aplicarPermisos() {

        if (usuarioActual == null) return;

        boolean esAdmin = usuarioActual.getRol().equals("ADMIN");

        // Habilita o deshabilita botones según el rol
        btnGuardar.setDisable(!esAdmin);
        btnActualizar.setDisable(!esAdmin);
        btnEliminar.setDisable(!esAdmin);
    }
}