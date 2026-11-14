package RoomServDev;

import RoomServDev.controller.ControladorClientes;
import RoomServDev.controller.ControladorHabitaciones;
import RoomServDev.controller.ControladorHoteles;
import RoomServDev.controller.ControladorReservas;
import RoomServDev.persistence.factory.DAOFactory;
import RoomServDev.view.VistaClientes;
import RoomServDev.view.VistaHabitaciones;
import RoomServDev.view.VistaHoteles;
import RoomServDev.view.VistaMenus;
import RoomServDev.view.VistaReservas;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

        VistaMenus vistaGeneral = new VistaMenus(input);
        VistaClientes vistaClientes = new VistaClientes(input);
        VistaHoteles vistaHoteles = new VistaHoteles(input);
        VistaHabitaciones vistaHabitaciones = new VistaHabitaciones(input);
        VistaReservas vistaReservas = new VistaReservas(input);

        ControladorClientes clientes = new ControladorClientes(vistaClientes, factory.getClienteDAO());
        ControladorHoteles hoteles = new ControladorHoteles(factory.getHotelDAO(), factory.getHabitacionDAO());
        ControladorHabitaciones habitaciones = new ControladorHabitaciones(
                factory.getHabitacionDAO(), factory.getReservaDAO());
        ControladorReservas reservas = new ControladorReservas(
                factory.getReservaDAO(), clientes, hoteles, habitaciones);

        ejecutar(vistaGeneral, vistaHoteles, vistaHabitaciones, vistaReservas,
                clientes, hoteles, habitaciones, reservas);
    }

    private static void ejecutar(VistaMenus vista, VistaHoteles vistaHoteles,
                                 VistaHabitaciones vistaHabitaciones, VistaReservas vistaReservas,
                                 ControladorClientes clientes,
                                 ControladorHoteles hoteles, ControladorHabitaciones habitaciones,
                                 ControladorReservas reservas) {
        boolean salir = false;
        while (!salir) {
            switch (vista.mostrarMenuPrincipal()) {
                case '1' -> clientes.gestionarMenu();
                case '2' -> hoteles.gestionarMenu(vistaHoteles);
                case '3' -> habitaciones.gestionarMenu(vistaHabitaciones, hoteles);
                case '4' -> reservas.gestionarMenu(vistaReservas);
                case '0' -> {
                    vista.mostrarMensaje("Saliendo del sistema...");
                    salir = true;
                }
                default -> vista.mostrarError("Opción no válida.");
            }
        }
    }
}
