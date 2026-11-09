
package RoomServDev;

import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.model.Simple;
import RoomServDev.model.Suite;
import RoomServDev.persistence.dao.HabitacionDAO;
import RoomServDev.persistence.dao.HotelDAO;
import RoomServDev.persistence.factory.DAOFactory;

public class TestHabitacionDAO {

    public static void main(String[] args) {

        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

        HabitacionDAO habitacionDAO = factory.getHabitacionDAO();
        HotelDAO hotelDAO = factory.getHotelDAO();
        int simpleId = 0;
        int dobleId = 0;
        int suiteId = 0;
        Hotel hotel = new Hotel();

        try {
            // Crear un hotel temporal para la prueba.
            hotel.setNombre("Hotel prueba DAO " + System.currentTimeMillis());
            hotel.setDireccion("Calle de prueba 1");
            hotel.setCiudad("Roses");
            hotel.setTelefono(972000000);

            hotelDAO.insertar(hotel);

            int idHotel = hotel.getIdHotel();

            System.out.println("Hotel de prueba creado: " + idHotel);

            // Crear una habitación Simple.
            Simple simple = new Simple(
                    901, 1, 50.00, "Simple", "90x200", true);

            // Crear una habitación Doble.
            Doble doble = new Doble(
                    902, 2, 80.00, 2, "Doble",
                    "Matrimonio", true, 25.00, true);

            // Crear una Suite.
            Suite suite = new Suite(
                    903, 4, 150.00, "Suite",
                    2, true, true, true, "Minibar y vistas");

            // Insertar las tres habitaciones.
            habitacionDAO.insertar(simple, idHotel);
            simpleId = simple.getIdHabitacion();
            habitacionDAO.insertar(doble, idHotel);
            dobleId = doble.getIdHabitacion();
            habitacionDAO.insertar(suite, idHotel);
            suiteId = suite.getIdHabitacion();

            System.out.println("\nHabitaciones insertadas correctamente.");

            // Recuperar las habitaciones desde MySQL.
            Habitacion simpleRecuperada = habitacionDAO.obtenerPorId(simple.getIdHabitacion());

            Habitacion dobleRecuperada = habitacionDAO.obtenerPorId(doble.getIdHabitacion());

            Habitacion suiteRecuperada = habitacionDAO.obtenerPorId(suite.getIdHabitacion());

            // Comprobar los atributos de Simple.
            if (simpleRecuperada instanceof Simple s) {
                System.out.println("\n--- HABITACIÓN SIMPLE ---");
                System.out.println("ID: " + s.getIdHabitacion());
                System.out.println("Cama: " + s.getMedidaCama());
                System.out.println("Escritorio: " + s.isTieneEscritorio());
            }

            // Comprobar los atributos de Doble.
            if (dobleRecuperada instanceof Doble d) {
                System.out.println("\n--- HABITACIÓN DOBLE ---");
                System.out.println("ID: " + d.getIdHabitacion());
                System.out.println("Tipo de cama: " + d.getTipoCamaDoble());
                System.out.println("Supletoria: " + d.isSupletoria());
                System.out.println("Suplemento: " + d.getSuplementSupletoria());
                System.out.println("Balcón: " + d.isBalcon());
            }

            // Comprobar los atributos de Suite.
            if (suiteRecuperada instanceof Suite s) {
                System.out.println("\n--- SUITE ---");
                System.out.println("ID: " + s.getIdHabitacion());
                System.out.println("Ambientes: " + s.getNumAmbientes());
                System.out.println("Jacuzzi: " + s.isJacuzzi());
                System.out.println("Spa: " + s.isIncluyeSpa());
                System.out.println("Terraza: " + s.isIncluyeTerraza());
                System.out.println("Otros servicios: " + s.getOtrosServs());
            }

            // Comprobar la consulta de habitaciones por hotel.
            System.out.println("\nHabitaciones del hotel: "
                    + habitacionDAO.obtenerPorHotel(idHotel).size());

            System.out.println("\nPRUEBA COMPLETADA CORRECTAMENTE.");

        } catch (Exception e) {
            System.out.println("ERROR EN LA PRUEBA:");
            e.printStackTrace();

        } finally {
            try {
                if (simpleId > 0)
                    habitacionDAO.eliminar(simpleId);
                if (dobleId > 0)
                    habitacionDAO.eliminar(dobleId);
                if (suiteId > 0)
                    habitacionDAO.eliminar(suiteId);

                if (hotel.getIdHotel() > 0) {
                    hotelDAO.eliminar(hotel.getIdHotel());
                }

                System.out.println("\nRegistros temporales eliminados.");

            } catch (Exception e) {
                System.out.println(
                        "No se pudieron eliminar todos los registros temporales.");
                e.printStackTrace();
            }
        }
    }
}
