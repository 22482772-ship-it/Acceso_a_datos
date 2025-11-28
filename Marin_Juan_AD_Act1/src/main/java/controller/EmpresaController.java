package controller;

import dao.ProductosDAOImp;
import model.empleados;
import model.pedidos;
import model.productos;
import model.productos_fav;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date; //

public class EmpresaController {

    private ProductosDAOImp dao;

    public EmpresaController() {
        this.dao = new ProductosDAOImp();
    }

    // Cargar Productos del JSON
    public void agregarProductos(productos producto) {
        try {
            dao.insertarProducto(producto);
        } catch (SQLException e) {
            System.out.println("Error al insertar producto del JSON: " + e.getMessage());
        }
    }

    // --- Agregar Empleado ---
    public void agregarEmpleado(String nombre) {
        try {
            empleados nuevoEmpleado = new empleados();
            nuevoEmpleado.setNombre(nombre);

            dao.agregarEmpleado(nuevoEmpleado);
            System.out.println("Empleado " + nombre + " registrado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al agregar empleado: " + e.getMessage());
        }
    }

    // Agregar Pedido (Con lógica de negocio) ---
    public void agregarPedido(int idProducto, int idEmpleado, int cantidad) {
        try {
            // Buscamos el precio del producto para calcular el total
            double precioUnitario = 0.0;
            boolean encontrado = false;

            ArrayList<productos> listaProductos = dao.listarProductos();
            for (productos p : listaProductos) {
                if (p.getId() == idProducto) {
                    precioUnitario = p.getPrecio();
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("Error: No existe ningún producto con el ID " + idProducto);
                return;
            }

            // Calculamos el total
            double precioTotal = precioUnitario * cantidad;

            pedidos nuevoPedido = new pedidos(
                    0,              // ID (Autoincremental)
                    cantidad,       // Cantidad
                    precioTotal,    // Precio Total calculado
                    idProducto,     // ID Producto
                    idEmpleado      // ID Empleado
            );

            dao.agregarPedido(nuevoPedido);
            System.out.println("Pedido registrado. Total a pagar: " + precioTotal + "€");

        } catch (SQLException e) {
            System.out.println("Error al procesar el pedido: " + e.getMessage());
        }
    }

    // Mostrar todos los productos ---
    public void mostrarTodosProductos() {
        ArrayList<productos> lista = dao.listarProductos();
        System.out.println("\n--- LISTA DE PRODUCTOS ---");
        for (productos p : lista) {
            System.out.println("ID: " + p.getId() + " | " + p.getNombre() + " | " + p.getPrecio() + "€");
        }
    }

    // --- Mostrar productos baratos (< 600€) ---
    public void mostrarProductosBaratos() {
        ArrayList<productos> lista = dao.listarProductosBaratos();
        System.out.println("\n--- PRODUCTOS BARATOS (< 600€) ---");
        if (lista.isEmpty()) {
            System.out.println("No hay productos baratos.");
        } else {
            for (productos p : lista) {
                System.out.println("ID: " + p.getId() + " | " + p.getNombre() + " | " + p.getPrecio() + "€");
            }
        }
    }

    // --- Insertar en tabla Productos_Fav ---
    public void insertarFavoritos() {
        dao.insertarFavoritos();
    }

    // --- Mostrar tabla Productos_Fav ---
    public void mostrarFavoritos() {
        ArrayList<productos_fav> lista = dao.listarProductosFav();
        System.out.println("\n--- LISTA DE FAVORITOS ---");
        if (lista.isEmpty()) {
            System.out.println("No hay favoritos guardados.");
        } else {
            for (productos_fav p : lista) {
                System.out.println("ID Registro: " + p.getId() + " | ID Producto Original: " + p.getId_productos());
            }
        }
    }
}