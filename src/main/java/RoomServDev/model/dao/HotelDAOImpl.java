package RoomServDev.model.dao;

import RoomServDev.model.Hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HotelDAOImpl implements HotelDAO {

    private static final String SQL_INSERT =
            "INSERT INTO hoteles (nombre, direccion, ciudad, telefono) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID =
            "SELECT id_hotel, nombre, direccion, ciudad, telefono FROM hoteles WHERE id_hotel = ?";
    private static final String SQL_SELECT_BY_NOMBRE =
            "SELECT id_hotel, nombre, direccion, ciudad, telefono FROM hoteles WHERE LOWER(nombre) = LOWER(?)";
    private static final String SQL_SELECT_ALL =
            "SELECT id_hotel, nombre, direccion, ciudad, telefono FROM hoteles ORDER BY nombre";
    private static final String SQL_UPDATE =
            "UPDATE hoteles SET nombre = ?, direccion = ?, ciudad = ?, telefono = ? WHERE id_hotel = ?";
    private static final String SQL_DELETE =
            "DELETE FROM hoteles WHERE id_hotel = ?";

    @Override
    public void insertar(Hotel hotel) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hotel.getNombre().trim());
            ps.setString(2, hotel.getDireccion().trim());
            ps.setString(3, hotel.getCiudad().trim());
            ps.setInt(4, hotel.getTelefono());

            ps.executeUpdate();

            // Si la clase Hotel dispone de un atributo id o setId, se asigna el autonumérico generado
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    // Si tu clase Hotel tiene setId(int), descomenta la siguiente línea:
                    // hotel.setId(idGenerado);
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
    public Hotel obtenerPorNombres(String nombre) throws Exception {
        if (nombre == null || nombre.isBlank()){
            return null;
        }

        String sql = "SELECT id_hotel,nombre,direccion,ciudad,telefono FROM hoteles WHERE LOWER(nombre) = LOWER(?)";
        try (Connection conn = ConexionBD.obtenerConexion();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Hotel hotel = new Hotel();
                    hotel.setIdHotel(rs.getInt("id_hotel"));
                    hotel.setNombre(rs.getString("nombre"));
                    hotel.setDireccion(rs.getString("direccion"));
                    hotel.setCiudad(rs.getString("ciudad"));
                    hotel.setTelefono(rs.getInt("telefono"));
                    return hotel;
                }
            }
        }
    return null;
    }

    @Override
    public List<Hotel> obtenerTodosHoteles() throws Exception {
        List<Hotel> lista = new ArrayList<>();
        String sql = "SELECT id_hotel, nombre, direccion, ciudad, telefono FROM hoteles";

        try (Connection conn = ConexionBD.obtenerConexion();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                Hotel hotel = new Hotel();
                hotel.setIdHotel(rs.getInt("id_hotel"));
                hotel.setNombre(rs.getString("nombre"));
                hotel.setDireccion(rs.getString("direccion"));
                hotel.setCiudad(rs.getString("ciudad"));
                hotel.setTelefono(rs.getInt("telefono"));

                lista.add(hotel);
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

            // Si la entidad Hotel no tiene getId(), puedes buscar el id por su nombre o añadir el getter correspondiente
            // ps.setInt(5, hotel.getId());

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

    // Mapeo de ResultSet a la entidad Hotel del modelo
    private Hotel mapearHotel(ResultSet rs) throws SQLException {
        Hotel h = new Hotel();
        // Si tu modelo Hotel dispone de setId(int):
        // h.setId(rs.getInt("id_hotel"));
        h.setNombre(rs.getString("nombre"));
        h.setDireccion(rs.getString("direccion"));
        h.setCiudad(rs.getString("ciudad"));
        h.setTelefono(rs.getInt("telefono"));
        return h;
    }
}