package RoomServDev.model.dao;

import RoomServDev.model.Reserva;
import java.util.List;

public interface ReservaDAO {
    void insertar(Reserva reserva) throws Exception;
    Reserva obtenerPorId(int idReserva) throws Exception;
    List<Reserva> obtenerPorCliente(String dniCliente) throws Exception;
    List<Reserva> obtenerTodas() throws Exception;
    void actualizarEstado(int idReserva, boolean activa)  throws Exception;
    void eliminar(int idReserva) throws Exception;
}
