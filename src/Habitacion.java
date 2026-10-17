public abstract class Habitacion {
    private int numHabitacion;
    private int capacidadMax;
    private double precioNoche;

    public Habitacion() {
    }

    public Habitacion(int n, int n2, double d) {
        this.numHabitacion = n;
        this.capacidadMax = n2;
        this.precioNoche = d;
    }

    public int getNumHabitacion() {
        return this.numHabitacion;
    }

    public void setNumHabitacion(int n) {
        this.numHabitacion = n;
    }

    public int getCapacidadMax() {
        return this.capacidadMax;
    }

    public void setCapacidadMax(int n) {
        this.capacidadMax = n;
    }

    public double getPrecioNoche() {
        return this.precioNoche;
    }

    public void setPrecioNoche(double d) {
        this.precioNoche = d;
    }

    public String toString() {
        return "Habitacion{numHabitacion=" + this.numHabitacion + ", capacidadMax=" + this.capacidadMax + ", precioNoche=" + this.precioNoche + "}";
    }
}
