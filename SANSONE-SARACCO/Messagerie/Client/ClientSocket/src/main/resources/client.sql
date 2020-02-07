-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Feb 07, 2020 at 02:49 PM
-- Server version: 5.7.29-0ubuntu0.18.04.1
-- PHP Version: 7.2.24-0ubuntu0.18.04.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `client`
--

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `msg_id` int(11) NOT NULL,
  `msg_user_id` int(11) DEFAULT NULL,
  `msg_message` varbinary(65500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`msg_id`, `msg_user_id`, `msg_message`) VALUES
(4, 2, 0xaced0005737200074d6573736167659b0ddf06999ba4d10200025b000249567400025b425b00076d65737361676571007e00017870757200025b42acf317f8060854e002000078700000000c753538186ff7987d4ea27e217571007e0003000000136831c32d1f56c7290bb7f63c9c17e0271ae33e),
(5, 1, 0xaced0005737200074d6573736167659b0ddf06999ba4d10200025b000249567400025b425b00076d65737361676571007e00017870757200025b42acf317f8060854e002000078700000000c39136a6d91b36c8b7cbb5b627571007e00030000001348f24996fe1c86aecf5e79aa68f2f2d0158635),
(6, 2, 0xaced0005737200074d6573736167659b0ddf06999ba4d10200025b000249567400025b425b00076d65737361676571007e00017870757200025b42acf317f8060854e002000078700000000cd2c50c678bdabe96bf53f7a97571007e0003000000164d345012e92eacfbf64274a60e876953bebb25bcb61a),
(7, 1, 0xaced0005737200074d6573736167659b0ddf06999ba4d10200025b000249567400025b425b00076d65737361676571007e00017870757200025b42acf317f8060854e002000078700000000ca3fcbe96d7f43227a6d2f9317571007e00030000001319cc9335104ce61c3c7f196132de30cdfc1431);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `usr_id` int(11) NOT NULL,
  `usr_name` text NOT NULL,
  `usr_salt` varchar(30) NOT NULL,
  `usr_hash_pwd` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`usr_id`, `usr_name`, `usr_salt`, `usr_hash_pwd`) VALUES
(1, 'Aymeric', 'D92EmZvKGe', 'a672cb53fe66f99eb2c3c8cfeb5c78718104c1ac6708387626728eec6956a10c'),
(2, 'Virginie', 'ZyJfcwKEY4', '04e33b5bff54a8cd122a1c2882ab5e50631834427fb6016165c6c842dd074af7');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`msg_id`),
  ADD KEY `msg_user_id` (`msg_user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`usr_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `msg_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `usr_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`msg_user_id`) REFERENCES `users` (`usr_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
