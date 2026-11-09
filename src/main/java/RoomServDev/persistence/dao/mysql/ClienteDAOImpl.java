package RoomServDev.persistence.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import RoomServDev.model.Cliente;
import RoomServDev.persistence.dao.ClienteDAO;
import RoomServDev.persistence.dao.ConexionBD;

//para evitar ataques SQL Injection
public class ClienteDAOImpl implements ClienteDAO {

    private static final String SQL_INSERT = "INSERT INTO clientes (dni, nombre, apellido1, apellido2, telefono, email) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_DNI = "SELECT dni,nombre,apellido1,apellido2,telefono,email FROM clientes WHERE dni = ?";
    private static final String SQL_SELECT_ALL = "SELECT dni,nombre,apellido1,apellido2,telefono,email FROM clientes ORDER BY apellido1, nombre";
    private static final String SQL_UPDATE = "UPDATE clientes SET dni=? , nombre = ?, apellido1 = ?, apellido2=?, telefono=?, email=? WHERE dni=?";
    private static final String SQL_DELETE = "DELETE FROM clientes WHERE dni=?";

    @Override

    public void insertar(Cliente cliente) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, cliente.getDni().toUpperCase().trim());
            ps.setString(2, cliente.getNombre().trim());
            ps.setString(3, cliente.getApellido1().trim());
            ps.setString(4, cliente.getApellido2() != null ? cliente.getApellido2().trim() : null);
            ps.setInt(5, cliente.getTelefono());
            ps.setString(6, cliente.getEmail());

            ps.executeUpdate();
        }
    }

    @Override

    public Cliente obtenerPorDni(String dni) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_DNI)) {

            ps.setString(1, dni.toUpperCase().trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Cliente> obtenerTodosClientes() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(String dniAntiguo, Cliente cliente) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, cliente.getDni().toUpperCase().trim());
            ps.setString(2, cliente.getNombre().trim());
            ps.setString(3, cliente.getApellido1().trim());
            ps.setString(4, cliente.getApellido2() != null ? cliente.getApellido2().trim() : null);
            ps.setInt(5, cliente.getTelefono());
            ps.setString(6, cliente.getEmail());
            ps.setString(7, dniAntiguo.toUpperCase().trim());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(String dni) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setString(1, dni.toUpperCase().trim());
            ps.executeUpdate();
        }
    }

    // Metodo auxiliar para transformar una fila de ResultSet en una entidad Cliente

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getString("nombre"),
                rs.getString("apellido1"),
                rs.getString("apellido2"),
                rs.getString("dni"),
                rs.getInt("telefono"),
                rs.getString("email"));
    }
}
