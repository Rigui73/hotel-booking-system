
package RoomServDev.persistence.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import RoomServDev.model.Hotel;
import RoomServDev.persistence.dao.ConexionBD;
import RoomServDev.persistence.dao.HotelDAO;

public class HotelDAOImpl implements HotelDAO {

    private static final String SQL_INSERT = "INSERT INTO hoteles (nombre, direccion, ciudad, telefono) VALUES (?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID = "SELECT id_hotel, nombre, direccion, ciudad, telefono FROM hoteles WHERE id_hotel = ?";

    private static final String SQL_SELECT_BY_NOMBRE = "SELECT id_hotel, nombre, direccion, ciudad, telefono FROM hoteles WHERE LOWER(nombre) = LOWER(?)";

    private static final String SQL_SELECT_ALL = "SELECT id_hotel, nombre, direccion, ciudad, telefono FROM hoteles ORDER BY nombre";

    private static final String SQL_UPDATE = "UPDATE hoteles SET nombre = ?, direccion = ?, ciudad = ?, telefono = ? WHERE id_hotel = ?";

    private static final String SQL_DELETE = "DELETE FROM hoteles WHERE id_hotel = ?";

    @Override
    public void insertar(Hotel hotel) throws SQLException {

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(
                        SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hotel.getNombre().trim());
            ps.setString(2, hotel.getDireccion().trim());
            ps.setString(3, hotel.getCiudad().trim());
            ps.setInt(4, hotel.getTelefono());

            ps.executeUpdate();

            // Recuperar el ID autoincremental generado por MySQL.
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    hotel.setIdHotel(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Hotel obtenerPorId(int idHotel) throws SQLException {

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, idHotel);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearHotel(rs);
                }
            }
        }

        return null;
    }

    @Override
    public Hotel obtenerPorNombres(String nombre) throws SQLException {

        if (nombre == null || nombre.isBlank()) {
            return null;
        }

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_NOMBRE)) {

            ps.setString(1, nombre.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearHotel(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Hotel> obtenerTodosHoteles() throws SQLException {

        List<Hotel> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearHotel(rs));
            }
        }

        return lista;
    }

    @Override
    public void actualizar(Hotel hotel) throws SQLException {

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, hotel.getNombre().trim());
            ps.setString(2, hotel.getDireccion().trim());
            ps.setString(3, hotel.getCiudad().trim());
            ps.setInt(4, hotel.getTelefono());
            ps.setInt(5, hotel.getIdHotel());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int idHotel) throws SQLException {

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, idHotel);

            ps.executeUpdate();
        }
    }

    /**
     * Convierte una fila del ResultSet en un objeto Hotel.
     */
    private Hotel mapearHotel(ResultSet rs) throws SQLException {

        Hotel hotel = new Hotel();

        hotel.setIdHotel(rs.getInt("id_hotel"));
        hotel.setNombre(rs.getString("nombre"));
        hotel.setDireccion(rs.getString("direccion"));
        hotel.setCiudad(rs.getString("ciudad"));
        hotel.setTelefono(rs.getInt("telefono"));

        return hotel;
    }
}
