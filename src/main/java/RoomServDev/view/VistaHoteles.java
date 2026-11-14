package RoomServDev.view;

import RoomServDev.model.Hotel;

import java.util.List;
import java.util.Scanner;

public class VistaHoteles {
    private final Scanner input;

    public VistaHoteles(Scanner input) {
        this.input = input;
    }

    public char mostrarMenuHoteles() {
        System.out.println("\n--- GESTIÓN DE HOTELES ---");
        System.out.println("1. Listar todos los hoteles.");
        System.out.println("2. Añadir hotel.");
        System.out.println("3. Editar hotel.");
        System.out.println("4. Eliminar hotel.");
        System.out.println("0. Volver al menú principal.");
        return pedirOpcion();
    }

    public void listarHoteles(List<Hotel> hoteles) {
        System.out.println("\n--- Listado de hoteles disponibles: ---");
        if (hoteles == null || hoteles.isEmpty()) {
            mostrarMensaje("No hay hoteles registrados.");
            return;
        }
        for (Hotel hotel : hoteles) {
            System.out.println("- " + hotel.getNombre() + " (" + hotel.getCiudad() + ") | Tel: "
                    + hotel.getTelefono() + " | Dir: " + hotel.getDireccion());
        }
    }

    public String pedirNombreHotel() {
        return pedirTexto("Introduce el nombre del hotel: ");
    }

    public String pedirDireccionHotel() {
        return pedirTexto("Introduce la dirección del hotel: ");
    }

    public String pedirCiudadHotel() {
        return pedirTexto("Introduce la ciudad del hotel: ");
    }

    public int pedirTelefonoHotel() {
        return pedirEntero("Introduce el teléfono del hotel: ");
    }

    public String pedirNuevaDireccion() {
        return pedirTexto("Introduce la nueva dirección: ");
    }

    public String pedirNuevaCiudad() {
        return pedirTexto("Introduce la nueva ciudad: ");
    }

    public int pedirNuevoTelefono() {
        return pedirEntero("Introduce el nuevo teléfono: ");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("[ERROR] " + error);
    }

    private String pedirTexto(String mensaje) {
        System.out.print(mensaje);
        return input.nextLine().trim();
    }

    private int pedirEntero(String mensaje) {
        System.out.print(mensaje);
        return Integer.parseInt(input.nextLine().trim());
    }

    private char pedirOpcion() {
        System.out.print("Elige una opción: ");
        String respuesta = input.nextLine().trim();
        return respuesta.isEmpty() ? ' ' : respuesta.charAt(0);
    }
}
