
package RoomServDev.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;

import RoomServDev.persistence.connection.DatabaseConnection;

/**
 * Proporciona conexiones a la base de datos.
 * Delega la conexión en DatabaseConnection.
 */
public class ConexionBD {

    // Constructor privado para impedir la instanciación.
    private ConexionBD() {
    }

    /**
     * Obtiene una conexión con MySQL.
     *
     * @return conexión activa
     * @throws SQLException si ocurre un error de conexión
     */
    public static Connection obtenerConexion() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    /**
     * Cierra una conexión de forma segura.
     *
     * @param conn conexión que se desea cerrar
     */
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println(
                        "Error al cerrar la conexión JDBC: " + e.getMessage());
            }
        }
    }
}
