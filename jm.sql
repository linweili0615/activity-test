/*
Navicat MySQL Data Transfer

Source Server         : 10.100.99.164
Source Server Version : 50557
Source Host           : 10.100.99.164:3306
Source Database       : jm

Target Server Type    : MYSQL
Target Server Version : 50557
File Encoding         : 65001

Date: 2018-10-31 12:48:37
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
  `pwd` varchar(255) NOT NULL,
  `error_count` int(2) DEFAULT '0',
  `status` int(2) DEFAULT '0',
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_token
-- ----------------------------
DROP TABLE IF EXISTS `user_token`;
CREATE TABLE `user_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `status` int(2) DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;