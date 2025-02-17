-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-02-2025 a las 00:29:58
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `travel_agency`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `flights`
--

CREATE TABLE `flights` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `departure_date` date DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `flight_number` varchar(255) DEFAULT NULL,
  `origin` varchar(255) DEFAULT NULL,
  `rate_per_person` double DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `seat_type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `flights`
--

INSERT INTO `flights` (`id`, `active`, `departure_date`, `destination`, `flight_number`, `origin`, `rate_per_person`, `return_date`, `seat_type`) VALUES
(1, b'1', '2024-02-10', 'Miami', 'BAMI-1235', 'Barcelona', 650, '2024-02-15', 'Economy'),
(2, b'1', '2024-02-10', 'Madrid', 'MIMA1420', 'Miami', 4320, '2024-02-20', 'Business'),
(3, b'1', '2024-02-10', 'Madrid', 'MIMA-1420', 'Miami', 2573, '2024-02-21', 'Economy'),
(4, b'1', '2024-02-10', 'Buenos Aires', 'BABU-5536', 'Barcelona', 732, '2024-02-17', 'Economy'),
(5, b'1', '2024-02-12', 'Barcelona', 'BUBA-3369', 'Buenos Aires', 1253, '2024-02-23', 'Business'),
(6, b'1', '2024-01-02', 'Barcelona', 'IGBA-3369', 'Iguazú', 540, '2024-01-16', 'Economy'),
(7, b'1', '2024-01-23', 'Cartagena', 'BOCA-4213', 'Bogotá', 800, '2024-02-05', 'Economy'),
(8, b'1', '2024-01-23', 'Medellín', 'CAME-0321', 'Cartagena', 780, '2024-01-31', 'Economy'),
(9, b'1', '2024-02-15', 'Iguazú', 'BOIG-6567', 'Bogotá', 570, '2024-02-28', 'Business'),
(10, b'1', '2024-03-01', 'Buenos Aires', 'BOBA-6567', 'Bogotá', 398, '2024-03-14', 'Economy'),
(11, b'1', '2024-02-10', 'Madrid', 'BOMA-4442', 'Bogotá', 1100, '2024-02-24', 'Economy'),
(12, b'0', '2024-04-17', 'Miami', 'MEMI-9986', 'Medellín', 1164, '2024-05-02', 'Business'),
(13, b'1', '2024-02-10', 'Miami', 'BAMI-9999', 'Ciudad Real', 950, '2024-02-15', 'Economy');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `flight_bookings`
--

CREATE TABLE `flight_bookings` (
  `id` int(11) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `flight_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `flight_bookings`
--

INSERT INTO `flight_bookings` (`id`, `active`, `flight_id`) VALUES
(1, b'1', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `flight_booking_users`
--

CREATE TABLE `flight_booking_users` (
  `flight_booking_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `flight_booking_users`
--

INSERT INTO `flight_booking_users` (`flight_booking_id`, `user_id`) VALUES
(1, 1),
(1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hotels`
--

CREATE TABLE `hotels` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `booked` enum('NO','SI') NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `date_from` date DEFAULT NULL,
  `date_to` date DEFAULT NULL,
  `hotel_code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rate_per_night` double DEFAULT NULL,
  `room_type` enum('DOBLE','INDIVIDUAL','SUITE','TRIPLE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `hotels`
--

INSERT INTO `hotels` (`id`, `active`, `booked`, `city`, `date_from`, `date_to`, `hotel_code`, `name`, `rate_per_night`, `room_type`) VALUES
(1, b'1', 'NO', 'Miami', '2024-02-10', '2024-03-23', 'AR-0003', 'Atlantis Resort 2', 820, 'TRIPLE'),
(2, b'1', 'NO', 'Buenos Aires', '2024-02-10', '2024-03-19', 'RC-0001', 'Ritz-Carlton', 543, 'INDIVIDUAL'),
(3, b'1', 'NO', 'Medellín', '2024-02-12', '2024-04-17', 'RC-0002', 'Ritz-Carlton 2', 720, 'DOBLE'),
(4, b'1', 'NO', 'Madrid', '2024-04-17', '2024-05-23', 'GH-0002', 'Grand Hyatt', 579, 'DOBLE'),
(5, b'1', 'NO', 'Buenos Aires', '2024-01-02', '2024-02-19', 'GH-0001', 'Grand Hyatt 2', 415, 'INDIVIDUAL'),
(6, b'1', 'NO', 'Barcelona', '2024-01-23', '2024-11-23', 'HL-0001', 'Hilton', 390, 'INDIVIDUAL'),
(7, b'1', 'NO', 'Barcelona', '2024-01-23', '2024-10-15', 'HL-0002', 'Hilton 2', 584, 'DOBLE'),
(8, b'1', 'NO', 'Barcelona', '2024-02-15', '2024-03-27', 'MT-0003', 'Marriott', 702, 'DOBLE'),
(9, b'1', 'NO', 'Madrid', '2024-03-01', '2024-04-17', 'SH-0004', 'Sheraton', 860, 'SUITE'),
(10, b'1', 'NO', 'Iguazú', '2024-02-10', '2024-03-20', 'SH-0002', 'Sheraton 2', 640, 'DOBLE'),
(11, b'1', 'NO', 'Cartagena', '2024-04-17', '2024-06-12', 'IR-0004', 'InterContinental', 937, 'SUITE'),
(12, b'1', 'SI', 'Miami', '2024-02-10', '2024-03-20', 'AR-0002', 'Atlantis Resort', 630, 'DOBLE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hotel_bookings`
--

CREATE TABLE `hotel_bookings` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `hotel_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `hotel_bookings`
--

INSERT INTO `hotel_bookings` (`id`, `active`, `hotel_id`) VALUES
(1, b'1', 12);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hotel_booking_users`
--

CREATE TABLE `hotel_booking_users` (
  `hotel_booking_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `hotel_booking_users`
--

INSERT INTO `hotel_booking_users` (`hotel_booking_id`, `user_id`) VALUES
(1, 5),
(1, 6),
(1, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `dni` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','USER') NOT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `active`, `dni`, `email`, `name`, `password`, `phone`, `role`, `surname`, `username`) VALUES
(1, b'1', '12345678A', 'user1@example.com', 'John', 'hashed_password1', '123456789', 'USER', 'Doe', 'johndoe'),
(2, b'1', '87654321B', 'user2@example.com', 'Alice', 'hashed_password2', '987654321', 'USER', 'Smith', 'alicesmith'),
(3, b'0', '11223344C', 'user3@example.com', 'Bob', 'hashed_password3', '654987321', 'USER', 'Brown', 'bobbrown'),
(4, b'1', '55667788D', 'user4@example.com', 'Eva', 'hashed_password4', '789456123', 'USER', 'White', 'evawhite'),
(5, b'1', '12345678B', 'alberto@example.com', 'Alberto', 'hashed_password5', '123456789', 'USER', 'Arroyo', '12345678B'),
(6, b'1', '87654321Z', 'ana@example.com', 'Ana', 'hashed_password6', '987654321', 'USER', 'Pérez', '87654321Z'),
(7, b'1', '23456789C', 'luis@example.com', 'Luis', 'hashed_password7', '555666777', 'USER', 'Martinez', '23456789C');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `flights`
--
ALTER TABLE `flights`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `flight_bookings`
--
ALTER TABLE `flight_bookings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKhxnetdmvuk9rbjavrkajmuwhf` (`flight_id`);

--
-- Indices de la tabla `flight_booking_users`
--
ALTER TABLE `flight_booking_users`
  ADD KEY `FKpw5r6jaouc95sg4ygyfou1dqg` (`user_id`),
  ADD KEY `FK4u41xpwc44uvqo2im5uf659qj` (`flight_booking_id`);

--
-- Indices de la tabla `hotels`
--
ALTER TABLE `hotels`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `hotel_bookings`
--
ALTER TABLE `hotel_bookings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK8r7xq1018oerk42vjgqy7bmr3` (`hotel_id`);

--
-- Indices de la tabla `hotel_booking_users`
--
ALTER TABLE `hotel_booking_users`
  ADD KEY `FKsrvobxo3lgfvbwjkab8c6hked` (`user_id`),
  ADD KEY `FKqlifo4u59ndrto52t8vggu6gw` (`hotel_booking_id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `flights`
--
ALTER TABLE `flights`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `flight_bookings`
--
ALTER TABLE `flight_bookings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `hotels`
--
ALTER TABLE `hotels`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `hotel_bookings`
--
ALTER TABLE `hotel_bookings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `flight_bookings`
--
ALTER TABLE `flight_bookings`
  ADD CONSTRAINT `FKhxnetdmvuk9rbjavrkajmuwhf` FOREIGN KEY (`flight_id`) REFERENCES `flights` (`id`);

--
-- Filtros para la tabla `flight_booking_users`
--
ALTER TABLE `flight_booking_users`
  ADD CONSTRAINT `FK4u41xpwc44uvqo2im5uf659qj` FOREIGN KEY (`flight_booking_id`) REFERENCES `flight_bookings` (`id`),
  ADD CONSTRAINT `FKpw5r6jaouc95sg4ygyfou1dqg` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `hotel_bookings`
--
ALTER TABLE `hotel_bookings`
  ADD CONSTRAINT `FK8r7xq1018oerk42vjgqy7bmr3` FOREIGN KEY (`hotel_id`) REFERENCES `hotels` (`id`);

--
-- Filtros para la tabla `hotel_booking_users`
--
ALTER TABLE `hotel_booking_users`
  ADD CONSTRAINT `FKqlifo4u59ndrto52t8vggu6gw` FOREIGN KEY (`hotel_booking_id`) REFERENCES `hotel_bookings` (`id`),
  ADD CONSTRAINT `FKsrvobxo3lgfvbwjkab8c6hked` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
