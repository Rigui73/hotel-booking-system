public class Suite extends Habitacion {
    private int numAmbientes;
    private boolean jacuzzi;
    private boolean incluyeSpa;
    private boolean incluyeTerraza;
    private String otrosServs;

    public Suite() {
    }

    public Suite(int n, int n2, double d, int n3, boolean bl, boolean bl2, boolean bl3, String string) {
        super(n, n2, d);
        this.numAmbientes = n3;
        this.jacuzzi = bl;
        this.incluyeSpa = bl2;
        this.incluyeTerraza = bl3;
        this.otrosServs = string;
    }

    public int getNumAmbientes() {
        return this.numAmbientes;
    }

    public void setNumAmbientes(int n) {
        this.numAmbientes = n;
    }

    public boolean isJacuzzi() {
        return this.jacuzzi;
    }

    public void setJacuzzi(boolean bl) {
        this.jacuzzi = bl;
    }

    public boolean incluyeSpa() {
        return this.incluyeSpa;
    }

    public void setIncluyeSpa(boolean bl) {
        this.incluyeSpa = bl;
    }

    public boolean incluyeTerraza() {
        return this.incluyeTerraza;
    }

    public void setIncluyeTerraza(boolean bl) {
        this.incluyeTerraza = bl;
    }

    public String getOtrosServs() {
        return this.otrosServs;
    }

    public void setOtrosServs(String string) {
        this.otrosServs = string;
    }

    @Override
    public String toString() {
        return "Suite{" + super.toString() + ", numAmbientes=" + this.numAmbientes + ", jacuzzi=" + this.jacuzzi + ", incluyeSpa=" + this.incluyeSpa + ", incluyeTerraza=" + this.incluyeTerraza + ", otrosServs='" + this.otrosServs + "'}";
    }
}
