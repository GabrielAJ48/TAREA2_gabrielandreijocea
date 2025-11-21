-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 21-11-2025 a las 23:13:09
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `circo_bd_andrei`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `artista`
--

CREATE TABLE `artista` (
  `artista_id` bigint(20) NOT NULL,
  `persona_id` bigint(20) NOT NULL,
  `apodo` varchar(60) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `artista`
--

INSERT INTO `artista` (`artista_id`, `persona_id`, `apodo`) VALUES
(1, 2, NULL),
(2, 3, NULL),
(3, 4, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `artista_especialidad`
--

CREATE TABLE `artista_especialidad` (
  `artista_id` bigint(20) NOT NULL,
  `especialidad_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `artista_especialidad`
--

INSERT INTO `artista_especialidad` (`artista_id`, `especialidad_id`) VALUES
(1, 1),
(2, 2),
(3, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coordinacion`
--

CREATE TABLE `coordinacion` (
  `coordinacion_id` bigint(20) NOT NULL,
  `persona_id` bigint(20) NOT NULL,
  `es_senior` tinyint(1) NOT NULL,
  `fecha_senior` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `coordinacion`
--

INSERT INTO `coordinacion` (`coordinacion_id`, `persona_id`, `es_senior`, `fecha_senior`) VALUES
(1, 1, 1, '2025-04-10');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `credenciales`
--

CREATE TABLE `credenciales` (
  `credencial_id` bigint(20) NOT NULL,
  `persona_id` bigint(20) DEFAULT NULL,
  `nombre_usuario` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `perfil` enum('COORDINACION','ARTISTA','INVITADO') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `credenciales`
--

INSERT INTO `credenciales` (`credencial_id`, `persona_id`, `nombre_usuario`, `password`, `perfil`) VALUES
(1, 1, 'pepe', 'pepe', 'COORDINACION'),
(2, 2, 'jean', 'jean', 'ARTISTA'),
(3, 3, 'levi', 'levi', 'ARTISTA'),
(4, 4, 'sasha', 'sasha', 'ARTISTA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `especialidad`
--

CREATE TABLE `especialidad` (
  `especialidad_id` bigint(20) NOT NULL,
  `nombre` enum('ACROBACIA','HUMOR','MAGIA','EQUILIBRISMO','MALABARISMO') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `especialidad`
--

INSERT INTO `especialidad` (`especialidad_id`, `nombre`) VALUES
(1, 'ACROBACIA'),
(2, 'HUMOR'),
(3, 'MAGIA'),
(4, 'EQUILIBRISMO'),
(5, 'MALABARISMO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `espectaculo`
--

CREATE TABLE `espectaculo` (
  `espectaculo_id` bigint(20) NOT NULL,
  `nombre` varchar(25) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `coordinador_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `espectaculo`
--

INSERT INTO `espectaculo` (`espectaculo_id`, `nombre`, `fecha_inicio`, `fecha_fin`, `coordinador_id`) VALUES
(1, 'cantajuegos', '2025-05-10', '2025-05-15', 1),
(2, 'circo del sol', '2023-06-21', '2023-08-25', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `numero`
--

CREATE TABLE `numero` (
  `numero_id` bigint(20) NOT NULL,
  `espectaculo_id` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `orden` int(11) NOT NULL,
  `duracion` decimal(4,1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `numero`
--

INSERT INTO `numero` (`numero_id`, `espectaculo_id`, `nombre`, `orden`, `duracion`) VALUES
(1, 1, 'numero1', 1, 5.5),
(2, 1, 'numerin2', 2, 10.0),
(3, 1, 'numero3', 3, 6.0),
(4, 2, 'leones', 1, 3.0),
(5, 2, 'el sol', 2, 4.0),
(6, 2, 'timon y pumba', 3, 7.0),
(7, 2, 'final', 4, 8.0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `numero_artista`
--

CREATE TABLE `numero_artista` (
  `numero_id` bigint(20) NOT NULL,
  `artista_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `numero_artista`
--

INSERT INTO `numero_artista` (`numero_id`, `artista_id`) VALUES
(1, 1),
(2, 1),
(2, 3),
(3, 1),
(4, 2),
(5, 2),
(6, 2),
(7, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

CREATE TABLE `persona` (
  `persona_id` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `pais_id` char(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`persona_id`, `nombre`, `email`, `pais_id`) VALUES
(1, 'pepe', 'pepe@mail.com', 'TR'),
(2, 'jean', 'jean@mail.com', 'LI'),
(3, 'levi', 'levi@mail.com', 'VN'),
(4, 'sasha', 'sasha@mail.com', 'EE');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `artista`
--
ALTER TABLE `artista`
  ADD PRIMARY KEY (`artista_id`),
  ADD KEY `artista_ibfk_1` (`persona_id`);

--
-- Indices de la tabla `artista_especialidad`
--
ALTER TABLE `artista_especialidad`
  ADD PRIMARY KEY (`artista_id`,`especialidad_id`),
  ADD KEY `artista_especialidad_ibfk_2` (`especialidad_id`);

--
-- Indices de la tabla `coordinacion`
--
ALTER TABLE `coordinacion`
  ADD PRIMARY KEY (`coordinacion_id`),
  ADD KEY `coordinacion_ibfk_1` (`persona_id`);

--
-- Indices de la tabla `credenciales`
--
ALTER TABLE `credenciales`
  ADD PRIMARY KEY (`credencial_id`),
  ADD UNIQUE KEY `nombre_usuario` (`nombre_usuario`),
  ADD KEY `credenciales_ibfk_1` (`persona_id`);

--
-- Indices de la tabla `especialidad`
--
ALTER TABLE `especialidad`
  ADD PRIMARY KEY (`especialidad_id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `espectaculo`
--
ALTER TABLE `espectaculo`
  ADD PRIMARY KEY (`espectaculo_id`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `espectaculo_ibfk_1` (`coordinador_id`);

--
-- Indices de la tabla `numero`
--
ALTER TABLE `numero`
  ADD PRIMARY KEY (`numero_id`),
  ADD UNIQUE KEY `espectaculo_id` (`espectaculo_id`,`orden`);

--
-- Indices de la tabla `numero_artista`
--
ALTER TABLE `numero_artista`
  ADD PRIMARY KEY (`numero_id`,`artista_id`),
  ADD KEY `numero_artista_ibfk_2` (`artista_id`);

--
-- Indices de la tabla `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`persona_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `artista`
--
ALTER TABLE `artista`
  MODIFY `artista_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `coordinacion`
--
ALTER TABLE `coordinacion`
  MODIFY `coordinacion_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `credenciales`
--
ALTER TABLE `credenciales`
  MODIFY `credencial_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `especialidad`
--
ALTER TABLE `especialidad`
  MODIFY `especialidad_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `espectaculo`
--
ALTER TABLE `espectaculo`
  MODIFY `espectaculo_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `numero`
--
ALTER TABLE `numero`
  MODIFY `numero_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `persona`
--
ALTER TABLE `persona`
  MODIFY `persona_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `artista`
--
ALTER TABLE `artista`
  ADD CONSTRAINT `artista_ibfk_1` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`persona_id`);

--
-- Filtros para la tabla `artista_especialidad`
--
ALTER TABLE `artista_especialidad`
  ADD CONSTRAINT `artista_especialidad_ibfk_1` FOREIGN KEY (`artista_id`) REFERENCES `artista` (`artista_id`),
  ADD CONSTRAINT `artista_especialidad_ibfk_2` FOREIGN KEY (`especialidad_id`) REFERENCES `especialidad` (`especialidad_id`);

--
-- Filtros para la tabla `coordinacion`
--
ALTER TABLE `coordinacion`
  ADD CONSTRAINT `coordinacion_ibfk_1` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`persona_id`);

--
-- Filtros para la tabla `credenciales`
--
ALTER TABLE `credenciales`
  ADD CONSTRAINT `credenciales_ibfk_1` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`persona_id`);

--
-- Filtros para la tabla `espectaculo`
--
ALTER TABLE `espectaculo`
  ADD CONSTRAINT `espectaculo_ibfk_1` FOREIGN KEY (`coordinador_id`) REFERENCES `coordinacion` (`coordinacion_id`);

--
-- Filtros para la tabla `numero`
--
ALTER TABLE `numero`
  ADD CONSTRAINT `numero_ibfk_1` FOREIGN KEY (`espectaculo_id`) REFERENCES `espectaculo` (`espectaculo_id`);

--
-- Filtros para la tabla `numero_artista`
--
ALTER TABLE `numero_artista`
  ADD CONSTRAINT `numero_artista_ibfk_1` FOREIGN KEY (`numero_id`) REFERENCES `numero` (`numero_id`),
  ADD CONSTRAINT `numero_artista_ibfk_2` FOREIGN KEY (`artista_id`) REFERENCES `artista` (`artista_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
