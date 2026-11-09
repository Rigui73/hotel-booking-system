CREATE DATABASE  IF NOT EXISTS `roomservdev_db`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `roomservdev_db`;
--
-- Host: 127.0.0.1    Database: roomservdev_db
-- ------------------------------------------------------
-- Server version	9.6.0

--
-- ELIMINACIÓN DE TABLAS (orden inverso de dependencias)
--

DROP TABLE IF EXISTS `reservas`;
DROP TABLE IF EXISTS `habitaciones_suite`;
DROP TABLE IF EXISTS `habitaciones_doble`;
DROP TABLE IF EXISTS `habitaciones_simple`;
DROP TABLE IF EXISTS `habitaciones`;
DROP TABLE IF EXISTS `clientes`;
DROP TABLE IF EXISTS `hoteles`;

--
-- Table structure for table `clientes`
--
CREATE TABLE `clientes` (
  `dni` varchar(9) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido1` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido2` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono` int NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`dni`),
  UNIQUE KEY `dni_UNIQUE` (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `hoteles`
--
CREATE TABLE `hoteles` (
  `id_hotel` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ciudad` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` int NOT NULL,
  PRIMARY KEY (`id_hotel`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `habitaciones`
--
CREATE TABLE `habitaciones` (
    id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
    num_habitacion INT NOT NULL,
    capacidad INT NOT NULL,
    precio_noche DECIMAL(8, 2) NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    categoria VARCHAR(20) NOT NULL,
    id_hotel INT NOT NULL,
    CONSTRAINT fk_habitaciones_hoteles
        FOREIGN KEY (id_hotel) REFERENCES hoteles (id_hotel) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `habitaciones_simple`
--
CREATE TABLE habitaciones_simple (
    id_habitacion INT NOT NULL,
    medida_cama VARCHAR(20) NOT NULL,
    tiene_escritorio BOOLEAN NOT NULL,

    PRIMARY KEY (id_habitacion),

    CONSTRAINT fk_simple_habitacion
        FOREIGN KEY (id_habitacion)
        REFERENCES habitaciones (id_habitacion)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `habitaciones_doble`
--
CREATE TABLE habitaciones_doble (
    id_habitacion INT NOT NULL,
    tipo_cama_doble VARCHAR(30) NOT NULL,
    supletoria BOOLEAN NOT NULL,
    suplemento_supletoria DECIMAL(8,2) NOT NULL,
    balcon BOOLEAN NOT NULL,

    PRIMARY KEY (id_habitacion),

    CONSTRAINT fk_doble_habitacion
        FOREIGN KEY (id_habitacion)
        REFERENCES habitaciones (id_habitacion)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `habitaciones_suite`
--
CREATE TABLE habitaciones_suite (
    id_habitacion INT NOT NULL,
    num_ambientes INT NOT NULL,
    jacuzzi BOOLEAN NOT NULL,
    incluye_spa BOOLEAN NOT NULL,
    incluye_terraza BOOLEAN NOT NULL,
    otros_servicios VARCHAR(255),

    PRIMARY KEY (id_habitacion),

    CONSTRAINT fk_suite_habitacion
        FOREIGN KEY (id_habitacion)
        REFERENCES habitaciones (id_habitacion)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `reservas`
--
CREATE TABLE `reservas` (
  `id_reserva` int NOT NULL AUTO_INCREMENT,
  `fecha_entrada` date NOT NULL,
  `fecha_salida` date NOT NULL,
  `num_huespedes` int NOT NULL,
  `importe_total` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `dni_cliente` varchar(9) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_habitacion` int NOT NULL,
  PRIMARY KEY (`id_reserva`),
  KEY `fk_reservas_habitaciones_idx` (`id_habitacion`),
  KEY `fk_reservas_clientes_idx` (`dni_cliente`),
  CONSTRAINT `fk_reservas_clientes` FOREIGN KEY (`dni_cliente`) REFERENCES `clientes` (`dni`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_reservas_habitaciones` FOREIGN KEY (`id_habitacion`) REFERENCES `habitaciones` (`id_habitacion`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
