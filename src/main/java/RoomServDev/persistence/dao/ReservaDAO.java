package RoomServDev.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import RoomServDev.model.Reserva;
import RoomServDev.model.ReservaSolapadaException;
import RoomServDev.model.ReservaSolapadaException;

public interface ReservaDAO {

    void insertar(Reserva reserva) throws SQLException, RoomServDev.model.ReservaSolapadaException;

    Reserva obtenerPorId(int idReserva) throws SQLException;

    List<Reserva> obtenerPorCliente(String dniCliente) throws SQLException;

    List<Reserva> obtenerTodas() throws SQLException;

    void actualizar(Reserva reserva) throws SQLException, RoomServDev.model.ReservaSolapadaException;

    void actualizarEstado(int idReserva, boolean activa) throws SQLException;

    void eliminar(int idReserva) throws SQLException;
}