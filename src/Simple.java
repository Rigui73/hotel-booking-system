public class Simple
extends Habitacion {
    private String medidaCama;
    private boolean tieneEscritorio;

    public Simple() {
    }

    public Simple(int n, int n2, double d, String string, boolean bl) {
        super(n, n2, d);
        this.medidaCama = string;
        this.tieneEscritorio = bl;
    }

    public String getMedidaCama() {
        return this.medidaCama;
    }

    public void setMedidaCama(String string) {
        this.medidaCama = string;
    }

    public boolean isTieneEscritorio() {
        return this.tieneEscritorio;
    }

    public void setTieneEscritorio(boolean bl) {
        this.tieneEscritorio = bl;
    }

    @Override
    public String toString() {
        return "Simple{" + super.toString() + ", medidaCama='" + this.medidaCama + "', tieneEscritorio=" + this.tieneEscritorio + "}";
    }
}
