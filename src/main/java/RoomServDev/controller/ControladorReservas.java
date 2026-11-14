package RoomServDev.controller;

import RoomServDev.model.Cliente;
import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.model.Reserva;
import RoomServDev.model.ReservaSolapadaException;
import RoomServDev.persistence.dao.ReservaDAO;
import RoomServDev.view.VistaReservas;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ControladorReservas {

    private final ReservaDAO reservaDAO;
    private final ControladorClientes ctrlClientes;
    private final ControladorHoteles ctrlHoteles;
    private final ControladorHabitaciones ctrlHabitaciones;

    // Necesita los otros controladores para comprobar que cliente, hotel y habitación existen
    public ControladorReservas(ReservaDAO reservaDAO, ControladorClientes ctrlClientes,
                               ControladorHoteles ctrlHoteles, ControladorHabitaciones ctrlHabitaciones) {
        this.reservaDAO = reservaDAO;
        this.ctrlClientes = ctrlClientes;
        this.ctrlHoteles = ctrlHoteles;
        this.ctrlHabitaciones = ctrlHabitaciones;
    }

    // ********* Crear *********

    // El id, la fecha de reserva, el estado y el importe los pone el controlador, no el usuario
    public Reserva crearReserva(String dni, String nombreHotel, int numHabitacion,
                                LocalDate entrada, LocalDate salida, int numHuespedes)
            throws ReservaSolapadaException, SQLException, Exception {

        Cliente cliente = ctrlClientes.buscarCliente(dni);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe ningún cliente con DNI " + dni);
        }

        Hotel hotel = obtenerHotel(nombreHotel);
        Habitacion habitacion = obtenerHabitacion(hotel, numHabitacion);

        if (!habitacion.isDisponible()) {
            throw new IllegalStateException("La habitación " + numHabitacion + " no está disponible.");
        }

        validarFechas(entrada, salida);
        validarHuespedes(habitacion, numHuespedes);

        // Fechas en formato java.sql.Date compatibles con la entidad Reserva
        java.sql.Date fechaHoy = java.sql.Date.valueOf(LocalDate.now());
        java.sql.Date fEntrada = java.sql.Date.valueOf(entrada);
        java.sql.Date fSalida = java.sql.Date.valueOf(salida);

        // Instanciación con los 10 atributos de tu constructor de Reserva
        // (idReserva=0 para que MySQL asigne el AUTO_INCREMENT al persistir)
        Reserva reserva = new Reserva(
                0,
                fechaHoy,
                fEntrada,
                fSalida,
                numHuespedes,
                calcularImporte(habitacion, entrada, salida),
                true,
                hotel.getNombre(),
                numHabitacion,
                cliente.getDni()
        );

        reservaDAO.insertar(reserva);
        return reserva;
    }

    public List<Reserva> listarReservas() throws SQLException {
        return  reservaDAO.obtenerTodas();
    }

    public List<Reserva> listarReservasCliente(String dni) throws SQLException {
        if (dni == null) return new ArrayList<>();
        return reservaDAO.obtenerPorCliente(dni.trim());
    }

    public Reserva buscarReserva(int idReserva) throws SQLException {
        return reservaDAO.obtenerPorId(idReserva);
    }

    // Editar: la reserva
    public void editarReserva(int idReserva, LocalDate nuevaEntrada, LocalDate nuevaSalida, int nuevosHuespedes)
            throws ReservaSolapadaException, SQLException {

        Reserva reserva = obtenerReservaActiva(idReserva);
        Hotel hotel = obtenerHotel(reserva.getNombreHotel());
        Habitacion habitacion = obtenerHabitacion(hotel, reserva.getNumHabitacion());

        validarFechas(nuevaEntrada, nuevaSalida);
        validarHuespedes(habitacion, nuevosHuespedes);

        // El DAO vuelve a validar el solapamiento dentro de su transacción.
        reserva.setFechaEntrada(java.sql.Date.valueOf(nuevaEntrada));
        reserva.setFechaSalida(java.sql.Date.valueOf(nuevaSalida));
        reserva.setNumHuespedes(nuevosHuespedes);
        reserva.setImporteTotal(calcularImporte(habitacion, nuevaEntrada, nuevaSalida));
        reservaDAO.actualizar(reserva);
    }

    // Cancelar: la reserva queda en el historial pero deja de estar activa
    public void cancelarReserva(int idReserva) throws SQLException, ReservaSolapadaException {
        obtenerReservaActiva(idReserva);
        reservaDAO.actualizarEstado(idReserva, false);
    }

    // Eliminar: la reserva desaparece por completo
    public void eliminarReserva(int idReserva) throws SQLException {
        if (buscarReserva(idReserva) == null) {
            throw new IllegalArgumentException("No existe ninguna reserva con id " + idReserva);
        }
        reservaDAO.eliminar(idReserva);
    }

    public void gestionarMenu(VistaReservas vista) {
        boolean volver = false;
        while (!volver) {
            char opcion = vista.mostrarMenuReservas();
            try {
                switch (opcion) {
                    case '1' -> vista.listarReservas(listarReservas());
                    case '2' -> {
                        String dni = vista.pedirDni();
                        String hotel = vista.pedirNombreHotel();
                        int habitacion = vista.pedirNumHabitacion();
                        int huespedes = vista.pedirHuespedes();
                        LocalDate entrada = vista.pedirFechaEntrada();
                        LocalDate salida = vista.pedirFechaSalida();
                        Reserva reserva = crearReserva(dni, hotel, habitacion, entrada, salida, huespedes);
                        vista.mostrarMensaje("Reserva confirmada. Id: " + reserva.getIdReserva()
                                + ". Importe: " + reserva.getImporteTotal() + "€");
                    }
                    case '3' -> {
                        int id = vista.pedirIdReserva();
                        LocalDate entrada = vista.pedirNuevaFechaEntrada();
                        LocalDate salida = vista.pedirNuevaFechaSalida();
                        int huespedes = vista.pedirNuevosHuespedes();
                        editarReserva(id, entrada, salida, huespedes);
                        vista.mostrarMensaje("Reserva modificada correctamente.");
                    }
                    case '4' -> {
                        int id = vista.pedirIdReserva();
                        String modo = vista.pedirModoCancelacion();
                        if (modo.equalsIgnoreCase("E")) {
                            eliminarReserva(id);
                            vista.mostrarMensaje("Reserva eliminada; las fechas vuelven a estar disponibles.");
                        } else if (modo.equalsIgnoreCase("C")) {
                            cancelarReserva(id);
                            vista.mostrarMensaje("Reserva cancelada; las fechas vuelven a estar disponibles.");
                        } else {
                            vista.mostrarError("Indica C para cancelar o E para eliminar.");
                        }
                    }
                    case '0' -> volver = true;
                    default -> vista.mostrarError("Opción no válida.");
                }
            } catch (Exception e) {
                vista.mostrarError(e.getMessage());
            }
        }
    }

    // ********* Auxiliares (privados) *********

    private double calcularImporte(Habitacion habitacion, LocalDate entrada, LocalDate salida) {
        long noches = ChronoUnit.DAYS.between(entrada, salida);
        double precioNoche = habitacion.getPrecioNoche();
        if (habitacion instanceof Doble && ((Doble) habitacion).isSupletoria()) {
            precioNoche += ((Doble) habitacion).getSuplementSupletoria();
        }
        return noches * precioNoche;
    }

    private void validarFechas(LocalDate entrada, LocalDate salida) {
        if (entrada == null || salida == null) {
            throw new IllegalArgumentException("Las fechas son obligatorias.");
        }
        if (entrada.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de entrada no puede ser anterior a hoy.");
        }
        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la de entrada.");
        }
    }

    private void validarHuespedes(Habitacion habitacion, int numHuespedes) {
        if (numHuespedes <= 0) {
            throw new IllegalArgumentException("Debe haber al menos un huésped.");
        }
        if (numHuespedes > habitacion.getCapacidad()) {
            throw new IllegalArgumentException("La habitación admite como máximo "
                    + habitacion.getCapacidad() + " huéspedes.");
        }
    }

    private Hotel obtenerHotel(String nombreHotel) throws SQLException {
        Hotel hotel = ctrlHoteles.buscarHotel(nombreHotel);
        if (hotel == null) {
            throw new IllegalArgumentException("No existe ningún hotel llamado " + nombreHotel);
        }
        return hotel;
    }

    private Habitacion obtenerHabitacion(Hotel hotel, int numHabitacion) throws SQLException {
        Habitacion habitacion = ctrlHabitaciones.buscarHabitacion(hotel, numHabitacion);
        if (habitacion == null) {
            throw new IllegalArgumentException("El hotel " + hotel.getNombre()
                    + " no tiene la habitación " + numHabitacion);
        }
        return habitacion;
    }

    private Reserva obtenerReservaActiva(int idReserva) throws SQLException {
        Reserva reserva = buscarReserva(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("No existe ninguna reserva con id " + idReserva);
        }
        if (!reserva.isEstado()) {
            throw new IllegalStateException("La reserva " + idReserva + " está cancelada.");
        }
        return reserva;
    }

}
