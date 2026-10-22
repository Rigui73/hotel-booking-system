package RoomServDev;

import RoomServDev.controller.ControladorClientes;
import RoomServDev.controller.ControladorHabitaciones;
import RoomServDev.controller.ControladorHoteles;
import RoomServDev.controller.ControladorReservas;
import RoomServDev.model.Datos;
import RoomServDev.model.Hotel;
import RoomServDev.view.vistaMenus;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        // 1. Un único objeto Datos para toda la aplicación
        Datos datos = new Datos();

        // 2. Los controladores comparten ese mismo Datos
        ControladorClientes ctrlClientes = new ControladorClientes(datos);
        ControladorHoteles ctrlHoteles = new ControladorHoteles(datos);
        ControladorHabitaciones ctrlHabitaciones = new ControladorHabitaciones(datos);
        ControladorReservas ctrlReservas = new ControladorReservas(
                datos, ctrlClientes, ctrlHoteles, ctrlHabitaciones);

        // 3. Datos de ejemplo para no empezar con la aplicación vacía
        cargarDatosEjemplo(ctrlClientes, ctrlHoteles, ctrlHabitaciones, ctrlReservas);

        // 4. La vista recibe los controladores y arranca el menú
        vistaMenus vista = new vistaMenus(ctrlClientes, ctrlHoteles, ctrlHabitaciones, ctrlReservas);

        System.out.println("========================================================");
        System.out.println(" GESTOR DE RESERVAS DE HOTELES - By: ROOM SERVICE DEV");
        System.out.println("========================================================");

        vista.mostrarMenuPrincipal();
    }

    private static void cargarDatosEjemplo(ControladorClientes ctrlClientes,
                                           ControladorHoteles ctrlHoteles,
                                           ControladorHabitaciones ctrlHabitaciones,
                                           ControladorReservas ctrlReservas) {
        try {
            // Cliente
            ctrlClientes.crearCliente("Adria", "Rigall", "Prat", "12345678A",
                    612345678, "arigallp@uoc.edu");

            // Hotel
            ctrlHoteles.crearHotel("Hotel Costa Brava", "Carrer Major, 15", "Roses", 972123456);
            Hotel hotel = ctrlHoteles.buscarHotel("Hotel Costa Brava");

            // Habitaciones
            ctrlHabitaciones.crearHabitacionSimple(hotel, 101, 1, 75.00, true, "90x200", true);
            ctrlHabitaciones.crearHabitacionDoble(hotel, 201, 3, 120.00, true, "Matrimonio",
                    true, 25.00, true);
            ctrlHabitaciones.crearHabitacionSuite(hotel, 301, 4, 220.00, true, 2,
                    true, true, true, "Minibar y servicio de habitaciones");

            // Reserva (fechas relativas a hoy, porque no se admiten reservas en el pasado)
            LocalDate entrada = LocalDate.now().plusDays(7);
            ctrlReservas.crearReserva("12345678A", "Hotel Costa Brava", 201,
                    entrada, entrada.plusDays(3), 3);

        } catch (Exception e) {
            System.out.println("Error al cargar los datos de ejemplo: " + e.getMessage());
        }
    }
}
