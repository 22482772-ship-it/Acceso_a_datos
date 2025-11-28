package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;
    public static Connection getConnection(){
        if(connection == null){
            createConnection();
        }
        return connection;
    }
    private static void createConnection(){
        String user = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/almacen";
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos");
            System.out.println(e.getMessage());
        }
    }
}
