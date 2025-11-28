package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.EmpresaController;
import model.ProductosResponse;
import model.productos;

import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmpresaController controller = new EmpresaController();
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== MENÚ NORTHWIND ===");
            System.out.println("1. Cargar base de datos desde JSON");
            System.out.println("2. Agregar Empleado");
            System.out.println("3. Realizar Pedido");
            System.out.println("4. Mostrar todos los productos");
            System.out.println("5. Mostrar productos baratos (< 600€)");
            System.out.println("6. Guardar favoritos (> 1000€)");
            System.out.println("7. Mostrar favoritos");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            // Control de errores por si meten letras en vez de números
            int opcion;
            if (sc.hasNextInt()) {
                opcion = sc.nextInt();
                sc.nextLine(); // Limpiar buffer
            } else {
                System.out.println("Por favor, introduce un número.");
                sc.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    cargarJson(controller);
                    break;

                case 2:
                    System.out.print("Nombre del empleado: ");
                    String nombreEmp = sc.nextLine();
                    controller.agregarEmpleado(nombreEmp);
                    break;

                case 3:
                    System.out.println("--- NUEVO PEDIDO ---");
                    System.out.print("ID del Producto: ");
                    int idProd = sc.nextInt();
                    System.out.print("ID del Empleado: ");
                    int idEmp = sc.nextInt();
                    System.out.print("Cantidad: ");
                    int cantidad = sc.nextInt();
                    sc.nextLine(); // Limpiar buffer

                    controller.agregarPedido(idProd, idEmp, cantidad);
                    break;

                case 4:
                    controller.mostrarTodosProductos();
                    break;

                case 5:
                    controller.mostrarProductosBaratos();
                    break;

                case 6:
                    controller.insertarFavoritos();
                    break;

                case 7:
                    controller.mostrarFavoritos();
                    break;

                case 0:
                    salir = true;
                    System.out.println("Saliendo de la aplicación...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
        sc.close();
    }


    private static void cargarJson(EmpresaController controller) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            URL url = new URL("https://dummyjson.com/products");
            ProductosResponse response = mapper.readValue(url, ProductosResponse.class);

            int count = 0;
            for (productos item : response.getProductos()) {
                controller.agregarProductos(item);
                System.out.println("Producto agregado: " + item.getNombre());
                count++;
            }
            System.out.println("--> Carga finalizada. Total productos procesados: " + count);

        } catch (Exception e) {
            System.out.println("Error crítico al cargar JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}