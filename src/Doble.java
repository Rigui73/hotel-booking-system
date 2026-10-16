public class Doble extends Habitacion {
    private String tipoCamaDoble;
    private boolean supletoria;
    private double suplementSupletoria;
    private boolean balcon;

    public Doble() {
    }

    public Doble(int n, int n2, double d, String string, boolean bl, double d2, boolean bl2) {
        super(n, n2, d);
        this.tipoCamaDoble = string;
        this.supletoria = bl;
        this.suplementSupletoria = d2;
        this.balcon = bl2;
    }

    public String getTipoCamaDoble() {
        return this.tipoCamaDoble;
    }

    public void setTipoCamaDoble(String string) {
        this.tipoCamaDoble = string;
    }

    public boolean isSupletoria() {
        return this.supletoria;
    }

    public void setSupletoria(boolean bl) {
        this.supletoria = bl;
    }

    public double getSuplementSupletoria() {
        return this.suplementSupletoria;
    }

    public void setSuplementSupletoria(double d) {
        this.suplementSupletoria = d;
    }

    public boolean isBalcon() {
        return this.balcon;
    }

    public void setBalcon(boolean bl) {
        this.balcon = bl;
    }

    @Override
    public String toString() {
        return "Doble{" + super.toString() + ", tipoCamaDoble='" + this.tipoCamaDoble + "', supletoria=" + this.supletoria + ", suplementSupletoria=" + this.suplementSupletoria + ", balcon=" + this.balcon + "}";
    }
}
