package RoomServDev;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Scanner;

import RoomServDev.controller.ControladorClientes;
import RoomServDev.model.*;
import RoomServDev.model.dao.ClienteDAO;
import RoomServDev.model.dao.ClienteDAOImpl;
import RoomServDev.view.VistaCliente;
import RoomServDev.view.vistaMenus;

public class Main {

    private final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Main app = new Main();
        app.programa();
    }

    void programa() {
        // 1. Instanciamos el modelo central de datos
        Datos datos = new Datos();

        // 2. Precargamos los datos iniciales de prueba en el modelo
        cargarDatosPrueba(datos);

        // Creamos objeto que implementa la interfaz

        ClienteDAO miClienteDAO = new ClienteDAOImpl();

        // 3. Instanciamos vistas y controladores

        VistaCliente vistaCliente = new VistaCliente(input);
        ControladorClientes controladorClientes = new ControladorClientes(vistaCliente, miClienteDAO);

        // Vista de menús provisional para el resto de módulos aún no migrados
        vistaMenus vistaGeneral = new vistaMenus(input);

        boolean salir = false;

        do {
            System.out.println("\n========================================================");
            System.out.println(" GESTOR DE RESERVAS DE HOTELES - By: ROOM SERVICE DEV");
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
                    ejecutarSubmenuHoteles(vistaGeneral, datos);
                    break;
                case '3':
                    ejecutarSubmenuHabitaciones(vistaGeneral, datos);
                    break;
                case '4':
                    ejecutarSubmenuReservas(vistaGeneral, datos);
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

    // --- CARGA DE DATOS EN MEMORIA ---
    private void cargarDatosPrueba(Datos datos) {
        try {
            // Cliente inicial
            datos.anadirCliente(new Cliente(
                    "Adria", "Rigall", "Prat",
                    "12345678A", 612345678, "arigallp@uoc.edu"
            ));

            // Hotel inicial con sus habitaciones
            Hotel hotel = new Hotel();
            hotel.setNombre("Hotel Central");
            hotel.setCiudad("Barcelona");
            hotel.setDireccion("Gran Via 123");
            hotel.setTelefono(933001122);

            Simple habSimple = new Simple(101, 1, 75.00, "Individual", "90x200", true);
            Doble habDoble = new Doble(201, 3, 120.00, "Matrimonio", true, 25.00, true);
            Suite suite = new Suite(301, 4, 220.00, "Suite", 2, true, true, true, "Minibar y terraza");

            hotel.getHabitaciones().add(habSimple);
            hotel.getHabitaciones().add(habDoble);
            hotel.getHabitaciones().add(suite);

            datos.anadirHotel(hotel);

        } catch (Exception e) {
            System.err.println("[AVISO] Error cargando datos iniciales: " + e.getMessage());
        }
    }

    // --- BUCLES TEMPORALES MIENTRAS SE MIGRA AL RESPECTIVO CONTROLADOR ---
    private void ejecutarSubmenuHoteles(vistaMenus vista, Datos datos) {
        boolean volver = false;
        do {
            char op = vista.mostrarMenuHoteles();
            switch (op) {
                case '1' -> vista.listarHoteles(datos.listarHoteles());
                case '2' -> {
                    try {
                        datos.anadirHotel(vista.anadirHotel());
                        vista.mostrarMensaje("Hotel añadido correctamente.");
                    } catch (Exception e) {
                        vista.mostrarError(e.getMessage());
                    }
                }
                case '3' -> {
                    vista.editarHotel();
                    // Aquí conectaremos la modificación cuando definamos ControladorHotel
                }
                case '4' -> {
                    String nombreHotel = vista.pedirNombreHotel();
                    try {
                        datos.eliminarHotel(nombreHotel);
                        vista.mostrarMensaje("Hotel eliminado con éxito.");
                    } catch (Exception e) {
                        vista.mostrarError(e.getMessage());
                    }
                }
                case '0' -> volver = true;
                default -> vista.mostrarError("Opción no válida.");
            }
        } while (!volver);
    }

    private void ejecutarSubmenuHabitaciones(vistaMenus vista, Datos datos) {
        boolean volver = false;
        do {
            char op = vista.mostrarMenuHabitaciones();
            switch (op) {
                case '1' -> {
                    String h = vista.pedirNombreHotel();
                    for (Hotel hotel : datos.listarHoteles()) {
                        if (hotel.getNombre().equalsIgnoreCase(h)) {
                            vista.listarHabitaciones(hotel.getHabitaciones());
                            break;
                        }
                    }
                }
                case '0' -> volver = true;
                default -> vista.mostrarError("Opción no válida.");
            }
        } while (!volver);
    }

    private void ejecutarSubmenuReservas(vistaMenus vista, Datos datos) {
        boolean volver = false;
        do {
            char op = vista.mostrarMenuReservas();
            switch (op) {
                case '1' -> vista.listarReservas(datos.listarReservas());
                case '2' -> {
                    datos.anadirReservas(vista.anadirReserva());
                    vista.mostrarMensaje("Reserva registrada.");
                }
                case '0' -> volver = true;
                default -> vista.mostrarError("Opción no válida.");
            }
        } while (!volver);
    }
}