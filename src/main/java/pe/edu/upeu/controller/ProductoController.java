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

    // Guarda temporalmente los bytes de la imagen seleccionada.
    // Se usa al guardar o actualizar el producto.
    private byte[] imagenSeleccionada = null;

    // Usuario logueado (recibido desde el Login)
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

        // Cuando el usuario selecciona una fila,
        // carga sus datos en el formulario incluyendo la foto
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
    // Si tiene imagen en BD, la muestra en el ImageView.
    // =====================================================
    private void cargarFormulario(Producto p) {

        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));

        // Si el producto tiene imagen guardada en BD,
        // convierte los bytes a Image y la muestra
        if (p.getImagen() != null) {
            Image img = new Image(new ByteArrayInputStream(p.getImagen()));
            imgProducto.setImage(img);
            imagenSeleccionada = p.getImagen(); // mantiene la foto actual
        } else {
            // Si no tiene foto, limpia el ImageView
            imgProducto.setImage(null);
            imagenSeleccionada = null;
        }
    }

    // =====================================================
    // SELECCIONAR FOTO
    // Abre el explorador de archivos para elegir una imagen.
    // Muestra la imagen al instante en el ImageView.
    // Guarda los bytes en imagenSeleccionada para luego persistir.
    // =====================================================
    @FXML
    public void seleccionarFoto() {

        // FileChooser filtra solo imágenes
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar imagen del producto");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"
                )
        );

        // Abre el diálogo y espera la selección del usuario
        File archivo = chooser.showOpenDialog(null);

        if (archivo != null) {
            try {
                // Lee todos los bytes del archivo seleccionado
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

            // Asigna los bytes de la imagen seleccionada.
            // Si no se eligió foto, queda null.
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

            // Si el usuario seleccionó una foto nueva, se actualiza.
            // Si no seleccionó nada, mantiene la foto anterior (ya está en imagenSeleccionada).
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
    // Borra el producto seleccionado (y su imagen BLOB)
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
    // Resetea el formulario, la imagen y la variable temporal
    // =====================================================
    private void limpiar() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtStock.clear();
        imgProducto.setImage(null);  // limpia la foto del formulario
        imagenSeleccionada = null;   // resetea los bytes temporales
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