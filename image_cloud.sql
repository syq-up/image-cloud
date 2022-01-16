/*
 Navicat Premium Data Transfer

 Source Server         : Remote
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 39.102.50.39:3306
 Source Schema         : image_cloud

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 16/01/2022 11:47:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for image
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `filename` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图像文件名',
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '图像存储路径',
  `size` int(11) NOT NULL COMMENT '图像大小',
  `deleted` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除（0否1是）',
  `user_id` int(6) UNSIGNED ZEROFILL NOT NULL COMMENT '用户id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图像信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for setting
-- ----------------------------
DROP TABLE IF EXISTS `setting`;
CREATE TABLE `setting`  (
  `user_id` int(6) UNSIGNED ZEROFILL NOT NULL COMMENT '用户id（主键）',
  `theme` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'dark' COMMENT '主题（默认“dark”）',
  `theme_color` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '#409EFF' COMMENT '主题颜色（十六进制）',
  `show_date_in_list` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '是否在图像列表显示时间标签（0否1是，默认1）',
  `show_date_in_recycle` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '是否在回收站显示时间标签（0否1是，默认1）',
  `fixed_header` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否固定头部（0否1是，默认0）',
  `dynamic_title` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '是否显示动态标题（0否1是，默认1）',
  `upload_directly` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '是否立即上传图像（0否1是，默认1）',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户的设置表，包含一些布局设置、上传设置等。' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(6) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `deleted` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除（0否1是）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_id` int(6) UNSIGNED ZEROFILL NOT NULL COMMENT '用户id（主键）',
  `nickname` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `avatar_path` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户头像路径',
  `stored_num` int(11) NOT NULL DEFAULT 0 COMMENT '已存储图像总数',
  `stored_size` bigint(20) NOT NULL DEFAULT 0 COMMENT '已存储图像总大小（字节）',
  `secondary_path` json NULL COMMENT '用户创建的所有次级路径，json数组',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
