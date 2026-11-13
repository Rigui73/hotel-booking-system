package RoomServDev.controller;

import RoomServDev.model.*;
import RoomServDev.view.vistaMenus;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class HotelController {

    // El controlador conecta la vista con los datos
    private vistaMenus vista;
    private Datos repositorio;

    // Constructor: inyectamos las dependencias
    public HotelController() {
        this.vista = new vistaMenus();
        this.repositorio = new Datos();
    }

    // ***********************************************************
    // BUCLE PRINCIPAL DEL SISTEMA
    // ***********************************************************
    public void iniciar() {
        boolean salir = false;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("\n=== ROOM SERVICE DEV - MENÚ PRINCIPAL ===");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Hoteles");
            System.out.println("3. Gestión de Habitaciones");
            System.out.println("4. Gestión de Reservas");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    gestionarClientes();
                    break;
                case "2":
                    gestionarHoteles();
                    break;
                case "3":
                    gestionarHabitaciones();
                    break;
                case "4":
                    gestionarReservas();
                    break;
                case "0":
                    salir = true;
                    vista.mostrarMensaje("Saliendo del sistema...");
                    break;
                default:
                    vista.mostrarMensaje("Opción no válida.");
            }
        } while (!salir);
    }

    // ***********************************************************
    // SUB-BUCLES DE GESTIÓN
    // ***********************************************************

    //********* gestion Clientes ************

    private void gestionarClientes() {
        boolean atras = false;

        do {
            // 1. Pedimos a la vista que pinte el menú y nos devuelva qué ha elegido el usuario
            char opcion = vista.mostrarMenuClientes();

            // 2. El controlador decide la acción
            switch (opcion) {
                case '1': // Listar clientes
                    List<Cliente> clientes = repositorio.listarClientes();
                    vista.listarClientes(clientes);
                    break;

                case '2': // Añadir cliente
                    try {
                        // La vista empaqueta el objeto ciego
                        Cliente nuevoCliente = vista.anadirCliente();
                        // El controlador lo guarda en el modelo
                        repositorio.anadirCliente(nuevoCliente);
                        vista.mostrarMensaje("Cliente añadido con éxito.");
                    } catch (ClienteDuplicadoException e) {
                        // Atrapamos tu excepción personalizada y se la pasamos a la vista
                        vista.mostrarMensaje("ERROR: " + e.getMessage());
                    }
                    break;

                case '3': // Editar cliente
                    String dniEDitar = vista.pedirDni();
                    Cliente clienteActual = repositorio.buscarClientePorDni(dniEDitar);

                    if (clienteActual == null) {
                        vista.mostrarMensaje("ERROR: No se ha encontrado ningún cliente con DNI " + dniEDitar);
                    }else{
                        vista.mostrarMensaje("Introduce los nuevos datos para el cliente " + clienteActual.getNombre() + ":");
                        // Reutilizamos el formulario de alta
                        Cliente clienteModificado = vista.anadirCliente();

                        // registramos el nuevo DNI

                        try {
                            //pasamos dni viejo y el objeto con los datos nuevos
                            repositorio.modificarClienteConDni(dniEDitar, clienteModificado);
                            vista.mostrarMensaje("Cliente modificado con éxito.");
                        }catch (ClienteDuplicadoException e) {
                            vista.mostrarMensaje("ERROR: " + e.getMessage());
                        }
                    }
                    break;


                case '4': // Eliminar cliente
                    try {
                        String dni = vista.pedirDni();

                        // comprobamos si cliente existe en el sistema
                        Cliente clienteAEliminar = repositorio.buscarClientePorDni(dni);

                        if (clienteAEliminar == null) {
                            vista.mostrarMensaje("ERROR: No se ha encontrado cliente con DNI " + dni);
                        } else {
                            //si existe intentamos borrarlo (pudiendo saltar la excepcion si tiene reservas
                            repositorio.eliminarCliente(dni);
                            vista.mostrarMensaje("Cliente eliminado con éxito.");
                        }
                    }catch (ClienteConReservaException e) {
                        vista.mostrarMensaje("ERROR: " + e.getMessage());
                    }
                    break;

                case '0': // Volver
                    atras = true;
                    break;

                default:
                    vista.mostrarMensaje("Opción no válida.");
            }
        } while (!atras);
    }


    // ******** gestion de Hoteles *********

    private void gestionarHoteles() {
        boolean atras = false;

        do {
            char opcion = vista.mostrarMenuHoteles();

            switch (opcion) {
                case '1': // Listar hoteles
                    List<Hotel> hoteles = repositorio.listarHoteles();
                    vista.listarHoteles(hoteles);
                    break;

                case '2': // Añadir hotel
                    try {
                        Hotel nuevoHotel = vista.anadirHotel();
                        repositorio.anadirHotel(nuevoHotel);
                        vista.mostrarMensaje("Hotel añadido con éxito.");
                    } catch (HotelDuplicadoException e) {
                        vista.mostrarMensaje("ERROR: " + e.getMessage());
                    }
                    break;

                case '3': // Editar hotel

                    String nombreEditar = vista.pedirNombreHotel();
                    Hotel hotelActual = buscarHotelInterno(nombreEditar);

                    if (hotelActual == null) {
                        vista.mostrarMensaje("ERROR: No se ha encontrado ningún hotel llamado '" + nombreEditar + "'.");
                    } else {
                        vista.mostrarMensaje("Introduce los nuevos datos para el hotel '" + hotelActual.getNombre() + "':");

                        //Reutilizamos el formulario para capturar los datos fácilmente

                        Hotel datosNuevos = vista.anadirHotel();

                        try {
                            //pasamos el nombre viejo y el objeto completo con los datos nuevos
                            repositorio.modificarHotelConNombre(nombreEditar, datosNuevos);
                            vista.mostrarMensaje("El hotel ha sido modificado con éxito.");
                        } catch (HotelDuplicadoException e) {
                            vista.mostrarMensaje("ERROR: " + e.getMessage());
                        }
                    }
                    break;

                case '4': // Eliminar hotel
                    try {
                        String nombreHotel = vista.pedirNombreHotel();

                        // Comprobamos si hotel existe en el sistema
                        Hotel hotelAEliminar = buscarHotelInterno(nombreHotel);
                        if (hotelAEliminar == null) {
                            vista.mostrarMensaje("ERROR: No se ha encontrado ningún hotel '" + nombreHotel + "'.");
                        } else {
                            //si existe, intentamos borrarlo ( aquí puede saltar tu excepción si tiene reservas.
                            repositorio.eliminarHotel(nombreHotel);
                            vista.mostrarMensaje("Hotel eliminado con éxito.");
                        }
                    }catch (HotelConReservaException e) {
                        vista.mostrarMensaje("ERROR: " + e.getMessage());
                    }
                    break;

                case '0': // Volver
                    atras = true;
                    break;

                default:
                    vista.mostrarMensaje("Opción no válida.");
            }
        } while (!atras);
    }

    //********** gestión de habitaciones ***************

    private void gestionarHabitaciones() {
        boolean atras = false;

        do {
            char opcion = vista.mostrarMenuHabitaciones();

            switch (opcion) {
                case '1': // Listar habitaciones de un hotel
                    String nombreBuscar = vista.pedirNombreHotel();
                    Hotel hotelEncontrado = buscarHotelInterno(nombreBuscar);

                    if (hotelEncontrado != null) {
                        List<Habitacion> habs = repositorio.listarHabitacionesHotel(hotelEncontrado);
                        if (habs.isEmpty()) {
                            vista.mostrarMensaje("El hotel no tiene habitaciones registradas aún.");
                        } else {
                            vista.listarHabitaciones(habs);
                        }
                    } else {
                        vista.mostrarMensaje("ERROR: No existe ningún hotel con ese nombre.");
                    }
                    break;

                case '2': // Añadir habitación
                    String nombreHotelDestino = vista.pedirNombreHotel();
                    Hotel hotelDestino = buscarHotelInterno(nombreHotelDestino);

                    if (hotelDestino != null) {
                        Habitacion nuevaHabitacion = vista.anadirHabitacion();
                        if (nuevaHabitacion != null) {
                            hotelDestino.anadirHabitacion(nuevaHabitacion);
                            vista.mostrarMensaje("Habitación añadida al hotel '" + hotelDestino.getNombre() + "' con éxito.");
                        }
                    } else {
                        vista.mostrarMensaje("ERROR: No existe ningún hotel con ese nombre. Registre el hotel primero.");
                    }
                    break;

                case '0': // Volver
                    atras = true;
                    break;

                default:
                    vista.mostrarMensaje("Opción no válida.");
            }
        } while (!atras);
    }

    //************* gestión de Reservas **********************

    private void gestionarReservas() {
        boolean atras = false;

        do {
            char opcion = vista.mostrarMenuReservas();

            switch (opcion) {
                case '1': // Listar reservas
                    List<Reserva> reservas = repositorio.listarReservas();
                    vista.listarReservas(reservas);
                    break;

                case '2': // Añadir reserva
                    Reserva nuevaReserva = vista.anadirReserva();
                    if (nuevaReserva != null) {

                    // validamos que existe el cliente

                    Cliente cliente = repositorio.buscarClientePorDni(nuevaReserva.getDniCliente());
                    if(cliente == null) {
                        vista.mostrarMensaje("ERROR: El cliente con DNI " + nuevaReserva.getDniCliente() + " no está registrado. Debes crearlo primero.");
                        break;
                    }

                    //validamos que existe el hotel

                    Hotel hotel = buscarHotelInterno(nuevaReserva.getNombreHotel());
                    if (hotel == null) {
                        vista.mostrarMensaje(("ERROR: El hotel '" + nuevaReserva.getNombreHotel() + "' no existe en el sistema.")  );
                        break;
                    }

                    //Validamos que la habitacion existe dentro del hotel

                    Habitacion habitacionAsignada = null;
                    for(Habitacion hab : hotel.getHabitaciones()){
                        if (hab.getNumHabitacion() == nuevaReserva.getNumHabitacion()) {
                            habitacionAsignada = hab;
                            break;
                        }
                    }

                    if (habitacionAsignada == null) {
                        vista.mostrarMensaje("ERROR: La habitación " + nuevaReserva.getNumHabitacion() + " no pertenece al hotel. '" + hotel.getNombre() + "'.");
                    break;
                    }

                    //si todoo es correcto calculamos el precio real y guardamos

                    double precioTotal = repositorio.calcularImporteReserva(nuevaReserva, habitacionAsignada);
                    nuevaReserva.setImporteTotal(precioTotal);

                    repositorio.anadirReservas(nuevaReserva);
                    vista.mostrarMensaje("RESERVA CONFIRMADA! REGISTRO GUARDADO CON ÉXITO.\n Importe total: " +  precioTotal + "€");
            }
                    break;


                case '3': // Cancelar reserva
                    System.out.print("Introduce el ID de la reserva a cancelar: ");
                    Scanner sc = new Scanner(System.in);
                    int idBuscar = sc.nextInt();

                    Reserva reservaACancelar = null;
                    //Buscamos la reseerva en nuestra lista
                    for (Reserva r : repositorio.listarReservas()) {
                        if (r.getIdReserva() == idBuscar) {
                            reservaACancelar = r;
                            break;
                        }
                    }
                    if (reservaACancelar == null) {
                        vista.mostrarMensaje("ERROR: No existe ninguna reserva con el id " + idBuscar);
                    }else if (!reservaACancelar.isEstado()){
                        vista.mostrarMensaje("AVISO: Esta reserva ya se encontraba cancelada");
                    }else{
                        //validación de fechas. Verificamos que la fecha actual es anterior a la fecha de entrada
                        Date fechaActual = new Date();
                        if (fechaActual.before(reservaACancelar.getFechaReserva())) {
                            repositorio.cancelarReserva(reservaACancelar);
                            vista.mostrarMensaje("RESERVA " + idBuscar + " CANCELADA");
                        }else{
                            vista.mostrarMensaje("ERROR: No se puede cancelar la reserva porque la fecha de entrada ya ha pasado o es hoy.");
                        }
                    }
                break;

                case '0': // Volver
                    atras = true;
                    break;

                default:
                    vista.mostrarMensaje("Opción no válida.");
            }
        } while (!atras);
    }

    // ********* Metodo auxiliar del Controlador ************

    private Hotel buscarHotelInterno(String nombreHotel) {
        for (Hotel h : repositorio.listarHoteles()) {
            if (h.getNombre().equalsIgnoreCase(nombreHotel)) {
                return h;
            }
        }
        return null;
    }

}