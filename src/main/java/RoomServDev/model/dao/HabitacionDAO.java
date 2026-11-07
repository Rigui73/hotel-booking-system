package RoomServDev.model.dao;

import RoomServDev.model.Habitacion;
import java.util.List;


public interface HabitacionDAO {
    void insertar(Habitacion habitacion, int idHotel) throws Exception;
    Habitacion obtenerPorId(int idHabitacion) throws Exception;
    List<Habitacion> obtenerPorHotel(int idHotel) throws Exception;
    void actualizar(Habitacion habitacion) throws Exception;
    void eliminar(int idHabitacion) throws Exception;
}
