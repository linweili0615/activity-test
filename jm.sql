/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.3
Source Server Version : 50561
Source Host           : 192.168.1.3:3306
Source Database       : jm

Target Server Type    : MYSQL
Target Server Version : 50561
File Encoding         : 65001

Date: 2018-11-14 23:08:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for interface
-- ----------------------------
DROP TABLE IF EXISTS `interface`;
CREATE TABLE `interface` (
  `id` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `headers` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `body` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `update_author` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of interface
-- ----------------------------

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` varchar(255) NOT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `update_author` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES ('24719c71-e80d-11e8-9ff0-0242ac110002', '用户服务', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('2a6dfe5a-e80d-11e8-9ff0-0242ac110002', '用户服务后台', '0', 'linweili', 'linweili', '2018-11-14 12:59:47', '2018-11-14 12:59:47');
INSERT INTO `project` VALUES ('35350488-e80d-11e8-9ff0-0242ac110002', '单点登录', '0', 'linweili', 'linweili', '2018-11-14 13:00:05', '2018-11-14 13:00:05');
INSERT INTO `project` VALUES ('38f2feef-e80d-11e8-9ff0-0242ac110002', '佣金服务', '0', 'linweili', 'linweili', '2018-11-14 13:00:12', '2018-11-14 13:00:12');
INSERT INTO `project` VALUES ('3e572bbf-e80d-11e8-9ff0-0242ac110002', '团宝箱服务', '0', 'linweili', 'linweili', '2018-11-14 13:00:21', '2018-11-14 13:00:21');
INSERT INTO `project` VALUES ('423a3e62-e80d-11e8-9ff0-0242ac110002', '会员服务', '0', 'linweili', 'linweili', '2018-11-14 13:00:27', '2018-11-14 13:00:27');
INSERT INTO `project` VALUES ('47013873-e80d-11e8-9ff0-0242ac110002', '商家后台', '0', 'linweili', 'linweili', '2018-11-14 13:00:35', '2018-11-14 13:00:35');
INSERT INTO `project` VALUES ('50fea2a2-e80d-11e8-9ff0-0242ac110002', '极验', '0', 'linweili', 'linweili', '2018-11-14 13:00:52', '2018-11-14 13:00:52');
INSERT INTO `project` VALUES ('552f92ec-e80d-11e8-9ff0-0242ac110002', '呼叫系统', '0', 'linweili', 'linweili', '2018-11-14 13:00:59', '2018-11-14 13:00:59');
INSERT INTO `project` VALUES ('63b87049-e81a-11e8-9ff0-0242ac110002', '用户服务1', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('67ed7b5b-e81a-11e8-9ff0-0242ac110002', '用户服务2', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('6a59233c-e81a-11e8-9ff0-0242ac110002', '用户服务3', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('6de93131-e81a-11e8-9ff0-0242ac110002', '用户服务4', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('70b2fa9f-e81a-11e8-9ff0-0242ac110002', '用户服务5', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('730ff1fd-e81a-11e8-9ff0-0242ac110002', '用户服务56', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('78473178-e81a-11e8-9ff0-0242ac110002', '用户服务7', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('7ab289fe-e81a-11e8-9ff0-0242ac110002', '用户服务8', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('7cf85706-e81a-11e8-9ff0-0242ac110002', '用户服务9', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('80431262-e81a-11e8-9ff0-0242ac110002', '用户服务0', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('8290a5e0-e81a-11e8-9ff0-0242ac110002', '用户服务60', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');
INSERT INTO `project` VALUES ('85c5a2b4-e81a-11e8-9ff0-0242ac110002', '用户服务50', '0', 'linweili', 'linweili', '2018-11-14 12:59:37', '2018-11-14 12:59:37');

-- ----------------------------
-- Table structure for testcase
-- ----------------------------
DROP TABLE IF EXISTS `testcase`;
CREATE TABLE `testcase` (
  `id` int(11) NOT NULL,
  `project_id` varchar(255) DEFAULT NULL,
  `case_name` varchar(255) DEFAULT NULL,
  `case_list` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `update_author` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of testcase
-- ----------------------------

-- ----------------------------
-- Table structure for testproject
-- ----------------------------
DROP TABLE IF EXISTS `testproject`;
CREATE TABLE `testproject` (
  `id` varchar(255) NOT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `update_author` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of testproject
-- ----------------------------

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
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('dd7863e5-eac9-41d4-bf67-b846f3fa8a90', 'test_user', '15866660001', 'afe48d8e5878947928495483da730f63', '0', '0', '2018-11-07 13:25:21', '2018-11-07 13:25:21');

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_token
-- ----------------------------
INSERT INTO `user_token` VALUES ('9', 'dd7863e5-eac9-41d4-bf67-b846f3fa8a90', 'eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJkZDc4NjNlNS1lYWM5LTQxZDQtYmY2Ny1iODQ2ZjNmYThhOTAiLCJpYXQiOjE1NDIyMDczNTgsInN1YiI6ImFjdGl2aXR5IiwiaXNzIjoiMjk1Mjg3NzY1QHFxLmNvbSIsImV4cCI6MTU0MjIxMDk1OH0.GfgqOhtgwrzflDizductcq9v8k0HjHSBcsi4cZ8ZG1s', '0', '2018-11-14 23:55:58', '2018-11-07 13:27:18', '2018-11-14 14:53:27');
