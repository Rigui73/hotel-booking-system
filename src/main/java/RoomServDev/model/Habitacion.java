package RoomServDev.model;

public class Habitacion {

    private int idHabitacion;
    private int idHotel;
    private int numHabitacion;
    private int capacidad;
    private double precioNoche;
    private String categoria;
    private boolean disponible;

    public Habitacion(int numHabitacion, int capacidad, double precioNoche,
            String categoria, boolean disponible) {

        this.numHabitacion = numHabitacion;
        this.capacidad = capacidad;
        this.precioNoche = precioNoche;
        this.categoria = categoria;
        this.disponible = disponible;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public int getNumHabitacion() {
        return numHabitacion;
    }

    public void setNumHabitacion(int numHabitacion) {
        this.numHabitacion = numHabitacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
