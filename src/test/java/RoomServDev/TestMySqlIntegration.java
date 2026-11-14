package RoomServDev;

import RoomServDev.controller.ControladorClientes;
import RoomServDev.controller.ControladorHabitaciones;
import RoomServDev.controller.ControladorHoteles;
import RoomServDev.controller.ControladorReservas;
import RoomServDev.model.Cliente;
import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.model.Reserva;
import RoomServDev.model.ReservaSolapadaException;
import RoomServDev.model.Simple;
import RoomServDev.model.Suite;
import RoomServDev.persistence.connection.DatabaseConnection;
import RoomServDev.persistence.dao.ClienteDAO;
import RoomServDev.persistence.dao.HabitacionDAO;
import RoomServDev.persistence.dao.HotelDAO;
import RoomServDev.persistence.dao.ReservaDAO;
import RoomServDev.persistence.factory.DAOFactory;
import RoomServDev.view.VistaClientes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

/** Integration checks for an explicitly selected local MySQL database. */
class TestMySqlIntegration {

    private final DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
    private final ClienteDAO clienteDAO = factory.getClienteDAO();
    private final HotelDAO hotelDAO = factory.getHotelDAO();
    private final HabitacionDAO habitacionDAO = factory.getHabitacionDAO();
    private final ReservaDAO reservaDAO = factory.getReservaDAO();
    private final Set<Integer> reservationIds = new HashSet<>();
    private final Set<Integer> roomIds = new HashSet<>();
    private String dniCliente;
    private int idHotel;
    private String nombreHotel;

    @Test
    @EnabledIfSystemProperty(named = "roomservdev.integration", matches = "true")
    void validaCrudTransaccionesYSolapamientosEnMySql() throws Exception {
        verificarDestinoSeguro();

        String runId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String marca = "IT-ROOMSERVDEV-" + runId;
        nombreHotel = marca;
        dniCliente = dniTemporalUnico();

        VistaClientes vistaCliente = new VistaClientes(new Scanner(new StringReader("")));
        ControladorClientes clientes = new ControladorClientes(vistaCliente, clienteDAO);
        ControladorHoteles hoteles = new ControladorHoteles(hotelDAO, habitacionDAO);
        ControladorHabitaciones habitaciones = new ControladorHabitaciones(habitacionDAO, reservaDAO);
        ControladorReservas reservas = new ControladorReservas(reservaDAO, clientes, hoteles, habitaciones);

        clientes.crearCliente(marca, "Prueba", "Integración", dniCliente, 900000001,
                runId.toLowerCase() + "@example.invalid");
        Cliente cliente = clienteDAO.obtenerPorDni(dniCliente);
        assertNotNull(cliente, "El cliente creado debe recuperarse por su DNI.");
        clientes.editarCliente(dniCliente, dniCliente, marca + " Editado", "Prueba", "Integración",
                900000002, runId.toLowerCase() + "-edit@example.invalid");
        assertEquals(marca + " Editado", clienteDAO.obtenerPorDni(dniCliente).getNombre());

        assertNull(hotelDAO.obtenerPorNombres(nombreHotel), "El nombre temporal debe estar libre antes de insertarlo.");
        hoteles.crearHotel(nombreHotel, "Calle de integración 1", "Barcelona", 900000003);
        Hotel hotel = hoteles.buscarHotel(nombreHotel);
        assertNotNull(hotel);
        idHotel = hotel.getIdHotel();
        assertTrue(idHotel > 0, "El ID AUTO_INCREMENT del hotel debe asignarse.");
        hoteles.editarHotel(nombreHotel, "Calle actualizada 2", "Girona", 900000004);
        hotel = hotelDAO.obtenerPorId(idHotel);
        assertEquals("Girona", hotel.getCiudad());

        int numeroHabitacion = 800 + ThreadLocalRandom.current().nextInt(1_000);
        habitaciones.crearHabitacionSimple(hotel, numeroHabitacion, 2, 100.00, true, "90x200", true);
        Habitacion habitacion = habitaciones.buscarHabitacion(hotel, numeroHabitacion);
        assertInstanceOf(Simple.class, habitacion);
        assertTrue(habitacion.getIdHabitacion() > 0, "El ID AUTO_INCREMENT de la habitación debe asignarse.");
        roomIds.add(habitacion.getIdHabitacion());

        habitaciones.crearHabitacionDoble(hotel, numeroHabitacion + 1, 3, 125.00, true,
                "Matrimonio", true, 20.00, true);
        Habitacion doble = habitaciones.buscarHabitacion(hotel, numeroHabitacion + 1);
        assertInstanceOf(Doble.class, doble);
        roomIds.add(doble.getIdHabitacion());

        habitaciones.crearHabitacionSuite(hotel, numeroHabitacion + 2, 4, 200.00, true,
                2, true, true, true, "IT-ROOMSERVDEV");
        Habitacion suite = habitaciones.buscarHabitacion(hotel, numeroHabitacion + 2);
        assertInstanceOf(Suite.class, suite);
        roomIds.add(suite.getIdHabitacion());

        Habitacion simple = habitaciones.buscarHabitacion(hotel, numeroHabitacion);
        simple.setCapacidad(3);
        simple.setPrecioNoche(110.00);
        habitaciones.editarHabitacion(hotel, numeroHabitacion, 3, 110.00, true);
        assertEquals(110.00, habitaciones.buscarHabitacion(hotel, numeroHabitacion).getPrecioNoche(), 0.001);

        // Force failure after the parent room INSERT, at the required subtype row.
        int roomsBeforeFailure = habitacionDAO.obtenerPorHotel(idHotel).size();
        Simple invalidSubtype = new Simple(numeroHabitacion + 3, 1, 50.00, "Simple", null, true);
        assertThrows(SQLException.class, () -> habitacionDAO.insertar(invalidSubtype, idHotel));
        assertEquals(0, invalidSubtype.getIdHabitacion(), "Failed insertion must not leave a phantom generated ID.");
        assertEquals(roomsBeforeFailure, habitacionDAO.obtenerPorHotel(idHotel).size(),
                "Rollback must remove the parent room inserted before the subtype failure.");

        LocalDate entrada = LocalDate.now().plusDays(10);
        LocalDate salida = entrada.plusDays(3);
        Reserva primera = reservas.crearReserva(dniCliente, nombreHotel, numeroHabitacion, entrada, salida, 2);
        reservationIds.add(primera.getIdReserva());
        assertTrue(primera.getIdReserva() > 0, "El ID AUTO_INCREMENT de la reserva debe asignarse.");
        assertNotNull(reservaDAO.obtenerPorId(primera.getIdReserva()));
        assertEquals(1, reservaDAO.obtenerPorCliente(dniCliente).size());
        assertTrue(procedimientoEncuentraSolapamiento(primera.getIdReserva(), entrada, salida));
        assertFalse(procedimientoEncuentraSolapamiento(primera.getIdReserva(),
                entrada.plusDays(30), entrada.plusDays(32)));

        LocalDate finConsecutiva = salida.plusDays(2);
        Reserva consecutiva = reservas.crearReserva(dniCliente, nombreHotel, numeroHabitacion,
                salida, finConsecutiva, 2);
        reservationIds.add(consecutiva.getIdReserva());
        assertTrue(consecutiva.getIdReserva() > 0, "Una estancia consecutiva debe aceptarse.");

        assertThrows(ReservaSolapadaException.class, () -> reservas.crearReserva(dniCliente, nombreHotel,
                numeroHabitacion, entrada.plusDays(1), salida.plusDays(1), 2));
        assertEquals(2, reservaDAO.obtenerPorCliente(dniCliente).size(),
                "Una reserva solapada rechazada no debe dejar una fila insertada.");

        LocalDate nuevaEntrada = salida.plusDays(30);
        LocalDate nuevaSalida = nuevaEntrada.plusDays(2);
        reservas.editarReserva(consecutiva.getIdReserva(), nuevaEntrada, nuevaSalida, 1);
        assertEquals(Date.valueOf(nuevaEntrada), reservaDAO.obtenerPorId(consecutiva.getIdReserva()).getFechaEntrada());

        // Inactive reservations do not block dates; activating one must recheck under lock.
        LocalDate conflictoEntrada = salida.plusDays(60);
        LocalDate conflictoSalida = conflictoEntrada.plusDays(3);
        Reserva inactiva = new Reserva(0, Date.valueOf(LocalDate.now()), Date.valueOf(conflictoEntrada),
                Date.valueOf(conflictoSalida), 1, 300.00, false, nombreHotel, numeroHabitacion, dniCliente);
        reservaDAO.insertar(inactiva);
        reservationIds.add(inactiva.getIdReserva());
        Reserva ocupante = reservas.crearReserva(dniCliente, nombreHotel, numeroHabitacion,
                conflictoEntrada, conflictoSalida, 1);
        reservationIds.add(ocupante.getIdReserva());
        assertTrue(procedimientoEncuentraSolapamiento(ocupante.getIdReserva(), conflictoEntrada, conflictoSalida));
        assertThrows(ReservaSolapadaException.class,
                () -> reservaDAO.actualizarEstado(inactiva.getIdReserva(), true));
        assertFalse(reservaDAO.obtenerPorId(inactiva.getIdReserva()).isEstado(),
                "La activación rechazada debe conservar el estado inactivo.");

        reservas.cancelarReserva(primera.getIdReserva());
        assertFalse(reservaDAO.obtenerPorId(primera.getIdReserva()).isEstado());
        assertTrue(habitacionDAO.obtenerPorId(habitacion.getIdHabitacion()).isDisponible(),
                "Cancelar una reserva no debe cambiar el estado operativo de la habitación.");
        reservas.eliminarReserva(consecutiva.getIdReserva());
        reservationIds.remove(consecutiva.getIdReserva());
        reservaDAO.eliminar(inactiva.getIdReserva());
        reservationIds.remove(inactiva.getIdReserva());
        reservaDAO.eliminar(ocupante.getIdReserva());
        reservationIds.remove(ocupante.getIdReserva());
        reservas.eliminarReserva(primera.getIdReserva());
        reservationIds.remove(primera.getIdReserva());

        for (int roomId : Set.copyOf(roomIds)) {
            habitacionDAO.eliminar(roomId);
            roomIds.remove(roomId);
        }
        hoteles.eliminarHotel(nombreHotel);
        idHotel = 0;
        clientes.eliminarCliente(dniCliente);
        dniCliente = null;
    }

    @AfterEach
    void limpiarSoloRegistrosDeEstaPrueba() throws Exception {
        if (dniCliente != null) {
            for (Reserva reserva : reservaDAO.obtenerPorCliente(dniCliente)) {
                reservaDAO.eliminar(reserva.getIdReserva());
            }
        }
        for (int id : reservationIds) {
            if (reservaDAO.obtenerPorId(id) != null) reservaDAO.eliminar(id);
        }
        Hotel testHotel = nombreHotel == null ? null : hotelDAO.obtenerPorNombres(nombreHotel);
        if (testHotel != null) {
            for (Habitacion habitacion : habitacionDAO.obtenerPorHotel(testHotel.getIdHotel())) {
                habitacionDAO.eliminar(habitacion.getIdHabitacion());
            }
            hotelDAO.eliminar(testHotel.getIdHotel());
        } else {
            for (int id : roomIds) {
                if (habitacionDAO.obtenerPorId(id) != null) habitacionDAO.eliminar(id);
            }
            if (idHotel > 0 && hotelDAO.obtenerPorId(idHotel) != null) hotelDAO.eliminar(idHotel);
        }
        if (dniCliente != null && clienteDAO.obtenerPorDni(dniCliente) != null) clienteDAO.eliminar(dniCliente);
    }

    private void verificarDestinoSeguro() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             var statement = conn.createStatement();
             ResultSet rs = statement.executeQuery("SELECT DATABASE(), @@hostname, @@port")) {
            assertTrue(rs.next());
            assertEquals("roomservdev_db", rs.getString(1));
            assertEquals("ADRIA-PC", rs.getString(2));
            assertEquals(3306, rs.getInt(3));
        }
    }

    private boolean procedimientoEncuentraSolapamiento(int idReserva, LocalDate entrada, LocalDate salida)
            throws SQLException {
        Reserva reserva = reservaDAO.obtenerPorId(idReserva);
        assertNotNull(reserva);
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_comprobar_solapamiento_reserva(?, ?, ?, ?)}")) {
            cs.setInt(1, reservaHabitacionId(reserva));
            cs.setDate(2, Date.valueOf(entrada));
            cs.setDate(3, Date.valueOf(salida));
            cs.setNull(4, java.sql.Types.INTEGER);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next();
            }
        }
    }

    private int reservaHabitacionId(Reserva reserva) throws SQLException {
        for (Habitacion habitacion : habitacionDAO.obtenerPorHotel(idHotel)) {
            if (habitacion.getNumHabitacion() == reserva.getNumHabitacion()) return habitacion.getIdHabitacion();
        }
        throw new SQLException("No se encuentra la habitación temporal de la reserva.");
    }

    private String dniTemporalUnico() throws SQLException {
        while (true) {
            int numero = ThreadLocalRandom.current().nextInt(10_000_000, 100_000_000);
            String dni = numero + "Z";
            if (clienteDAO.obtenerPorDni(dni) == null) return dni;
        }
    }
}
