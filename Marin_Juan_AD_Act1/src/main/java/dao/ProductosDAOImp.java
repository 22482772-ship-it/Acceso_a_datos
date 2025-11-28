package dao;

import database.*;
import model.empleados;
import model.pedidos;
import model.productos;
import model.productos_fav;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductosDAOImp implements ProductoDAO {

    private Connection connection;

    // Constructor único
    public ProductosDAOImp() {
        connection = DBConnection.getConnection();
    }

    @Override
    public void insertarProducto(productos producto) throws SQLException {
        String query = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
                productos_SchemeDB.TABLA_PRODUCTOS,
                productos_SchemeDB.COL_ID,
                productos_SchemeDB.COL_NOMBRE,
                productos_SchemeDB.COL_DESCRIPCION,
                productos_SchemeDB.COL_CANTIDAD,
                productos_SchemeDB.COL_PRECIO);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, producto.getId());
            preparedStatement.setString(2, producto.getNombre());
            preparedStatement.setString(3, producto.getDescripcion());
            preparedStatement.setInt(4, producto.getCantidad());
            preparedStatement.setDouble(5, producto.getPrecio());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void agregarEmpleado(empleados empleado) throws SQLException {
        String query = String.format("INSERT INTO %s (%s) VALUES (?)",
                empleados_SchemeDB.TABLA_EMPLEADOS,
                empleados_SchemeDB.COL_NOMBRE);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, empleado.getNombre());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void agregarPedido(pedidos pedido) throws SQLException {
        String query = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                pedidos_SchemeDB.TABLA_PEDIDOS,
                pedidos_SchemeDB.COL_ID_EMPLEADO,
                pedidos_SchemeDB.COL_ID_PRODUCTO,
                pedidos_SchemeDB.COL_CANTIDAD,
                pedidos_SchemeDB.COL_PRECIO_TOTAL);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pedido.getId_empleado());
            preparedStatement.setInt(2, pedido.getId_producto());
            preparedStatement.setInt(3, pedido.getCantidad());
            preparedStatement.setDouble(4, pedido.getPrecio_total());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public ArrayList<productos> listarProductos() {
        ArrayList<productos> listaResultante = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", productos_SchemeDB.TABLA_PRODUCTOS);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                listaResultante.add(mapearProducto(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Fallo al listar productos: " + e.getMessage());
        }
        return listaResultante;
    }

    @Override
    public void insertarFavoritos() {
        String query = String.format("INSERT INTO %s (%s) SELECT %s FROM %s WHERE %s > ? AND %s NOT IN (SELECT %s FROM %s)" ,
// 1. Parte del INSERT
                productos_fav_SchemeDB.TABLA_PRODUCTOS_FAV,
                productos_fav_SchemeDB.COL_ID_PRODUCTOS,

                // 2. Parte del SELECT original
                productos_SchemeDB.COL_ID,
                productos_SchemeDB.TABLA_PRODUCTOS,
                productos_SchemeDB.COL_PRECIO,

                // 3. Parte del NOT IN
                productos_SchemeDB.COL_ID,                  // El ID del producto original...
                productos_fav_SchemeDB.COL_ID_PRODUCTOS,     // ...no debe estar en la columna de favoritos...
                productos_fav_SchemeDB.TABLA_PRODUCTOS_FAV);  // ...de la tabla favoritos.

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Asignamos el valor 1000 al interrogante (?)
            preparedStatement.setDouble(1, 1000.0);

            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
            System.out.println("Se han insertado " + filasAfectadas + " productos en favoritos.");
        } else {
                System.out.println("Ya existe el producto en favoritos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar favoritos: " + e.getMessage());
        }
    }

    public ArrayList<productos> listarProductosBaratos() {
        ArrayList<productos> listaResultante = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s < ?",
                productos_SchemeDB.TABLA_PRODUCTOS,
                productos_SchemeDB.COL_PRECIO);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, 600.0); // Asignamos 600

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    listaResultante.add(mapearProducto(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println("Fallo al listar baratos: " + e.getMessage());
        }
        return listaResultante;
    }

    public ArrayList<productos_fav> listarProductosFav() {
        ArrayList<productos_fav> listaResultante = new ArrayList<>();
        String query = String.format("SELECT * FROM %s",
                productos_fav_SchemeDB.TABLA_PRODUCTOS_FAV);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int id_productos = resultSet.getInt(productos_fav_SchemeDB.COL_ID_PRODUCTOS);
                listaResultante.add(new productos_fav(id, id_productos));
            }
        } catch (SQLException e) {
            System.out.println("Fallo al listar favoritos: " + e.getMessage());
        }
        return listaResultante;
    }

    // Mapeo de Producto
    private productos mapearProducto(ResultSet resultSet) throws SQLException {
        return new productos(
                resultSet.getInt(productos_SchemeDB.COL_ID),
                resultSet.getString(productos_SchemeDB.COL_NOMBRE),
                resultSet.getString(productos_SchemeDB.COL_DESCRIPCION),
                resultSet.getInt(productos_SchemeDB.COL_CANTIDAD),
                resultSet.getDouble(productos_SchemeDB.COL_PRECIO)
        );
    }
}