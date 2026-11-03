package RoomServDev.model.dao;

public class MySQLDAOFactory extends DAOFactory {
    @Override
    public ClienteDAO getClienteDAO() {
        return new ClienteDAOImpl();
    }

    /* TODO

    @Override
    public HotelDAO getHotelDAO() {
        return new HotelDAOImpl();
    }

    @Override
    public HabitacionDAO getHabitacionDAO() {
        return new HabitacionDAOImpl();
    }

    @Override
    public ReservaDAO getReservaDAO() {
        return new ReservaDAOImpl();
    }

    */
}
