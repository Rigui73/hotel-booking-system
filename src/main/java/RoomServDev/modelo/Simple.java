package RoomServDev.modelo;

public class Simple extends Habitacion {

    //*********Atributos específicos de Simple*********

    private String medidaCama;
    private boolean tieneEscritorio;

    //*********Constructor*********

    public Simple(int numHabitacion, int capacidad, double precioNoche,
                  String categoria, String medidaCama, boolean tieneEscritorio) {

        //*********Invocamos constructor padre*********

        super(numHabitacion,capacidad,precioNoche, categoria);

        //inicializamos atributos propios
        this.medidaCama = medidaCama;
        this.tieneEscritorio = tieneEscritorio;
    }

    // ********* GETTERS y SETTERS específicos de Simple *********

    public String getMedidaCama(){
        return medidaCama;
    }

    public void setMedidaCama(String medidaCama){
        this.medidaCama = medidaCama;
    }
    public boolean isTieneEscritorio(){
        return tieneEscritorio;
    }
    public void setTieneEscritorio(boolean tieneEscritorio){
        this.tieneEscritorio = tieneEscritorio;
    }
}
