package RoomServDev.model.dao;

import RoomServDev.model.Hotel;
import java.util.List;

public interface HotelDAO {
    void insertar(Hotel hotel) throws Exception;
    Hotel obtenerPorId(int idHotel)  throws Exception;
    Hotel obtenerPorNombres(String nombres)  throws Exception;
    List<Hotel> obtenerTodosHoteles() throws Exception;
    void actualizar(Hotel hotel) throws Exception;
    void eliminar(int idHotel) throws Exception;
}
