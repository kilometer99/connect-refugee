-- phpMyAdmin SQL Dump
-- version 5.1.1deb5ubuntu1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 06, 2022 at 08:34 PM
-- Server version: 8.0.31-0ubuntu0.22.04.1
-- PHP Version: 8.1.2-1ubuntu2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `connect-refugee`
--

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `name`, `type`) VALUES
(1, 'Accommodation', 'category'),
(2, 'Insurance', 'category'),
(3, 'S Permit', 'category'),
(4, 'Small Jobs', 'category'),
(5, 'Document translation', 'category'),
(6, 'Others', 'category'),
(7, 'Zürich', 'city'),
(8, 'Basel', 'city'),
(9, 'Bern', 'city'),
(10, 'Geneva', 'city'),
(11, 'Lausanne', 'city'),
(12, 'Lugano', 'city');

-- --------------------------------------------------------

--
-- Table structure for table `category_posts`
--

CREATE TABLE `category_posts` (
  `category_id` bigint NOT NULL,
  `posts_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(14);

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

CREATE TABLE `post` (
  `id` bigint NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` varchar(255) DEFAULT NULL,
  `description` longtext,
  `title` varchar(255) DEFAULT NULL,
  `category_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `reply`
--

CREATE TABLE `reply` (
  `id` bigint NOT NULL,
  `message` longtext,
  `replied_by` varchar(255) DEFAULT NULL,
  `replied_date` varchar(255) DEFAULT NULL,
  `parent_reply_id` bigint DEFAULT NULL,
  `post_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `category_posts`
--
ALTER TABLE `category_posts`
  ADD PRIMARY KEY (`category_id`,`posts_id`),
  ADD UNIQUE KEY `UK_1maol788hp92rxc9l799wfkl5` (`posts_id`);

--
-- Indexes for table `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKg6l1ydp1pwkmyj166teiuov1b` (`category_id`);

--
-- Indexes for table `reply`
--
ALTER TABLE `reply`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKa2ah1l9ea6d6kc1xgm30na7kv` (`parent_reply_id`),
  ADD KEY `FKnpyg5e6pqr2v1y4y6pacte11q` (`post_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `category_posts`
--
ALTER TABLE `category_posts`
  ADD CONSTRAINT `FK82w6sik5voqb8ysv5yxvi28wp` FOREIGN KEY (`posts_id`) REFERENCES `post` (`id`),
  ADD CONSTRAINT `FKgjtl6hhu4h103nu9ec8btedp9` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`);

--
-- Constraints for table `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `FKg6l1ydp1pwkmyj166teiuov1b` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`);

--
-- Constraints for table `reply`
--
ALTER TABLE `reply`
  ADD CONSTRAINT `FKa2ah1l9ea6d6kc1xgm30na7kv` FOREIGN KEY (`parent_reply_id`) REFERENCES `reply` (`id`),
  ADD CONSTRAINT `FKnpyg5e6pqr2v1y4y6pacte11q` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);
COMMIT;
