package dao;

import model.empleados;
import model.pedidos;
import model.productos;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ProductoDAO {
    //los metodos contra la base de datos de productos
    //metodos abstractos
    void insertarProducto(productos producto) throws SQLException;
    void agregarEmpleado(empleados empleado) throws SQLException;
    void agregarPedido(pedidos pedido) throws SQLException;
    void insertarFavoritos();
    ArrayList<productos> listarProductos();
}
