package RoomServDev.model.dao;

import java.sql.Driver;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;

public class ConexionBD {

    //parametros configuracion conexion

    private static final String CONTROLADOR = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/roomservdev_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "hotel";
    private static final String CLAVE = "hotel123";

    // registro del driver en la JVM
    static {
        try {
            Class.forName(CONTROLADOR);
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] No se encontró el driver JDBC de MySQL: ");
        }
    }

    // Constructor privado para impedir instanciacion directa
    private ConexionBD() {
    }
    /*
    - obtiene conexion fisica con la bbdd MySQL
    - @return Objeto connection activo
    - @throws SQLException si ocurre un error de autenticación o enlace de red
     */

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(ConexionBD.URL, ConexionBD.USUARIO, ConexionBD.CLAVE);
    }

    /* Cierra de forma segura una conexión abierta verificando su estado.
    @param con Conexion a liberar.
     */

    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("[ERROR] Fallo al cerrar la conexion JDBC: " + e.getMessage());
            }
        }
    }
}