package RoomServDev.view;

import java.util.Scanner;

public class VistaPrincipal {
    private final Scanner scanner;

    public VistaPrincipal(Scanner scanner) {
        this.scanner = scanner;
    }

    public int mostrarMenuPrincipal(){
        System.out.println("\n***********************************");
        System.out.println("*************** SISTEMA DE GESTIÓN HOTELERA *******************");
        System.out.println("\n***********************************");
        System.out.println("1. Módulo Clientes");
        System.out.println("2. Módulo Hotel y Habitaciones");
        System.out.println("3. Módulo Reservas");
        System.out.println("0. Salir de la aplicación");
        System.out.println("\n***********************************");
        System.out.print("Seleccione un módulo: ");
        while(true){
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Introduzca un número: ");
            }
        }
    }

    public void despedida(){
        System.out.println("\n Cerrando aplicación... Hasta pronto!");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }
}
