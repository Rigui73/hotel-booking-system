package modelo_grupal;

public class Doble extends Habitacion {
    private String tipoCamaDoble;
    private boolean supletoria;
    private double suplementSupletoria;
    private boolean balcon;

    public Doble() {
        super();
    }

    public Doble(int numHabitacion, int capacidadMax, double precioNoche,
                 String tipoCamaDoble, boolean supletoria, double suplementSupletoria, boolean balcon) {
        super(numHabitacion, capacidadMax, precioNoche);
        this.tipoCamaDoble = tipoCamaDoble;
        this.supletoria = supletoria;
        this.suplementSupletoria = suplementSupletoria;
        this.balcon = balcon;
    }

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

    @Override
    public String toString() {
        return "Doble{" +
                super.toString() +
                ", tipoCamaDoble='" + tipoCamaDoble + '\'' +
                ", supletoria=" + supletoria +
                ", suplementSupletoria=" + suplementSupletoria +
                ", balcon=" + balcon +
                '}';
    }
}