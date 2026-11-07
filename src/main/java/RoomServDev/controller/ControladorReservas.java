package RoomServDev.controller;

import RoomServDev.model.Cliente;
import RoomServDev.model.Datos;
import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.model.Reserva;
import RoomServDev.model.ReservaSolapadaException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControladorReservas {

    private final Datos datos;
    private final ControladorClientes ctrlClientes;
    private final ControladorHoteles ctrlHoteles;
    private final ControladorHabitaciones ctrlHabitaciones;

    // Necesita los otros controladores para comprobar que cliente, hotel y habitación existen
    public ControladorReservas(Datos datos, ControladorClientes ctrlClientes,
                               ControladorHoteles ctrlHoteles, ControladorHabitaciones ctrlHabitaciones) {
        this.datos = datos;
        this.ctrlClientes = ctrlClientes;
        this.ctrlHoteles = ctrlHoteles;
        this.ctrlHabitaciones = ctrlHabitaciones;
    }

    // ********* Crear *********

    // El id, la fecha de reserva, el estado y el importe los pone el controlador, no el usuario
    public Reserva crearReserva(String dni, String nombreHotel, int numHabitacion,
                                LocalDate entrada, LocalDate salida, int numHuespedes)
            throws ReservaSolapadaException, Exception {

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
        comprobarSolapamiento(hotel.getNombre(), numHabitacion, entrada, salida, null);

        // Cálculo del importe total: días de estancia * precio por noche
        long diasEstancia = java.time.temporal.ChronoUnit.DAYS.between(entrada, salida);
        if (diasEstancia <= 0) diasEstancia = 1;
        double importeTotal = diasEstancia * habitacion.getPrecioNoche();

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
                importeTotal,
                true,
                hotel.getNombre(),
                numHabitacion,
                cliente.getDni()
        );

        // Registro en la colección o persistencia
        datos.anadirReservas(reserva);

        return reserva;
    }

    public List<Reserva> listarReservasCliente(String dni) {
        if (dni == null) return new ArrayList<>();
        return datos.consultarReservasCliente(dni.trim());
    }

    public Reserva buscarReserva(int idReserva) {
        for (Reserva r : datos.listarReservas()) {
            if (r.getIdReserva() == idReserva) return r;
        }
        return null;
    }

    // Editar: la reserva
    public void editarReserva(int idReserva, LocalDate nuevaEntrada, LocalDate nuevaSalida, int nuevosHuespedes)
            throws ReservaSolapadaException {

        Reserva reserva = obtenerReservaActiva(idReserva);
        Hotel hotel = obtenerHotel(reserva.getNombreHotel());
        Habitacion habitacion = obtenerHabitacion(hotel, reserva.getNumHabitacion());

        validarFechas(nuevaEntrada, nuevaSalida);
        validarHuespedes(habitacion, nuevosHuespedes);

        // Datos.modificarReserva comprueba el solapamiento y actualiza fechas y huéspedes
        datos.modificarReserva(reserva, aDate(nuevaEntrada), aDate(nuevaSalida), nuevosHuespedes);
        reserva.setImporteTotal(calcularImporte(habitacion, nuevaEntrada, nuevaSalida));
    }

    // Cancelar: la reserva queda en el historial pero deja de estar activa
    public void cancelarReserva(int idReserva) {
        datos.cancelarReserva(obtenerReservaActiva(idReserva));
    }

    // Eliminar: la reserva desaparece por completo
    public void eliminarReserva(int idReserva) {
        Reserva reserva = buscarReserva(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("No existe ninguna reserva con id " + idReserva);
        }
        datos.listarReservas().remove(reserva);
    }

    // ********* Auxiliares (privados) *********

    private int generarId() {
        int max = 0;
        for (Reserva r : datos.listarReservas()) {
            if (r.getIdReserva() > max) max = r.getIdReserva();
        }
        return max + 1;
    }

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

    // "ignorar" sirve para no comparar una reserva consigo misma
    private void comprobarSolapamiento(String nombreHotel, int numHabitacion,
                                       LocalDate entrada, LocalDate salida, Reserva ignorar)
            throws ReservaSolapadaException {
        for (Reserva r : datos.listarReservas()) {
            if (r == ignorar || !r.isEstado()) continue;
            if (r.getNombreHotel().equalsIgnoreCase(nombreHotel) && r.getNumHabitacion() == numHabitacion) {
                LocalDate rEntrada = aLocalDate(r.getFechaEntrada());
                LocalDate rSalida = aLocalDate(r.getFechaSalida());
                if (entrada.isBefore(rSalida) && salida.isAfter(rEntrada)) {
                    throw new ReservaSolapadaException("La habitación " + numHabitacion
                            + " ya está reservada del " + rEntrada + " al " + rSalida);
                }
            }
        }
    }

    private Hotel obtenerHotel(String nombreHotel) {
        Hotel hotel = ctrlHoteles.buscarHotel(nombreHotel);
        if (hotel == null) {
            throw new IllegalArgumentException("No existe ningún hotel llamado " + nombreHotel);
        }
        return hotel;
    }

    private Habitacion obtenerHabitacion(Hotel hotel, int numHabitacion) {
        Habitacion habitacion = ctrlHabitaciones.buscarHabitacion(hotel, numHabitacion);
        if (habitacion == null) {
            throw new IllegalArgumentException("El hotel " + hotel.getNombre()
                    + " no tiene la habitación " + numHabitacion);
        }
        return habitacion;
    }

    private Reserva obtenerReservaActiva(int idReserva) {
        Reserva reserva = buscarReserva(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("No existe ninguna reserva con id " + idReserva);
        }
        if (!reserva.isEstado()) {
            throw new IllegalStateException("La reserva " + idReserva + " está cancelada.");
        }
        return reserva;
    }

    // La vista trabaja con LocalDate y el modelo con Date: estos dos métodos convierten
    private Date aDate(LocalDate fecha) {
        return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate aLocalDate(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
