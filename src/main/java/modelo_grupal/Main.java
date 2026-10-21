package modelo_grupal;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================================");
        System.out.println(" DEMOSTRACIÓN DEL MODELO DE DOMINIO (ENTREGA GRUPAL)  ");
        System.out.println("======================================================\n");

        // 1. Instanciación del Hotel
        Hotel hotel = new Hotel("Hotel Costa Daurada", "Passeig Marítim, 12", "Cambrils", 977360000);
        System.out.println("1. HOTEL REGISTRADO:");
        System.out.println("   " + hotel);

        // 2. Instanciación polimórfica de Habitaciones
        Habitacion habSimple = new Simple(101, 1, 55.0, "90 cm", true);
        Habitacion habDoble = new Doble(201, 2, 85.0, "Matrimonial", true, 20.0, true);
        Habitacion habSuite = new Suite(301, 4, 190.0, 2, true, true, true, "Minibar premium y albornoz");

        // Composición Hotel -> Habitaciones
        hotel.getHabitaciones().add(habSimple);
        hotel.getHabitaciones().add(habDoble);
        hotel.getHabitaciones().add(habSuite);

        System.out.println("\n2. HABITACIONES ASIGNADAS AL HOTEL (Composición 1 -> *):");
        for (Habitacion h : hotel.getHabitaciones()) {
            System.out.println("   - [" + h.getClass().getSimpleName() + "] Hab. Nº "
                    + h.getNumHabitacion() + " | Aforo máx: " + h.getCapacidadMax()
                    + " pers. | Tarifa/noche: " + h.getPrecioNoche() + " €");
        }

        // 3. Instanciación del Cliente
        Cliente cliente = new Cliente("Marta", "García", "López", "48765432X", 654987321, "marta.garcia@example.com");
        System.out.println("\n3. CLIENTE TITULAR:");
        System.out.println("   " + cliente);

        // 4. Instanciación y gestión directa de la Reserva
        LocalDate entrada = LocalDate.now();
        LocalDate salida = entrada.plusDays(4); // 4 noches
        long numNoches = ChronoUnit.DAYS.between(entrada, salida);

        Reserva reserva = new Reserva(1, LocalDate.now(), entrada, salida, 2, 0.0, true, cliente);

        // Agregación Reserva -> Habitaciones (asociamos la suite)
        reserva.getHabitaciones().add(habSuite);

        // Cálculo manual del importe total en base a noches y precioNoche de las habitaciones asociadas
        double total = 0.0;
        for (Habitacion hab : reserva.getHabitaciones()) {
            total += hab.getPrecioNoche() * numNoches;
        }
        reserva.setImporteTotal(total);

        System.out.println("\n4. RESERVA FORMALIZADA (Asociación con Cliente y Agregación con Habitación):");
        System.out.println("   ID Reserva     : " + reserva.getIdReserva());
        System.out.println("   Titular        : " + reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellido1());
        System.out.println("   Fechas         : Del " + reserva.getFechaEntrada() + " al " + reserva.getFechaSalida() + " (" + numNoches + " noches)");
        System.out.println("   Habitación     : Nº " + habSuite.getNumHabitacion() + " (" + habSuite.getClass().getSimpleName() + ")");
        System.out.println("   Importe Total  : " + reserva.getImporteTotal() + " €");
        System.out.println("   Estado activa  : " + reserva.isEstado());

        // Demostración de cambio de estado (cancelación)
        reserva.setEstado(false);
        System.out.println("\n5. CANCELACIÓN DE RESERVA:");
        System.out.println("   Estado tras cancelación: " + (reserva.isEstado() ? "Activa" : "Cancelada (false)"));
        System.out.println("   Nota: La habitación física sigue existiendo en el hotel (Agregación débil).");
        System.out.println("======================================================");
    }
}