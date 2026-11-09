package RoomServDev.persistence.factory;

import RoomServDev.persistence.dao.ClienteDAO;
import RoomServDev.persistence.dao.HabitacionDAO;
import RoomServDev.persistence.dao.HotelDAO;
import RoomServDev.persistence.dao.ReservaDAO;
import RoomServDev.persistence.dao.mysql.ClienteDAOImpl;
import RoomServDev.persistence.dao.mysql.HabitacionDAOImpl;
import RoomServDev.persistence.dao.mysql.HotelDAOImpl;
import RoomServDev.persistence.dao.mysql.ReservaDAOImpl;

public class MySQLDAOFactory extends DAOFactory {
    @Override
    public ClienteDAO getClienteDAO() {
        return new ClienteDAOImpl();
    }


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
}
