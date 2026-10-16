import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        System.out.println("========================================================");
        System.out.println(" GESTOR DE RESERVAS DE HOTELES - By: DE ROOM SERVICE DEV");
        System.out.println("========================================================");

        // Crear hotel
        Hotel hotel = new Hotel("Hotel Costa Brava","Carrer Major, 15","Roses",972123456);

        // Crear habitaciones
        Simple habitacionSimple = new Simple(101, 1, 75.00, "Individual", true);
        Doble habitacionDoble = new Doble(201, 3, 120.00, "Matrimonio", true, 25.00, true);
        Suite suite = new Suite(301, 4, 220.00, 2, true, true, true,"Minibar y servicio de habitaciones");

        hotel.getHabitaciones().add(habitacionSimple);
        hotel.getHabitaciones().add(habitacionDoble);
        hotel.getHabitaciones().add(suite);

        System.out.println("\n--- DATOS DEL HOTEL ---");
        System.out.println(hotel);

        System.out.println("\n--- HABITACIONES DISPONIBLES ---");
        for (Habitacion habitacion : hotel.getHabitaciones()) {
            System.out.println(habitacion);
        }

        // Registrar un cliente
        Cliente cliente = new Cliente("Asria","Rigall","Prat","12345678A",612345678,"arigallp@uoc.edu");

        System.out.println("\n--- CLIENTE REGISTRADO ---");
        System.out.println(cliente);

        // Crear una reserva
        Reserva reserva = new Reserva(1001,LocalDate.now(),LocalDate.of(2026, 11, 15),LocalDate.of(2026, 11, 18),3,3 * (habitacionDoble.getPrecioNoche()
                + habitacionSimple.getPrecioNoche()),true,cliente);

        // Asociar las habitaciones a la reserva
        reserva.getHabitaciones().add(habitacionDoble);
        reserva.getHabitaciones().add(habitacionSimple);

        // Mostrar reserva final
        System.out.println("\n--- RESERVA ---");
        System.out.println("Número de reserva: "
                + reserva.getIdReserva());
        System.out.println("Cliente: "
                + reserva.getCliente().getNombre() + " "
                + reserva.getCliente().getApellido1());
        System.out.println("DNI: "
                + reserva.getCliente().getDni());
        System.out.println("Fecha de reserva: "
                + reserva.getFechaReserva());
        System.out.println("Fecha de entrada: "
                + reserva.getFechaEntrada());
        System.out.println("Fecha de salida: "
                + reserva.getFechaSalida());
        System.out.println("Número de noches: 3");
        System.out.println("Número de huéspedes: "
                + reserva.getNumHuespedes());

        System.out.println("\nHabitaciones reservadas:");

        for (Habitacion habitacion : reserva.getHabitaciones()) {
            System.out.printf(
                    "Habitación %d - %.2f EUR/noche%n",
                    habitacion.getNumHabitacion(),
                    habitacion.getPrecioNoche()
            );
        }

        System.out.printf(
                "%nImporte total: %.2f EUR%n",
                reserva.getImporteTotal()
        );

        System.out.println("Estado: "
                + (reserva.isEstado() ? "Confirmada" : "Cancelada"));
    }
}