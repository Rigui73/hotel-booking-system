package RoomServDev.model;

import java.util.ArrayList;
import java.util.List;

public class Hotel {

    private int idHotel;
    private String nombre;
    private String direccion;
    private String ciudad;
    private int telefono;

    private List<Habitacion> habitaciones;

    public Hotel() {
        this.habitaciones = new ArrayList<>();
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    // getter para obtener la lista de habitaciones de un hotel
    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    // setter para meter habitaciones a un hotel

    public void anadirHabitacion(Habitacion habitacion) {
        this.habitaciones.add(habitacion);
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "idHotel=" + idHotel + '\'' +
                "nombre='" + nombre + '\'' +
                ",direccion='" + direccion + '\'' +
                ",ciudad='" + ciudad + '\'' +
                ",telefono=" + telefono +
                '}';
    }

}