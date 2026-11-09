package RoomServDev.persistence.dao;

import RoomServDev.model.Cliente;
import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.model.Reserva;
import RoomServDev.model.Simple;
import RoomServDev.model.Suite;
import RoomServDev.persistence.factory.DAOFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class TestIntegracionDAO {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TEST DE INTEGRACIÓN JDBC + DAO ===");

        try {
            // 1. Obtener la factoría de MySQL
            DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            ClienteDAO clienteDAO = factory.getClienteDAO();
            HotelDAO hotelDAO = factory.getHotelDAO();
            HabitacionDAO habitacionDAO = factory.getHabitacionDAO();
            ReservaDAO reservaDAO = factory.getReservaDAO();

            System.out.println("[OK] Factoría y DAOs instanciados correctamente.");

            // -------------------------------------------------------------
            // TEST 1: Insertar y listar Hotel
            // -------------------------------------------------------------
            System.out.println("\n--- 1. Prueba HotelDAO ---");
            String nombreHotelPrueba = "Hotel Test Marítimo";
            Hotel hotelExistente = hotelDAO.obtenerPorNombres(nombreHotelPrueba);

            if (hotelExistente == null) {
                Hotel nuevoHotel = new Hotel();
                nuevoHotel.setNombre(nombreHotelPrueba);
                nuevoHotel.setDireccion("Paseo Central 100");
                nuevoHotel.setCiudad("Cambrils");
                nuevoHotel.setTelefono(977998877);
                hotelDAO.insertar(nuevoHotel);
                System.out.println("[OK] Hotel insertado correctamente en MySQL.");
            } else {
                System.out.println("[INFO] El hotel de prueba ya existía en la base de datos.");
            }

            Hotel hotelRecuperado = hotelDAO.obtenerPorNombres(nombreHotelPrueba);
            System.out.println("[OK] Hotel recuperado: " + hotelRecuperado.getNombre() + " (" + hotelRecuperado.getCiudad() + ")");

            // -------------------------------------------------------------
            // TEST 2: Insertar y listar Habitaciones polimórficas
            // -------------------------------------------------------------
            System.out.println("\n--- 2. Prueba HabitacionDAO (Polimorfismo) ---");
            // Usamos un idHotel de referencia (el 1 si cargaste data.sql)
            int idHotelRef = 1;

            Simple habSimple = new Simple(101, 1, 75.00, "Simple", "90x200", true);
            Doble habDoble = new Doble(201, 3, 120.00, 2, "Doble", "Matrimonio", true, 25.00, true);
            Suite habSuite = new Suite(301, 4, 220.00, "Suite", 2, true, true, true, "Minibar y terraza");

            habitacionDAO.insertar(habSimple, idHotelRef);
            habitacionDAO.insertar(habDoble, idHotelRef);
            habitacionDAO.insertar(habSuite, idHotelRef);
            System.out.println("[OK] Habitaciones Simple, Doble y Suite insertadas.");

            List<Habitacion> habitaciones = habitacionDAO.obtenerPorHotel(idHotelRef);
            System.out.println("[OK] Habitaciones recuperadas para el hotel " + idHotelRef + ":");
            for (Habitacion h : habitaciones) {
                System.out.println("  - Habitación " + h.getNumHabitacion() +
                        " | Clase: " + h.getClass().getSimpleName() +
                        " | Capacidad: " + h.getCapacidad() +
                        " | Precio: " + h.getPrecioNoche() + "€");
            }

            // -------------------------------------------------------------
            // TEST 3: Cliente y Transacción ACID en Reserva
            // -------------------------------------------------------------
            System.out.println("\n--- 3. Prueba ReservaDAO (Transacción ACID) ---");
            String dniPrueba = "99887766K";
            Cliente cliente = clienteDAO.obtenerPorDni(dniPrueba);
            if (cliente == null) {
                clienteDAO.insertar(new Cliente("Marta", "Pérez", "Vidal", dniPrueba, 699887766, "marta@test.com"));
                System.out.println("[OK] Cliente de prueba registrado.");
            }

            // Creamos una reserva para comprobar que bloquea la habitación
            Date entrada = Date.valueOf(LocalDate.now().plusDays(5));
            Date salida = Date.valueOf(LocalDate.now().plusDays(10));

            Reserva nuevaReserva = new Reserva(
                    0, entrada, entrada, salida, 2, 600.0, true,
                    hotelRecuperado.getNombre(), 101, dniPrueba
            );

            reservaDAO.insertar(nuevaReserva);
            System.out.println("[OK] Reserva transaccional guardada. ID generado: " + nuevaReserva.getIdReserva());

            List<Reserva> reservasCliente = reservaDAO.obtenerPorCliente(dniPrueba);
            System.out.println("[OK] Total reservas encontradas para el cliente " + dniPrueba + ": " + reservasCliente.size());

            System.out.println("\n=== TODOS LOS TESTS SE COMPLETARON CON ÉXITO ===");

        } catch (Exception e) {
            System.err.println("\n[ERROR DURANTE LA PRUEBA]: " + e.getMessage());
            e.printStackTrace();
        }
    }
}