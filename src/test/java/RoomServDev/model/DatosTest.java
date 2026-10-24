package RoomServDev.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;


class DatosTest {

    private Datos repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new Datos();

        // Pre-cargamos un cliente de prueba en el diccionario
        Cliente clientePrueba = new Cliente("Jordi", "Roig", "Cosido", "59439439B", 340093439, "apg@gmail.com");
        repositorio.getClientes().put("59439439B", clientePrueba);
    }

    @Test
    void testModificarCliente() {
        // 1. GIVEN: Preparamos los nuevos datos para el cliente existente
        Cliente datosNuevos = new Cliente("Jordi_New", "Roig_New", "Cosido", "59439439B", 654984325, "apg@gmail.com");

        // 2. WHEN: Ejecutamos el método real de tu clase Datos
        repositorio.modificarCliente(datosNuevos);

        // 3. THEN: Comprobamos que el cambio se ha guardado en el diccionario
        Cliente clienteGuardado = repositorio.getClientes().get("59439439B");
        assertEquals("Jordi_New", clienteGuardado.getNombre(), "El nombre no se actualizó.");
        assertEquals("Roig_New", clienteGuardado.getApellido1(), "La dirección no se actualizó.");
    }


    private int numHabitacion;
    private int capacidad;
    private double precioNoche;
    private boolean disponible;
    private String categoria;



    @Test
    void testCalcularImporteReserva() {
        // 1. GIVEN: Preparamos una habitación y una reserva con fechas
        // Asumiendo que tu constructor de Habitacion es: Habitacion(num, categoria, capacidad, precio)
        Habitacion habitacion = new Simple(101, 4, 139.0, "Simple" , "Sencilla", true);

        // Configuramos 3 días de diferencia (del 1 al 4 de enero)
        Date fechaEntrada = new Date(2026, 10, 2);
        Date fechaSalida = new Date(2026, 10, 6);

        // Asumiendo que tu constructor de Reserva es: Reserva(dni, hotel, numHab, entrada, salida, huespedes)
        Reserva reserva = new Reserva(1, new Date(), fechaEntrada, fechaSalida, 2, 0.0, true, "Hotel Test", 101, "12345678A");

        // 2. WHEN: Ejecutamos el cálculo usando tu método real
        double importeCalculado = repositorio.calcularImporteReserva(reserva, habitacion);

        // 3. THEN: Verificamos que el cálculo matemático es exacto (3 noches * 50.0 = 150.0)
        assertEquals(556.0, importeCalculado, "El cálculo del importe total falla.");
    }
}