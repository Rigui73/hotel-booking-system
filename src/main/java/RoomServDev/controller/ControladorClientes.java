package RoomServDev.controller;

import RoomServDev.model.Cliente;
import RoomServDev.model.ClienteConReservaException;
import RoomServDev.model.ClienteDuplicadoException;
import RoomServDev.persistence.dao.ClienteDAO;
import RoomServDev.view.VistaCliente;
import java.util.List;

public class ControladorClientes {

    // atributos
    private final VistaCliente vista;
    private final ClienteDAO clienteDAO;


    public ControladorClientes(VistaCliente vista, ClienteDAO clienteDAO) {
        this.vista = vista;
        this.clienteDAO = clienteDAO;
    }

    // ********* Crear *********

    public void crearCliente(String nombre, String apellido1, String apellido2,
                             String dni, int telefono, String email) throws ClienteDuplicadoException, java.sql.SQLException {
        validarDatos(nombre, apellido1, dni, telefono, email);
        String ap2 = (apellido2 != null) ? apellido2.trim() : null;

        try {
            clienteDAO.insertar(new Cliente(nombre.trim(), apellido1.trim(), ap2, dni.trim().toUpperCase(), telefono, email.trim()));
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            throw new ClienteDuplicadoException("El DNI " + dni + " ya existe en el sistema");
        }
    }

    // ********* Consultar *********

    public List<Cliente> listarClientes() throws Exception {
        return clienteDAO.obtenerTodosClientes();
    }

    public Cliente buscarCliente(String dni) throws Exception {
        if (dni == null) return null;
        return clienteDAO.obtenerPorDni(dni.trim().toUpperCase());
    }

    //CREAR ESTE NUEVO METODO SI PROCEDE : OBTENER CLIENTEPORNOMBRE

    // ********* Editar *********

    // dni: el DNI actual, para encontrar al cliente. nuevoDni: el que tendrá después (puede ser el mismo)
    public void editarCliente(String dniActual, String nuevoDni, String nombre, String apellido1, String apellido2, int telefono, String email) throws Exception {
        Cliente cliente = buscarCliente(dniActual);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe ningún cliente con DNI " + dniActual);
        }
        validarDatos(nombre, apellido1, nuevoDni, telefono, email);

        String ap2 = (apellido2 != null) ? apellido2.trim() : null;
        Cliente clienteActualizado = new Cliente(nombre.trim(), apellido1.trim(), ap2,
                nuevoDni.trim().toUpperCase(), telefono, email.trim());

        try {
            clienteDAO.actualizar(dniActual, clienteActualizado);
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            throw new ClienteDuplicadoException("El nuevo DNI " + nuevoDni + " ya existe en el sistema");
        }
    }

    // ********* Eliminar *********

    public void eliminarCliente(String dni) throws Exception {
        Cliente cliente = buscarCliente(dni);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe ningún cliente con DNI " + dni);
        }
        try {
            clienteDAO.eliminar(cliente.getDni());
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            throw new ClienteConReservaException("No se puede eliminar el cliente con DNI " + dni + " porque tiene reservas asociadas.");
        }
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
        }catch (java.sql.SQLException e) {
            vista.mostrarError("Error de base de datos al registrar el cliente: " + e.getMessage());
        }catch (Exception e) {
            vista.mostrarError("Error inesperado al registrar el cliente: " + e.getMessage());
        }
    }

    private void consultarClientePorDni() {
        String dni = vista.pedirDNI();

        try {
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
        } catch (Exception e) {
            vista.mostrarError("Error al consultar cliente: " + e.getMessage());
        }
    }

    private void listarTodos() {
        try {
            List<Cliente> lista = listarClientes();
            vista.mostrarListaClientes(lista);
        } catch (Exception e) {
            vista.mostrarError("Error al listar los clientes desde MySQL: " + e.getMessage());
        }
    }

    private void modificarClienteDesdeVista() {
        String dniActual = vista.pedirDNI();

        try {
            Cliente c = buscarCliente(dniActual);
            if (c == null) {
                vista.mostrarError("No existe ningún cliente registrado con DNI " + dniActual);
                return;
            }

            vista.mostrarMensaje("Introduzca los nuevos datos para el cliente (DNI actual: " + dniActual + "):");

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
        } catch (Exception e) {
            vista.mostrarError("Error al modificar cliente: " + e.getMessage());
        }
    }

    private void borrarClienteDesdeVista() {
        String dni = vista.pedirDNI();
        try {
            eliminarCliente(dni);
            vista.mostrarMensaje("Cliente con DNI " + dni + " eliminado satisfactoriamente.");
        } catch (ClienteConReservaException | IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        } catch (Exception e) {
            vista.mostrarError("Error al eliminar cliente: " + e.getMessage());
        }
    }

}



