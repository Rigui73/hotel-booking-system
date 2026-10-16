package modelo_grupal;

public class Suite extends Habitacion {
    private int numAmbientes;
    private boolean jacuzzi;
    private boolean incluyeSpa;
    private boolean incluyeTerraza;
    private String otrosServs;

    public Suite() {
        super();
    }

    public Suite(int numHabitacion, int capacidadMax, double precioNoche,
                 int numAmbientes, boolean jacuzzi, boolean incluyeSpa,
                 boolean incluyeTerraza, String otrosServs) {
        super(numHabitacion, capacidadMax, precioNoche);
        this.numAmbientes = numAmbientes;
        this.jacuzzi = jacuzzi;
        this.incluyeSpa = incluyeSpa;
        this.incluyeTerraza = incluyeTerraza;
        this.otrosServs = otrosServs;
    }

    public int getNumAmbientes() {
        return numAmbientes;
    }

    public void setNumAmbientes(int numAmbientes) {
        this.numAmbientes = numAmbientes;
    }

    public boolean isJacuzzi() {
        return jacuzzi;
    }

    public void setJacuzzi(boolean jacuzzi) {
        this.jacuzzi = jacuzzi;
    }

    public boolean incluyeSpa() {
        return incluyeSpa;
    }

    public void setIncluyeSpa(boolean incluyeSpa) {
        this.incluyeSpa = incluyeSpa;
    }

    public boolean incluyeTerraza() {
        return incluyeTerraza;
    }

    public void setIncluyeTerraza(boolean incluyeTerraza) {
        this.incluyeTerraza = incluyeTerraza;
    }

    public String getOtrosServs() {
        return otrosServs;
    }

    public void setOtrosServs(String otrosServs) {
        this.otrosServs = otrosServs;
    }

    @Override
    public String toString() {
        return "Suite{" +
                super.toString() +
                ", numAmbientes=" + numAmbientes +
                ", jacuzzi=" + jacuzzi +
                ", incluyeSpa=" + incluyeSpa +
                ", incluyeTerraza=" + incluyeTerraza +
                ", otrosServs='" + otrosServs + '\'' +
                '}';
    }
}