package RoomServDev.controller;

import RoomServDev.model.Datos;
import RoomServDev.model.Hotel;
import RoomServDev.model.HotelConReservaException;
import RoomServDev.model.HotelDuplicadoException;

import java.util.ArrayList;
import java.util.List;

public class ControladorHoteles {

    private final Datos datos;

    public ControladorHoteles(Datos datos) {
        this.datos = datos;
    }

    // ********* Crear *********

    public void crearHotel(String nombre, String direccion, String ciudad, int telefono)
            throws HotelDuplicadoException {
        validarDatos(nombre, direccion, ciudad, telefono);
        datos.anadirHotel(new Hotel(nombre.trim(), direccion.trim(), ciudad.trim(), telefono));
    }

    // ********* Consultar *********

    // Devuelve una copia para que la vista no pueda modificar la lista original
    public List<Hotel> listarHoteles() {
        return List.copyOf(datos.listarHoteles());
    }

    public List<Hotel> listarHotelesPorCiudad(String ciudad) {
        if (estaVacio(ciudad)) return new ArrayList<>();
        return datos.buscarHotelesPorCiudad(ciudad.trim());
    }

    // Opción 3 del menú de listar: hoteles con al menos una habitación disponible
    public List<Hotel> listarHotelesConDisponibilidad() {
        List<Hotel> resultado = new ArrayList<>();
        for (Hotel hotel : datos.listarHoteles()) {
            if (datos.contarHabitacionesDisponibles(hotel) > 0) {
                resultado.add(hotel);
            }
        }
        return resultado;
    }

    // Para seleccionar un hotel por su posición en la lista (empieza en 0)
    public Hotel obtenerHotel(int indice) {
        List<Hotel> hoteles = datos.listarHoteles();
        if (indice < 0 || indice >= hoteles.size()) return null;
        return hoteles.get(indice);
    }

    public Hotel buscarHotel(String nombre) {
        if (estaVacio(nombre)) return null;
        for (Hotel hotel : datos.listarHoteles()) {
            if (hotel.getNombre().equalsIgnoreCase(nombre.trim())) return hotel;
        }
        return null;
    }

    // ********* Editar *********

    // El nombre identifica al hotel (las reservas lo guardan), por eso no se cambia
    public void editarHotel(String nombre, String nuevaDireccion, String nuevaCiudad, int nuevoTelefono) {
        Hotel hotel = buscarHotel(nombre);
        if (hotel == null) {
            throw new IllegalArgumentException("No existe ningún hotel llamado " + nombre);
        }
        validarDatos(hotel.getNombre(), nuevaDireccion, nuevaCiudad, nuevoTelefono);
        datos.modificarHotel(hotel.getNombre(), nuevaDireccion.trim(), nuevaCiudad.trim(), nuevoTelefono);
    }

    // ********* Eliminar *********

    public void eliminarHotel(String nombre) throws HotelConReservaException {
        Hotel hotel = buscarHotel(nombre);
        if (hotel == null) {
            throw new IllegalArgumentException("No existe ningún hotel llamado " + nombre);
        }
        datos.eliminarHotel(hotel.getNombre());
    }

    // ********* Validaciones (privadas) *********

    private void validarDatos(String nombre, String direccion, String ciudad, int telefono) {
        if (estaVacio(nombre) || estaVacio(direccion) || estaVacio(ciudad)) {
            throw new IllegalArgumentException("Nombre, dirección y ciudad son obligatorios.");
        }
        if (telefono < 100000000 || telefono > 999999999) {
            throw new IllegalArgumentException("El teléfono debe tener 9 cifras.");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
