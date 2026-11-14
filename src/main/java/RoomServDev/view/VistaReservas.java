package RoomServDev.view;

import RoomServDev.model.Reserva;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class VistaReservas {
    private final Scanner input;

    public VistaReservas(Scanner input) {
        this.input = input;
    }

    public char mostrarMenuReservas() {
        System.out.println("\n--- GESTIÓN DE RESERVAS ---");
        System.out.println("1. Listar reservas.");
        System.out.println("2. Añadir reserva.");
        System.out.println("3. Editar reserva.");
        System.out.println("4. Cancelar/Eliminar reserva.");
        System.out.println("0. Volver al menú principal.");
        return pedirOpcion();
    }

    public void listarReservas(List<Reserva> reservas) {
        System.out.println("\n--- Listado de reservas: ---");
        if (reservas == null || reservas.isEmpty()) {
            mostrarMensaje("No hay reservas registradas.");
            return;
        }
        for (Reserva reserva : reservas) {
            System.out.println("- Id: " + reserva.getIdReserva()
                    + " Hotel: " + reserva.getNombreHotel()
                    + " Hab: " + reserva.getNumHabitacion()
                    + " DNI: " + reserva.getDniCliente()
                    + " Entrada: " + reserva.getFechaEntrada()
                    + " Salida: " + reserva.getFechaSalida()
                    + " Importe: " + reserva.getImporteTotal()
                    + " Activa: " + (reserva.isEstado() ? "Sí" : "No"));
        }
    }

    public String pedirDni() {
        return pedirTexto("Introduce el DNI del cliente: ");
    }

    public String pedirNombreHotel() {
        return pedirTexto("Introduce el nombre del hotel: ");
    }

    public int pedirNumHabitacion() {
        return pedirEntero("Introduce el número de habitación: ");
    }

    public int pedirHuespedes() {
        return pedirEntero("Introduce el número de huéspedes: ");
    }

    public int pedirIdReserva() {
        return pedirEntero("Id de la reserva: ");
    }

    public int pedirNuevosHuespedes() {
        return pedirEntero("Nuevo número de huéspedes: ");
    }

    public LocalDate pedirFechaEntrada() {
        return pedirFecha("Fecha de entrada");
    }

    public LocalDate pedirFechaSalida() {
        return pedirFecha("Fecha de salida");
    }

    public LocalDate pedirNuevaFechaEntrada() {
        return pedirFecha("Nueva fecha de entrada");
    }

    public LocalDate pedirNuevaFechaSalida() {
        return pedirFecha("Nueva fecha de salida");
    }

    public String pedirModoCancelacion() {
        return pedirTexto("Escribe C para cancelar o E para eliminar: ");
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

    private LocalDate pedirFecha(String mensaje) {
        System.out.print(mensaje + " (aaaa-mm-dd): ");
        return LocalDate.parse(input.nextLine().trim());
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
