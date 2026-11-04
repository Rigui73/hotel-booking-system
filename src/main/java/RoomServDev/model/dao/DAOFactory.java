package RoomServDev.model.dao;

public abstract class DAOFactory {

    public static final int MYSQL = 1;

    public abstract ClienteDAO getClienteDAO();
    public abstract HotelDAO getHotelDAO();
    public abstract HabitacionDAO getHabitacionDAO();
    public abstract ReservaDAO getReservaDAO();

    public static DAOFactory getDAOFactory(int tipo){
        switch (tipo){
            case MYSQL:
                return new MySQLDAOFactory();
                default:
                    throw  new IllegalArgumentException("Motor de base de datos no soportado");
        }
    }
}
