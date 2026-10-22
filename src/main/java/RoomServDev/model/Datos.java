package RoomServDev.model;

// ********* librerias *********

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class Datos {

    // ********* Java Generics y estr. dinámicas *********

    // *********Map para clientes: busqueda rápida por DNI (Clave:String, Valor:Cliente) *********

    private Map<String, Cliente> clientes;

    // *********List para hoteles y reservas: Mantiene orden de inserción y permite iterar *********

    private List<Hotel> hoteles;
    private List<Reserva> reservas;

    // ********* Constructor  *********

    public Datos() {
        this.clientes = new HashMap<>();
        this.hoteles = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    //  ********************************************* Métodos para gestionar los clientes  *********************************************

    // 1. LISTA TODOS LOS CLIENTES.

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes.values());
    }

    //2. BUSCA CLIENTES POR NOMBRE / APELLIDOS

    public List<Cliente> buscarClientePorNombre(String textoBusqueda) {
        List<Cliente> clientesEncontrados = new ArrayList<>();
        //Pasamos el texto a minusculas para que la búsqueda no falle por mayúsculas
        String textoNormalizado = textoBusqueda.toLowerCase();

        for (Cliente cliente : clientes.values()) {
            //Buscamos si el texto coincide parcial o totalmente con nombre o apellidos
            if (cliente.getNombre().toLowerCase().contains(textoNormalizado) ||
                    cliente.getApellido1().toLowerCase().contains(textoNormalizado) ||
                    cliente.getApellido2().toLowerCase().contains(textoNormalizado)) {

                clientesEncontrados.add(cliente);
            }
        }
        return clientesEncontrados;
    }


    //3.- BUSCA CLIENTE POR DNI

    public Cliente buscarClientePorDni(String dni) {
        // devuelve el objeto Cliente o null si no encuentra el dni
        return clientes.get(dni);
    }


    //4. AÑADE NUEVO CLIENTE

    public void anadirCliente(Cliente cliente) throws ClienteDuplicadoException {

        // Excpt1. Existe el DNI?

        if (clientes.containsKey(cliente.getDni())) {
            throw new ClienteDuplicadoException("El cliente con DNI " + cliente.getDni() + " ya está registrado");
        }
        clientes.put(cliente.getDni(), cliente);
    }

    //5.- MODIFICA DATOS DE UN CLIENTE

    public void modificarCliente(Cliente clienteModificado) {
        //en un Map si hacemos "put" de un DNI ya existente, sobreescribirá los datos antiguos con el nuevo objeto
        if (clientes.containsKey(clienteModificado.getDni())) {
            clientes.put(clienteModificado.getDni(), clienteModificado);
        }
    }


    //6. ELIMINA CLIENTE SIEMPRE QUE SEA POSIBLE

    public void eliminarCliente(String dni) throws ClienteConReservaException {
        // obtenemos todas las reservas del cliente
        List<Reserva> reservasDelCliente = consultarReservasCliente(dni);

        // comprobamos si hay alguna reserva activa
        for (Reserva reserva : reservasDelCliente) {
            // isEstado = True será que la reserva está en curso
            if (reserva.isEstado()) {
                throw new ClienteConReservaException("Error: El cliente con DNI " + dni + " no puede ser eliminado porque tiene una reserva en curso.");
            }
        }
        // si no salta la excepción se puede eliminar el cliente

        clientes.remove(dni);
    }

    //7.- CAMBIA EL DNI DE UN CLIENTE

    public void cambiarDniCliente(String dniActual, String dniNuevo) throws ClienteDuplicadoException {
        Cliente cliente = clientes.get(dniActual);

        // si el cliente no existe o el DNI es el mismo, no hay nada que cambiar
        if (cliente == null || dniActual.equals(dniNuevo)) {
            return;
        }

        // el nuevo DNI no puede pertenecer a otro cliente
        if (clientes.containsKey(dniNuevo)) {
            throw new ClienteDuplicadoException("El cliente con DNI " + dniNuevo + " ya está registrado");
        }

        // el DNI es la clave del Map: hay que sacar al cliente y volver a meterlo con la nueva clave
        clientes.remove(dniActual);
        cliente.setDni(dniNuevo);
        clientes.put(dniNuevo, cliente);

        // las reservas guardan el DNI del cliente, así que también hay que actualizarlas
        for (Reserva reserva : reservas) {
            if (reserva.getDniCliente().equalsIgnoreCase(dniActual)) {
                reserva.setDniCliente(dniNuevo);
            }
        }
    }


    // ********************************************* Métodos para la gestión de hoteles  *********************************************


    //1.-MOSTRAR LISTADO DE HOTELES

    public List<Hotel> listarHoteles() {
        return hoteles;
    }

    //2.- BUSCAR HOTELES POR CIUDAD

    public List<Hotel> buscarHotelesPorCiudad(String ciudad) {
        List<Hotel> hotelesEncontrados = new ArrayList<>();
        for (Hotel hotel : hoteles) {
            if (hotel.getCiudad().equalsIgnoreCase(ciudad)) {
                hotelesEncontrados.add(hotel);
            }
        }
        return hotelesEncontrados;
    }

    //3.- LISTAR HABITACIONES DE UN HOTEL CONCRETO

    public List<Habitacion> listarHabitacionesHotel(Hotel hotel) {
        return hotel.getHabitaciones();
    }

    //4.- MOSTRAR HABITACIONES DE UNA DETERMINADA CATEGORIA

    public List<Habitacion> mostrarHabitacionesPorCategoria(String categoria) {
        List<Habitacion> habitacionesFiltradas = new ArrayList();

        for (Hotel hotel : hoteles) {
            for (Habitacion habitacion : hotel.getHabitaciones()) {
                if (habitacion.getCategoria().equalsIgnoreCase(categoria)) {
                    habitacionesFiltradas.add(habitacion);
                }
            }
        }
        return habitacionesFiltradas;
    }


    //5.- MOSTRAR HABITACIONES DISPONIBLES DE UN HOTEL

    public List<Habitacion> mostrarHabitacionesDisponibles(Hotel hotel) {
        List<Habitacion> disponibles = new ArrayList<>();
        for (Habitacion habitacion : hotel.getHabitaciones()) {
            if (habitacion.isDisponible()) {
                disponibles.add(habitacion);
            }
        }
        return disponibles;
    }


    //6.- CONSULTAR NUMERO HABITACIONES DISPONIBLES EN UN HOTEL

    public int contarHabitacionesDisponibles(Hotel hotel) {
        return mostrarHabitacionesDisponibles(hotel).size();
    }

    //7.- BUSCAR HABITACIONES DISPONIBLES PARA UNAS DETERMINADAS FECHAS

    public List<Habitacion> buscarHabitacionesFechas(Date fechaEntradaSolicitada, Date fechaSalidaSolicitada) {

        List<Habitacion> habitacionesLibres = new ArrayList<>();

        //recorremos con un bucle todas las habitaciones de todos los hoteles

        for (Hotel hotel : hoteles) {
            for (Habitacion habitacion : hotel.getHabitaciones()) {

                //asumimos que la habitacion esta disponible hasta que se demuestre lo contrario
                boolean estaDisponible = true;

                //revisamos todas las reservas
                for (Reserva reserva : reservas) {

                    // la reserva es para la habitacion seleccionada del hotel seleccionado?
                    //incluimos aqui getNumHabitacion() y getNombre en Reserva

                    if (reserva.getNombreHotel().equalsIgnoreCase(hotel.getNombre()) &&
                            reserva.getNumHabitacion() == habitacion.getNumHabitacion()) {
                        //Validación de fechas
                        if (fechaEntradaSolicitada.before(reserva.getFechaSalida()) &&
                                fechaSalidaSolicitada.after(reserva.getFechaEntrada())) {

                            estaDisponible = false;
                            break;//salimos del bucle
                        }
                    }
                }
                //si después de mirar todas las reservas, la bandera sigue en true, la añadimos
                if (estaDisponible) {
                    habitacionesLibres.add(habitacion);
                }
            }
        }
        return habitacionesLibres;
    }

    //8.- FILTRAR HABITACIONES POR NUMERO DE PERSONAS (CAPACIDAD)

    public List<Habitacion> filtrarHabitacionesPorHuespedes(int numHuespedes) {
        List<Habitacion> habitacionesFiltradas = new ArrayList<>();
        for (Hotel hotel : hoteles) {
            for (Habitacion habitacion : hotel.getHabitaciones()) {
                if (habitacion.getCapacidad() >= numHuespedes) {
                    habitacionesFiltradas.add(habitacion);
                }
            }
        }
        return habitacionesFiltradas;
    }


    //9.- AÑADIR NUEVO HOTEL

    public void anadirHotel(Hotel hotel) throws HotelDuplicadoException {

        for (Hotel h : hoteles) {
            if (h.getNombre().equalsIgnoreCase(hotel.getNombre()) &&
                    h.getCiudad().equalsIgnoreCase(hotel.getCiudad())) {
                throw new HotelDuplicadoException("El hotel " + hotel.getNombre() + " ya está registrado en " + hotel.getCiudad());
            }
        }

        hoteles.add(hotel);
    }

    //10.- MODIFICAR HOTEL

    public void modificarHotel(String nombreHotelOriginal, String nuevaDireccion, String nuevaCiudad, int nuevoTelefono) {

        //recorremos la lista buscando el hotel por su nombre
        for (Hotel hotel : hoteles) {
            if (hotel.getNombre().equalsIgnoreCase(nombreHotelOriginal)) {

                //cuando lo encontramos, usamos los setters para actualizar sus datos
                hotel.setDireccion(nuevaDireccion);
                hotel.setCiudad(nuevaCiudad);
                hotel.setTelefono(nuevoTelefono);

                //como ya lo hemos encontrado y modificado, rompemos el bucle
                break;
            }
        }
    }

    //10.- ELIMINAR HOTEL

    public void eliminarHotel(String nombreHotel) throws HotelConReservaException {
        // Comprobamos si hotel tiene alguna reserva activa
        for (Reserva reserva : reservas) {
            //buscamos si la reserva pertenece al hotel y si el estado es true
            if (reserva.getNombreHotel().equalsIgnoreCase(nombreHotel) && reserva.isEstado()) {
                throw new HotelConReservaException("Error: No se puede eliminar el hotel '" + nombreHotel + "' porque tiene reservas en curso.");
            }
        }
        Hotel hotelAEliminar = null;
        for (Hotel hotel : hoteles) {
            if (hotel.getNombre().equalsIgnoreCase(nombreHotel)) {
                hotelAEliminar = hotel;
                break; //Encontrado! Salimos del bucle
            }
        }
        //Borrado del hotel de la lista de hoteles
        if (hotelAEliminar != null) {
            hoteles.remove(hotelAEliminar);
        }
    }


    // ********************************************* Métodos para la gestión de reservas *********************************************


    //1- AÑADIR RESERVAS

    public void anadirReservas(Reserva reserva) {
        reservas.add(reserva);
    }

    public List<Reserva> listarReservas() {
        return reservas;
    }


    //2- CALCULAR IMPORTE DE LA RESERVA

    public double calcularImporteReserva(Reserva reserva, Habitacion habitacion) {
        //obtenemos tiempo entre la fecha entrada y fecha salida
        long diferenciamlsg = reserva.getFechaSalida().getTime() - reserva.getFechaEntrada().getTime();

        //convertimos los milisegundos a dias usando TimeUnit de Java
        long diasReserva = java.util.concurrent.TimeUnit.DAYS.convert(diferenciamlsg, java.util.concurrent.TimeUnit.MILLISECONDS);

        //Multiplicamos los dias por el precio por noche de la habitacion
        return diasReserva * habitacion.getPrecioNoche();
    }

    //3.- CONSULTAR RESERVA POR DNI CLIENTE

    public List<Reserva> consultarReservasCliente(String dniCliente) {
        List<Reserva> reservasDelCliente = new ArrayList<>();

        for (Reserva reserva : reservas) {
            if (reserva.getDniCliente().equalsIgnoreCase(dniCliente)) {
                reservasDelCliente.add(reserva);
            }
        }
        return reservasDelCliente;
    }

    //4.- MODIFICAR RESERVAS

    public void modificarReserva(Reserva reservaOriginal, Date nuevaEntrada, Date nuevaSalida, int nuevosHuespedes) throws ReservaSolapadaException {
        for (Reserva reservaExistente : reservas) {
            // verificamos que sea la misma habitacion del mismo hotel
            // verificamos que reserva esta ACTIVA y que no sea la misma reservas que estamos modificando
            if (reservaExistente != reservaOriginal &&
                    reservaExistente.isEstado() &&
                    reservaExistente.getNombreHotel().equalsIgnoreCase(reservaOriginal.getNombreHotel()) &&
                    reservaExistente.getNumHabitacion() == reservaOriginal.getNumHabitacion()) {

                //Comprobamos si las nuevas fechas pisan la reserva existente
                if (nuevaEntrada.before(reservaExistente.getFechaSalida()) &&
                        nuevaSalida.after(reservaExistente.getFechaEntrada())) {

                    // si fechas son coincidentes lanzamos Excepción personalizada
                    throw new ReservaSolapadaException("No se puede modificar: las nuevas fechas se solapan con otra reserva para esta habitación");
                }
            }
        }
        //si el bucle termina sin encontrar bloqueos por fechas, está garantizada la actualización de los datos de la reserva.
        reservaOriginal.setFechaEntrada(nuevaEntrada);
        reservaOriginal.setFechaSalida(nuevaSalida);
        reservaOriginal.setNumHuespedes(nuevosHuespedes);
    }


    //5.- CANCELAR RESERVAS

    public void cancelarReserva(Reserva reserva) {
        //Cambiamos el estado de la reserva a false (inactiva/cancelada)
        reserva.setEstado(false);
    }
}
