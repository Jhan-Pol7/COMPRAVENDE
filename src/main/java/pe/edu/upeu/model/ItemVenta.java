package pe.edu.upeu.model;

/**
 * Representa un item dentro del carrito de venta
 */
public class ItemVenta {

    private int idProducto;
    private String nombre;
    private int cantidad;
    private double precioUnitario;

    public ItemVenta() {}

    public ItemVenta(int idProducto, String nombre, int cantidad, double precioUnitario) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    // GETTERS Y SETTERS

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}