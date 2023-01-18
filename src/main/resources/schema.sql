-- MySQL dump 10.13  Distrib 8.0.27, for Linux (x86_64)
--
-- Table structure for table `client`
--
USE poc_db;

DROP TABLE IF EXISTS `client`; 
CREATE TABLE `client` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_on` datetime NOT NULL,
  `deleted_on` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `version` int DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_on` datetime NOT NULL,
  `deleted_on` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `version` int DEFAULT NULL,
  `brand_name` varchar(255) NOT NULL,
  `code` varchar(6) NOT NULL,
  `manufacturing_date` date DEFAULT NULL,
  `max_retail_price` decimal(19,2) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

