
package RoomServDev.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import RoomServDev.model.Habitacion;

public interface HabitacionDAO {

    void insertar(Habitacion habitacion, int idHotel) throws SQLException;

    Habitacion obtenerPorId(int idHabitacion) throws SQLException;

    List<Habitacion> obtenerPorHotel(int idHotel) throws SQLException;

    void actualizar(Habitacion habitacion) throws SQLException;

    void eliminar(int idHabitacion) throws SQLException;
}
