
package RoomServDev;

import RoomServDev.persistence.connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseConnection {

    public static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            System.out.println("Conexión establecida correctamente.");
            System.out.println("Base de datos: " + connection.getCatalog());

        } catch (SQLException | IllegalStateException e) {

            System.out.println("Error al conectar con la base de datos.");
            e.printStackTrace();
        }
    }
}
