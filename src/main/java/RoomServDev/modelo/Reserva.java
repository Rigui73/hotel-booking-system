package RoomServDev.modelo;

import java.util.Date;


public class Reserva {

// ********* Atributos  *********

    private int idReserva;
    private Date fechaReserva;
    private Date fechaEntrada;
    private Date fechaSalida;
    private int numHuespedes;
    private double importeTotal;
    private boolean estado;
    private String nombreHotel;
    private int numHabitacion;
    private String dniCliente; // importante para relacionar reserva con cliente que hace la reserva

    // ********* Constructor  *********

    public Reserva(int idReserva, Date fechaReserva, Date fechaEntrada,
                   Date fechaSalida, int numHuespedes, double importeTotal, boolean estado,
                   String nombreHotel, int numHabitacion, dniCliente){

        this.idReserva = idReserva;
        this.fechaReserva = fechaReserva;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.numHuespedes = numHuespedes;
        this.importeTotal = importeTotal;
        this.estado = estado;
        this.nombreHotel = nombreHotel;
        this.numHabitacion = numHabitacion;
        this.dniCliente = dniCliente;
        }

    // *********GETTERS y SETTERS*********

    public int getIdReserva() {
        return idReserva;
    }
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }
    public Date getFechaReserva(){
        return fechaReserva;
    }
    public void setFechaReserva(Date fechaReserva){
        this.fechaReserva = fechaReserva;
    }
    public Date getFechaEntrada(){
        return fechaEntrada;
    }
    public void setFechaEntrada(Date fechaEntrada){
        this.fechaEntrada = fechaEntrada;
    }
    public Date getFechaSalida(){
        return fechaSalida;
    }
    public void setFechaSalida(Date fechaSalida){
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

    public String getNombreHotel() {
        return nombreHotel;
    }

    public void setNombreHotel(String nombreHotel) {
        this.nombreHotel = nombreHotel;
    }

    public int getNumHabitacion() {
        return numHabitacion;
    }
    public void setNumHabitacion(int numHabitacion) {
        this.numHabitacion = numHabitacion;
    }


    public String getDniCliente() {
        return dniCliente;
    }
    public void setDniCliente(String dniCliente){
        this.dniCliente = dniCliente;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", fechaReserva=" + fechaReserva +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaSalida= " + fechaSalida +
                ", numHuespedes=" + numHuespedes +
                ", importeTotal=" + importeTotal +
                ", estado=" + estado +
                ", nombreHotel=" + nombreHotel +
                ", numHabitacion=" + numHabitacion +
                ", dniCliente='" + dniCliente + '\'' +
                '}';
    }
}

