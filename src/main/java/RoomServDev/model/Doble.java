package RoomServDev.model;

//*********«extends» de Habitacion*********

public class Doble extends Habitacion {

    //*********atributos específicos de Doble*********

    private String tipoCamaDoble;
    private boolean supletoria;
    private double suplementSupletoria;
    private boolean balcon;

    //*********Constructor*********

    public Doble(int numHabitacion, int capacidadMax, double precioNoche,
                 String tipoCamaDoble, boolean supletoria, double suplementSupletoria, boolean balcon) {
        //*********invocamos constructor de la clase padre*********

        super(numHabitacion, capacidadMax, precioNoche, true, "Doble");

        // *********inicializamos los atributos propios de la subclase*********
        this.tipoCamaDoble = tipoCamaDoble;
        this.supletoria = supletoria;
        this.suplementSupletoria = suplementSupletoria;
        this.balcon = balcon;
    }

    // ********* GETTERS y SETTERS específicos de Doble *********

    public String getTipoCamaDoble() {
        return tipoCamaDoble;
    }

    public void setTipoCamaDoble(String tipoCamaDoble) {
        this.tipoCamaDoble = tipoCamaDoble;
    }

    public boolean isSupletoria() {
        return supletoria;
    }

    public void setSupletoria(boolean supletoria) {
        this.supletoria = supletoria;
    }

    public double getSuplementSupletoria() {
        return suplementSupletoria;
    }

    public void setSuplementSupletoria(double suplementSupletoria) {
        this.suplementSupletoria = suplementSupletoria;
    }

    public boolean isBalcon() {
        return balcon;
    }

    public void setBalcon(boolean balcon) {
        this.balcon = balcon;
    }
}