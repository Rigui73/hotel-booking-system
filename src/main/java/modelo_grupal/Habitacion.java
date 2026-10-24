package modelo_grupal;

public abstract class Habitacion {
    private int numHabitacion;
    private int capacidadMax;
    private double precioNoche;

    public Habitacion() {
    }

    public Habitacion(int numHabitacion, int capacidadMax, double precioNoche) {
        this.numHabitacion = numHabitacion;
        this.capacidadMax = capacidadMax;
        this.precioNoche = precioNoche;
    }

    public int getNumHabitacion() {
        return numHabitacion;
    }

    public void setNumHabitacion(int numHabitacion) {
        this.numHabitacion = numHabitacion;
    }

    public int getCapacidadMax() {
        return capacidadMax;
    }

    public void setCapacidadMax(int capacidadMax) {
        this.capacidadMax = capacidadMax;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
    }

    @Override
    public String toString() {
        return "Habitacion{" +
                "numHabitacion=" + numHabitacion +
                ", capacidadMax=" + capacidadMax +
                ", precioNoche=" + precioNoche +
                '}';
    }

    public int getCapacidadMaxima() {
        return this.capacidadMax;
    }
}