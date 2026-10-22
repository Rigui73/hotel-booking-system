package RoomServDev.controller;

import RoomServDev.model.Datos;
import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.model.Reserva;
import RoomServDev.model.Simple;
import RoomServDev.model.Suite;

import java.util.ArrayList;
import java.util.List;

public class ControladorHabitaciones {

    // Las habitaciones se guardan dentro de cada Hotel
    private final Datos datos;

    public ControladorHabitaciones(Datos datos) {
        this.datos = datos;
    }

    // ********* Crear (uno por tipo, cada tipo tiene atributos distintos) *********

    public void crearHabitacionSimple(Hotel hotel, int numHabitacion, int capacidad, double precioNoche,
                                      boolean disponible, String medidaCama, boolean tieneEscritorio) {
        validarDatos(hotel, numHabitacion, capacidad, precioNoche);

        Simple simple = new Simple(numHabitacion, capacidad, precioNoche, "Simple", medidaCama, tieneEscritorio);
        simple.setDisponible(disponible);
        hotel.anadirHabitacion(simple);
    }

    public void crearHabitacionDoble(Hotel hotel, int numHabitacion, int capacidad, double precioNoche,
                                     boolean disponible, String tipoCama, boolean supletoria,
                                     double suplementoSupletoria, boolean balcon) {
        validarDatos(hotel, numHabitacion, capacidad, precioNoche);
        if (supletoria && suplementoSupletoria < 0) {
            throw new IllegalArgumentException("El suplemento de la supletoria no puede ser negativo.");
        }
        if (!supletoria) suplementoSupletoria = 0;

        Doble doble = new Doble(numHabitacion, capacidad, precioNoche, tipoCama,
                supletoria, suplementoSupletoria, balcon);
        doble.setDisponible(disponible);
        hotel.anadirHabitacion(doble);
    }

    public void crearHabitacionSuite(Hotel hotel, int numHabitacion, int capacidad, double precioNoche,
                                     boolean disponible, int numAmbientes, boolean jacuzzi,
                                     boolean incluyeSpa, boolean incluyeTerraza, String otrosServicios) {
        validarDatos(hotel, numHabitacion, capacidad, precioNoche);
        if (numAmbientes <= 0) {
            throw new IllegalArgumentException("Una suite debe tener al menos un ambiente.");
        }

        Suite suite = new Suite(numHabitacion, capacidad, precioNoche, "Suite", numAmbientes,
                jacuzzi, incluyeSpa, incluyeTerraza, otrosServicios);
        suite.setDisponible(disponible);
        hotel.anadirHabitacion(suite);
    }

    // ********* Consultar *********

    public List<Habitacion> listarHabitaciones(Hotel hotel) {
        if (hotel == null) return new ArrayList<>();
        return List.copyOf(datos.listarHabitacionesHotel(hotel));
    }

    // tipo: 1 = Simple, 2 = Doble, 3 = Suite (igual que el menú de la vista)
    public List<Habitacion> listarHabitacionesPorTipo(Hotel hotel, int tipo) {
        List<Habitacion> resultado = new ArrayList<>();
        if (hotel == null) return resultado;

        for (Habitacion h : hotel.getHabitaciones()) {
            if ((tipo == 1 && h instanceof Simple)
                    || (tipo == 2 && h instanceof Doble)
                    || (tipo == 3 && h instanceof Suite)) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    public List<Habitacion> listarHabitacionesDisponibles(Hotel hotel) {
        if (hotel == null) return new ArrayList<>();
        return datos.mostrarHabitacionesDisponibles(hotel);
    }

    public Habitacion buscarHabitacion(Hotel hotel, int numHabitacion) {
        if (hotel == null) return null;
        for (Habitacion h : hotel.getHabitaciones()) {
            if (h.getNumHabitacion() == numHabitacion) return h;
        }
        return null;
    }

    // ********* Editar (atributos comunes a todos los tipos) *********

    public void editarHabitacion(Hotel hotel, int numHabitacion, int capacidad,
                                 double precioNoche, boolean disponible) {
        Habitacion h = buscarHabitacionObligatoria(hotel, numHabitacion);
        if (capacidad <= 0 || precioNoche <= 0) {
            throw new IllegalArgumentException("La capacidad y el precio deben ser mayores que 0.");
        }
        h.setCapacidad(capacidad);
        h.setPrecioNoche(precioNoche);
        h.setDisponible(disponible);
    }

    public void cambiarDisponibilidad(Hotel hotel, int numHabitacion, boolean disponible) {
        buscarHabitacionObligatoria(hotel, numHabitacion).setDisponible(disponible);
    }

    // ********* Eliminar *********

    public void eliminarHabitacion(Hotel hotel, int numHabitacion) {
        Habitacion h = buscarHabitacionObligatoria(hotel, numHabitacion);
        if (tieneReservasActivas(hotel, numHabitacion)) {
            throw new IllegalStateException("No se puede eliminar la habitación " + numHabitacion
                    + " porque tiene reservas activas.");
        }
        hotel.getHabitaciones().remove(h);
    }

    // ********* Auxiliares (privados) *********

    private Habitacion buscarHabitacionObligatoria(Hotel hotel, int numHabitacion) {
        if (hotel == null) throw new IllegalArgumentException("No se ha seleccionado ningún hotel.");
        Habitacion h = buscarHabitacion(hotel, numHabitacion);
        if (h == null) {
            throw new IllegalArgumentException("El hotel " + hotel.getNombre()
                    + " no tiene la habitación " + numHabitacion);
        }
        return h;
    }

    private boolean tieneReservasActivas(Hotel hotel, int numHabitacion) {
        for (Reserva r : datos.listarReservas()) {
            if (r.isEstado()
                    && r.getNombreHotel().equalsIgnoreCase(hotel.getNombre())
                    && r.getNumHabitacion() == numHabitacion) {
                return true;
            }
        }
        return false;
    }

    private void validarDatos(Hotel hotel, int numHabitacion, int capacidad, double precioNoche) {
        if (hotel == null) {
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
