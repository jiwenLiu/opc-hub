-- OPC Hub 单机版数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS opc_hub DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE opc_hub;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(50) NOT NULL COMMENT '密码(MD5)',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `avatar` VARCHAR(255) COMMENT '头像',
    `role` VARCHAR(20) DEFAULT 'opc' COMMENT '角色: opc-一人公司, enterprise-企业',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 服务分类表
CREATE TABLE IF NOT EXISTS `service_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `icon` VARCHAR(255) COMMENT '图标',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务分类表';

-- OPC服务表
CREATE TABLE IF NOT EXISTS `opc_service` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '发布者ID',
    `service_name` VARCHAR(100) NOT NULL COMMENT '服务名称',
    `category_id` BIGINT COMMENT '分类ID',
    `description` TEXT COMMENT '服务描述',
    `price` DECIMAL(10,2) COMMENT '价格',
    `unit` VARCHAR(20) COMMENT '单位',
    `delivery_days` INT COMMENT '交付天数',
    `tags` VARCHAR(255) COMMENT '标签',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态: pending-待审核, published-已发布, rejected-已拒绝',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OPC服务表';

-- 企业需求表
CREATE TABLE IF NOT EXISTS `enterprise_requirement` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '发布者ID',
    `title` VARCHAR(200) NOT NULL COMMENT '需求标题',
    `category_id` BIGINT COMMENT '分类ID',
    `description` TEXT COMMENT '需求描述',
    `budget` DECIMAL(10,2) COMMENT '预算',
    `deadline` VARCHAR(50) COMMENT '截止日期',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业需求表';

-- 交易订单表
CREATE TABLE IF NOT EXISTS `trade_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    `buyer_id` BIGINT NOT NULL COMMENT '买家ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家ID',
    `service_id` BIGINT NOT NULL COMMENT '服务ID',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态: pending-待支付, paid-已支付, delivered-已交付, completed-已完成, cancelled-已取消',
    `pay_time` DATETIME COMMENT '支付时间',
    `delivery_time` DATETIME COMMENT '交付时间',
    `complete_time` DATETIME COMMENT '完成时间',
    `remark` TEXT COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易订单表';

-- 企业认证表
CREATE TABLE IF NOT EXISTS `enterprise_certification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `company_name` VARCHAR(100) COMMENT '公司名称',
    `unified_social_credit_code` VARCHAR(50) COMMENT '统一社会信用代码',
    `legal_person` VARCHAR(50) COMMENT '法人代表',
    `business_license` VARCHAR(255) COMMENT '营业执照',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态',
    `reject_reason` TEXT COMMENT '拒绝原因',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业认证表';

-- OPC认证表
CREATE TABLE IF NOT EXISTS `opc_certification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `id_card` VARCHAR(20) COMMENT '身份证号',
    `face_id` VARCHAR(255) COMMENT '人脸ID',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态',
    `reject_reason` TEXT COMMENT '拒绝原因',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OPC认证表';

-- 初始化默认分类
INSERT INTO `service_category` (`name`, `icon`, `sort_order`) VALUES
('IT服务', 'computer', 1),
('设计服务', 'brush', 2),
('营销服务', 'trending-up', 3),
('法律服务', 'shield', 4),
('财税服务', 'calculator', 5),
('咨询服务', 'help-circle', 6),
('其他服务', 'more-horizontal', 7);

-- 初始化测试用户 (密码: 123456)
INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `status`) VALUES
('testopc', 'e10adc3949ba59abbe56e057f20f883e', '测试用户', 'opc', 1),
('testent', 'e10adc3949ba59abbe56e057f20f883e', '测试企业', 'enterprise', 1);