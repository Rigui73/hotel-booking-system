package modelo_grupal;

public class Simple extends Habitacion {
    private String medidaCama;
    private boolean tieneEscritorio;

    public Simple() {
        super();
    }

    public Simple(int numHabitacion, int capacidadMax, double precioNoche,
                  String medidaCama, boolean tieneEscritorio) {
        super(numHabitacion, capacidadMax, precioNoche);
        this.medidaCama = medidaCama;
        this.tieneEscritorio = tieneEscritorio;
    }

    public String getMedidaCama() {
        return medidaCama;
    }

    public void setMedidaCama(String medidaCama) {
        this.medidaCama = medidaCama;
    }

    public boolean isTieneEscritorio() {
        return tieneEscritorio;
    }

    public void setTieneEscritorio(boolean tieneEscritorio) {
        this.tieneEscritorio = tieneEscritorio;
    }

    @Override
    public String toString() {
        return "Simple{" +
                super.toString() +
                ", medidaCama='" + medidaCama + '\'' +
                ", tieneEscritorio=" + tieneEscritorio +
                '}';
    }
}