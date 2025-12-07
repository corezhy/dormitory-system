-- MySQL dump 10.13  Distrib 9.1.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: dorm-myproject
-- ------------------------------------------------------
-- Server version	9.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `building`
--

DROP TABLE IF EXISTS `building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `building` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '楼栋ID',
  `name` varchar(50) NOT NULL COMMENT '楼栋名称',
  `code` varchar(20) NOT NULL COMMENT '楼栋编码（如B01）',
  `gender_type` tinyint NOT NULL COMMENT '性别类型：0=男，1=女',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1=启用，0=停用',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='楼栋表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '班级ID',
  `code` varchar(20) NOT NULL COMMENT '班级编码（如 COMP2301）',
  `name` varchar(50) NOT NULL COMMENT '班级全称（如 计算机科学与技术2301班）',
  `major_code` varchar(30) NOT NULL COMMENT '所属专业代码（关联 major.code）',
  `grade` int NOT NULL COMMENT '入学年级（如 2023）',
  `counselor` varchar(50) DEFAULT NULL COMMENT '辅导员姓名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间（由应用层设置）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_major_grade` (`major_code`,`grade`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='班级表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `dept_code` varchar(20) NOT NULL COMMENT '部门编码',
  `dept_name` varchar(50) NOT NULL COMMENT '部门名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dept_code` (`dept_code`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dorm_room`
--

DROP TABLE IF EXISTS `dorm_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dorm_room` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '宿舍ID',
  `building_id` bigint NOT NULL COMMENT '所属楼栋ID（逻辑外键）',
  `floor_id` bigint NOT NULL COMMENT '所属楼层ID（逻辑外键）',
  `room_number` int NOT NULL COMMENT '房间号',
  `full_code` varchar(20) NOT NULL COMMENT '完整编号，如"1-1-01"',
  `capacity` int NOT NULL DEFAULT '6' COMMENT '总床位数',
  `available_beds` int NOT NULL DEFAULT '6' COMMENT '空闲床铺数（0~6）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1=正常，0=停用/维修',
  `create_time` datetime NOT NULL COMMENT '创建时间（由应用层设置）',
  `update_time` datetime NOT NULL COMMENT '最后更新时间（由应用层设置）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_full_code` (`full_code`),
  KEY `idx_building_floor` (`building_id`,`floor_id`),
  CONSTRAINT `chk_available_beds` CHECK (((`available_beds` >= 0) and (`available_beds` <= `capacity`)))
) ENGINE=InnoDB AUTO_INCREMENT=1086 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='宿舍表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `emp_no` varchar(20) NOT NULL COMMENT '员工编号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` tinyint DEFAULT NULL COMMENT '性别：0-女，1-男，2-未知',
  `phone` varchar(15) DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `entry_date` date NOT NULL COMMENT '入职日期',
  `position` varchar(50) DEFAULT NULL COMMENT '职位',
  `dept_id` bigint unsigned DEFAULT NULL COMMENT '所属部门ID（逻辑外键）',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像路径',
  `username` varchar(50) NOT NULL COMMENT '登录用户名',
  `password` varchar(255) NOT NULL COMMENT '密码（加密）',
  `create_time` datetime NOT NULL COMMENT '创建时间（手动指定）',
  `update_time` datetime NOT NULL COMMENT '更新时间（手动指定）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `emp_no` (`emp_no`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `id_card` (`id_card`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `floor`
--

DROP TABLE IF EXISTS `floor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `floor` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '楼层ID',
  `building_id` bigint NOT NULL COMMENT '所属楼栋ID（逻辑外键 → building.id）',
  `floor_number` int NOT NULL COMMENT '楼层号',
  `major_code` varchar(30) DEFAULT NULL COMMENT '限定专业代码（逻辑外键 → major.code），NULL表示不限',
  `total_rooms` int NOT NULL COMMENT '本层宿舍总数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_building_floor` (`building_id`,`floor_number`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_major_code` (`major_code`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='楼层表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `major`
--

DROP TABLE IF EXISTS `major`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `major` (
  `code` varchar(30) NOT NULL COMMENT '专业代码（主键）',
  `name` varchar(100) NOT NULL COMMENT '专业名称',
  `department` varchar(100) DEFAULT NULL COMMENT '所属院系',
  `is_enabled` tinyint NOT NULL DEFAULT '1' COMMENT '是否启用：1=是，0=否',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`code`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='专业字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '学生ID',
  `student_no` varchar(20) NOT NULL COMMENT '学号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` tinyint NOT NULL COMMENT '性别：0=男，1=女',
  `major_code` varchar(30) NOT NULL COMMENT '专业代码（逻辑外键 → major.code）',
  `class_id` bigint NOT NULL COMMENT '班级ID（逻辑外键 → class.id）',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `violation_score` int NOT NULL DEFAULT '0' COMMENT '违纪扣分（默认0）',
  `building_id` bigint DEFAULT NULL COMMENT '所在楼栋ID（逻辑外键）',
  `floor_id` bigint DEFAULT NULL COMMENT '所在楼层ID（逻辑外键）',
  `room_id` bigint DEFAULT NULL COMMENT '所在宿舍ID（逻辑外键）',
  `bed_no` int DEFAULT NULL COMMENT '床位号（1~6）',
  `check_in_time` datetime DEFAULT NULL COMMENT '入住时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1=在住，0=已退宿',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间（由应用层设置）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_no` (`student_no`),
  UNIQUE KEY `uk_room_bed` (`room_id`,`bed_no`) COMMENT '同一宿舍床位唯一',
  KEY `idx_major_code` (`major_code`),
  KEY `idx_building_floor_room` (`building_id`,`floor_id`,`room_id`),
  KEY `idx_class_id` (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=463 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-07 18:43:42
