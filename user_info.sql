/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.3
Source Server Version : 50723
Source Host           : 192.168.1.3:3306
Source Database       : springboot

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-10-24 22:38:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` varchar(255) NOT NULL COMMENT '用户id',
  `username` varchar(255) NOT NULL,
  `telno` varchar(255) NOT NULL,
  `error_count` int(2) DEFAULT '0',
  `status` int(2) DEFAULT '0',
  `add_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
