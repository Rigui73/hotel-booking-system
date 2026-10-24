package RoomServDev.view;

import RoomServDev.controller.ControladorClientes;
import RoomServDev.controller.ControladorHabitaciones;
import RoomServDev.controller.ControladorHoteles;
import RoomServDev.controller.ControladorReservas;
import RoomServDev.model.Cliente;
import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.model.Reserva;
import RoomServDev.model.Simple;
import RoomServDev.model.Suite;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class vistaMenus {

    private final Scanner input = new Scanner(System.in);

    private final ControladorClientes ctrlClientes;
    private final ControladorHoteles ctrlHoteles;
    private final ControladorHabitaciones ctrlHabitaciones;
    private final ControladorReservas ctrlReservas;

    // ********* Constructor *********

    public vistaMenus(ControladorClientes ctrlClientes, ControladorHoteles ctrlHoteles,
                      ControladorHabitaciones ctrlHabitaciones, ControladorReservas ctrlReservas) {
        this.ctrlClientes = ctrlClientes;
        this.ctrlHoteles = ctrlHoteles;
        this.ctrlHabitaciones = ctrlHabitaciones;
        this.ctrlReservas = ctrlReservas;
    }

    // ********************************************* MENÚ PRINCIPAL *********************************************

    public void mostrarMenuPrincipal() {
        boolean salir = false;

        do {
            System.out.println("");
            System.out.println("1. Gestionar clientes.");
            System.out.println("2. Gestionar hoteles.");
            System.out.println("3. Gestionar habitaciones.");
            System.out.println("4. Gestionar reservas.");
            System.out.println("0. Salir de la aplicación.");
            System.out.println("");

            switch (pedirOpcionMenus()) {
                case '1':
                    mostrarMenuClientes();
                    break;
                case '2':
                    mostrarMenuHoteles();
                    break;
                case '3':
                    mostrarMenuHabitaciones();
                    break;
                case '4':
                    mostrarMenuReservas();
                    break;
                case '0':
                    salir = true;
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (!salir);
    }

    // ********************************************* CLIENTES *********************************************

    public void mostrarMenuClientes() {
        boolean salir = false;

        do {
            System.out.println("");
            System.out.println("1. Listar clientes.");
            System.out.println("2. Añadir clientes.");
            System.out.println("3. Editar clientes.");
            System.out.println("4. Eliminar clientes.");
            System.out.println("0. Volver al menú principal.");
            System.out.println("");

            switch (pedirOpcionMenus()) {
                case '1':
                    listarClientes();
                    break;
                case '2':
                    anadirClientes();
                    break;
                case '3':
                    editarClientes();
                    break;
                case '4':
                    eliminarCliente();
                    break;
                case '0':
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (!salir);
    }

    void listarClientes() {
        System.out.println("--- Listado de los clientes disponibles: ---");
        System.out.println("");

        List<Cliente> clientes = ctrlClientes.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente c : clientes) {
            mostrarCliente(c);
        }
    }

    void anadirClientes() {
        System.out.println("--- Añadir un nuevo cliente: ---");
        System.out.println("");

        String nombre = pedirTexto("Introduce el nombre del nuevo cliente: ");
        String apellido1 = pedirTexto("Introduce el primer apellido del nuevo cliente: ");
        String apellido2 = pedirTexto("Introduce el segundo apellido del nuevo cliente: ");
        String dni = pedirTexto("Introduce el DNI del nuevo cliente: ");
        int telefono = pedirEntero("Introduce el teléfono del nuevo cliente: ");
        String correo = pedirTexto("Introduce el correo del nuevo cliente: ");

        try {
            ctrlClientes.crearCliente(nombre, apellido1, apellido2, dni, telefono, correo);
            System.out.println("Cliente creado correctamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void editarClientes() {
        System.out.println("--- Editar un cliente: ---");
        System.out.println("");

        String dni = pedirTexto("Introduce el DNI del cliente: ");
        Cliente cliente = ctrlClientes.buscarCliente(dni);
        if (cliente == null) {
            System.out.println("No existe ningún cliente con ese DNI.");
            return;
        }

        mostrarCliente(cliente);
        System.out.println("(Pulsa Enter para mantener el valor actual)");

        String nuevoDni = pedirTextoOpcional("DNI", cliente.getDni());
        String nombre = pedirTextoOpcional("Nombre", cliente.getNombre());
        String apellido1 = pedirTextoOpcional("Primer apellido", cliente.getApellido1());
        String apellido2 = pedirTextoOpcional("Segundo apellido", cliente.getApellido2());
        int telefono = pedirEnteroOpcional("Teléfono", cliente.getTelefono());
        String correo = pedirTextoOpcional("Correo", cliente.getEmail());

        try {
            ctrlClientes.editarCliente(dni, nuevoDni, nombre, apellido1, apellido2, telefono, correo);
            System.out.println("Cliente modificado correctamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void eliminarCliente() {
        System.out.println("--- Eliminar un cliente: ---");
        System.out.println("");

        String dni = pedirTexto("Introduce el DNI del cliente: ");

        try {
            ctrlClientes.eliminarCliente(dni);
            System.out.println("Cliente eliminado correctamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ********************************************* HOTELES *********************************************

    public void mostrarMenuHoteles() {
        boolean salir = false;

        do {
            System.out.println("");
            System.out.println("1. Listar hoteles.");
            System.out.println("2. Añadir hoteles.");
            System.out.println("3. Editar hoteles.");
            System.out.println("4. Eliminar hoteles.");
            System.out.println("0. Volver al menú principal.");
            System.out.println("");

            switch (pedirOpcionMenus()) {
                case '1':
                    listarHoteles();
                    break;
                case '2':
                    anadirHotel();
                    break;
                case '3':
                    editarHotel();
                    break;
                case '4':
                    eliminarHotel();
                    break;
                case '0':
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (!salir);
    }

    void listarHoteles() {
        System.out.println("--- Listado de los hoteles disponibles: ---");
        System.out.println("");

        System.out.println("1. Listar todos los hoteles.");
        System.out.println("2. Listar los hoteles por ciudad.");
        System.out.println("3. Listar hoteles con habitaciones disponibles.");

        switch (pedirOpcionMenus()) {
            case '1':
                listarTodosHoteles();
                break;
            case '2':
                listarCiudadHoteles();
                break;
            case '3':
                listarHotelesDisponibles();
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    void listarTodosHoteles() {
        mostrarListaHoteles(ctrlHoteles.listarHoteles());
    }

    void listarCiudadHoteles() {
        String ciudad = pedirTexto("Introduce la ciudad: ");
        mostrarListaHoteles(ctrlHoteles.listarHotelesPorCiudad(ciudad));
    }

    void listarHotelesDisponibles() {
        mostrarListaHoteles(ctrlHoteles.listarHotelesConDisponibilidad());
    }

    void anadirHotel() {
        System.out.println("--- Añadir un nuevo hotel: ---");
        System.out.println("");

        String nombre = pedirTexto("Introduce el nombre del hotel: ");
        String direccion = pedirTexto("Introduce la dirección del hotel: ");
        String ciudad = pedirTexto("Introduce la ciudad del hotel: ");
        int telefono = pedirEntero("Introduce el teléfono del hotel: ");

        try {
            ctrlHoteles.crearHotel(nombre, direccion, ciudad, telefono);
            System.out.println("Hotel creado correctamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void editarHotel() {
        System.out.println("--- Editar un hotel: ---");
        System.out.println("");

        Hotel hotel = seleccionarHotel();
        if (hotel == null) return;

        System.out.println("(Pulsa Enter para mantener el valor actual)");
        String direccion = pedirTextoOpcional("Dirección", hotel.getDireccion());
        String ciudad = pedirTextoOpcional("Ciudad", hotel.getCiudad());
        int telefono = pedirEnteroOpcional("Teléfono", hotel.getTelefono());

        try {
            ctrlHoteles.editarHotel(hotel.getNombre(), direccion, ciudad, telefono);
            System.out.println("Hotel modificado correctamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void eliminarHotel() {
        System.out.println("--- Eliminar un hotel: ---");
        System.out.println("");

        Hotel hotel = seleccionarHotel();
        if (hotel == null) return;

        if (!pedirSiNo("¿Seguro que quieres eliminar " + hotel.getNombre() + "? (S/N): ")) return;

        try {
            ctrlHoteles.eliminarHotel(hotel.getNombre());
            System.out.println("Hotel eliminado correctamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Muestra los hoteles numerados y devuelve el elegido (o null si se cancela)
    Hotel seleccionarHotel() {
        List<Hotel> hoteles = ctrlHoteles.listarHoteles();

        if (hoteles.isEmpty()) {
            System.out.println("No hay hoteles registrados.");
            return null;
        }

        for (int i = 0; i < hoteles.size(); i++) {
            Hotel h = hoteles.get(i);
            System.out.println((i + 1) + ". " + h.getNombre() + " (" + h.getCiudad() + ")");
        }
        System.out.println("0. Cancelar");

        int opcion = pedirEntero("Selecciona un hotel: ");
        if (opcion == 0) return null;

        Hotel elegido = ctrlHoteles.obtenerHotel(opcion - 1); // el usuario ve desde 1, la lista empieza en 0
        if (elegido == null) System.out.println("Opción no válida.");
        return elegido;
    }

    // ********************************************* HABITACIONES *********************************************

    public void mostrarMenuHabitaciones() {
        boolean salir = false;

        do {
            System.out.println("");
            System.out.println("1. Listar habitaciones.");
            System.out.println("2. Añadir habitaciones.");
            System.out.println("3. Editar habitaciones.");
            System.out.println("4. Eliminar habitaciones.");
            System.out.println("0. Volver al menú principal.");
            System.out.println("");

            switch (pedirOpcionMenus()) {
                case '1':
                    listarHabitaciones();
                    break;
                case '2':
                    anadirHabitacion();
                    break;
                case '3':
                    editarHabitacion();
                    break;
                case '4':
                    eliminarHabitacion();
                    break;
                case '0':
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (!salir);
    }

    void listarHabitaciones() {
        System.out.println("--- Listar habitaciones: ---");
        System.out.println("");

        Hotel hotel = seleccionarHotel();
        if (hotel == null) return;

        System.out.println("");
        System.out.println("1. Todas");
        System.out.println("2. Simples");
        System.out.println("3. Dobles");
        System.out.println("4. Suites");
        System.out.println("5. Solo disponibles");
        System.out.println("");

        List<Habitacion> habitaciones;
        switch (pedirOpcionMenus()) {
            case '1':
                habitaciones = ctrlHabitaciones.listarHabitaciones(hotel);
                break;
            case '2':
                habitaciones = ctrlHabitaciones.listarHabitacionesPorTipo(hotel, 1);
                break;
            case '3':
                habitaciones = ctrlHabitaciones.listarHabitacionesPorTipo(hotel, 2);
                break;
            case '4':
                habitaciones = ctrlHabitaciones.listarHabitacionesPorTipo(hotel, 3);
                break;
            case '5':
                habitaciones = ctrlHabitaciones.listarHabitacionesDisponibles(hotel);
                break;
            default:
                System.out.println("Opción no válida.");
                return;
        }

        if (habitaciones.isEmpty()) {
            System.out.println("No hay habitaciones que mostrar.");
            return;
        }
        for (Habitacion h : habitaciones) {
            mostrarHabitacion(h);
        }
    }

    void anadirHabitacion() {
        System.out.println("--- Añadir una nueva habitación: ---");
        System.out.println("");

        Hotel hotel = seleccionarHotel();
        if (hotel == null) return;

        System.out.println("");
        System.out.println("1. Simple");
        System.out.println("2. Doble");
        System.out.println("3. Suite");
        System.out.println("");
        int tipo = pedirEntero("Introduce el tipo de habitación que quieres crear: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo de habitación no válido.");
            return;
        }

        int numHabitacion = pedirEntero("Introduce el número de habitación: ");
        int capacidad = pedirEntero("Introduce la capacidad de la habitación: ");
        double precioNoche = pedirDouble("Introduce el precio de cada noche de la habitación: ");
        boolean disponible = pedirSiNo("¿Está disponible la habitación? (S/N): ");

        try {
            if (tipo == 1) {
                String medidaCama = pedirTexto("Introduce la medida de la cama (ej. 90x200): ");
                boolean escritorio = pedirSiNo("¿Tiene escritorio? (S/N): ");

                ctrlHabitaciones.crearHabitacionSimple(hotel, numHabitacion, capacidad, precioNoche,
                        disponible, medidaCama, escritorio);

            } else if (tipo == 2) {
                String tipoCama = pedirTexto("Introduce el tipo de cama: ");
                boolean supletoria = pedirSiNo("¿Tiene cama supletoria? (S/N): ");
                double costeSupletoria = 0;
                if (supletoria) {
                    costeSupletoria = pedirDouble("Introduce el coste por noche de la supletoria: ");
                }
                boolean balcon = pedirSiNo("¿Tiene balcón? (S/N): ");

                ctrlHabitaciones.crearHabitacionDoble(hotel, numHabitacion, capacidad, precioNoche,
                        disponible, tipoCama, supletoria, costeSupletoria, balcon);

            } else {
                int ambientes = pedirEntero("Introduce el número de ambientes de la habitación: ");
                boolean jacuzzi = pedirSiNo("¿Tiene jacuzzi? (S/N): ");
                boolean spa = pedirSiNo("¿Incluye spa? (S/N): ");
                boolean terraza = pedirSiNo("¿Tiene terraza? (S/N): ");
                String serviciosExtra = pedirTexto("Introduce qué servicios extra tiene: ");

                ctrlHabitaciones.crearHabitacionSuite(hotel, numHabitacion, capacidad, precioNoche,
                        disponible, ambientes, jacuzzi, spa, terraza, serviciosExtra);
            }
            System.out.println("Habitación creada correctamente.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void editarHabitacion() {
        System.out.println("--- Editar una habitación: ---");
        System.out.println("");

        Hotel hotel = seleccionarHotel();
        if (hotel == null) return;

        int numHabitacion = pedirEntero("Introduce el número de la habitación: ");
        Habitacion h = ctrlHabitaciones.buscarHabitacion(hotel, numHabitacion);
        if (h == null) {
            System.out.println("Ese hotel no tiene la habitación " + numHabitacion + ".");
            return;
        }

        mostrarHabitacion(h);
        System.out.println("(Pulsa Enter para mantener el valor actual)");
        int capacidad = pedirEnteroOpcional("Capacidad", h.getCapacidad());
        double precio = pedirDoubleOpcional("Precio por noche", h.getPrecioNoche());
        boolean disponible = pedirSiNoOpcional("Disponible (S/N)", h.isDisponible());

        try {
            ctrlHabitaciones.editarHabitacion(hotel, numHabitacion, capacidad, precio, disponible);
            System.out.println("Habitación modificada correctamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void eliminarHabitacion() {
        System.out.println("--- Eliminar una habitación: ---");
        System.out.println("");

        Hotel hotel = seleccionarHotel();
        if (hotel == null) return;

        int numHabitacion = pedirEntero("Introduce el número de la habitación: ");

        try {
            ctrlHabitaciones.eliminarHabitacion(hotel, numHabitacion);
            System.out.println("Habitación eliminada correctamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ********************************************* RESERVAS *********************************************

    public void mostrarMenuReservas() {
        boolean salir = false;

        do {
            System.out.println("");
            System.out.println("1. Listar reservas.");
            System.out.println("2. Añadir reservas.");
            System.out.println("3. Editar reservas.");
            System.out.println("4. Cancelar o eliminar reservas.");
            System.out.println("0. Volver al menú principal.");
            System.out.println("");

            switch (pedirOpcionMenus()) {
                case '1':
                    listarReservas();
                    break;
                case '2':
                    anadirReserva();
                    break;
                case '3':
                    editarReserva();
                    break;
                case '4':
                    eliminarReserva();
                    break;
                case '0':
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (!salir);
    }

    void listarReservas() {
        System.out.println("--- Listar reservas: ---");
        System.out.println("");

        System.out.println("1. Todas las reservas.");
        System.out.println("2. Solo reservas activas.");
        System.out.println("3. Reservas de un cliente.");
        System.out.println("4. Buscar una reserva por id.");

        switch (pedirOpcionMenus()) {
            case '1':
                mostrarListaReservas(ctrlReservas.listarReservas());
                break;
            case '2':
                mostrarListaReservas(ctrlReservas.listarReservasActivas());
                break;
            case '3':
                String dni = pedirTexto("Introduce el DNI del cliente: ");
                mostrarListaReservas(ctrlReservas.listarReservasCliente(dni));
                break;
            case '4':
                int id = pedirEntero("Introduce el id de la reserva: ");
                Reserva r = ctrlReservas.buscarReserva(id);
                if (r == null) {
                    System.out.println("No existe ninguna reserva con ese id.");
                } else {
                    mostrarReserva(r);
                }
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    void anadirReserva() {
        System.out.println("--- Crear una reserva ---");
        System.out.println("");

        // El id, la fecha de reserva, el estado y el importe los calcula el controlador
        String dni = pedirTexto("Introduce el DNI del cliente: ");
        if (ctrlClientes.buscarCliente(dni) == null) {
            System.out.println("No existe ningún cliente con ese DNI. Dalo de alta primero.");
            return;
        }

        Hotel hotel = seleccionarHotel();
        if (hotel == null) return;

        List<Habitacion> disponibles = ctrlHabitaciones.listarHabitacionesDisponibles(hotel);
        if (disponibles.isEmpty()) {
            System.out.println("Este hotel no tiene habitaciones disponibles.");
            return;
        }
        System.out.println("Habitaciones disponibles:");
        for (Habitacion h : disponibles) {
            mostrarHabitacion(h);
        }

        int numHabitacion = pedirEntero("Introduce el número de la habitación: ");
        LocalDate entrada = pedirFecha("Introduce la fecha de entrada (AAAA-MM-DD): ");
        LocalDate salida = pedirFecha("Introduce la fecha de salida (AAAA-MM-DD): ");
        int numHuespedes = pedirEntero("Introduce el número de huéspedes: ");

        try {
            Reserva r = ctrlReservas.crearReserva(dni, hotel.getNombre(), numHabitacion,
                    entrada, salida, numHuespedes);
            System.out.println("Reserva creada correctamente:");
            mostrarReserva(r);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void editarReserva() {
        System.out.println("--- Editar una reserva ---");
        System.out.println("");

        int id = pedirEntero("Introduce el id de la reserva: ");
        Reserva r = ctrlReservas.buscarReserva(id);
        if (r == null) {
            System.out.println("No existe ninguna reserva con ese id.");
            return;
        }

        mostrarReserva(r);
        System.out.println("(Pulsa Enter para mantener el valor actual)");
        LocalDate entrada = pedirFechaOpcional("Fecha de entrada", aLocalDate(r.getFechaEntrada()));
        LocalDate salida = pedirFechaOpcional("Fecha de salida", aLocalDate(r.getFechaSalida()));
        int numHuespedes = pedirEnteroOpcional("Número de huéspedes", r.getNumHuespedes());

        try {
            ctrlReservas.editarReserva(id, entrada, salida, numHuespedes);
            System.out.println("Reserva modificada correctamente:");
            mostrarReserva(ctrlReservas.buscarReserva(id));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void eliminarReserva() {
        System.out.println("--- Cancelar o eliminar una reserva ---");
        System.out.println("");

        int id = pedirEntero("Introduce el id de la reserva: ");

        System.out.println("1. Cancelar (se queda en el historial como cancelada).");
        System.out.println("2. Eliminar definitivamente.");

        try {
            switch (pedirOpcionMenus()) {
                case '1':
                    ctrlReservas.cancelarReserva(id);
                    System.out.println("Reserva cancelada correctamente.");
                    break;
                case '2':
                    if (pedirSiNo("¿Seguro que quieres eliminarla? (S/N): ")) {
                        ctrlReservas.eliminarReserva(id);
                        System.out.println("Reserva eliminada correctamente.");
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ********************************************* MOSTRAR DATOS *********************************************

    void mostrarCliente(Cliente c) {
        System.out.println(c.getDni() + " | " + c.getNombre() + " " + c.getApellido1() + " " + c.getApellido2()
                + " | Tel: " + c.getTelefono() + " | " + c.getEmail());
    }

    void mostrarListaHoteles(List<Hotel> hoteles) {
        if (hoteles.isEmpty()) {
            System.out.println("No hay hoteles que mostrar.");
            return;
        }
        for (Hotel h : hoteles) {
            System.out.println(h.getNombre() + " | " + h.getDireccion() + ", " + h.getCiudad()
                    + " | Tel: " + h.getTelefono() + " | Habitaciones: " + h.getHabitaciones().size());
        }
    }

    void mostrarHabitacion(Habitacion h) {
        String extra = "";
        if (h instanceof Simple) {
            Simple s = (Simple) h;
            extra = "Cama " + s.getMedidaCama() + (s.isTieneEscritorio() ? ", escritorio" : "");
        } else if (h instanceof Doble) {
            Doble d = (Doble) h;
            extra = "Cama " + d.getTipoCamaDoble()
                    + (d.isSupletoria() ? ", supletoria (+" + d.getSuplementSupletoria() + " €)" : "")
                    + (d.isBalcon() ? ", balcón" : "");
        } else if (h instanceof Suite) {
            Suite s = (Suite) h;
            extra = s.getNumAmbientes() + " ambientes"
                    + (s.isJacuzzi() ? ", jacuzzi" : "")
                    + (s.isIncluyeSpa() ? ", spa" : "")
                    + (s.isIncluyeTerraza() ? ", terraza" : "")
                    + (s.getOtrosServs() == null || s.getOtrosServs().isBlank() ? "" : ", " + s.getOtrosServs());
        }

        System.out.printf("Nº %d | %s | %d pers. | %.2f €/noche | %s | %s%n",
                h.getNumHabitacion(), h.getCategoria(), h.getCapacidad(), h.getPrecioNoche(),
                h.isDisponible() ? "Disponible" : "No disponible", extra);
    }

    void mostrarListaReservas(List<Reserva> reservas) {
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas que mostrar.");
            return;
        }
        for (Reserva r : reservas) {
            mostrarReserva(r);
        }
    }

    void mostrarReserva(Reserva r) {
        System.out.printf("#%d | %s | %s hab. %d | %s → %s | %d huésp. | %.2f € | %s%n",
                r.getIdReserva(), r.getDniCliente(), r.getNombreHotel(), r.getNumHabitacion(),
                aLocalDate(r.getFechaEntrada()), aLocalDate(r.getFechaSalida()),
                r.getNumHuespedes(), r.getImporteTotal(), r.isEstado() ? "Activa" : "Cancelada");
    }

    // ********************************************* PEDIR DATOS (con control de errores) *********************************************

    char pedirOpcionMenus() {
        System.out.print("Elige una opción: ");
        String respuesta = input.nextLine().trim();
        if (respuesta.isEmpty()) {
            respuesta = " ";
        }
        return respuesta.charAt(0);
    }

    String pedirTexto(String mensaje) {
        System.out.print(mensaje);
        return input.nextLine().trim();
    }

    int pedirEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
            }
        }
    }

    double pedirDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(input.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido (ej. 75.50).");
            }
        }
    }

    boolean pedirSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String respuesta = input.nextLine().trim().toUpperCase();
            if (respuesta.equals("S") || respuesta.equals("SI") || respuesta.equals("SÍ")) return true;
            if (respuesta.equals("N") || respuesta.equals("NO")) return false;
            System.out.println("Responde S o N.");
        }
    }

    LocalDate pedirFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalDate.parse(input.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha no válido. Usa AAAA-MM-DD (ej. 2026-11-15).");
            }
        }
    }

    // ********* Versiones "opcionales": si se pulsa Enter se mantiene el valor actual *********

    String pedirTextoOpcional(String campo, String actual) {
        System.out.print(campo + " [" + actual + "]: ");
        String respuesta = input.nextLine().trim();
        return respuesta.isEmpty() ? actual : respuesta;
    }

    int pedirEnteroOpcional(String campo, int actual) {
        while (true) {
            System.out.print(campo + " [" + actual + "]: ");
            String respuesta = input.nextLine().trim();
            if (respuesta.isEmpty()) return actual;
            try {
                return Integer.parseInt(respuesta);
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
            }
        }
    }

    double pedirDoubleOpcional(String campo, double actual) {
        while (true) {
            System.out.print(campo + " [" + actual + "]: ");
            String respuesta = input.nextLine().trim();
            if (respuesta.isEmpty()) return actual;
            try {
                return Double.parseDouble(respuesta.replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido (ej. 75.50).");
            }
        }
    }

    boolean pedirSiNoOpcional(String campo, boolean actual) {
        while (true) {
            System.out.print(campo + " [" + (actual ? "S" : "N") + "]: ");
            String respuesta = input.nextLine().trim().toUpperCase();
            if (respuesta.isEmpty()) return actual;
            if (respuesta.equals("S") || respuesta.equals("SI") || respuesta.equals("SÍ")) return true;
            if (respuesta.equals("N") || respuesta.equals("NO")) return false;
            System.out.println("Responde S o N.");
        }
    }

    LocalDate pedirFechaOpcional(String campo, LocalDate actual) {
        while (true) {
            System.out.print(campo + " [" + actual + "]: ");
            String respuesta = input.nextLine().trim();
            if (respuesta.isEmpty()) return actual;
            try {
                return LocalDate.parse(respuesta);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha no válido. Usa AAAA-MM-DD (ej. 2026-11-15).");
            }
        }
    }

    // El modelo guarda las fechas como Date; para mostrarlas las pasamos a LocalDate
    private LocalDate aLocalDate(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
