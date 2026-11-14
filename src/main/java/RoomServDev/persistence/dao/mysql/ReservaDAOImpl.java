package RoomServDev.persistence.dao.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import RoomServDev.model.Reserva;
import RoomServDev.model.ReservaSolapadaException;
import RoomServDev.persistence.connection.DatabaseConnection;
import RoomServDev.persistence.dao.ReservaDAO;

public class ReservaDAOImpl implements ReservaDAO {

    private static final String SQL_BUSCAR_HABITACION = "SELECT hab.id_habitacion, hab.disponible " +
            "FROM habitaciones hab JOIN hoteles hot ON hab.id_hotel = hot.id_hotel " +
            "WHERE LOWER(hot.nombre) = LOWER(?) AND hab.num_habitacion = ? FOR UPDATE";
    private static final String SQL_HABITACION_POR_ID = "SELECT id_habitacion, disponible FROM habitaciones WHERE id_habitacion = ? FOR UPDATE";
    private static final String SQL_RESERVA_POR_ID = "SELECT id_reserva, estado, id_habitacion, fecha_entrada, fecha_salida "
            +
            "FROM reservas WHERE id_reserva = ? FOR UPDATE";
    private static final String SQL_RESERVA_HABITACION_BLOQUEADA = "SELECT id_habitacion FROM reservas WHERE id_reserva = ? FOR UPDATE";
    private static final String SQL_INSERT = "INSERT INTO reservas (fecha_entrada, fecha_salida, num_huespedes, importe_total, estado, dni_cliente, id_habitacion) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BASE = "SELECT r.id_reserva, r.fecha_entrada, r.fecha_salida, r.num_huespedes, r.importe_total, "
            +
            "r.estado, r.dni_cliente, h.nombre AS nombre_hotel, hab.num_habitacion " +
            "FROM reservas r JOIN habitaciones hab ON r.id_habitacion = hab.id_habitacion " +
            "JOIN hoteles h ON hab.id_hotel = h.id_hotel ";
    private static final String SQL_SELECT_BY_ID = SQL_SELECT_BASE + "WHERE r.id_reserva = ?";
    private static final String SQL_SELECT_BY_CLIENTE = SQL_SELECT_BASE +
            "WHERE r.dni_cliente = ? ORDER BY r.fecha_entrada DESC";
    private static final String SQL_SELECT_ALL = SQL_SELECT_BASE + "ORDER BY r.fecha_entrada DESC";
    private static final String SQL_UPDATE = "UPDATE reservas SET fecha_entrada = ?, fecha_salida = ?, num_huespedes = ?, importe_total = ? "
            +
            "WHERE id_reserva = ? AND estado = 1";
    private static final String SQL_UPDATE_ESTADO = "UPDATE reservas SET estado = ? WHERE id_reserva = ? AND estado <> ?";
    private static final String SQL_DELETE = "DELETE FROM reservas WHERE id_reserva = ?";
    private static final String CALL_COMPROBAR_SOLAPAMIENTO = "{CALL sp_comprobar_solapamiento_reserva(?, ?, ?, ?)}";

    @Override
    public void insertar(Reserva reserva) throws SQLException, ReservaSolapadaException {
        validarReserva(reserva);
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idGenerado;
                int idHabitacion;
                try (PreparedStatement ps = conn.prepareStatement(SQL_BUSCAR_HABITACION)) {
                    ps.setString(1, reserva.getNombreHotel().trim());
                    ps.setInt(2, reserva.getNumHabitacion());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("No se encontró la habitación " + reserva.getNumHabitacion()
                                    + " en el hotel " + reserva.getNombreHotel());
                        }
                        idHabitacion = rs.getInt("id_habitacion");
                        comprobarHabitacionOperativa(rs.getBoolean("disponible"), reserva.getNumHabitacion());
                    }
                }

                Date entrada = aSqlDate(reserva.getFechaEntrada());
                Date salida = aSqlDate(reserva.getFechaSalida());
                comprobarSolapamiento(conn, idHabitacion, entrada, salida, null);

                try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, entrada);
                    ps.setDate(2, salida);
                    ps.setInt(3, reserva.getNumHuespedes());
                    ps.setDouble(4, reserva.getImporteTotal());
                    ps.setBoolean(5, reserva.isEstado());
                    ps.setString(6, reserva.getDniCliente().trim().toUpperCase());
                    ps.setInt(7, idHabitacion);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se pudo insertar la reserva.");
                    }
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next())
                            throw new SQLException("MySQL no devolvió el ID de la reserva.");
                        idGenerado = keys.getInt(1);
                    }
                }
                conn.commit();
                reserva.setIdReserva(idGenerado);
            } catch (SQLException | ReservaSolapadaException | RuntimeException e) {
                rollback(conn, e);
                throw e;
            }
        }
    }

    @Override
    public Reserva obtenerPorId(int idReserva) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearReserva(rs) : null;
            }
        }
    }

    @Override
    public List<Reserva> obtenerPorCliente(String dniCliente) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_CLIENTE)) {
            ps.setString(1, dniCliente.trim().toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    lista.add(mapearReserva(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Reserva> obtenerTodas() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(mapearReserva(rs));
        }
        return lista;
    }

    @Override
    public void actualizar(Reserva reserva) throws SQLException, ReservaSolapadaException {
        validarReserva(reserva);
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idHabitacion = obtenerIdHabitacionReserva(conn, reserva.getIdReserva());
                comprobarHabitacionBloqueada(conn, idHabitacion, reserva.getNumHabitacion());
                comprobarReservaActiva(conn, reserva.getIdReserva(), idHabitacion);
                comprobarSolapamiento(conn, idHabitacion, aSqlDate(reserva.getFechaEntrada()),
                        aSqlDate(reserva.getFechaSalida()), reserva.getIdReserva());

                try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
                    ps.setDate(1, aSqlDate(reserva.getFechaEntrada()));
                    ps.setDate(2, aSqlDate(reserva.getFechaSalida()));
                    ps.setInt(3, reserva.getNumHuespedes());
                    ps.setDouble(4, reserva.getImporteTotal());
                    ps.setInt(5, reserva.getIdReserva());
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("La reserva no existe, está cancelada o no se pudo modificar.");
                    }
                }
                conn.commit();
            } catch (SQLException | ReservaSolapadaException | RuntimeException e) {
                rollback(conn, e);
                throw e;
            }
        }
    }

    @Override
    public void actualizarEstado(int idReserva, boolean activa) throws SQLException, ReservaSolapadaException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idHabitacion = obtenerIdHabitacionReserva(conn, idReserva);
                if (activa) {
                    comprobarHabitacionBloqueada(conn, idHabitacion, idReserva);
                }
                ReservaBloqueada estadoActual = obtenerReservaBloqueada(conn, idReserva, idHabitacion);
                if (estadoActual.activa == activa) {
                    throw new SQLException("La reserva ya se encuentra en ese estado.");
                }
                if (activa) {
                    comprobarSolapamiento(conn, idHabitacion, estadoActual.entrada, estadoActual.salida, idReserva);
                }
                try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_ESTADO)) {
                    ps.setBoolean(1, activa);
                    ps.setInt(2, idReserva);
                    ps.setBoolean(3, activa);
                    if (ps.executeUpdate() != 1)
                        throw new SQLException("No se pudo cambiar el estado de la reserva.");
                }
                conn.commit();
            } catch (SQLException | ReservaSolapadaException | RuntimeException e) {
                rollback(conn, e);
                throw e;
            }
        }
    }

    @Override
    public void eliminar(int idReserva) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idHabitacion = obtenerIdHabitacionReserva(conn, idReserva);
                obtenerReservaBloqueada(conn, idReserva, idHabitacion);
                try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
                    ps.setInt(1, idReserva);
                    if (ps.executeUpdate() != 1)
                        throw new SQLException("No se pudo eliminar la reserva.");
                }
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                rollback(conn, e);
                throw e;
            }
        }
    }

    private void comprobarSolapamiento(Connection conn, int idHabitacion, Date entrada, Date salida,
            Integer idIgnorar) throws SQLException, ReservaSolapadaException {
        try (CallableStatement cs = conn.prepareCall(CALL_COMPROBAR_SOLAPAMIENTO)) {
            cs.setInt(1, idHabitacion);
            cs.setDate(2, entrada);
            cs.setDate(3, salida);
            if (idIgnorar == null)
                cs.setNull(4, java.sql.Types.INTEGER);
            else
                cs.setInt(4, idIgnorar);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    throw new ReservaSolapadaException("La habitación ya está reservada del "
                            + rs.getDate("fecha_entrada") + " al " + rs.getDate("fecha_salida"));
                }
            }
        }
    }

    private int obtenerIdHabitacionReserva(Connection conn, int idReserva) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_RESERVA_HABITACION_BLOQUEADA)) {
            ps.setInt(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    throw new SQLException("No existe la reserva " + idReserva);
                return rs.getInt("id_habitacion");
            }
        }
    }

    private void comprobarHabitacionBloqueada(Connection conn, int idHabitacion, int referencia) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_HABITACION_POR_ID)) {
            ps.setInt(1, idHabitacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    throw new SQLException("No existe la habitación asociada a la reserva " + referencia);
                comprobarHabitacionOperativa(rs.getBoolean("disponible"), referencia);
            }
        }
    }

    private void comprobarReservaActiva(Connection conn, int idReserva, int idHabitacion) throws SQLException {
        if (!obtenerReservaBloqueada(conn, idReserva, idHabitacion).activa) {
            throw new SQLException("La reserva " + idReserva + " está cancelada.");
        }
    }

    private ReservaBloqueada obtenerReservaBloqueada(Connection conn, int idReserva, int idHabitacion)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_RESERVA_POR_ID)) {
            ps.setInt(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    throw new SQLException("No existe la reserva " + idReserva);
                if (rs.getInt("id_habitacion") != idHabitacion) {
                    throw new SQLException("La habitación asociada a la reserva cambió durante la operación.");
                }
                return new ReservaBloqueada(rs.getBoolean("estado"), rs.getDate("fecha_entrada"),
                        rs.getDate("fecha_salida"));
            }
        }
    }

    private void comprobarHabitacionOperativa(boolean disponible, int referencia) throws SQLException {
        if (!disponible)
            throw new SQLException("La habitación asociada a " + referencia + " está fuera de servicio.");
    }

    private void validarReserva(Reserva reserva) {
        if (reserva == null)
            throw new IllegalArgumentException("La reserva es obligatoria.");
        if (reserva.getFechaEntrada() == null || reserva.getFechaSalida() == null
                || !aSqlDate(reserva.getFechaSalida()).after(aSqlDate(reserva.getFechaEntrada()))) {
            throw new IllegalArgumentException("La salida debe ser posterior a la entrada.");
        }
        if (reserva.getNumHuespedes() <= 0)
            throw new IllegalArgumentException("Debe haber al menos un huésped.");
        if (reserva.getImporteTotal() < 0)
            throw new IllegalArgumentException("El importe no puede ser negativo.");
        if (reserva.getDniCliente() == null || reserva.getDniCliente().isBlank()) {
            throw new IllegalArgumentException("El DNI del cliente es obligatorio.");
        }
        if (reserva.getNombreHotel() == null || reserva.getNombreHotel().isBlank()) {
            throw new IllegalArgumentException("El hotel es obligatorio.");
        }
    }

    private Date aSqlDate(java.util.Date fecha) {
        if (fecha instanceof Date sqlDate)
            return sqlDate;
        return new Date(fecha.getTime());
    }

    private void rollback(Connection conn, Exception original) {
        try {
            conn.rollback();
        } catch (SQLException rollbackError) {
            original.addSuppressed(rollbackError);
        }
    }

    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        Date fechaEntrada = rs.getDate("fecha_entrada");
        Date fechaSalida = rs.getDate("fecha_salida");
        return new Reserva(rs.getInt("id_reserva"), fechaEntrada, fechaEntrada, fechaSalida,
                rs.getInt("num_huespedes"), rs.getDouble("importe_total"), rs.getBoolean("estado"),
                rs.getString("nombre_hotel"), rs.getInt("num_habitacion"), rs.getString("dni_cliente"));
    }

    private static final class ReservaBloqueada {
        private final boolean activa;
        private final Date entrada;
        private final Date salida;

        private ReservaBloqueada(boolean activa, Date entrada, Date salida) {
            this.activa = activa;
            this.entrada = entrada;
            this.salida = salida;
        }
    }
}
