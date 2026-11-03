USE roomservdev_db;

-- 1. Insertamos Hoteles (Los ID serán 1 y 2 por el AUTO_INCREMENT)
INSERT INTO hoteles (nombre, direccion, categoria, telefono) VALUES
('Hotel Playa Cambrils', 'Paseo Marítimo 12', '4 Estrellas', 977123456),
('Hostal Tarraco', 'Calle Mayor 45', '2 Estrellas', 977654321);

-- 2. Insertamos Clientes
INSERT INTO clientes (dni, nombre, email) VALUES
('12345678A', 'Luis Perez', 'lperez@email.com'),
('87654321B', 'Tomas Garrido', 'tgarrido@email.com');

-- 3. Insertamos Habitaciones (Vinculadas a los ID de los hoteles)
INSERT INTO habitaciones (id_hotel, numero, tipo, estado) VALUES
(1, '101', 'Doble', 'LIBRE'),
(1, '102', 'Suite', 'OCUPADA'),  -- Esta la usaremos para la reserva
(2, '1A', 'Individual', 'LIBRE');

-- 4. Insertamos Reservas (Vinculadas al DNI de los clientes y al ID de la habitación)
-- Usamos formato de fecha AAAA-MM-DD. Estado 1 = Activa.
INSERT INTO reservas (fecha_entrada, fecha_salida, num_huespedes, importe_total, estado, dni_cliente, id_habitacion) VALUES
('2026-11-10', '2026-11-15', 2, 450.00, 1, '12345678A', 2),
('2026-12-01', '2026-12-05', 1, 200.00, 1, '87654321B', 3);