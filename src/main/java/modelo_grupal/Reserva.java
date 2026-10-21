package modelo_grupal;

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
    private Cliente clienteG;
    private List<Habitacion> habitaciones;

    public Reserva() {
        this.habitaciones = new ArrayList<>();
    }

    public Reserva(int idReserva, LocalDate fechaReserva, LocalDate fechaEntrada,
                   LocalDate fechaSalida, int numHuespedes, double importeTotal,
                   boolean estado, Cliente clienteG) {
        this.idReserva = idReserva;
        this.fechaReserva = fechaReserva;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.numHuespedes = numHuespedes;
        this.importeTotal = importeTotal;
        this.estado = estado;
        this.clienteG = clienteG;
        this.habitaciones = new ArrayList<>();
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public int getNumHuespedes() {
        return numHuespedes;
    }

    public void setNumHuespedes(int numHuespedes) {
        this.numHuespedes = numHuespedes;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return clienteG;
    }

    public void setCliente(Cliente clienteG) {
        this.clienteG = clienteG;
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", fechaReserva=" + fechaReserva +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaSalida=" + fechaSalida +
                ", numHuespedes=" + numHuespedes +
                ", importeTotal=" + importeTotal +
                ", estado=" + estado +
                ", cliente=" + (clienteG != null ? clienteG.getDni() : "null") +
                ", numHabitaciones=" + habitaciones.size() +
                '}';
    }

    public void calcularImporteTotal() {
    }

    public void cancelarReserva() {
    }
}