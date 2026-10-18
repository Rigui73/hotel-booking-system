package RoomServDev.modelo;

import java.util.ArrayList;
import java.util.List;

public class Hotel {

    // ********* Atributos  *********

    private String nombre;
    private String direccion;
    private String ciudad;
    private int telefono;

    private List<Habitacion> habitaciones;

// ********* Constructor  *********

    public Hotel(String nombre, String direccion,
                 String ciudad, int telefono){
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.telefono = telefono;

        this.habitaciones = new ArrayList<>();
    }

// *********GETTERS y SETTERS *********

    public String getNombre(){
        return nombre;
    }

    public void setNombre (String nombre){
        this.nombre = nombre;
    }

    public String getDireccion(){
        return direccion;
    }

    public void setDireccion (String direccion){
        this.direccion = direccion;
    }

    public String getCiudad(){
        return ciudad;
    }

    public void setCiudad (String ciudad){
        this.ciudad = ciudad;
    }

    public int getTelefono(){
        return telefono;
    }

    public void setTelefono(int telefono){
        this.telefono = telefono;
    }

    //getter para obtener la lista de habitaciones de un hotel
    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    //setter para meter habitaciones a un hotel

    public void añadirHabitacion(Habitacion habitacion){
        this.habitaciones.add(habitacion);
    }



    @Override
        public String toString(){
        return "Hotel{" +
                "nombre='" + nombre + '\'' +
                ",direccion='" + direccion + '\''+
                ",ciudad='" + ciudad + '\'' +
                ",telefono=" + telefono +
                '}';
    }
}