
package RoomServDev.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import RoomServDev.model.Hotel;

public interface HotelDAO {

    void insertar(Hotel hotel) throws SQLException;

    Hotel obtenerPorId(int idHotel) throws SQLException;

    Hotel obtenerPorNombres(String nombre) throws SQLException;

    List<Hotel> obtenerTodosHoteles() throws SQLException;

    void actualizar(Hotel hotel) throws SQLException;

    void eliminar(int idHotel) throws SQLException;
}
