package RoomServDev.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import RoomServDev.model.Reserva;

public interface ReservaDAO {

    void insertar(Reserva reserva) throws SQLException;

    Reserva obtenerPorId(int idReserva) throws SQLException;

    List<Reserva> obtenerPorCliente(String dniCliente) throws SQLException;

    List<Reserva> obtenerTodas() throws SQLException;

    void actualizarEstado(int idReserva, boolean activa) throws SQLException;

    void eliminar(int idReserva) throws SQLException;
}