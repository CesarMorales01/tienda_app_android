package com.example.Modelos;

import com.orm.SugarRecord;

public class CarritoCompras extends SugarRecord {
    private String producto, cantidad, precio;

    public CarritoCompras(){}

    public CarritoCompras(String producto, String cantidad, String precio) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
