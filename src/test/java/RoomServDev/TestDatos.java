package RoomServDev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import RoomServDev.model.Cliente;
import RoomServDev.model.ClienteDuplicadoException;
import RoomServDev.model.Datos;
import RoomServDev.model.Habitacion;
import RoomServDev.model.Reserva;
import RoomServDev.model.Simple;

class TestDatos {

        private Datos repositorio;

        @BeforeEach
        void setUp() throws ClienteDuplicadoException {
                repositorio = new Datos();

                Cliente clientePrueba = new Cliente(
                                "Jordi",
                                "Roig",
                                "Cosido",
                                "59439439B",
                                340093439,
                                "apg@gmail.com");

                repositorio.anadirCliente(clientePrueba);
        }

        @Test
        void testModificarCliente() throws ClienteDuplicadoException {

                Cliente datosNuevos = new Cliente(
                                "Jordi_New",
                                "Roig_New",
                                "Cosido",
                                "59439439B",
                                654984325,
                                "apg@gmail.com");

                repositorio.modificarClienteConDni(
                                "59439439B",
                                datosNuevos);

                Cliente clienteGuardado = repositorio.buscarClientePorDni("59439439B");

                assertNotNull(clienteGuardado, "El cliente debería existir.");

                assertEquals(
                                "Jordi_New",
                                clienteGuardado.getNombre(),
                                "El nombre no se actualizó.");

                assertEquals(
                                "Roig_New",
                                clienteGuardado.getApellido1(),
                                "El primer apellido no se actualizó.");
        }

        @Test
        void testCalcularImporteReserva() {

                Habitacion habitacion = new Simple(
                                101,
                                4,
                                139.0,
                                "Simple",
                                "Sencilla",
                                true);

                Date fechaReserva = convertirFecha(2026, 9, 1);
                Date fechaEntrada = convertirFecha(2026, 10, 2);
                Date fechaSalida = convertirFecha(2026, 10, 6);

                Reserva reserva = new Reserva(
                                1,
                                fechaReserva,
                                fechaEntrada,
                                fechaSalida,
                                2,
                                0.0,
                                true,
                                "Hotel de prueba",
                                101,
                                "59439439B");

                double importeCalculado = repositorio.calcularImporteReserva(reserva, habitacion);

                assertEquals(
                                556.0,
                                importeCalculado,
                                0.01,
                                "El cálculo del importe total es incorrecto.");
        }

        private Date convertirFecha(int anio, int mes, int dia) {
                return Date.from(
                                LocalDate.of(anio, mes, dia)
                                                .atStartOfDay(ZoneId.systemDefault())
                                                .toInstant());
        }
}