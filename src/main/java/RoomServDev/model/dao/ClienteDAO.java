package RoomServDev.model.dao;

import RoomServDev.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    void insertar (Cliente cliente) throws java.sql.SQLException;
    Cliente obtenerPorDni(String dni) throws java.sql.SQLException;
    List<Cliente> obtenerTodosClientes() throws java.sql.SQLException;;
    void actualizar (String dniAntiguo, Cliente cliente) throws java.sql.SQLException;;
    void eliminar (String dni) throws java.sql.SQLException;;
}

