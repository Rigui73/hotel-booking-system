package RoomServDev.view;

import RoomServDev.model.Cliente;

import java.util.List;
import java.util.Scanner;


public class VistaClientes {
    private final Scanner scanner;

    public VistaClientes(Scanner scanner) {
        this.scanner = scanner;
    }

    public int mostrarMenu() {
        System.out.println("\n******************************************");
        System.out.println("*********************************MENU DE CLIENTES*********************************");
        System.out.println("\n******************************************");
        System.out.println("1. Registrar nuevo cliente");
        System.out.println("2. Buscar cliente por DNI");
        System.out.println("3. Listar todos los clientes");
        System.out.println("4. Modificar datos de cliente");
        System.out.println("5. Eliminar datos de cliente");
        System.out.println("0. Volver al menú principal");
        System.out.println("\n******************************************");
        System.out.println("Selecciona una opcion:");
        return leerEntero();
    }


    public String pedirDNI() {
        System.out.print("Introduce el DNI del Cliente: ");
        return scanner.nextLine().trim();
    }

    public String pedirNombre() {
        System.out.print("Introduce el nombre del cliente: ");
        return scanner.nextLine().trim();
    }

    public String pedirApellido1() {
        System.out.print("Introduce el primer apellido: ");
        return scanner.nextLine().trim();
    }

    public String pedirApellido2() {
        System.out.print("Introduce el segundo apellido (Enter si no tiene): ");
        return scanner.nextLine().trim();
    }

    public String pedirTelefono() {
        System.out.print("Introduce el telefono de contacto: ");
        return scanner.nextLine().trim();
    }

    public String pedirEmail() {
        System.out.print("Introduce el email del cliente: ");
        return scanner.nextLine().trim();
    }

    // --- METODO AÑADIDO PARA LA OPCIÓN 3 DEL MENÚ ---
    public void mostrarListaClientes(List<Cliente> clientes) {
        if (clientes == null || clientes.isEmpty()) {
            System.out.println("\n[INFO] No hay clientes registrados actualmente.");
            return;
        }
        System.out.println("\n================================ LISTADO DE CLIENTES ================================");
        for (Cliente c : clientes) {
            String ap2 = (c.getApellido2() == null || c.getApellido2().isBlank()) ? "" : " " + c.getApellido2();
            System.out.printf("- DNI: %-10s | %s %s%s | Tel: %-9d | %s%n",
                    c.getDni(), c.getNombre(), c.getApellido1(), ap2, c.getTelefono(), c.getEmail());
        }
        System.out.println("====================================================================================");
    }




public void mostrarDetalleCliente(Cliente cliente){
    String apellido2 = (cliente.getApellido2() == null || cliente.getApellido2().isBlank())
            ? "" : " " + cliente.getApellido2();
    System.out.println("\n*********************** FICHA DEL CLIENTE ******************************");
    System.out.println("DNI:        " + cliente.getDni());
    System.out.println("Nombre:     " + cliente.getNombre() + " " + cliente.getApellido1() + apellido2);
    System.out.println("Telefono:     " + cliente.getTelefono());
    System.out.println("Email:     " + cliente.getEmail());
    System.out.println("**************************************************************************");
}

public void mostrarMensaje(String mensaje){
    System.out.println("[INFO] " + mensaje);
}

public void mostrarError(String error){
    System.out.println("[ERROR] " + error);
}

private int leerEntero(){
    while(true){
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e){
            System.out.println("Entrada inválida. Introduzca un número: ");
            }
        }
    }
}
