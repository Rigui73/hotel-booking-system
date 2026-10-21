package RoomServDev.view;

import RoomServDev.model.Cliente;

import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class vistaMenus {
    Scanner input = new Scanner(System.in);

    public void mostrarMenuClientes(){
        boolean salir = false;
        char opcion;

        do {
            System.out.println("");
            System.out.println("1. Listar clientes.");
            System.out.println("2. Añadir clientes.");
            System.out.println("3. Editar clientes.");
            System.out.println("4. Eliminar clientes.");
            System.out.println("0. Volver al menu prinicpal.");
            System.out.println("");

            opcion = pedirOpcionMenus();

            switch (opcion){
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
            }

        } while (!salir);

    }

    void listarClientes(){
        System.out.println("--- Listado de los clientes disponibles: ---");
        System.out.println("");

        // Listar los clientes
    }

    void anadirClientes(){
        System.out.println("--- Añadir un nuevo cliente: ---");
        System.out.println("");

        System.out.print("Introduce el nombre del nuevo cliente: ");
        String nombreCliente = input.nextLine();

        System.out.print("Introduce el primer apellido del nuevo cliente: ");
        String apellido1Cliente = input.nextLine();

        System.out.print("Introduce el segundo apellido del nuevo cliente: ");
        String apellido2Cliente = input.nextLine();

        System.out.print("Introduce el dni del nuevo cliente: ");
        String dniCliente = input.nextLine();

        System.out.print("Introduce el teléfono del nuevo cliente: ");
        int telefonoCliente = input.nextInt();
        input.nextLine();

        System.out.print("Introduce el correo del nuevo cliente: ");
        String correoCliente = input.nextLine();

        // Crear cliente
    }

    void editarClientes(){
        System.out.println("--- Editar un cliente: ---");
        System.out.println("");

        System.out.print("Introduce el dni del cliente: ");
        String dni = input.nextLine();

        // Buscar cliente
    }

    void eliminarCliente(){
        System.out.println("--- Eliminar un cliente: ---");
        System.out.println("");

        System.out.print("Introduce el dni del cliente: ");
        String dni = input.nextLine();

        // Eliminar cliente
    }

    public void mostrarMenuHoteles(){
        boolean salir = false;
        char opcion;

        do {
            System.out.println("");
            System.out.println("1. Listar hoteles.");
            System.out.println("2. Añadir hoteles.");
            System.out.println("3. Editar hoteles.");
            System.out.println("4. Eliminar hoteles.");
            System.out.println("0. Volver al menu principal.");
            System.out.println("");

            opcion = pedirOpcionMenus();

            switch (opcion){
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
            }

        } while (!salir);
    }

    void listarHoteles(){
        char opcion;

        System.out.println("--- Listado de los clientes disponibles: ---");
        System.out.println("");

        System.out.println("1. Listar todos los hoteles.");
        System.out.println("2. Listar los hoteles por ciudad.");
        System.out.println("3. Listar hoteles por");

        opcion = pedirOpcionMenus();

        switch (opcion){
            case '1':
                listarTodosHoteles();
                break;
            case '2':
                listarCiudadHoteles();
                break;
            case '3':
                listar_Hoteles();
        }

    }

    void listarTodosHoteles(){
        // Listar todos los hoteles
    }

    void listarCiudadHoteles(){
        // Listar hoteles por ciudad
    }

    void listar_Hoteles(){
        // Listar hoteles por algo.
    }

    void anadirHotel(){
        System.out.println("--- Añadir un nuevo hotel: ---");
        System.out.println("");

        System.out.print("Introduce el nombre del hotel: ");
        String nombreHotel = input.nextLine();

        System.out.print("Introduce la dirección del hotel: ");
        String direccionHotel = input.nextLine();

        System.out.print("Introduce la ciudad del hotel: ");
        String ciudadHotel = input.nextLine();

        System.out.print("Introduce el teléfono del hotel: ");
        int telefonoHotel = input.nextInt();
        input.nextLine();

        // Crear hotel
    }

    void editarHotel(){
        System.out.println("--- Editar un hotel: ---");
        System.out.println("");

        System.out.print("Introduce el nombre del hotel: ");
        String nombreHotel = input.nextLine();

        // Editar hotel
    }

    void eliminarHotel(){
        System.out.println("--- Eliminar un hotel: ---");
        System.out.println("");

        System.out.print("Introduce el nombre del hotel: ");
        String dni = input.nextLine();

        // Eliminar hotel
    }

    public void mostrarMenuHabitaciones(){
        boolean salir = false;
        char opcion;

        do {
            System.out.println("");
            System.out.println("1. Listar habitaciones.");
            System.out.println("2. Añadir habitaciones.");
            System.out.println("3. Editar habitaciones.");
            System.out.println("4. Eliminar habitaciones.");
            System.out.println("0. Volver al menu principal.");
            System.out.println("");

            opcion = pedirOpcionMenus();

            switch (opcion){
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
            }

        } while (!salir);
    }

    void listarHabitaciones(){
        System.out.println("--- Listar una habitación: ---");
        System.out.println("");

        System.out.print("Selecciona de que hotel quieres listar las habitaciones: ");
        String nombreHotel = input.nextLine();

        // Buscar hotel y nombre

        System.out.println("");
        System.out.println("1. Simple");
        System.out.println("2. Doble");
        System.out.println("3. Suite");
        System.out.println("");

        System.out.print("Introduce el tipo de habitación que quieres listar dentro de este hotel: ");
        int tipoHabitacion = input.nextInt();
        input.nextLine();

        switch (tipoHabitacion){
            case '1':
                // Listar habitaciones simples.
                break;
            case '2':
                // Listar habitaciones dobles.
                break;
            case '3':
                // Listar habitaciones suite.
                break;
        }
    }

    void anadirHabitacion(){
        System.out.println("--- Añadir una nueva habitación: ---");
        System.out.println("");

        System.out.print("Selecciona el hotel en el que añadir la habitación: ");
        String nombreHotel = input.nextLine();

        // Buscar hotel

        System.out.print("Introduce el tipo de habitación que quieres crear: ");
        int tipoHabitacion = input.nextInt();
        input.nextLine();

        System.out.print("Introduce el número de habitación: ");
        int numHabitacionHabitacion = input.nextInt();
        input.nextLine();

        System.out.print("Introduce la capacidad de la habitación: ");
        int numCapacidadHabitacion = input.nextInt();
        input.nextLine();

        System.out.print("Introduce el precio de cada noche de la habitación: ");
        double precioNocheHabitacion = input.nextDouble();
        input.nextLine();

        System.out.print("Introduce si esta disponible la habitación: (Si = TRUE | No = FALSE): ");
        boolean disponibleHabitacion = input.nextBoolean();
        input.nextLine();

        System.out.print("");
        System.out.print("1. Simple");
        System.out.print("2. Doble");
        System.out.print("3. Suite");
        System.out.print("");

        System.out.print("Introduce la categoría de la habitación: ");
        int respuestaCategoriaHabitacion = input.nextInt();
        input.nextLine();

        String categoriaHabitacion;
        if (respuestaCategoriaHabitacion == 1){
            categoriaHabitacion = "Simple";

            System.out.print("Introduce la medida de la cama: ");
            int medidaCamaSimple = input.nextInt();
            input.nextLine();

            System.out.print("Introduce si la habitación tiene escritorio: (Si = TRUE | No = FALSE): ");
            boolean escritorioHabitacion = input.nextBoolean();
            input.nextLine();

            // Crear habitación simple

        } else if (respuestaCategoriaHabitacion == 2){
            categoriaHabitacion = "Doble";

            System.out.print("Introduce el tipo de cama: ");
            String tipoCamaDoble = input.nextLine();

            System.out.print("Introduce si la habitación tiene supletoria: (Si = TRUE | No = FALSE): ");
            boolean supletoriaHabitacion = input.nextBoolean();
            input.nextLine();

            System.out.print("Introduce si la habitación tiene supletoria: (Si = TRUE | No = FALSE): ");
            double costeSupletoriaHabitacion = input.nextDouble();
            input.nextLine();

            System.out.print("Introduce si la habitación tiene balcon: (Si = TRUE | No = FALSE): ");
            boolean balconHabitacion = input.nextBoolean();
            input.nextLine();

            // Crear habitación doble

        } else if (respuestaCategoriaHabitacion == 3){
            categoriaHabitacion = "Suite";

            System.out.print("Introduce el número de ambientes de la habitación: ");
            int ambientesSuite = input.nextInt();
            input.nextLine();

            System.out.print("Introduce si la habitación tiene jacuzzi: (Si = TRUE | No = FALSE): ");
            boolean jacuzziHabitacion = input.nextBoolean();
            input.nextLine();

            System.out.print("Introduce si la habitación tiene spa: (Si = TRUE | No = FALSE): ");
            boolean spaHabitacion = input.nextBoolean();
            input.nextLine();

            System.out.print("Introduce si la habitación tiene terraza: (Si = TRUE | No = FALSE): ");
            boolean terrazaHabitacion = input.nextBoolean();
            input.nextLine();

            System.out.print("Introduce que tipos de servicio extra tiene: ");
            String serviciosExtra = input.nextLine();

            // Crear habitación Suite

        } else {
            System.out.print("Error al crear la habitación.");
        }

    }

    void editarHabitacion(){
        System.out.println("--- Editar una habitación: ---");
        System.out.println("");

        System.out.print("Introduce el nombre del hotel del cual quieres editar la habitación:");
        String nombreHotel = input.nextLine();

        // Editar una habitación
    }

    void eliminarHabitacion(){
        System.out.println("--- Eliminar una habitación: ---");
        System.out.println("");

        System.out.print("Introduce el nombre del hotel del cual quieres eliminar la habitación:");
        String nombreHotel = input.nextLine();

        // Eliminar la habitación
    }

    public void mostrarMenuReservas(){
        boolean salir = false;
        char opcion;

        do {
            System.out.println("");
            System.out.println("1. Listar reservas.");
            System.out.println("2. Añadir reservas.");
            System.out.println("3. Editar reservas.");
            System.out.println("4. Eliminar reservas.");
            System.out.println("0. Volver al menu principal.");
            System.out.println("");

            opcion = pedirOpcionMenus();

            switch (opcion){
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
            }

        } while (!salir);
    }

    void listarReservas(){
        System.out.println("--- Listar una reserva: ---");
        System.out.println("");

        System.out.print("Introduce el id de la reserva: ");
        int idReserva = input.nextInt();
        input.nextLine();

        // Listar reservas
    }

    void anadirReserva(){
        System.out.println("--- Crear una reserva ---");
        System.out.println("");

        System.out.print("Introduce el id de la reserva: ");
        int idReserva = input.nextInt();
        input.nextLine();

        LocalDate fehcaReserva = LocalDate.now();

        System.out.print("Introduce la fecha de inicio de la reserva (AAAA-MM-DD): ");
        LocalDate fechaEntradaReserva = LocalDate.parse(input.next());
        input.nextLine();

        System.out.print("Introduce la fecha de salida de la reserva (AAAA-MM-DD): ");
        LocalDate fechaSalidaReserva = LocalDate.parse(input.next());
        input.nextLine();

        System.out.print("Introduce el número de huespedes: ");
        int numHuespedesReserva = input.nextInt();
        input.nextLine();

        // Calcular el precio total

        System.out.print("Introduce si la reserva esta activa: (Si = TRUE | No = FALSE): ");
        boolean activaReserva = input.nextBoolean();
        input.nextLine();

        System.out.print("Introduce el nombre del hotel: ");
        String nombreHotel = input.nextLine();

        System.out.print("Introduce el número de la habitación: ");
        int numHabitacionReserva = input.nextInt();
        input.nextLine();

        System.out.print("Introduce el dni del cliente: ");
        String dniCliente = input.nextLine();

        // Crear reserva
    }

    void editarReserva(){
        System.out.println("--- Editar una reserva ---");
        System.out.println("");

        System.out.print("Introduce el id de la reserva: ");
        int idReserva = input.nextInt();
        input.nextLine();

        // Editar reserva
    }

    void eliminarReserva(){
        System.out.println("--- Elimianr una reserva ---");
        System.out.println("");

        System.out.print("Introduce el id de la reserva: ");
        int idReserva = input.nextInt();
        input.nextLine();

        // Eliminar reserva
    }

    char pedirOpcionMenus(){
        String respuesta;
        System.out.print("Elige una opción: ");
        respuesta = input.nextLine();
        if (respuesta.isEmpty()){
            respuesta = " ";
        }
        return respuesta.charAt(0);
    }
}
