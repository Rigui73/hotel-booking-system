package RoomServDev.controller;

import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.model.HotelConReservaException;
import RoomServDev.model.HotelDuplicadoException;
import RoomServDev.persistence.dao.HabitacionDAO;
import RoomServDev.persistence.dao.HotelDAO;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ControladorHoteles {

    private final HotelDAO hotelDAO;
    private final HabitacionDAO habitacionDAO;

    public ControladorHoteles(HotelDAO hotelDAO, HabitacionDAO habitacionDAO) {
        this.hotelDAO = hotelDAO;
        this.habitacionDAO = habitacionDAO;
    }

    // ********* Crear *********

    public void crearHotel(String nombre, String direccion, String ciudad, int telefono)
            throws HotelDuplicadoException, SQLException {
        validarDatos(nombre, direccion, ciudad, telefono);

        Hotel hotel = new Hotel();
        hotel.setNombre(nombre.trim());
        hotel.setDireccion(direccion.trim());
        hotel.setCiudad(ciudad.trim());
        hotel.setTelefono(telefono);

        try {
            hotelDAO.insertar(hotel);
        } catch (SQLIntegrityConstraintViolationException e){
            throw new HotelDuplicadoException("El hotel " + nombre.trim() + " ya esta registrado.");
        }
    }

    public List<Hotel> listarHoteles() throws SQLException {
        return List.copyOf(hotelDAO.obtenerTodosHoteles());
    }

    public List<Hotel> listarHotelesPorCiudad(String ciudad) throws SQLException{
        if (estaVacio(ciudad)) return new ArrayList<>();

        String ciudadBuscada = ciudad.trim();
        List<Hotel> resultado = new ArrayList<>();
        for (Hotel hotel : hotelDAO.obtenerTodosHoteles()){
            if (hotel.getCiudad() != null && hotel.getCiudad().equalsIgnoreCase(ciudadBuscada)){
                resultado.add(hotel);
            }
        }
        return resultado;
    }

    // Opción 3 del menú de listar: hoteles con al menos una habitación disponible
    public List<Hotel> listarHotelesConDisponibilidad() throws SQLException {
        List<Hotel> resultado = new ArrayList<>();
        for (Hotel hotel : hotelDAO.obtenerTodosHoteles()) {
            if (contarHabitacionesDisponibles(hotel) > 0) {
                resultado.add(hotel);
            }
        }
        return resultado;
    }

    // Para seleccionar un hotel por su posición en la lista (empieza en 0)
    public Hotel obtenerHotel(int indice) throws SQLException{
        List<Hotel> hoteles = hotelDAO.obtenerTodosHoteles();
        if (indice < 0 || indice >= hoteles.size()) return null;
        return hoteles.get(indice);
    }

    public Hotel buscarHotel(String nombre) throws SQLException {
        if (estaVacio(nombre)) return null;
        return hotelDAO.obtenerPorNombres(nombre.trim());
    }

    // ********* Editar *********

    // El nombre identifica al hotel (las reservas lo guardan), por eso no se cambia
    public void editarHotel(String nombre, String nuevaDireccion, String nuevaCiudad, int nuevoTelefono) throws SQLException{
        Hotel hotel = buscarHotel(nombre);
        if (hotel == null) {
            throw new IllegalArgumentException("No existe ningún hotel llamado " + nombre);
        }
        validarDatos(hotel.getNombre(), nuevaDireccion, nuevaCiudad, nuevoTelefono);
        hotel.setDireccion(nuevaDireccion.trim());
        hotel.setCiudad(nuevaCiudad.trim());
        hotel.setTelefono(nuevoTelefono);
        hotelDAO.actualizar(hotel);
    }

    // ********* Eliminar *********

    public void eliminarHotel(String nombre) throws HotelConReservaException, SQLException {
        Hotel hotel = buscarHotel(nombre);
        if (hotel == null) {
            throw new IllegalArgumentException("No existe ningún hotel llamado " + nombre);
        }
        try {
            hotelDAO.eliminar(hotel.getIdHotel());
        } catch (SQLIntegrityConstraintViolationException e){
            throw new HotelConReservaException("No se puede eliminar el hotel " + nombre + " porque tiene habitaciones o reservas asociadas.");
        }
    }

    private int contarHabitacionesDisponibles(Hotel hotel) throws SQLException {
        int disponibles = 0;
        for (Habitacion habitacion : habitacionDAO.obtenerPorHotel(hotel.getIdHotel())){
            if (habitacion.isDisponible()){
                disponibles++;
            }
        }
        return disponibles;
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
