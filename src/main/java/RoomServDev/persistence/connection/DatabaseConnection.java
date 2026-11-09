
package RoomServDev.persistence.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona las conexiones con la base de datos MySQL.
 */
public class DatabaseConnection {

    private static final String CONFIG_FILE = "/db.properties";

    /**
     * Obtiene una conexión con la base de datos.
     *
     * @return conexión JDBC
     * @throws SQLException si ocurre un error de conexión
     */
    public static Connection getConnection() throws SQLException {

        Properties properties = new Properties();

        try (InputStream input = DatabaseConnection.class
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new IllegalStateException(
                        "No se ha encontrado el archivo db.properties");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error al leer la configuración de la base de datos", e);
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        if (url == null || user == null || password == null) {
            throw new IllegalStateException(
                    "Faltan propiedades de conexión en db.properties");
        }

        return DriverManager.getConnection(url, user, password);
    }
}
