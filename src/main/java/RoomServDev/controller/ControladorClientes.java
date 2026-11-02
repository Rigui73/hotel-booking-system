package RoomServDev.controller;

import RoomServDev.model.Cliente;
import RoomServDev.model.ClienteConReservaException;
import RoomServDev.model.ClienteDuplicadoException;
import RoomServDev.model.Datos;

import java.util.List;

public class ControladorClientes {

    // Los datos se guardan en Datos (modelo); el controlador valida y hace de puente con la vista
    private final Datos datos;

    public ControladorClientes(Datos datos) {
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

        // Comprobar que el nuevo DNI no es de otro cliente ANTES de cambiar nada
        String dniLimpio = nuevoDni.trim().toUpperCase();
        Cliente otro = buscarCliente(dniLimpio);
        if (otro != null && otro != cliente) {
            throw new ClienteDuplicadoException("El cliente con DNI " + dniLimpio + " ya está registrado");
        }

        // Datos se encarga de cambiar la clave del Map y de actualizar las reservas
        datos.cambiarDniCliente(cliente.getDni(), dniLimpio);

        cliente.setNombre(nombre.trim());
        cliente.setApellido1(apellido1.trim());
        cliente.setApellido2(apellido2.trim());
        cliente.setTelefono(telefono);
        cliente.setEmail(email.trim());
        datos.modificarCliente(cliente);
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
}
