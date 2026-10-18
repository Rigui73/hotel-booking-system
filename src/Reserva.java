import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private int idReserva;
    private LocalDate fechaReserva;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private int numHuespedes;
    private double importeTotal;
    private boolean estado;
    private Cliente cliente;
    private List<Habitacion> habitaciones;

    public Reserva() {
        this.habitaciones = new ArrayList<Habitacion>();
    }

    public Reserva(int n, LocalDate localDate, LocalDate localDate2, LocalDate localDate3, int n2, double d, boolean bl, Cliente cliente) {
        this.idReserva = n;
        this.fechaReserva = localDate;
        this.fechaEntrada = localDate2;
        this.fechaSalida = localDate3;
        this.numHuespedes = n2;
        this.importeTotal = d;
        this.estado = bl;
        this.cliente = cliente;
        this.habitaciones = new ArrayList<Habitacion>();
    }

    public int getIdReserva() {
        return this.idReserva;
    }

    public void setIdReserva(int n) {
        this.idReserva = n;
    }

    public LocalDate getFechaReserva() {
        return this.fechaReserva;
    }

    public void setFechaReserva(LocalDate localDate) {
        this.fechaReserva = localDate;
    }

    public LocalDate getFechaEntrada() {
        return this.fechaEntrada;
    }

    public void setFechaEntrada(LocalDate localDate) {
        this.fechaEntrada = localDate;
    }

    public LocalDate getFechaSalida() {
        return this.fechaSalida;
    }

    public void setFechaSalida(LocalDate localDate) {
        this.fechaSalida = localDate;
    }

    public int getNumHuespedes() {
        return this.numHuespedes;
    }

    public void setNumHuespedes(int n) {
        this.numHuespedes = n;
    }

    public double getImporteTotal() {
        return this.importeTotal;
    }

    public void setImporteTotal(double d) {
        this.importeTotal = d;
    }

    public boolean isEstado() {
        return this.estado;
    }

    public void setEstado(boolean bl) {
        this.estado = bl;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Habitacion> getHabitaciones() {
        return this.habitaciones;
    }

    public void setHabitaciones(List<Habitacion> list) {
        this.habitaciones = list;
    }

    public String toString() {
        return "Reserva{idReserva=" + this.idReserva + ", fechaReserva=" + String.valueOf(this.fechaReserva) + ", fechaEntrada=" + String.valueOf(this.fechaEntrada) + ", fechaSalida=" + String.valueOf(this.fechaSalida) + ", numHuespedes=" + this.numHuespedes + ", importeTotal=" + this.importeTotal + ", estado=" + this.estado + ", cliente=" + (this.cliente != null ? this.cliente.getDni() : "null") + ", numHabitaciones=" + this.habitaciones.size() + "}";
    }
}
