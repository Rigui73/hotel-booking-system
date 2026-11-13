package RoomServDev.persistence.dao.mysql;

import RoomServDev.model.Reserva;
import RoomServDev.model.ReservaSolapadaException;
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
            "SELECT hab.id_habitacion, hab.disponible " +
                    "FROM habitaciones hab " +
                    "JOIN hoteles hot ON hab.id_hotel = hot.id_hotel " +
                    "WHERE LOWER(hot.nombre) = LOWER(?) AND hab.num_habitacion = ? FOR UPDATE";

    private static final String SQL_SOLAPAMIENTO =
            "SELECT id_reserva, fecha_entrada, fecha_salida FROM reservas " +
                    "WHERE estado = 1 AND id_habitacion = ? AND fecha_entrada < ? AND fecha_salida > ?";

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

    private static final String SQL_UPDATE_RESERVA =
            "UPDATE reservas SET fecha_entrada = ?, fecha_salida = ?, num_huespedes = ?, importe_total = ? " +
                    "WHERE id_reserva = ?";

    private static final String SQL_UPDATE_ESTADO =
            "UPDATE reservas SET estado = ? WHERE id_reserva = ?";

    private static final String SQL_ID_HABITACION_RESERVA =
            "SELECT id_reserva, estado, id_habitacion FROM reservas WHERE id_reserva = ? FOR UPDATE";

    private static final String SQL_LIBERAR_HABITACION =
            "UPDATE habitaciones SET disponible = 1 WHERE id_habitacion = " +
                    "(SELECT id_habitacion FROM reservas WHERE id_reserva = ?)";

    private static final String SQL_DELETE =
            "DELETE FROM reservas WHERE id_reserva = ?";

    // =========================================================================
    // OPERACIÓN TRANSACCIONAL ATÓMICA (ACID)
    // =========================================================================
    @Override
    public void insertar(Reserva reserva) throws SQLException, ReservaSolapadaException {
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

            int idHabitacionEncontrada;
            try (ResultSet rs = psBuscar.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("No se encontró la habitación " + reserva.getNumHabitacion() +
                            " en el hotel " + reserva.getNombreHotel());
                }
                idHabitacionEncontrada = rs.getInt("id_habitacion");
               //if (!rs.getBoolean("disponible")) {
               //     throw new SQLException("La habitación " + reserva.getNumHabitacion() + " no esta disponible.");
               // }
            }

            // 3. Insertamos el registro de la reserva
            java.sql.Date entrada = aSqlDate(reserva.getFechaEntrada());
            java.sql.Date salida = aSqlDate(reserva.getFechaSalida());
            comprobarSolapamiento(conn, idHabitacionEncontrada, entrada, salida, null);

            psInsert = conn.prepareStatement(SQL_INSERT_RESERVA, Statement.RETURN_GENERATED_KEYS);
            psInsert.setDate(1, entrada);
            psInsert.setDate(2, salida);
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
            //psBloquear = conn.prepareStatement(SQL_BLOQUEAR_HABITACION);
            //psBloquear.setInt(1, idHabitacionEncontrada);
            //psBloquear.executeUpdate();

            // 5. Si todo ha salido bien, confirmamos la transacción
            conn.commit();

        } catch (SQLException | ReservaSolapadaException e) {
            revertir(conn);
            throw e;
        } finally {
            cerrar(psBuscar);
            cerrar(psInsert);
            cerrar(psBloquear);
            cerrar(conn);
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
    public void actualizar(Reserva reserva) throws SQLException, ReservaSolapadaException {
        Connection conn = null;
        PreparedStatement psBuscar = null;
        PreparedStatement psUpdate = null;

        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            psBuscar = conn.prepareStatement(SQL_ID_HABITACION_RESERVA);
            psBuscar.setInt(1, reserva.getIdReserva());

            int idHabitacion;
            try (ResultSet rs = psBuscar.executeQuery()){
                if (!rs.next()){
                    throw new SQLException("No existe la reserva " + reserva.getIdReserva());
                }
                if (!rs.getBoolean("estado")){
                    throw new SQLException("La reserva " + reserva.getIdReserva() + " esta cancelada.");
                }
                idHabitacion = rs.getInt("id_habitacion");
            }

            java.sql.Date entrada = aSqlDate(reserva.getFechaEntrada());
            java.sql.Date salida = aSqlDate(reserva.getFechaSalida());
            comprobarSolapamiento(conn, idHabitacion, entrada, salida, reserva.getIdReserva());

            psUpdate = conn.prepareStatement(SQL_UPDATE_RESERVA);
            psUpdate.setDate(1, entrada);
            psUpdate.setDate(2, salida);
            psUpdate.setInt(3, reserva.getNumHuespedes());
            psUpdate.setDouble(4, reserva.getImporteTotal());
            psUpdate.setInt(5, reserva.getIdReserva());

            if (psUpdate.executeUpdate() == 0){
                throw new SQLException("No se ha podido actualizar la reserva " + reserva.getIdReserva());
            }

            conn.commit();
        } catch (SQLException | ReservaSolapadaException e){
            revertir(conn);
            throw e;
        } finally {
            cerrar(psBuscar);
            cerrar(psUpdate);
            cerrar(conn);
        }
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
        Connection conn = null;
        PreparedStatement psEstado = null;
        PreparedStatement psLiberar = null;
        PreparedStatement psDelete = null;

        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            boolean activa = false;
            psEstado = conn.prepareStatement(SQL_ID_HABITACION_RESERVA);
            psEstado.setInt(1, idReserva);
            try (ResultSet rs = psEstado.executeQuery()){
                if (!rs.next()){
                    throw new SQLException("No existe la reserva " + idReserva);
                }
                activa = rs.getBoolean("estado");
            }

            if (activa) {
                psLiberar = conn.prepareStatement(SQL_LIBERAR_HABITACION);
                psLiberar.setInt(1, idReserva);
                psLiberar.executeUpdate();
            }

            psDelete = conn.prepareStatement(SQL_DELETE);
            psDelete.setInt(1, idReserva);
            psDelete.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            revertir(conn);
            throw e;
        } finally {
            cerrar(psEstado);
            cerrar(psLiberar);
            cerrar(psDelete);
            cerrar(conn);
        }
    }

    private void comprobarSolapamiento(Connection conn, int idHabitacion, java.sql.Date entrada, java.sql.Date salida, Integer idIgnorar) throws SQLException, ReservaSolapadaException{
        String sql = SQL_SOLAPAMIENTO;
        if (idIgnorar != null){
            sql += " AND id_reserva <> ?";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, idHabitacion);
            ps.setDate(2, salida);
            ps.setDate(3, entrada);
            if (idIgnorar != null){
                ps.setInt(4, idIgnorar);
            }
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    throw new ReservaSolapadaException("La habitación ya esta reservada del " + rs.getDate("fecha_entrada") + " al " + rs.getDate("fecha_salida"));
                }
            }
        }
    }

    private java.sql.Date aSqlDate(java.util.Date fecha){
        if (fecha == null){
            throw new IllegalArgumentException("La fecha de la reserva es obligatoria.");
        }
        if (fecha instanceof java.sql.Date sqlDate){
            return sqlDate;
        }
        return new java.sql.Date(fecha.getTime());
    }

    private void revertir(Connection conn){
        if (conn != null){
            try {
                conn.rollback();
            } catch (SQLException exRollback){
                System.err.println("[ERROR] Error al revertir la transacción: " + exRollback.getMessage());
            }
        }
    }

    private void cerrar(AutoCloseable recurso){
        if (recurso == null){
            return;
        }

        try {
            if (recurso instanceof Connection conn){
                conn.setAutoCommit(true);
            }
            recurso.close();
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cerrar el recurso JDBC: " + e.getMessage());
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
