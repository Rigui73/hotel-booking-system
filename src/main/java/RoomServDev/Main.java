package RoomServDev;

import java.time.LocalDate;
import java.util.Scanner;

import RoomServDev.controller.ControladorClientes;
import RoomServDev.controller.ControladorHabitaciones;
import RoomServDev.controller.ControladorHoteles;
import RoomServDev.controller.ControladorReservas;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.persistence.dao.ClienteDAO;
import RoomServDev.persistence.dao.HabitacionDAO;
import RoomServDev.model.Reserva;
import RoomServDev.persistence.dao.HotelDAO;
import RoomServDev.persistence.dao.ReservaDAO;
import RoomServDev.persistence.factory.DAOFactory;
import RoomServDev.view.VistaCliente;
import RoomServDev.view.VistaHotel;
import RoomServDev.view.vistaMenus;

public class Main {

    private final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Main app = new Main();
        app.programa();
    }

    void programa() {

        // Creamos objeto que implementa la interfaz
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        HotelDAO hotelDAO = factory.getHotelDAO();
        ClienteDAO clienteDAO = factory.getClienteDAO();
        HabitacionDAO habitacionDAO = factory.getHabitacionDAO();
        ReservaDAO reservaDAO = factory.getReservaDAO();

        // 3. Instanciamos vistas y controladores

        VistaCliente vistaCliente = new VistaCliente(input);
        ControladorClientes controladorClientes = new ControladorClientes(vistaCliente, clienteDAO);
        ControladorHoteles controladorHoteles = new ControladorHoteles(hotelDAO, habitacionDAO);
        ControladorHabitaciones controladorHabitaciones = new ControladorHabitaciones(habitacionDAO, reservaDAO);
        ControladorReservas controladorReservas = new ControladorReservas(reservaDAO, controladorClientes, controladorHoteles, controladorHabitaciones);

        // Vista de menús provisional para el resto de módulos aún no migrados
        vistaMenus vistaGeneral = new vistaMenus(input);

        boolean salir = false;

        do {
            System.out.println("\n========================================================");
            System.out.println("GESTOR DE RESERVAS DE HOTELES - By: ROOM SERVICE DEV");
            System.out.println("========================================================");
            System.out.println("1. Clientes");
            System.out.println("2. Hoteles");
            System.out.println("3. Habitaciones");
            System.out.println("4. Reservas");
            System.out.println("0. Salir de la aplicación.");
            System.out.println("========================================================");

            char opcion = pedirOpcionMenus();

            switch (opcion) {
                case '1':
                    // Delegamos el ciclo completo de clientes a su controlador
                    controladorClientes.gestionarMenu();
                    break;
                case '2':
                    ejecutarSubmenuHoteles(vistaGeneral, controladorHoteles);
                    break;
                case '3':
                    ejecutarSubmenuHabitaciones(vistaGeneral, controladorHoteles, controladorHabitaciones);
                    break;
                case '4':
                    ejecutarSubmenuReservas(vistaGeneral, controladorReservas);
                    break;
                case '0':
                    System.out.println("[INFO] Saliendo del sistema...");
                    salir = true;
                    break;
                default:
                    System.err.println("[ERROR] Opción no válida.");
            }

        } while (!salir);
    }

    private char pedirOpcionMenus() {
        System.out.print("Elige una opción: ");
        String respuesta = input.nextLine().trim();
        return respuesta.isEmpty() ? ' ' : respuesta.charAt(0);
    }

    // --- BUCLES TEMPORALES MIENTRAS SE MIGRA AL RESPECTIVO CONTROLADOR ---
    private void ejecutarSubmenuHoteles(vistaMenus vista, ControladorHoteles controlador) {
        boolean volver = false;
        do {
            char op = vista.mostrarMenuHoteles();
            try {
                switch (op) {
                    case '1' -> vista.listarHoteles(controlador.listarHoteles());
                    case '2' -> {
                        Hotel nuevo = vista.anadirHotel();
                        controlador.crearHotel(nuevo.getNombre(), nuevo.getDireccion(), nuevo.getCiudad(), nuevo.getTelefono());
                        vista.mostrarMensaje("Hotel añadido correctamente.");
                    }
                    case '3' -> {
                        String nombre = vista.pedirNombreHotel();
                        String direccion = vista.pedirTexto("Introduce la nueva dirección: ");
                        String ciudad = vista.pedirTexto("Introduce la nueva ciudad: ");
                        int telefono = vista.pedirEntero("Introduce el nuevo teléfono: ");
                        controlador.editarHotel(nombre, direccion, ciudad, telefono);
                        vista.mostrarMensaje("Hotel editado correctamente.");
                    }
                    case '4' -> {
                        controlador.eliminarHotel(vista.pedirNombreHotel());
                        vista.mostrarMensaje("Hotel eliminado correctamente.");
                    }
                    case '0' -> volver = true;
                    default -> vista.mostrarError("Opción no válida.");
                }
            } catch (Exception e) {
                vista.mostrarError(e.getMessage());
            }
        } while (!volver);
    }

    private void ejecutarSubmenuHabitaciones(vistaMenus vista, ControladorHoteles controladorHoteles,
                                             ControladorHabitaciones controladorHabitaciones) {
        boolean volver = false;
        do {
            char op = vista.mostrarMenuHabitaciones();
            try {
                switch (op) {
                    case '1' -> {
                        Hotel hotel = buscarHotelObligatorio(vista, controladorHoteles);
                        if (hotel != null) {
                            vista.listarHabitaciones(controladorHabitaciones.listarHabitaciones(hotel));
                        }
                    }
                    case '2' -> {
                        Hotel hotel = buscarHotelObligatorio(vista, controladorHoteles);
                        if (hotel != null) {
                            Habitacion habitacion = vista.anadirHabitacion();
                            controladorHabitaciones.guardarHabitacion(hotel, habitacion);
                            vista.mostrarMensaje("Habitación añadida al hotel '" + hotel.getNombre() + "'.");
                        }
                    }
                    case '3' -> {
                        Hotel hotel = buscarHotelObligatorio(vista, controladorHoteles);
                        if (hotel != null) {
                            int numero = vista.pedirNumHabitacion();
                            int capacidad = vista.pedirEntero("Nueva capacidad: ");
                            double precio = vista.pedirPrecio();
                            boolean disponible = vista.pedirBooleano("¿Está disponible? (true/false): ");
                            controladorHabitaciones.editarHabitacion(hotel, numero, capacidad, precio, disponible);
                            vista.mostrarMensaje("Habitación modificada correctamente.");
                        }
                    }
                    case '4' -> {
                        Hotel hotel = buscarHotelObligatorio(vista, controladorHoteles);
                        if (hotel != null) {
                            controladorHabitaciones.eliminarHabitacion(hotel, vista.pedirNumHabitacion());
                            vista.mostrarMensaje("Habitación eliminada correctamente.");
                        }
                    }
                    case '0' -> volver = true;
                    default -> vista.mostrarError("Opción no válida.");
                }
            } catch (Exception e) {
                vista.mostrarError(e.getMessage());
            }
        } while (!volver);
    }

    private void ejecutarSubmenuReservas(vistaMenus vista, ControladorReservas controlador) {
        boolean volver = false;
        do {
            char op = vista.mostrarMenuReservas();
            try {
                switch (op) {
                    case '1' -> vista.listarReservas(controlador.listarReservas());
                    case '2' -> {
                        String dni = vista.pedirDni();
                        String hotel = vista.pedirNombreHotel();
                        int habitacion = vista.pedirNumHabitacion();
                        int huespedes = vista.pedirEntero("Introduce el número de huéspedes: ");
                        LocalDate entrada = vista.pedirFecha("Fecha de entrada");
                        LocalDate salida = vista.pedirFecha("Fecha de salida");
                        Reserva reserva = controlador.crearReserva(dni, hotel, habitacion, entrada, salida, huespedes);
                        vista.mostrarMensaje("Reserva confirmada. Id: " + reserva.getIdReserva()
                                + ". Importe: " + reserva.getImporteTotal() + "€");
                    }
                    case '3' -> {
                        int id = vista.pedirEntero("Id de la reserva: ");
                        LocalDate entrada = vista.pedirFecha("Nueva fecha de entrada");
                        LocalDate salida = vista.pedirFecha("Nueva fecha de salida");
                        int huespedes = vista.pedirEntero("Nuevo número de huéspedes: ");
                        controlador.editarReserva(id, entrada, salida, huespedes);
                        vista.mostrarMensaje("Reserva modificada correctamente.");
                    }
                    case '4' -> {
                        int id = vista.pedirEntero("Id de la reserva: ");
                        String modo = vista.pedirTexto("Escribe C para cancelar o E para eliminar: ");
                        if (modo.equalsIgnoreCase("E")) {
                            controlador.eliminarReserva(id);
                            vista.mostrarMensaje("Reserva eliminada. Si estaba activa, la habitación vuelve a estar libre.");
                        } else if (modo.equalsIgnoreCase("C")) {
                            controlador.cancelarReserva(id);
                            vista.mostrarMensaje("Reserva cancelada. La habitación vuelve a estar libre.");
                        } else {
                            vista.mostrarError("Indica C para cancelar o E para eliminar.");
                        }
                    }
                    case '0' -> volver = true;
                    default -> vista.mostrarError("Opción no válida.");
                }
            } catch (Exception e) {
                vista.mostrarError(e.getMessage());
            }
        } while (!volver);
    }

    private Hotel buscarHotelObligatorio(vistaMenus vista, ControladorHoteles controlador) throws Exception {
        String nombre = vista.pedirNombreHotel();
        Hotel hotel = controlador.buscarHotel(nombre);
        if (hotel == null) {
            vista.mostrarError("No existe ningún hotel con ese nombre.");
        }
        return hotel;
    }
}