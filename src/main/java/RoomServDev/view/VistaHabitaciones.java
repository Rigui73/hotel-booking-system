package RoomServDev.view;

import RoomServDev.model.Habitacion;

import java.util.List;
import java.util.Scanner;

public class VistaHabitaciones {
    private final Scanner input;

    public VistaHabitaciones(Scanner input) {
        this.input = input;
    }

    public char mostrarMenuHabitaciones() {
        System.out.println("\n--- GESTIÓN DE HABITACIONES ---");
        System.out.println("1. Listar habitaciones.");
        System.out.println("2. Añadir habitación.");
        System.out.println("3. Editar habitación.");
        System.out.println("4. Eliminar habitación.");
        System.out.println("0. Volver al menú principal.");
        return pedirOpcion();
    }

    public void listarHabitaciones(List<Habitacion> habitaciones) {
        System.out.println("\n--- Listado de habitaciones: ---");
        if (habitaciones == null || habitaciones.isEmpty()) {
            mostrarMensaje("No hay habitaciones para mostrar.");
            return;
        }
        for (Habitacion habitacion : habitaciones) {
            System.out.println("- Habitación Nº " + habitacion.getNumHabitacion() + " | Capacidad: "
                    + habitacion.getCapacidad() + " pers. | Precio: " + habitacion.getPrecioNoche()
                    + "€ | Disp: " + (habitacion.isDisponible() ? "Sí" : "No"));
        }
    }

    public String pedirNombreHotel() {
        return pedirTexto("Introduce el nombre del hotel: ");
    }

    public int pedirNumHabitacion() {
        return pedirEnteroValidado("Introduce el número de habitación: ",
                "Entrada inválida. Debe introducir un número entero.");
    }

    public int pedirCapacidad() {
        return pedirEntero("Introduce la capacidad: ");
    }

    public int pedirNuevaCapacidad() {
        return pedirEntero("Nueva capacidad: ");
    }

    public double pedirPrecio() {
        System.out.print("Introduce el precio por noche: ");
        while (true) {
            String valor = input.nextLine().trim();
            try {
                return Double.parseDouble(valor);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe introducir un importe numérico.");
                System.out.print("Introduce el precio por noche: ");
            }
        }
    }

    public double pedirNuevoPrecio() {
        System.out.print("Introduce el precio por noche: ");
        return Double.parseDouble(input.nextLine().trim());
    }

    public boolean pedirDisponibilidad() {
        return pedirBooleano("¿Está disponible? (true/false): ");
    }

    public boolean pedirEstadoOperativo() {
        return pedirBooleano("¿Está operativa? (true/false): ");
    }

    public String pedirCategoria() {
        return pedirTexto("Introduce la categoría (Simple, Doble, Suite): ");
    }

    public boolean pedirBalcon() {
        return pedirBooleano("¿Tiene balcón? (true/false): ");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("[ERROR] " + error);
    }

    private int pedirEntero(String mensaje) {
        System.out.print(mensaje);
        return Integer.parseInt(input.nextLine().trim());
    }

    private int pedirEnteroValidado(String mensaje, String error) {
        System.out.print(mensaje);
        while (true) {
            String valor = input.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println(error);
                System.out.print(mensaje);
            }
        }
    }

    private boolean pedirBooleano(String mensaje) {
        System.out.print(mensaje);
        return Boolean.parseBoolean(input.nextLine().trim());
    }

    private String pedirTexto(String mensaje) {
        System.out.print(mensaje);
        return input.nextLine().trim();
    }

    private char pedirOpcion() {
        System.out.print("Elige una opción: ");
        String respuesta = input.nextLine().trim();
        return respuesta.isEmpty() ? ' ' : respuesta.charAt(0);
    }
}
