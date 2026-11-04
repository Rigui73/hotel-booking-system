package RoomServDev.model.dao;

import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Simple;
import RoomServDev.model.Suite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDAOImpl implements HabitacionDAO {

    private static final String SQL_INSERT =
            "INSERT INTO habitaciones (num_habitacion, capacidad, precio_noche, disponible, categoria, id_hotel) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID =
            "SELECT id_habitacion, num_habitacion, capacidad, precio_noche, disponible, categoria, id_hotel " +
                    "FROM habitaciones WHERE id_habitacion = ?";
    private static final String SQL_SELECT_BY_HOTEL =
            "SELECT id_habitacion, num_habitacion, capacidad, precio_noche, disponible, categoria, id_hotel " +
                    "FROM habitaciones WHERE id_hotel = ? ORDER BY num_habitacion";
    private static final String SQL_UPDATE =
            "UPDATE habitaciones SET num_habitacion = ?, capacidad = ?, precio_noche = ?, disponible = ?, categoria = ? " +
                    "WHERE id_habitacion = ?";
    private static final String SQL_DELETE =
            "DELETE FROM habitaciones WHERE id_habitacion = ?";

    @Override
    public void insertar(Habitacion habitacion, int idHotel) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, habitacion.getNumHabitacion());
            ps.setInt(2, habitacion.getCapacidad());
            ps.setDouble(3, habitacion.getPrecioNoche());
            ps.setBoolean(4, habitacion.isDisponible());
            ps.setString(5, determinarCategoria(habitacion));
            ps.setInt(6, idHotel);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    // Si tu modelo Habitacion tiene setId(int):
                    // habitacion.setId(idGenerado);
                }
            }
        }
    }

    @Override
    public Habitacion obtenerPorId(int idHabitacion) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
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
        List<Habitacion> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_HOTEL)) {

            ps.setInt(1, idHotel);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearHabitacion(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Habitacion habitacion) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setInt(1, habitacion.getNumHabitacion());
            ps.setInt(2, habitacion.getCapacidad());
            ps.setDouble(3, habitacion.getPrecioNoche());
            ps.setBoolean(4, habitacion.isDisponible());
            ps.setString(5, determinarCategoria(habitacion));
            // ps.setInt(6, habitacion.getId()); // Clave primaria de la habitación

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int idHabitacion) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, idHabitacion);
            ps.executeUpdate();
        }
    }

    // =========================================================================
    // Métodos auxiliares de mapeo polimórfico
    // =========================================================================

    private String determinarCategoria(Habitacion h) {
        if (h instanceof Simple) return "Simple";
        if (h instanceof Doble) return "Doble";
        if (h instanceof Suite) return "Suite";
        return "Simple";
    }

    private Habitacion mapearHabitacion(ResultSet rs) throws SQLException {
        int numero = rs.getInt("num_habitacion");
        int capacidad = rs.getInt("capacidad");
        double precio = rs.getDouble("precio_noche");
        String cat = rs.getString("categoria");
        boolean disponible = rs.getBoolean("disponible");

        if ("Doble".equalsIgnoreCase(cat)) {
            // Doble: numHabitacion, capacidadMax, precioNoche, capacidad, categoria,
            //        tipoCamaDoble, supletoria, suplementSupletoria, balcon
            return new Doble(numero, capacidad, precio, capacidad, "Doble",
                    "Matrimonio", true, 25.00, true);
        } else if ("Suite".equalsIgnoreCase(cat)) {
            // Suite: numHabitacion, capacidad, precioNoche, categoria,
            //        numAmbientes, jacuzzi, incluyeSpa, incluyeTerraza, otrosServs
            return new Suite(numero, capacidad, precio, "Suite",
                    2, true, true, true, "Minibar y vistas");
        } else {
            // Simple: numHabitacion, capacidad, precioNoche, categoria,
            //         medidaCama, tieneEscritorio
            return new Simple(numero, capacidad, precio, "Simple",
                    "90x200", true);
        }
    }
}
