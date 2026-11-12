package RoomServDev.view;

import RoomServDev.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class vistaMenus {
    private final Scanner input;

    public vistaMenus() {
        this.input = new Scanner(System.in);
    }

    public vistaMenus(Scanner scanner) {
        this.input = scanner;
    }

    // ==========================================
    // CANALES DE INFORMACIÓN Y ERROR
    // ==========================================
    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("[ERROR] " + error);
    }

    // ==========================================
    // PETICIONES BÁSICAS DE IDENTIFICADORES
    // ==========================================
    public String pedirDni() {
        System.out.print("Introduce el DNI del cliente: ");
        return input.nextLine().trim();
    }

    public String pedirNombreHotel() {
        System.out.print("Introduce el nombre del hotel: ");
        return input.nextLine().trim();
    }

    public char pedirOpcionMenus() {
        System.out.print("Elige una opción: ");
        String respuesta = input.nextLine().trim();
        return respuesta.isEmpty() ? ' ' : respuesta.charAt(0);
    }

    public String pedirTexto(String mensaje){
        System.out.print(mensaje);
        return input.nextLine().trim();
    }

    public int pedirEntero(String mensaje){
        System.out.print(mensaje);
        return Integer.parseInt(input.nextLine().trim());
    }

    public boolean pedirBooleano(String mensaje) {
        System.out.print(mensaje);
        return Boolean.parseBoolean(input.nextLine().trim());
    }

    public LocalDate pedirFecha(String mensaje) {
        System.out.print(mensaje + " (aaaa-mm-dd): ");
        return LocalDate.parse(input.nextLine().trim());
    }

    // ==========================================
    // MÓDULO CLIENTES
    // ==========================================
    public char mostrarMenuClientes() {
        System.out.println("\n--- GESTIÓN DE CLIENTES ---");
        System.out.println("1. Listar clientes.");
        System.out.println("2. Añadir cliente.");
        System.out.println("3. Editar cliente.");
        System.out.println("4. Eliminar cliente.");
        System.out.println("0. Volver al menú principal.");
        return pedirOpcionMenus();
    }

    public void listarClientes(List<Cliente> clientes) {
        System.out.println("\n--- Listado de clientes disponibles: ---");
        if (clientes == null || clientes.isEmpty()) {
            mostrarMensaje("No hay clientes registrados.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println("- DNI: " + c.getDni() + " | " + c.getNombre() + " " + c.getApellido1() + " | Tel: " + c.getTelefono() + " | Email: " + c.getEmail());
        }
    }

    public void listarClientes() {
        System.out.println("--- Listado de los clientes disponibles: ---");
    }

    public Cliente anadirCliente() {
        System.out.println("\n--- Añadir un nuevo cliente: ---");
        System.out.print("Introduce el nombre: ");
        String nombre = input.nextLine().trim();

        System.out.print("Introduce el primer apellido: ");
        String ap1 = input.nextLine().trim();

        System.out.print("Introduce el segundo apellido: ");
        String ap2 = input.nextLine().trim();

        System.out.print("Introduce el DNI: ");
        String dni = input.nextLine().trim();

        System.out.print("Introduce el teléfono: ");
        int tel = Integer.parseInt(input.nextLine().trim());

        System.out.print("Introduce el correo: ");
        String email = input.nextLine().trim();

        return new Cliente(nombre, ap1, ap2, dni, tel, email);
    }

    public void anadirClientes() {
        anadirCliente();
    }

    public void editarClientes() {
        System.out.println("--- Editar un cliente: ---");
        pedirDni();
    }

    public void eliminarCliente() {
        System.out.println("--- Eliminar un cliente: ---");
        pedirDni();
    }

    // ==========================================
    // MÓDULO HOTELES
    // ==========================================
    public char mostrarMenuHoteles() {
        System.out.println("\n--- GESTIÓN DE HOTELES ---");
        System.out.println("1. Listar todos los hoteles.");
        System.out.println("2. Añadir hotel.");
        System.out.println("3. Editar hotel.");
        System.out.println("4. Eliminar hotel.");
        System.out.println("0. Volver al menú principal.");
        return pedirOpcionMenus();
    }

    public void listarHoteles(List<Hotel> hoteles) {
        System.out.println("\n--- Listado de hoteles disponibles: ---");
        if (hoteles == null || hoteles.isEmpty()) {
            mostrarMensaje("No hay hoteles registrados.");
            return;
        }
        for (Hotel h : hoteles) {
            System.out.println("- " + h.getNombre() + " (" + h.getCiudad() + ") | Tel: " + h.getTelefono() + " | Dir: " + h.getDireccion());
        }
    }

    public void listarHoteles() {
        System.out.println("--- Listado de hoteles: ---");
    }

    public Hotel anadirHotel() {
        System.out.println("\n--- Añadir un nuevo hotel: ---");
        System.out.print("Introduce el nombre del hotel: ");
        String nombre = input.nextLine().trim();

        System.out.print("Introduce la dirección del hotel: ");
        String dir = input.nextLine().trim();

        System.out.print("Introduce la ciudad del hotel: ");
        String ciudad = input.nextLine().trim();

        System.out.print("Introduce el teléfono del hotel: ");
        int tel = Integer.parseInt(input.nextLine().trim());

        Hotel hotel = new Hotel();
        hotel.setNombre(nombre);
        hotel.setDireccion(dir);
        hotel.setCiudad(ciudad);
        hotel.setTelefono(tel);
        return hotel;
    }

    public void editarHotel() {
        System.out.println("--- Editar un hotel: ---");
        pedirNombreHotel();
    }

    public void eliminarHotel() {
        System.out.println("--- Eliminar un hotel: ---");
        pedirNombreHotel();
    }

    // ==========================================
    // MÓDULO HABITACIONES
    // ==========================================
    public char mostrarMenuHabitaciones() {
        System.out.println("\n--- GESTIÓN DE HABITACIONES ---");
        System.out.println("1. Listar habitaciones.");
        System.out.println("2. Añadir habitación.");
        System.out.println("3. Editar habitación.");
        System.out.println("4. Eliminar habitación.");
        System.out.println("0. Volver al menú principal.");
        return pedirOpcionMenus();
    }

    public void listarHabitaciones(List<Habitacion> habitaciones) {
        System.out.println("\n--- Listado de habitaciones: ---");
        if (habitaciones == null || habitaciones.isEmpty()) {
            mostrarMensaje("No hay habitaciones para mostrar.");
            return;
        }
        for (Habitacion hab : habitaciones) {
            System.out.println("- Habitación Nº " + hab.getNumHabitacion() + " | Capacidad: " + hab.getCapacidad()
                    + " pers. | Precio: " + hab.getPrecioNoche() + "€ | Disp: " + (hab.isDisponible() ? "Sí" : "No"));
        }
    }

    public void listarHabitaciones() {
        System.out.println("--- Listar habitaciones: ---");
    }

    public int pedirNumHabitacion() {
        System.out.print("Introduce el número de habitación: ");
        while (!input.hasNextInt()) {
            System.out.println("Entrada inválida. Debe introducir un número entero.");
            input.next();
            System.out.print("Introduce el número de habitación: ");
        }
        int num = input.nextInt();
        input.nextLine(); // Limpiar el buffer
        return num;
    }

    public double pedirPrecio() {
        System.out.print("Introduce el precio por noche: ");
        while (!input.hasNextDouble()) {
            System.out.println("Entrada inválida. Debe introducir un importe numérico.");
            input.next();
            System.out.print("Introduce el precio por noche: ");
        }
        double precio = input.nextDouble();
        input.nextLine(); // Limpiar el buffer
        return precio;
    }

    public Habitacion anadirHabitacion() {
        System.out.println("\n--- Añadir una nueva habitación: ---");
        int num = pedirNumHabitacion();

        System.out.print("Introduce la capacidad: ");
        int cap = Integer.parseInt(input.nextLine().trim());

        double precio = pedirPrecio();

        System.out.print("¿Está disponible? (true/false): ");
        boolean disp = Boolean.parseBoolean(input.nextLine().trim());

        System.out.print("Introduce la categoría (Simple, Doble, Suite): ");
        String cat = input.nextLine().trim();

        Habitacion hab;
        if ("Doble".equalsIgnoreCase(cat)) {
            boolean balcon = pedirBooleano("¿Tiene balcón? (true/false): ");
            hab = new Doble(num, cap, precio, cap, "Doble", "Matrimonio", true, 25.0, balcon);
        } else if ("Suite".equalsIgnoreCase(cat)) {
            hab = new Suite(num, cap, precio, "Suite", 2, true, true, true, "Servicio completo");
        } else {
            hab = new Simple(num, cap, precio, "Simple", "90x200", true);
        }

        hab.setDisponible(disp);
        return hab;
    }

    public void editarHabitacion() {
        System.out.println("--- Editar una habitación: ---");
    }

    public void eliminarHabitacion() {
        System.out.println("--- Eliminar una habitación: ---");
    }

    // ==========================================
    // MÓDULO RESERVAS
    // ==========================================
    public char mostrarMenuReservas() {
        System.out.println("\n--- GESTIÓN DE RESERVAS ---");
        System.out.println("1. Listar reservas.");
        System.out.println("2. Añadir reserva.");
        System.out.println("3. Editar reserva.");
        System.out.println("4. Cancelar/Eliminar reserva.");
        System.out.println("0. Volver al menú principal.");
        return pedirOpcionMenus();
    }

    public void listarReservas(List<Reserva> reservas) {
        System.out.println("\n--- Listado de reservas: ---");
        if (reservas == null || reservas.isEmpty()) {
            mostrarMensaje("No hay reservas registradas.");
            return;
        }
        for (Reserva r : reservas) {
            System.out.println("- Id: " + r.getIdReserva()
            + " Hotel: " + r.getNombreHotel()
            + " Hab: " + r.getNumHabitacion()
            + " DNI: " + r.getDniCliente()
            + " Entrada: " + r.getFechaEntrada()
            + " Salida: " + r.getFechaSalida()
            + " Importe: " + r.getImporteTotal()
            + " Activa: " + (r.isEstado() ? "Sí" : "No"));
        }
    }

    public void listarReservas() {
        System.out.println("--- Listar reservas: ---");
    }

    public Reserva anadirReserva() {
        System.out.println("\n--- Crear una reserva ---");
        System.out.print("Introduce el DNI del cliente: ");
        String dni = input.nextLine().trim();

        System.out.print("Introduce el nombre del hotel: ");
        String hotel = input.nextLine().trim();

        System.out.print("Introduce el número de habitación: ");
        int hab = Integer.parseInt(input.nextLine().trim());

        int huespedes = pedirEntero("Introduce el número de huéspedes: ");
        LocalDate entrada = pedirFecha("Fecha de entrada");
        LocalDate salida = pedirFecha("Fecha de salida");

        Reserva r = new Reserva();
        r.setDniCliente(dni);
        r.setNombreHotel(hotel);
        r.setNumHabitacion(hab);
        r.setNumHuespedes(huespedes);
        r.setFechaEntrada(java.sql.Date.valueOf(entrada));
        r.setFechaSalida(java.sql.Date.valueOf(salida));
        r.setFechaReserva(java.sql.Date.valueOf(LocalDate.now()));
        r.setEstado(true);
        return r;
    }

    public void editarReserva() {
        System.out.println("--- Editar reserva: ---");
    }

    public void eliminarReserva() {
        System.out.println("--- Eliminar reserva: ---");
    }
}
