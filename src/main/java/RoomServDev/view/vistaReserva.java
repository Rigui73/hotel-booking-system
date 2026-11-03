package RoomServDev.view;

import java.util.Scanner;


public class vistaReserva {
    private Scanner scanner;

    public vistaReserva(Scanner scanner) {
        this.scanner = scanner;
    }

    public void VistaReserva(Scanner scanner){
        this.scanner = scanner;
    }

    public int mostrarMenu(){
        System.out.println("\n---------------------------------");
        System.out.println("       GESTIÓN DE RESERVAS       ");
        System.out.println("---------------------------------");
        System.out.println("1. Crear nueva reserva");
        System.out.println("2. Consultar reserva por ID");
        System.out.println("3. Listar reservas activas");
        System.out.println("4. Modificar fechas de reserva");
        System.out.println("5. Cancelar reserva");
        System.out.println("0. Volver al menú principal");
        System.out.println("---------------------------------");
        System.out.print("Seleccione una opción: ");
        return leerEntero();
    }

    public int pedirIdReserva(){
        System.out.print("Introduce el ID de la reserva: ");
        return leerEntero();
    }

    public String pedirFechaEntrada(){
        System.out.print("Introduce la fecha de entrada (AAAA-MM-DD): ");
        return scanner.nextLine().trim();
    }

    public String pedirFechaSalida(){
        System.out.print("Introduce la fecha de salida (AAAA-MM-DD): ");
        return scanner.nextLine().trim();
    }

    public void mostrarResumenReserva(int id, String dniCliente, int numHabitacion, String fEntrada, String fSalida, double total) {
        System.out.println("\n**************** RESUMEN DE RESERVA ****************");
        System.out.println("ID Reserva:        " + id);
        System.out.println("DNI Cliente:       " + dniCliente);
        System.out.println("Habitación:        Nº " + numHabitacion);
        System.out.println("Fecha de Entrada:  " + fEntrada);
        System.out.println("Fecha de Salida:   " + fSalida);
        System.out.printf( "Importe Total:     %.2f €%n", total);
        System.out.println("******************************************************");
    }

    public void mostrarMensaje(String mensaje){
        System.out.println("[INFO] " + mensaje);
    }

    public void mostrarError(String error){
        System.out.println("[ERROR] " + error);
    }

    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Introduzca un número: ");
            }
        }
    }
}

