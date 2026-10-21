
import java.time.LocalDate;
import java.sql.Date;
import java.util.Scanner;

import RoomServDev.model.Cliente;
import RoomServDev.model.Doble;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Hotel;
import RoomServDev.model.Reserva;
import RoomServDev.model.Simple;
import RoomServDev.model.Suite;

import RoomServDev.view.vistaMenus;

public class Main {

    Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        Main iniciar = new Main();
        iniciar.programa();

        System.out.println("========================================================");
        System.out.println(" GESTOR DE RESERVAS DE HOTELES - By: ROOM SERVICE DEV");
        System.out.println("========================================================");


        // Crear hotel
        Hotel hotel = new Hotel(
                "Hotel Costa Brava",
                "Carrer Major, 15",
                "Roses",
                972123456
        );

        // Crear habitaciones
        Simple habitacionSimple = new Simple(
                101,
                1,
                75.00,
                "Individual",
                "90x200",
                true
        );

        Doble habitacionDoble = new Doble(
                201,
                3,
                120.00,
                "Matrimonio",
                true,
                25.00,
                true
        );

        Suite suite = new Suite(
                301,
                4,
                220.00,
                "Suite",
                2,
                true,
                true,
                true,
                "Minibar y servicio de habitaciones"
        );

        // Añadir habitaciones al hotel
        hotel.getHabitaciones().add(habitacionSimple);
        hotel.getHabitaciones().add(habitacionDoble);
        hotel.getHabitaciones().add(suite);

        System.out.println("\n--- DATOS DEL HOTEL ---");
        System.out.println(hotel);

        System.out.println("\n--- HABITACIONES DEL HOTEL ---");

        for (Habitacion habitacion : hotel.getHabitaciones()) {
            System.out.println(habitacion);
        }

        // Registrar un cliente
        Cliente cliente = new Cliente(
                "Adria",
                "Rigall",
                "Prat",
                "12345678A",
                612345678,
                "arigallp@uoc.edu"
        );

        System.out.println("\n--- CLIENTE REGISTRADO ---");
        System.out.println(cliente);

        // Preparar las fechas de la reserva
        Date fechaReserva = Date.valueOf(LocalDate.now());
        Date fechaEntrada = Date.valueOf(
                LocalDate.of(2026, 11, 15)
        );
        Date fechaSalida = Date.valueOf(
                LocalDate.of(2026, 11, 18)
        );

        // Crear una reserva para la habitación doble
        Reserva reservaDoble = new Reserva(
                1001,
                fechaReserva,
                fechaEntrada,
                fechaSalida,
                3,
                3 * habitacionDoble.getPrecioNoche(),
                true,
                "Hotel Costa Brava",
                habitacionDoble.getNumHabitacion(),
                cliente.getDni()
        );

        // Crear una reserva para la habitación simple
        Reserva reservaSimple = new Reserva(
                1002,
                fechaReserva,
                fechaEntrada,
                fechaSalida,
                1,
                3 * habitacionSimple.getPrecioNoche(),
                true,
                "Hotel Costa Brava",
                habitacionSimple.getNumHabitacion(),
                cliente.getDni()
        );

        // Mostrar la reserva de la habitación doble
        System.out.println("\n--- RESERVA DE HABITACIÓN DOBLE ---");
        System.out.println(reservaDoble);

        System.out.printf(
                "Importe total: %.2f EUR%n",
                reservaDoble.getImporteTotal()
        );

        // Mostrar la reserva de la habitación simple
        System.out.println("\n--- RESERVA DE HABITACIÓN SIMPLE ---");
        System.out.println(reservaSimple);

        System.out.printf(
                "Importe total: %.2f EUR%n",
                reservaSimple.getImporteTotal()
        );

        System.out.println("\n--- FIN DEL PROGRAMA ---");
    }

    // Inicio del programa principal.
    void programa(){
        vistaMenus vista = new vistaMenus();
        boolean salir = false;
        char opcion;

        do {

            System.out.println("");
            System.out.println("1. Opciones de los clientes");
            System.out.println("2. Opciones de los hoteles.");
            System.out.println("3. Opciones de las habitaciones.");
            System.out.println("4. Opciones de las reservas");
            System.out.println("0. Salir de la aplicación.");
            System.out.println("");

            opcion = pedirOpcionMenus();

            switch (opcion){
                case '1':
                    vista.mostrarMenuClientes();
                    break;
                case '2':
                    vista.mostrarMenuHoteles();
                    break;
                case '3':
                    vista.mostrarMenuHabitaciones();
                    break;
                case '4':
                    vista.mostrarMenuReservas();
                    break;
                case '0':
                    salir = true;
            }

        } while (!salir);
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
