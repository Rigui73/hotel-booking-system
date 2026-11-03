package RoomServDev.view;

import java.util.Scanner;

public class VistaHotel {
    private final Scanner scanner;

    public VistaHotel(Scanner scanner) {
        this.scanner = scanner;
    }


    public int mostrarMenu(){
        System.out.println("\n************************************************");
        System.out.println("*  GESTIÓN HOTEL / HABITACIONES ");
        System.out.println("---------------------------------");
        System.out.println("1. Alta de nueva habitación");
        System.out.println("2. Consultar habitación por número");
        System.out.println("3. Listar todas las habitaciones");
        System.out.println("4. Cambiar estado/precio de habitación");
        System.out.println("5. Eliminar habitación");
        System.out.println("0. Volver al menú principal");
        System.out.println("---------------------------------");
        System.out.print("Seleccione una opción: ");
        return leerEntero();
    }

    public int pedirNumeroHabitacion(){
        System.out.println("Introduce el número de la habitación: ");
        return leerEntero();
    }

    public int pedirTipoHabitacion(){
        System.out.println("Introduce el tipo(Individual, Doble, Suite): ");
        return scanner.nextInt();
    }

    public double pedirPrecioNoche(){
        while(true){
            System.out.print("Introduce el precio noche: ");
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Error: Introduce un valor numérico decimal. ");
            }
        }
    }

    public void mostrarMensaje(String mensaje){
        System.out.println("[INFO] " + mensaje);
    }

    public void mostrarError(String error){
        System.err.println("[ERROR] " + error);
    }
    public int leerEntero(){
        while(true){
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Introduzca un número: ");
            }
        }
    }
}
