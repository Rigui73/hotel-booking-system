package RoomServDev.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

class DatosTest {

    private Datos repositorio;

    @BeforeEach
    void setUp() throws ClienteDuplicadoException {
        repositorio = new Datos();

        Cliente clientePrueba = new Cliente(
                "Jordi", "Roig", "Cosido",
                "59439439B", 340093439, "apg@gmail.com"
        );

        repositorio.anadirCliente(clientePrueba);
    }

    @Test
    void testModificarCliente() {
        // GIVEN: nuevos datos para un cliente existente
        Cliente datosNuevos = new Cliente(
                "Jordi_New", "Roig_New", "Cosido",
                "59439439B", 654984325, "apg@gmail.com"
        );

        // WHEN: modificamos el cliente
        repositorio.modificarCliente(datosNuevos);

        // THEN: comprobamos los datos actualizados
        Cliente clienteGuardado =
                repositorio.buscarClientePorDni("59439439B");

        assertNotNull(clienteGuardado, "El cliente debería existir.");
        assertEquals("Jordi_New", clienteGuardado.getNombre(),
                "El nombre no se actualizó.");
        assertEquals("Roig_New", clienteGuardado.getApellido1(),
                "El primer apellido no se actualizó.");
    }


    private int numHabitacion;
    private int capacidad;
    private double precioNoche;
    private boolean disponible;
    private String categoria;



    @Test
    void testCalcularImporteReserva() {
        Habitacion habitacion = new Simple(
                101, 4, 139.0, "Simple", "Sencilla", true
        );

        Date fechaEntrada = Date.from(
                LocalDate.of(2026, 10, 2)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );

        Date fechaSalida = Date.from(
                LocalDate.of(2026, 10, 6)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );

        Reserva reserva = new Reserva(
                1,
                new Date(),
                fechaEntrada,
                fechaSalida,
                2,
                0.0,
                true,
                "Hotel Test",
                101,
                "12345678A"
        );

        double importeCalculado =
                repositorio.calcularImporteReserva(reserva, habitacion);

        assertEquals(
                556.0,
                importeCalculado,
                0.01,
                "El cálculo del importe total falla."
        );
    }
}