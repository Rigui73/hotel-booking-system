USE roomservdev_db;

-- 1. Poblado de Clientes
INSERT INTO clientes (dni, nombre, apellido1, apellido2, telefono, email) VALUES
('12345678A', 'Adria', 'Rigall', 'Prat', 612345678, 'arigallp@uoc.edu'),
('23456789B', 'Laura', 'García', 'Martínez', 623456789, 'laura.garcia@email.com'),
('34567890C', 'Carlos', 'López', NULL, 634567890, 'clopez@email.com')
    ON DUPLICATE KEY UPDATE dni = dni;

-- 2. Poblado de Hoteles
INSERT INTO hoteles (id_hotel, nombre, direccion, ciudad, telefono) VALUES
(1, 'Hotel Central Barcelona', 'Gran Via de les Corts Catalanes 123', 'Barcelona', 933001122),
(2, 'Hotel Costa Daurada', 'Passeig Marítim 45', 'Cambrils', 977112233)
    ON DUPLICATE KEY UPDATE id_hotel = id_hotel;

-- 3. Poblado de Habitaciones (asociadas a los hoteles)
INSERT INTO habitaciones (id_habitacion, num_habitacion, capacidad, precio_noche, disponible, categoria, id_hotel) VALUES
(1, 101, 1, 75.00, 1, 'Simple', 1),
(2, 201, 3, 120.00, 1, 'Doble', 1),
(3, 301, 4, 220.00, 1, 'Suite', 1),
(4, 101, 2, 85.00, 1, 'Doble', 2),
(5, 201, 4, 195.00, 1, 'Suite', 2)
    ON DUPLICATE KEY UPDATE id_habitacion = id_habitacion;

-- 4. Poblado de atributos de subtipo (una fila por habitación y categoría)
INSERT INTO habitaciones_simple (id_habitacion, medida_cama, tiene_escritorio) VALUES
(1, '90x200', 1)
    ON DUPLICATE KEY UPDATE id_habitacion = id_habitacion;

INSERT INTO habitaciones_doble
    (id_habitacion, tipo_cama_doble, supletoria, suplemento_supletoria, balcon) VALUES
(2, 'Matrimonio', 1, 25.00, 1),
(4, 'Matrimonio', 0, 0.00, 0)
    ON DUPLICATE KEY UPDATE id_habitacion = id_habitacion;

INSERT INTO habitaciones_suite
    (id_habitacion, num_ambientes, jacuzzi, incluye_spa, incluye_terraza, otros_servicios) VALUES
(3, 2, 1, 1, 1, 'Minibar y vistas'),
(5, 2, 1, 1, 1, 'Minibar y vistas')
    ON DUPLICATE KEY UPDATE id_habitacion = id_habitacion;

-- 5. Poblado de Reservas iniciales
INSERT INTO reservas (id_reserva, fecha_entrada, fecha_salida, num_huespedes, importe_total, estado, dni_cliente, id_habitacion) VALUES
(1, '2026-11-15', '2026-11-18', 2, 360.00, 1, '12345678A', 2),
(2, '2026-12-01', '2026-12-05', 1, 300.00, 1, '23456789B', 1)
    ON DUPLICATE KEY UPDATE id_reserva = id_reserva;
