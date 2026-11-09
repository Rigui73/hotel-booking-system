package RoomServDev.persistence.dao.mysql;

import RoomServDev.model.Reserva;
import RoomServDev.persistence.dao.ConexionBD;
import RoomServDev.persistence.dao.ReservaDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAOImpl implements ReservaDAO {

    // Consultas SQL
    private static final String SQL_BUSCAR_ID_HABITACION =
            "SELECT hab.id_habitacion " +
                    "FROM habitaciones hab " +
                    "JOIN hoteles hot ON hab.id_hotel = hot.id_hotel " +
                    "WHERE LOWER(hot.nombre) = LOWER(?) AND hab.num_habitacion = ?";

    private static final String SQL_INSERT_RESERVA =
            "INSERT INTO reservas (fecha_entrada, fecha_salida, num_huespedes, importe_total, estado, dni_cliente, id_habitacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_BLOQUEAR_HABITACION =
            "UPDATE habitaciones SET disponible = 0 WHERE id_habitacion = ?";

    private static final String SQL_SELECT_BASE =
            "SELECT r.id_reserva, r.fecha_entrada, r.fecha_salida, r.num_huespedes, r.importe_total, " +
                    "       r.estado, r.dni_cliente, h.nombre AS nombre_hotel, hab.num_habitacion " +
                    "FROM reservas r " +
                    "JOIN habitaciones hab ON r.id_habitacion = hab.id_habitacion " +
                    "JOIN hoteles h ON hab.id_hotel = h.id_hotel ";

    private static final String SQL_SELECT_BY_ID =
            SQL_SELECT_BASE + "WHERE r.id_reserva = ?";

    private static final String SQL_SELECT_BY_CLIENTE =
            SQL_SELECT_BASE + "WHERE r.dni_cliente = ? ORDER BY r.fecha_entrada DESC";

    private static final String SQL_SELECT_ALL =
            SQL_SELECT_BASE + "ORDER BY r.fecha_entrada DESC";

    private static final String SQL_UPDATE_ESTADO =
            "UPDATE reservas SET estado = ? WHERE id_reserva = ?";

    private static final String SQL_LIBERAR_HABITACION =
            "UPDATE habitaciones SET disponible = 1 WHERE id_habitacion = " +
                    "(SELECT id_habitacion FROM reservas WHERE id_reserva = ?)";

    private static final String SQL_DELETE =
            "DELETE FROM reservas WHERE id_reserva = ?";

    // =========================================================================
    // OPERACIÓN TRANSACCIONAL ATÓMICA (ACID)
    // =========================================================================
    @Override
    public void insertar(Reserva reserva) throws SQLException {
        Connection conn = null;
        PreparedStatement psBuscar = null;
        PreparedStatement psInsert = null;
        PreparedStatement psBloquear = null;

        try {
            conn = ConexionBD.obtenerConexion();
            // 1. Iniciamos la transacción desacoplando el autocommit
            conn.setAutoCommit(false);

            // 2. Buscamos el id_habitacion a partir del nombre del hotel y número de habitación
            psBuscar = conn.prepareStatement(SQL_BUSCAR_ID_HABITACION);
            psBuscar.setString(1, reserva.getNombreHotel().trim());
            psBuscar.setInt(2, reserva.getNumHabitacion());

            int idHabitacionEncontrada = -1;
            try (ResultSet rs = psBuscar.executeQuery()) {
                if (rs.next()) {
                    idHabitacionEncontrada = rs.getInt("id_habitacion");
                } else {
                    throw new SQLException("No se encontró la habitación " + reserva.getNumHabitacion() +
                            " en el hotel " + reserva.getNombreHotel());
                }
            }

            // 3. Insertamos el registro de la reserva
            psInsert = conn.prepareStatement(SQL_INSERT_RESERVA, Statement.RETURN_GENERATED_KEYS);
            psInsert.setDate(1, new java.sql.Date(reserva.getFechaEntrada().getTime()));
            psInsert.setDate(2, new java.sql.Date(reserva.getFechaSalida().getTime()));
            psInsert.setInt(3, reserva.getNumHuespedes());
            psInsert.setDouble(4, reserva.getImporteTotal());
            psInsert.setBoolean(5, reserva.isEstado());
            psInsert.setString(6, reserva.getDniCliente().trim().toUpperCase());
            psInsert.setInt(7, idHabitacionEncontrada);

            psInsert.executeUpdate();

            // Obtenemos la clave primaria generada para asignarla al objeto
            try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reserva.setIdReserva(generatedKeys.getInt(1));
                }
            }

            // 4. Bloqueamos la disponibilidad de la habitación reservada
            psBloquear = conn.prepareStatement(SQL_BLOQUEAR_HABITACION);
            psBloquear.setInt(1, idHabitacionEncontrada);
            psBloquear.executeUpdate();

            // 5. Si todo ha salido bien, confirmamos la transacción
            conn.commit();

        } catch (SQLException e) {
            // Revertimos cambios si se produce cualquier fallo
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException exRollback) {
                    System.err.println("[ERROR] Error al revertir transacción: " + exRollback.getMessage());
                }
            }
            throw e;
        } finally {
            if (psBuscar != null) psBuscar.close();
            if (psInsert != null) psInsert.close();
            if (psBloquear != null) psBloquear.close();
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[ERROR] Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Reserva obtenerPorId(int idReserva) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, idReserva);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearReserva(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Reserva> obtenerPorCliente(String dniCliente) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_CLIENTE)) {

            ps.setString(1, dniCliente.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearReserva(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Reserva> obtenerTodas() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearReserva(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizarEstado(int idReserva, boolean activa) throws SQLException {
        Connection conn = null;
        PreparedStatement psEstado = null;
        PreparedStatement psLiberar = null;

        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            psEstado = conn.prepareStatement(SQL_UPDATE_ESTADO);
            psEstado.setBoolean(1, activa);
            psEstado.setInt(2, idReserva);
            psEstado.executeUpdate();

            // Si se da de baja/cancela la reserva, se reactiva la disponibilidad de la habitación
            if (!activa) {
                psLiberar = conn.prepareStatement(SQL_LIBERAR_HABITACION);
                psLiberar.setInt(1, idReserva);
                psLiberar.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (psEstado != null) psEstado.close();
            if (psLiberar != null) psLiberar.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void eliminar(int idReserva) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, idReserva);
            ps.executeUpdate();
        }
    }

    // =========================================================================
    // Mapeo de ResultSet a Reserva
    // =========================================================================
    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        int idReserva = rs.getInt("id_reserva");
        Date fechaEntrada = rs.getDate("fecha_entrada");
        Date fechaSalida = rs.getDate("fecha_salida");
        Date fechaReserva = fechaEntrada; // O la columna correspondiente si existe en tu tabla
        int numHuespedes = rs.getInt("num_huespedes");
        double importeTotal = rs.getDouble("importe_total");
        boolean estado = rs.getBoolean("estado");
        String nombreHotel = rs.getString("nombre_hotel");
        int numHabitacion = rs.getInt("num_habitacion");
        String dniCliente = rs.getString("dni_cliente");

        return new Reserva(idReserva, fechaReserva, fechaEntrada, fechaSalida,
                numHuespedes, importeTotal, estado,
                nombreHotel, numHabitacion, dniCliente);
    }
}
