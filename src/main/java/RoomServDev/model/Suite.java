package RoomServDev.model;

public class Suite extends Habitacion{

    //*********Atributos específicos de Suite*********

    private int numAmbientes;
    private boolean jacuzzi;
    private boolean incluyeSpa;
    private boolean incluyeTerraza;
    private String otrosServs;

    //*********Constructor*********

    public Suite(int numHabitacion, int capacidad, double precioNoche, String categoria,
                 int numAmbientes, boolean jacuzzi, boolean incluyeSpa,
                 boolean incluyeTerraza, String otrosServs){

        //*********Invocamos constructor padre*********

        super(numHabitacion, capacidad, precioNoche, true, categoria);

        this.numAmbientes = numAmbientes;
        this.jacuzzi = jacuzzi;
        this.incluyeSpa = incluyeSpa;
        this.incluyeTerraza = incluyeTerraza;
        this.otrosServs = otrosServs;
    }

    // ********* GETTERS y SETTERS específicos de Suite *********

    public int getNumAmbientes(){
        return numAmbientes;
    }

    public void setNumAmbientes(int numAmbientes) {
        this.numAmbientes = numAmbientes;
    }

    public boolean isJacuzzi(){
        return jacuzzi;
    }

    public void setJacuzzi(boolean jacuzzi) {
        this.jacuzzi = jacuzzi;
    }

    public boolean isIncluyeSpa(){
        return incluyeSpa;
    }

    public void setIncluyeSpa(boolean incluyeSpa) {
        this.incluyeSpa = incluyeSpa;
    }

    public boolean isIncluyeTerraza(){
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
}