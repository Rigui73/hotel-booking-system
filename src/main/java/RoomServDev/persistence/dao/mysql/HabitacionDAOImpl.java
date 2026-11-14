
package RoomServDev.persistence.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Simple;
import RoomServDev.model.Suite;
import RoomServDev.persistence.connection.DatabaseConnection;
import RoomServDev.persistence.dao.HabitacionDAO;

public class HabitacionDAOImpl implements HabitacionDAO {

    private static final String SQL_INSERT = "INSERT INTO habitaciones " +
            "(num_habitacion, capacidad, precio_noche, disponible, categoria, id_hotel) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT = "SELECT h.id_habitacion, h.id_hotel, h.num_habitacion, " +
            "h.capacidad, h.precio_noche, h.disponible, h.categoria, " +
            "s.medida_cama, s.tiene_escritorio, " +
            "d.tipo_cama_doble, d.supletoria, d.suplemento_supletoria, d.balcon, " +
            "su.num_ambientes, su.jacuzzi, su.incluye_spa, " +
            "su.incluye_terraza, su.otros_servicios " +
            "FROM habitaciones h " +
            "LEFT JOIN habitaciones_simple s ON h.id_habitacion = s.id_habitacion " +
            "LEFT JOIN habitaciones_doble d ON h.id_habitacion = d.id_habitacion " +
            "LEFT JOIN habitaciones_suite su ON h.id_habitacion = su.id_habitacion ";

    private static final String SQL_SELECT_BY_ID = SQL_SELECT + "WHERE h.id_habitacion = ?";

    private static final String SQL_SELECT_BY_HOTEL = SQL_SELECT + "WHERE h.id_hotel = ? ORDER BY h.num_habitacion";

    private static final String SQL_UPDATE = "UPDATE habitaciones SET num_habitacion = ?, capacidad = ?, " +
            "precio_noche = ?, disponible = ?, categoria = ?, id_hotel = ? " +
            "WHERE id_habitacion = ?";

    private static final String SQL_DELETE = "DELETE FROM habitaciones WHERE id_habitacion = ?";

    @Override
    public void insertar(Habitacion habitacion, int idHotel) throws SQLException {

        try (Connection conn = DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);
            int idAnterior = habitacion.getIdHabitacion();
            int hotelAnterior = habitacion.getIdHotel();

            try {
                int idGenerado;

                try (PreparedStatement ps = conn.prepareStatement(
                        SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, habitacion.getNumHabitacion());
                    ps.setInt(2, habitacion.getCapacidad());
                    ps.setDouble(3, habitacion.getPrecioNoche());
                    ps.setBoolean(4, habitacion.isDisponible());
                    ps.setString(5, determinarCategoria(habitacion));
                    ps.setInt(6, idHotel);

                    if (ps.executeUpdate() != 1)
                        throw new SQLException("No se pudo insertar la habitación.");

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "No se ha podido recuperar el ID de la habitación");
                        }

                        idGenerado = rs.getInt(1);
                    }
                }

                habitacion.setIdHabitacion(idGenerado);
                habitacion.setIdHotel(idHotel);

                insertarDatosEspecificos(conn, habitacion);

                conn.commit();

            } catch (SQLException | RuntimeException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                habitacion.setIdHabitacion(idAnterior);
                habitacion.setIdHotel(hotelAnterior);
                throw e;
            }
        }
    }

    @Override
    public Habitacion obtenerPorId(int idHabitacion) throws SQLException {

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, idHabitacion);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearHabitacion(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Habitacion> obtenerPorHotel(int idHotel) throws SQLException {

        List<Habitacion> habitaciones = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_HOTEL)) {

            ps.setInt(1, idHotel);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    habitaciones.add(mapearHabitacion(rs));
                }
            }
        }

        return habitaciones;
    }

    @Override
    public void actualizar(Habitacion habitacion) throws SQLException {

        try (Connection conn = DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

                    ps.setInt(1, habitacion.getNumHabitacion());
                    ps.setInt(2, habitacion.getCapacidad());
                    ps.setDouble(3, habitacion.getPrecioNoche());
                    ps.setBoolean(4, habitacion.isDisponible());
                    ps.setString(5, determinarCategoria(habitacion));
                    ps.setInt(6, habitacion.getIdHotel());
                    ps.setInt(7, habitacion.getIdHabitacion());

                    int filas = ps.executeUpdate();

                    if (filas == 0) {
                        throw new SQLException(
                                "No existe la habitación que se quiere actualizar");
                    }
                }

                eliminarDatosEspecificos(conn, habitacion.getIdHabitacion());

                insertarDatosEspecificos(conn, habitacion);

                conn.commit();

            } catch (SQLException | RuntimeException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }
        }
    }

    @Override
    public void eliminar(int idHabitacion) throws SQLException {

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, idHabitacion);
            if (ps.executeUpdate() != 1) {
                throw new SQLException("No existe la habitación " + idHabitacion + " para eliminar.");
            }
        }
    }

    /**
     * Determina la categoría según la subclase del objeto.
     */
    private String determinarCategoria(Habitacion habitacion) {

        if (habitacion instanceof Simple) {
            return "Simple";
        }

        if (habitacion instanceof Doble) {
            return "Doble";
        }

        if (habitacion instanceof Suite) {
            return "Suite";
        }

        throw new IllegalArgumentException(
                "Tipo de habitación no reconocido");
    }

    /**
     * Inserta los atributos específicos en la tabla correspondiente.
     */
    private void insertarDatosEspecificos(
            Connection conn, Habitacion habitacion) throws SQLException {

        if (habitacion instanceof Simple simple) {

            String sql = "INSERT INTO habitaciones_simple " +
                    "(id_habitacion, medida_cama, tiene_escritorio) " +
                    "VALUES (?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, habitacion.getIdHabitacion());
                ps.setString(2, simple.getMedidaCama());
                ps.setBoolean(3, simple.isTieneEscritorio());
                ps.executeUpdate();
            }

        } else if (habitacion instanceof Doble doble) {

            String sql = "INSERT INTO habitaciones_doble " +
                    "(id_habitacion, tipo_cama_doble, supletoria, " +
                    "suplemento_supletoria, balcon) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, habitacion.getIdHabitacion());
                ps.setString(2, doble.getTipoCamaDoble());
                ps.setBoolean(3, doble.isSupletoria());
                ps.setDouble(4, doble.getSuplementSupletoria());
                ps.setBoolean(5, doble.isBalcon());
                ps.executeUpdate();
            }

        } else if (habitacion instanceof Suite suite) {

            String sql = "INSERT INTO habitaciones_suite " +
                    "(id_habitacion, num_ambientes, jacuzzi, incluye_spa, " +
                    "incluye_terraza, otros_servicios) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, habitacion.getIdHabitacion());
                ps.setInt(2, suite.getNumAmbientes());
                ps.setBoolean(3, suite.isJacuzzi());
                ps.setBoolean(4, suite.isIncluyeSpa());
                ps.setBoolean(5, suite.isIncluyeTerraza());
                ps.setString(6, suite.getOtrosServs());
                ps.executeUpdate();
            }
        }
    }

    /**
     * Elimina los datos específicos anteriores de una habitación.
     */
    private void eliminarDatosEspecificos(
            Connection conn, int idHabitacion) throws SQLException {

        String[] tablas = {
                "habitaciones_simple",
                "habitaciones_doble",
                "habitaciones_suite"
        };

        for (String tabla : tablas) {

            String sql = "DELETE FROM " + tabla + " WHERE id_habitacion = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idHabitacion);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Reconstruye el objeto Java correspondiente a la categoría almacenada.
     */
    private Habitacion mapearHabitacion(ResultSet rs) throws SQLException {

        int idHabitacion = rs.getInt("id_habitacion");
        int idHotel = rs.getInt("id_hotel");
        int numero = rs.getInt("num_habitacion");
        int capacidad = rs.getInt("capacidad");
        double precio = rs.getDouble("precio_noche");
        boolean disponible = rs.getBoolean("disponible");
        String categoria = rs.getString("categoria");

        Habitacion habitacion;

        switch (categoria.toLowerCase()) {

            case "simple":
                habitacion = new Simple(
                        numero,
                        capacidad,
                        precio,
                        "Simple",
                        rs.getString("medida_cama"),
                        rs.getBoolean("tiene_escritorio"));
                break;

            case "doble":
                habitacion = new Doble(
                        numero,
                        capacidad,
                        precio,
                        capacidad,
                        "Doble",
                        rs.getString("tipo_cama_doble"),
                        rs.getBoolean("supletoria"),
                        rs.getDouble("suplemento_supletoria"),
                        rs.getBoolean("balcon"));
                break;

            case "suite":
                habitacion = new Suite(
                        numero,
                        capacidad,
                        precio,
                        "Suite",
                        rs.getInt("num_ambientes"),
                        rs.getBoolean("jacuzzi"),
                        rs.getBoolean("incluye_spa"),
                        rs.getBoolean("incluye_terraza"),
                        rs.getString("otros_servicios"));
                break;

            default:
                throw new SQLException(
                        "Categoría de habitación desconocida: " + categoria);
        }

        habitacion.setIdHabitacion(idHabitacion);
        habitacion.setIdHotel(idHotel);
        habitacion.setDisponible(disponible);

        return habitacion;
    }
}
