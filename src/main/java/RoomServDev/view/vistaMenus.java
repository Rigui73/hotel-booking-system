package RoomServDev.view;

import RoomServDev.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class vistaMenus {
    private Scanner input = new Scanner(System.in);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // ***********************************************************
    // MENÚS PRINCIPALES (Retornan la opción al Controlador)
    // ***********************************************************
    public char mostrarMenuClientes() {
        System.out.println("\n--- GESTIÓN DE CLIENTES ---");
        System.out.println("1. Listar clientes.");
        System.out.println("2. Añadir cliente.");
        System.out.println("3. Editar cliente.");
        System.out.println("4. Eliminar cliente.");
        System.out.println("0. Volver al menú principal.\n");
        return pedirOpcionMenus();
    }

    public char mostrarMenuHoteles() {
        System.out.println("\n--- GESTIÓN DE HOTELES ---");
        System.out.println("1. Listar hoteles.");
        System.out.println("2. Añadir hotel.");
        System.out.println("3. Editar hotel.");
        System.out.println("4. Eliminar hotel.");
        System.out.println("0. Volver al menú principal.\n");
        return pedirOpcionMenus();
    }

    public char mostrarMenuHabitaciones() {
        System.out.println("\n--- GESTIÓN DE HABITACIONES ---");
        System.out.println("1. Listar habitaciones de un hotel.");
        System.out.println("2. Añadir habitación.");
        System.out.println("0. Volver al menú principal.\n");
        return pedirOpcionMenus();
    }

    public char mostrarMenuReservas() {
        System.out.println("\n--- GESTIÓN DE RESERVAS ---");
        System.out.println("1. Listar reservas.");
        System.out.println("2. Añadir reserva.");
        System.out.println("3. Cancelar reserva.");
        System.out.println("0. Volver al menú principal.\n");
        return pedirOpcionMenus();
    }

    // ***********************************************************
    // CLIENTES
    // ***********************************************************
    public void listarClientes(List<Cliente> listaClientes) {
        System.out.println("--- Listado de clientes ---");
        for (Cliente c : listaClientes) {
            System.out.println(c.toString());
        }
    }

    public Cliente anadirCliente() {
        System.out.println("--- Añadir nuevo cliente ---");
        System.out.print("Nombre: ");
        String nombre = input.nextLine();
        System.out.print("Primer apellido: ");
        String apellido1 = input.nextLine();
        System.out.print("Segundo apellido: ");
        String apellido2 = input.nextLine();
        System.out.print("DNI: ");
        String dni = input.nextLine();
        System.out.print("Teléfono: ");
        int telefono = input.nextInt();
        input.nextLine();
        System.out.print("Correo electrónico: ");
        String correo = input.nextLine();

        return new Cliente(nombre, apellido1, apellido2, dni, telefono, correo);
    }

    public String pedirDni() {
        System.out.print("Introduce el DNI del cliente: ");
        return input.nextLine();
    }

    // ***********************************************************
    // HOTELES
    // ***********************************************************
    public void listarHoteles(List<Hotel> listaHoteles) {
        System.out.println("--- Listado de hoteles ---");
        for (Hotel h : listaHoteles) {
            System.out.println(h.toString());
        }
    }

    public Hotel anadirHotel() {
        System.out.println("--- Añadir nuevo hotel ---");
        System.out.print("Nombre del hotel: ");
        String nombre = input.nextLine();
        System.out.print("Dirección: ");
        String direccion = input.nextLine();
        System.out.print("Ciudad: ");
        String ciudad = input.nextLine();
        System.out.print("Teléfono: ");
        int telefono = input.nextInt();
        input.nextLine();

        return new Hotel(nombre, direccion, ciudad, telefono);
    }

    public String pedirNombreHotel() {
        System.out.print("Introduce el nombre del hotel: ");
        return input.nextLine();
    }

    // ***********************************************************
    // HABITACIONES
    // ***********************************************************
    public void listarHabitaciones(List<Habitacion> listaHabitaciones) {
        System.out.println("--- Listado de habitaciones ---");
        for (Habitacion hab : listaHabitaciones) {
            System.out.println("Habitación " + hab.getNumHabitacion() + " - Categoría: " + hab.getCategoria() + " - Precio: " + hab.getPrecioNoche());
        }
    }

    public Habitacion anadirHabitacion() {
        System.out.println("--- Añadir nueva habitación ---");
        System.out.print("Tipo (1. Simple | 2. Doble | 3. Suite): ");
        int tipo = input.nextInt();
        input.nextLine();

        System.out.print("Número de habitación: ");
        int num = input.nextInt();
        input.nextLine();

        System.out.print("Capacidad máxima: ");
        int capacidad = input.nextInt();
        input.nextLine();

        System.out.print("Precio por noche: ");
        double precio = input.nextDouble();
        input.nextLine();

        switch (tipo) {
            case 1:
                System.out.print("Medida de la cama (ej. 135x190): ");
                String medidaCama = input.nextLine();
                System.out.print("¿Tiene escritorio? (true/false): ");
                boolean escritorio = input.nextBoolean();
                input.nextLine();
                return new Simple(num, capacidad, precio, "Simple", medidaCama, escritorio);

            case 2:
                System.out.print("Tipo de cama doble: ");
                String tipoCamaDoble = input.nextLine();
                System.out.print("¿Admite supletoria? (true/false): ");
                boolean supletoria = input.nextBoolean();
                input.nextLine();
                System.out.print("Suplemento por supletoria: ");
                double suplemento = input.nextDouble();
                input.nextLine();
                System.out.print("¿Tiene balcón? (true/false): ");
                boolean balcon = input.nextBoolean();
                input.nextLine();
                return new Doble(num, capacidad, precio, tipoCamaDoble, supletoria, suplemento, balcon);

            case 3:
                System.out.print("Número de ambientes: ");
                int ambientes = input.nextInt();
                input.nextLine();
                System.out.print("¿Tiene jacuzzi? (true/false): ");
                boolean jacuzzi = input.nextBoolean();
                input.nextLine();
                System.out.print("¿Incluye spa? (true/false): ");
                boolean spa = input.nextBoolean();
                input.nextLine();
                System.out.print("¿Incluye terraza? (true/false): ");
                boolean terraza = input.nextBoolean();
                input.nextLine();
                System.out.print("Otros servicios extra: ");
                String otrosServicios = input.nextLine();
                return new Suite(num, capacidad, precio, "Suite", ambientes, jacuzzi, spa, terraza, otrosServicios);

            default:
                System.out.println("Tipo no válido. Retornando null.");
                return null;
        }
    }

    // ***********************************************************
    // RESERVAS
    // ***********************************************************
    public void listarReservas(List<Reserva> listaReservas) {
        System.out.println("--- Listado de reservas ---");
        for (Reserva r : listaReservas) {
            System.out.println(r.toString());
        }
    }

    public Reserva anadirReserva() {
        try {
            System.out.println("--- Crear nueva reserva ---");
            System.out.print("ID de la reserva: ");
            int id = input.nextInt();
            input.nextLine();

            System.out.print("DNI del cliente: ");
            String dniCliente = input.nextLine();

            System.out.print("Nombre del hotel: ");
            String nombreHotel = input.nextLine();

            System.out.print("Número de la habitación: ");
            int numHabitacion = input.nextInt();
            input.nextLine();

            System.out.print("Fecha de entrada (yyyy-MM-dd): ");
            Date fechaEntrada = dateFormat.parse(input.nextLine());

            System.out.print("Fecha de salida (yyyy-MM-dd): ");
            Date fechaSalida = dateFormat.parse(input.nextLine());

            System.out.print("Número de huéspedes: ");
            int huespedes = input.nextInt();
            input.nextLine();

            // Importe total inicial a 0.0 (el Controlador usará la clase Datos para calcular el precio real)
            return new Reserva(id, new Date(), fechaEntrada, fechaSalida, huespedes, 0.0, true, nombreHotel, numHabitacion, dniCliente);

        } catch (ParseException e) {
            System.out.println("Error de formato en las fechas. Se aborta la creación.");
            return null;
        }
    }

    // ***********************************************************
    // UTILIDADES GLOBALES
    // ***********************************************************
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    private char pedirOpcionMenus() {
        System.out.print("Elige una opción: ");
        String respuesta = input.nextLine();
        return respuesta.isEmpty() ? ' ' : respuesta.charAt(0);
    }
}