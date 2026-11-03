USE roomservdev_db;

-- Desactivar temporalmente chequeos de FK para inserción masiva segura
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Poblado de Clientes
INSERT INTO clientes (dni, nombre, apellido1, apellido2, telefono, email) VALUES
('12345678A', 'Adria', 'Rigall', 'Prat', 612345678, 'arigallp@uoc.edu'),
('23456789B', 'Laura', 'García', 'Martínez', 623456789, 'laura.garcia@email.com'),
('34567890C', 'Carlos', 'López', NULL, 634567890, 'clopez@email.com')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- 2. Poblado de Hoteles
INSERT INTO hoteles (id_hotel, nombre, direccion, ciudad, telefono) VALUES
(1, 'Hotel Central Barcelona', 'Gran Via de les Corts Catalanes 123', 'Barcelona', 933001122),
(2, 'Hotel Costa Daurada', 'Passeig Marítim 45', 'Cambrils', 977112233)
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- 3. Poblado de Habitaciones (asociadas a los hoteles)
INSERT INTO habitaciones (id_habitacion, num_habitacion, capacidad, precio_noche, disponible, categoria, id_hotel) VALUES
(1, 101, 1, 75.00, 1, 'Simple', 1),
(2, 201, 3, 120.00, 1, 'Doble', 1),
(3, 301, 4, 220.00, 1, 'Suite', 1),
(4, 101, 2, 85.00, 1, 'Doble', 2),
(5, 201, 4, 195.00, 1, 'Suite', 2)
    ON DUPLICATE KEY UPDATE precio_noche = VALUES(precio_noche);

-- 4. Poblado de Reservas iniciales
INSERT INTO reservas (id_reserva, fecha_entrada, fecha_salida, num_huespedes, importe_total, estado, dni_cliente, id_habitacion) VALUES
(1, '2026-11-15', '2026-11-18', 2, 360.00, 1, '12345678A', 2),
(2, '2026-12-01', '2026-12-05', 1, 300.00, 1, '23456789B', 1)
    ON DUPLICATE KEY UPDATE importe_total = VALUES(importe_total);

-- Reactivar chequeos de FK
SET FOREIGN_KEY_CHECKS = 1;