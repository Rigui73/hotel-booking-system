package RoomServDev.controller;

import RoomServDev.model.Cliente;
import RoomServDev.model.ClienteConReservaException;
import RoomServDev.model.ClienteDuplicadoException;
import RoomServDev.model.Datos;
import RoomServDev.view.VistaCliente;

import java.util.List;

public class ControladorClientes {

    // Los datos se guardan en Datos (modelo); el controlador valida y hace de puente con la vista
    private final VistaCliente vista;
    private final Datos datos;

    public ControladorClientes(VistaCliente vista, Datos datos) {
        this.vista = vista;
        this.datos = datos;
    }

    // ********* Crear *********

    public void crearCliente(String nombre, String apellido1, String apellido2,
                             String dni, int telefono, String email) throws ClienteDuplicadoException {
        validarDatos(nombre, apellido1, dni, telefono, email);
        datos.anadirCliente(new Cliente(nombre.trim(), apellido1.trim(), apellido2.trim(),
                dni.trim().toUpperCase(), telefono, email.trim()));
    }

    // ********* Consultar *********

    public List<Cliente> listarClientes() {
        return datos.listarClientes();
    }

    public Cliente buscarCliente(String dni) {
        if (dni == null) return null;
        return datos.buscarClientePorDni(dni.trim().toUpperCase());
    }

    public List<Cliente> buscarClientesPorNombre(String texto) {
        return datos.buscarClientePorNombre(texto);
    }

    // ********* Editar *********

    // dni: el DNI actual, para encontrar al cliente. nuevoDni: el que tendrá después (puede ser el mismo)
    public void editarCliente(String dni, String nuevoDni, String nombre, String apellido1, String apellido2,
                              int telefono, String email) throws ClienteDuplicadoException {
        Cliente cliente = buscarCliente(dni);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe ningún cliente con DNI " + dni);
        }
        validarDatos(nombre, apellido1, nuevoDni, telefono, email);

        String dniAntiguo = cliente.getDni();


        cliente.setNombre(nombre.trim());
        cliente.setApellido1(apellido1.trim());
        cliente.setApellido2(apellido2.trim());
        cliente.setTelefono(telefono);
        cliente.setEmail(email.trim());
        datos.modificarClienteConDni(dniAntiguo, nuevoDni, cliente);
    }

    // ********* Eliminar *********

    public void eliminarCliente(String dni) throws ClienteConReservaException {
        Cliente cliente = buscarCliente(dni);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe ningún cliente con DNI " + dni);
        }
        datos.eliminarCliente(cliente.getDni());
    }

    // ********* Validaciones (privadas) *********

    private void validarDatos(String nombre, String apellido1, String dni, int telefono, String email) {
        if (estaVacio(nombre) || estaVacio(apellido1)) {
            throw new IllegalArgumentException("El nombre y el primer apellido son obligatorios.");
        }
        if (!dniValido(dni)) {
            throw new IllegalArgumentException("El DNI debe tener 8 números y una letra.");
        }
        if (telefono < 100000000 || telefono > 999999999) {
            throw new IllegalArgumentException("El teléfono debe tener 9 cifras.");
        }
        if (!emailValido(email)) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
    }

    private boolean dniValido(String dni) {
        return dni != null && dni.trim().matches("\\d{8}[A-Za-z]");
    }

    private boolean emailValido(String email) {
        return email != null && email.trim().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    public void gestionarMenu() {
        int opcion;
        do {
            opcion = vista.mostrarMenu();
            switch (opcion) {
                case 1 -> registrarClienteDesdeVista();
                case 2 -> consultarClientePorDni();
                case 3 -> listarTodos();
                case 4 -> modificarClienteDesdeVista();
                case 5 -> borrarClienteDesdeVista();
                case 0 -> vista.mostrarMensaje("Volviendo al menú principal...");
                default -> vista.mostrarError("Opción no válida. Introduzca un número del 0 al 5.");
            }
        } while (opcion != 0);
    }

    private void registrarClienteDesdeVista() {
        try {
            String dni = vista.pedirDNI();
            String nombre = vista.pedirNombre();
            String apellido1 = vista.pedirApellido1();
            String apellido2 = vista.pedirApellido2();

            int telefono;
            try {
                telefono = Integer.parseInt(vista.pedirTelefono());
            } catch (NumberFormatException e) {
                vista.mostrarError("El teléfono debe contener únicamente números.");
                return;
            }

            String email = vista.pedirEmail();

            crearCliente(nombre, apellido1, apellido2, dni, telefono, email);
            vista.mostrarMensaje("Cliente registrado con éxito.");
        } catch (ClienteDuplicadoException | IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void consultarClientePorDni() {
        String dni = vista.pedirDNI();
        Cliente c = buscarCliente(dni);
        if (c != null) {
            String ap2 = (c.getApellido2() == null || c.getApellido2().isBlank()) ? "" : " " + c.getApellido2();
            vista.mostrarDetalleCliente(
                    c.getDni(),
                    c.getNombre() + " " + c.getApellido1() + ap2,
                    String.valueOf(c.getTelefono()),
                    c.getEmail()
            );
        } else {
            vista.mostrarError("No se encontró ningún cliente con DNI " + dni);
        }
    }

    private void listarTodos() {
        List<Cliente> lista = listarClientes();
        vista.mostrarListaClientes(lista);
    }

    private void modificarClienteDesdeVista() {
        String dniActual = vista.pedirDNI();
        Cliente c = buscarCliente(dniActual);

        if (c == null) {
            vista.mostrarError("No existe ningún cliente registrado con DNI " + dniActual);
            return;
        }

        vista.mostrarMensaje("Introduzca los nuevos datos para el cliente (DNI actual: " + dniActual + "):");
        try {
            String nuevoDni = vista.pedirDNI();
            String nombre = vista.pedirNombre();
            String apellido1 = vista.pedirApellido1();
            String apellido2 = vista.pedirApellido2();

            int telefono;
            try {
                telefono = Integer.parseInt(vista.pedirTelefono());
            } catch (NumberFormatException e) {
                vista.mostrarError("El teléfono debe contener únicamente números.");
                return;
            }

            String email = vista.pedirEmail();

            editarCliente(dniActual, nuevoDni, nombre, apellido1, apellido2, telefono, email);
            vista.mostrarMensaje("Cliente modificado correctamente.");
        } catch (ClienteDuplicadoException | IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void borrarClienteDesdeVista() {
        String dni = vista.pedirDNI();
        try {
            eliminarCliente(dni);
            vista.mostrarMensaje("Cliente con DNI " + dni + " eliminado satisfactoriamente.");
        } catch (ClienteConReservaException | IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    }

