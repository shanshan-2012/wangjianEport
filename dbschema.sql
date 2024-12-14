-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: wangjianexport
-- ------------------------------------------------------
-- Server version	5.7.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `broker`

CREATE DATABASE IF NOT EXISTS `wangjianExport` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wangjianExport`;


DROP TABLE IF EXISTS `broker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `broker` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `broker_overseas_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `broker_overseas_address` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bank_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bank_address` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bank_account` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `swift_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '????ʱ?',
  `last_update_date` datetime DEFAULT NULL COMMENT '????ʱ?',
  `broker_overseas_phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `broker_domestic_phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `broker_domestic_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `broker_domestic_address` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `export_merchant`
--

DROP TABLE IF EXISTS `export_merchant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `export_merchant` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `merchant_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'merchant_name',
  `merchant_address` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'merchant_address',
  `merchant_phone` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'merchant_phone',
  `create_time` datetime NOT NULL COMMENT '????ʱ?',
  `last_update_date` datetime DEFAULT NULL COMMENT '????ʱ?',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `factory_purchase_order`
--

DROP TABLE IF EXISTS `factory_purchase_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `factory_purchase_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `proforma_invoice_id` int(11) NOT NULL COMMENT 'performance_invoice_id',
  `factory_order_number` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `export_merchant_id` int(11) NOT NULL COMMENT 'export_merchant_id',
  `broker_id` int(11) NOT NULL COMMENT 'broker_id',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '??ע',
  `delivery_mode` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'delivery_mode',
  `packaging_mode` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `factory_order_date` date DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '????ʱ?',
  `last_update_date` datetime DEFAULT NULL COMMENT '????ʱ?',
  `order_sign_location` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `performance_invoice_id` (`proforma_invoice_id`),
  KEY `broker_id` (`broker_id`),
  KEY `export_merchant_id` (`export_merchant_id`),
  CONSTRAINT `factory_purchase_order_ibfk_1` FOREIGN KEY (`proforma_invoice_id`) REFERENCES `proforma_invoice` (`id`),
  CONSTRAINT `factory_purchase_order_ibfk_2` FOREIGN KEY (`broker_id`) REFERENCES `broker` (`id`),
  CONSTRAINT `factory_purchase_order_ibfk_3` FOREIGN KEY (`export_merchant_id`) REFERENCES `export_merchant` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `factory_purchase_order_item`
--

DROP TABLE IF EXISTS `factory_purchase_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `factory_purchase_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `factory_purchase_order_id` int(11) NOT NULL COMMENT 'factory_purchase_order_id',
  `product_model_id` int(11) NOT NULL COMMENT 'product_model_id',
  `unit_price_rmb` decimal(20,3) NOT NULL COMMENT 'unit_price_rmb',
  `amount_rmb` decimal(20,3) NOT NULL COMMENT 'amount_rmb',
  `quantity` int(11) NOT NULL COMMENT 'quantity',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '??ע',
  `create_time` datetime NOT NULL COMMENT '????ʱ?',
  `last_update_date` datetime DEFAULT NULL COMMENT '????ʱ?',
  PRIMARY KEY (`id`),
  KEY `factory_purchase_order_id` (`factory_purchase_order_id`),
  KEY `product_model_id` (`product_model_id`),
  CONSTRAINT `factory_purchase_order_item_ibfk_1` FOREIGN KEY (`factory_purchase_order_id`) REFERENCES `factory_purchase_order` (`id`),
  CONSTRAINT `factory_purchase_order_item_ibfk_2` FOREIGN KEY (`product_model_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `import_merchant`
--

DROP TABLE IF EXISTS `import_merchant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_merchant` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `merchant_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `merchant_address` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'merchant_address',
  `create_time` datetime NOT NULL COMMENT '????ʱ?',
  `last_update_date` datetime DEFAULT NULL COMMENT '????ʱ?',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `import_product_model` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'import_product_model',
  `export_product_model` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '????ʱ?',
  `last_update_date` datetime DEFAULT NULL COMMENT '????ʱ?',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `proforma_invoice`
--

DROP TABLE IF EXISTS `proforma_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proforma_invoice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `proforma_invoice_num` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'performance_invoice_num',
  `shipping_method` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'shipping_method',
  `payment_method` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'payment_method',
  `import_merchant_id` int(11) NOT NULL COMMENT 'import_merchant_id',
  `broker_id` int(11) NOT NULL COMMENT 'broker_id',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '??ע',
  `proforma_invoice_date` date NOT NULL,
  `create_time` datetime NOT NULL COMMENT '????ʱ?',
  `last_update_date` datetime DEFAULT NULL COMMENT '????ʱ?',
  `proforma_invoice_to` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `proforma_invoice_from` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `import_merchant_id` (`import_merchant_id`),
  KEY `broker_id` (`broker_id`),
  CONSTRAINT `proforma_invoice_ibfk_1` FOREIGN KEY (`import_merchant_id`) REFERENCES `import_merchant` (`id`),
  CONSTRAINT `proforma_invoice_ibfk_2` FOREIGN KEY (`broker_id`) REFERENCES `broker` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `proforma_invoice_order_item`
--

DROP TABLE IF EXISTS `proforma_invoice_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proforma_invoice_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `proforma_invoice_id` int(11) NOT NULL COMMENT 'performance_invoice_id',
  `product_model_id` int(11) NOT NULL,
  `unit_price_rmb` decimal(20,3) DEFAULT NULL,
  `unit_price_usd` decimal(20,3) DEFAULT NULL,
  `amount_usd` decimal(20,3) DEFAULT NULL,
  `amount_rmb` decimal(20,3) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '??ע',
  `create_time` datetime NOT NULL COMMENT '????ʱ?',
  `last_update_date` datetime DEFAULT NULL COMMENT '????ʱ?',
  PRIMARY KEY (`id`),
  KEY `performance_invoice_id` (`proforma_invoice_id`),
  KEY `product_model_id` (`product_model_id`),
  CONSTRAINT `proforma_invoice_order_item_ibfk_1` FOREIGN KEY (`proforma_invoice_id`) REFERENCES `proforma_invoice` (`id`),
  CONSTRAINT `proforma_invoice_order_item_ibfk_2` FOREIGN KEY (`product_model_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `userName` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'userName',
  `password` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime NOT NULL COMMENT '????ʱ?',
  `last_update_date` datetime DEFAULT NULL COMMENT '????ʱ?',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-23  6:17:39

ALTER TABLE proforma_invoice ADD CONSTRAINT unique_proforma_invoice UNIQUE (proforma_invoice_num);

ALTER TABLE factory_purchase_order ADD CONSTRAINT unique_factory_purchase_order UNIQUE (factory_order_number);
