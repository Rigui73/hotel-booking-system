package RoomServDev.model.dao;


import java.sql.Connection;
import java.sql.SQLException;

class Conexion {
    public static void main(String[] args) {
        System.out.println("Intentando conectar con roomservdev_db...");
        try (Connection conn = ConexionBD.obtenerConexion()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("[ÉXITO] Conexión establecida correctamente con MySQL.");
            }
        } catch (SQLException e) {
            System.err.println("[FALLO] Error de conexión: " + e.getMessage());
        }
    }
}