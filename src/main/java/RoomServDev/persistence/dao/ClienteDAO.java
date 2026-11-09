package RoomServDev.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import RoomServDev.model.Cliente;

public interface ClienteDAO {

    void insertar(Cliente cliente) throws SQLException;

    Cliente obtenerPorDni(String dni) throws SQLException;

    List<Cliente> obtenerTodosClientes() throws SQLException;

    void actualizar(String dniAntiguo, Cliente cliente) throws SQLException;

    void eliminar(String dni) throws SQLException;
}
