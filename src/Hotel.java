import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private String nombre;
    private String direccion;
    private String ciudad;
    private int telefono;
    private List<Habitacion> habitaciones;

    public Hotel() {
        this.habitaciones = new ArrayList<Habitacion>();
    }

    public Hotel(String string, String string2, String string3, int n) {
        this.nombre = string;
        this.direccion = string2;
        this.ciudad = string3;
        this.telefono = n;
        this.habitaciones = new ArrayList<Habitacion>();
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String string) {
        this.nombre = string;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(String string) {
        this.direccion = string;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public void setCiudad(String string) {
        this.ciudad = string;
    }

    public int getTelefono() {
        return this.telefono;
    }

    public void setTelefono(int n) {
        this.telefono = n;
    }

    public List<Habitacion> getHabitaciones() {
        return this.habitaciones;
    }

    public void setHabitaciones(List<Habitacion> list) {
        this.habitaciones = list;
    }

    public String toString() {
        return "Hotel{nombre='" + this.nombre + "', direccion='" + this.direccion + "', ciudad='" + this.ciudad + "', telefono=" + this.telefono + ", totalHabitaciones=" + this.habitaciones.size() + "}";
    }
}
