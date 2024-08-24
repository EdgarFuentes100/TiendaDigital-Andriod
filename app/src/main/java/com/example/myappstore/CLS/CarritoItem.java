package com.example.myappstore.CLS;
public class CarritoItem {
    private int idCarritoItem;
    private int idCarrito;
    private int idProducto;
    private int cantidad;

    public CarritoItem() {
    }

    public int getIdCarritoItem() {
        return idCarritoItem;
    }

    public void setIdCarritoItem(int idCarritoItem) {
        this.idCarritoItem = idCarritoItem;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
