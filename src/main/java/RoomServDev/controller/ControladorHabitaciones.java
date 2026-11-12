package RoomServDev.controller;

import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.model.Reserva;
import RoomServDev.model.Simple;
import RoomServDev.model.Suite;
import RoomServDev.persistence.dao.HabitacionDAO;
import RoomServDev.persistence.dao.ReservaDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControladorHabitaciones {

    // Las habitaciones se guardan dentro de cada Hotel
    private final HabitacionDAO habitacionDAO;
    private final ReservaDAO reservaDAO;

    public ControladorHabitaciones(HabitacionDAO habitacionDAO, ReservaDAO reservaDAO) {
        this.habitacionDAO = habitacionDAO;
        this.reservaDAO = reservaDAO;
    }

    // ********* Crear (uno por tipo, cada tipo tiene atributos distintos) *********

    public void crearHabitacionSimple(Hotel hotel, int numHabitacion, int capacidad, double precioNoche,
                                      boolean disponible, String medidaCama, boolean tieneEscritorio) throws SQLException {
        validarDatos(hotel, numHabitacion, capacidad, precioNoche);

        Simple simple = new Simple(numHabitacion, capacidad, precioNoche, "Simple", medidaCama, tieneEscritorio);
        simple.setDisponible(disponible);
        habitacionDAO.insertar(simple, hotel.getIdHotel());
    }

    public void crearHabitacionDoble(Hotel hotel, int numHabitacion, int capacidad, double precioNoche,
                                     boolean disponible, String tipoCama, boolean supletoria,
                                     double suplementoSupletoria, boolean balcon) throws SQLException {
        validarDatos(hotel, numHabitacion, capacidad, precioNoche);
        if (supletoria && suplementoSupletoria < 0) {
            throw new IllegalArgumentException("El suplemento de la supletoria no puede ser negativo.");
        }
        if (!supletoria) suplementoSupletoria = 0;

        Doble doble = new Doble(numHabitacion, capacidad, precioNoche, capacidad, "Doble",
                tipoCama, supletoria, suplementoSupletoria, balcon);
        doble.setDisponible(disponible);
        habitacionDAO.insertar(doble, hotel.getIdHotel());
    }

    public void crearHabitacionSuite(Hotel hotel, int numHabitacion, int capacidad, double precioNoche,
                                     boolean disponible, int numAmbientes, boolean jacuzzi,
                                     boolean incluyeSpa, boolean incluyeTerraza, String otrosServicios) throws SQLException {
        validarDatos(hotel, numHabitacion, capacidad, precioNoche);
        if (numAmbientes <= 0) {
            throw new IllegalArgumentException("Una suite debe tener al menos un ambiente.");
        }

        Suite suite = new Suite(numHabitacion, capacidad, precioNoche, "Suite", numAmbientes,
                jacuzzi, incluyeSpa, incluyeTerraza, otrosServicios);
        suite.setDisponible(disponible);
        habitacionDAO.insertar(suite, hotel.getIdHotel());
    }

    public void guardarHabitacion(Hotel hotel, Habitacion habitacion) throws SQLException {
        if (habitacion == null){
            throw new IllegalArgumentException("No se ha indicado la habitación.");
        }
        validarDatos(hotel, habitacion.getNumHabitacion(), habitacion.getCapacidad(), habitacion.getPrecioNoche());
        habitacionDAO.insertar(habitacion, hotel.getIdHotel());
    }

    // ********* Consultar *********

    public List<Habitacion> listarHabitaciones(Hotel hotel) throws SQLException {
        if (hotel == null) return new ArrayList<>();
        return List.copyOf(habitacionDAO.obtenerPorHotel(hotel.getIdHotel()));
    }

    // tipo: 1 = Simple, 2 = Doble, 3 = Suite (igual que el menú de la vista)
    public List<Habitacion> listarHabitacionesPorTipo(Hotel hotel, int tipo) throws SQLException {
        List<Habitacion> resultado = new ArrayList<>();
        if (hotel == null) return resultado;

        for (Habitacion h : habitacionDAO.obtenerPorHotel(hotel.getIdHotel())) {
            if ((tipo == 1 && h instanceof Simple)
                    || (tipo == 2 && h instanceof Doble)
                    || (tipo == 3 && h instanceof Suite)) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    public List<Habitacion> listarHabitacionesDisponibles(Hotel hotel) throws SQLException {
        List<Habitacion> disponibles = new ArrayList<>();
        if (hotel == null) return disponibles;
        for (Habitacion habitacion : habitacionDAO.obtenerPorHotel(hotel.getIdHotel())){
            if (habitacion.isDisponible()){
                disponibles.add(habitacion);
            }
        }
        return  disponibles;
    }

    public Habitacion buscarHabitacion(Hotel hotel, int numHabitacion) throws SQLException {
        if (hotel == null) return null;
        for (Habitacion h : habitacionDAO.obtenerPorHotel(hotel.getIdHotel())) {
            if (h.getNumHabitacion() == numHabitacion) return h;
        }
        return null;
    }

    // ********* Editar (atributos comunes a todos los tipos) *********

    public void editarHabitacion(Hotel hotel, int numHabitacion, int capacidad,
                                 double precioNoche, boolean disponible) throws SQLException {
        Habitacion h = buscarHabitacionObligatoria(hotel, numHabitacion);
        if (capacidad <= 0 || precioNoche <= 0) {
            throw new IllegalArgumentException("La capacidad y el precio deben ser mayores que 0.");
        }
        h.setCapacidad(capacidad);
        h.setPrecioNoche(precioNoche);
        h.setDisponible(disponible);
        habitacionDAO.actualizar(h);
    }

    public void cambiarDisponibilidad(Hotel hotel, int numHabitacion, boolean disponible) throws SQLException {
        Habitacion habitacion = buscarHabitacionObligatoria(hotel, numHabitacion);
        habitacion.setDisponible(disponible);
        habitacionDAO.actualizar(habitacion);
    }

    // ********* Eliminar *********

    public void eliminarHabitacion(Hotel hotel, int numHabitacion) throws SQLException {
        Habitacion h = buscarHabitacionObligatoria(hotel, numHabitacion);
        if (tieneReservasActivas(hotel, numHabitacion)) {
            throw new IllegalStateException("No se puede eliminar la habitación " + numHabitacion
                    + " porque tiene reservas activas.");
        }
        habitacionDAO.eliminar(h.getIdHabitacion());
    }

    // ********* Auxiliares (privados) *********

    private Habitacion buscarHabitacionObligatoria(Hotel hotel, int numHabitacion) throws SQLException {
        if (hotel == null) throw new IllegalArgumentException("No se ha seleccionado ningún hotel.");
        Habitacion h = buscarHabitacion(hotel, numHabitacion);
        if (h == null) {
            throw new IllegalArgumentException("El hotel " + hotel.getNombre()
                    + " no tiene la habitación " + numHabitacion);
        }
        return h;
    }

    private boolean tieneReservasActivas(Hotel hotel, int numHabitacion) throws SQLException {
        for (Reserva r : reservaDAO.obtenerTodas()) {
            if (r.isEstado()
                    && r.getNombreHotel().equalsIgnoreCase(hotel.getNombre())
                    && r.getNumHabitacion() == numHabitacion) {
                return true;
            }
        }
        return false;
    }

    private void validarDatos(Hotel hotel, int numHabitacion, int capacidad, double precioNoche) throws SQLException{
        if (hotel == null || hotel.getIdHotel() <= 0) {
            throw new IllegalArgumentException("No se ha seleccionado ningún hotel.");
        }
        if (numHabitacion <= 0 || capacidad <= 0 || precioNoche <= 0) {
            throw new IllegalArgumentException("Número, capacidad y precio deben ser mayores que 0.");
        }
        if (buscarHabitacion(hotel, numHabitacion) != null) {
            throw new IllegalArgumentException("Ya existe la habitación " + numHabitacion
                    + " en el hotel " + hotel.getNombre());
        }
    }
}
