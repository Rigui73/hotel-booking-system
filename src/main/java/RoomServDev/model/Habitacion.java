
package RoomServDev.model;

public abstract class Habitacion{

    // atributos

    private int numHabitacion;
    private int capacidad;
    private double precioNoche;
    private boolean disponible;
    private String categoria;

    //constructor

    public Habitacion(int numHabitacion, int capacidad, double precioNoche, boolean disponible, String categoria) {
        this.numHabitacion = numHabitacion;
        this.capacidad = capacidad;
        this.precioNoche = precioNoche;
        this.categoria = categoria;
        this.disponible = disponible;
    }

    // --------GETTERS y SETTERS ---------

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
    public String getCategoria(){
        return categoria;
    }
    public void setCategoria(String categoria){
        this.categoria = categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}

