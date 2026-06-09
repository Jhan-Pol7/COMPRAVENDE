package pe.edu.upeu.model;

// Representa una compra registrada en el sistema.
// Vincula un comprador, un producto y la fecha de la operación.
public class Transaccion {

    private int id;

    // ID del usuario que compró
    private int idComprador;

    // ID del producto comprado
    private int idProducto;

    // Nombre del producto (para mostrar en historial)
    private String nombreProducto;

    // Precio pagado en el momento de la compra
    private double precio;

    // Fecha en que se realizó la transacción
    private String fecha;

    public Transaccion() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdComprador() { return idComprador; }
    public void setIdComprador(int idComprador) { this.idComprador = idComprador; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}