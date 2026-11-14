package RoomServDev.view;

import java.util.Scanner;

/** Main console menu and application-level navigation messages. */
public class VistaMenus {
    private final Scanner input;

    public VistaMenus(Scanner scanner) {
        this.input = scanner;
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("[ERROR] " + error);
    }

    public char mostrarMenuPrincipal() {
        System.out.println("\n========================================================");
        System.out.println("GESTOR DE RESERVAS DE HOTELES - By: ROOM SERVICE DEV");
        System.out.println("========================================================");
        System.out.println("1. Clientes");
        System.out.println("2. Hoteles");
        System.out.println("3. Habitaciones");
        System.out.println("4. Reservas");
        System.out.println("0. Salir de la aplicación.");
        System.out.println("========================================================");
        System.out.print("Elige una opción: ");
        String respuesta = input.nextLine().trim();
        return respuesta.isEmpty() ? ' ' : respuesta.charAt(0);
    }
}
