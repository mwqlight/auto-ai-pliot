-- AI Cockpit Platform 数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS ai_cockpit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ai_cockpit;

-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `email` VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像',
    `phone` VARCHAR(20) COMMENT '手机号',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `role` VARCHAR(20) DEFAULT 'USER' COMMENT '角色：ADMIN-管理员 USER-普通用户',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 创建模型表
CREATE TABLE IF NOT EXISTS `model` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '模型ID',
    `name` VARCHAR(100) NOT NULL COMMENT '模型名称',
    `description` TEXT COMMENT '模型描述',
    `type` VARCHAR(50) NOT NULL COMMENT '模型类型：TEXT_CLASSIFICATION, TEXT_GENERATION, IMAGE_CLASSIFICATION, etc.',
    `framework` VARCHAR(50) NOT NULL COMMENT '框架：PYTORCH, TENSORFLOW, HUGGINGFACE',
    `version` VARCHAR(50) DEFAULT '1.0.0' COMMENT '版本号',
    `file_path` VARCHAR(500) COMMENT '模型文件路径',
    `file_size` BIGINT DEFAULT 0 COMMENT '文件大小',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-未训练 1-训练中 2-已训练 3-已发布 4-已下线',
    `accuracy` DECIMAL(5,4) DEFAULT 0.0000 COMMENT '准确率',
    `created_by` BIGINT NOT NULL COMMENT '创建者ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`created_by`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_by` (`created_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模型表';

-- 创建数据集表
CREATE TABLE IF NOT EXISTS `dataset` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '数据集ID',
    `name` VARCHAR(100) NOT NULL COMMENT '数据集名称',
    `description` TEXT COMMENT '数据集描述',
    `type` VARCHAR(50) NOT NULL COMMENT '数据类型：TEXT, IMAGE, AUDIO, VIDEO',
    `file_path` VARCHAR(500) COMMENT '数据集文件路径',
    `file_size` BIGINT DEFAULT 0 COMMENT '文件大小',
    `data_count` INT DEFAULT 0 COMMENT '数据条数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `created_by` BIGINT NOT NULL COMMENT '创建者ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`created_by`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_by` (`created_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据集表';

-- 创建训练任务表
CREATE TABLE IF NOT EXISTS `training_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `name` VARCHAR(100) NOT NULL COMMENT '任务名称',
    `model_id` BIGINT NOT NULL COMMENT '模型ID',
    `dataset_id` BIGINT NOT NULL COMMENT '数据集ID',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-等待中 1-运行中 2-已完成 3-失败 4-取消',
    `progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '进度百分比',
    `epochs` INT DEFAULT 10 COMMENT '训练轮数',
    `batch_size` INT DEFAULT 32 COMMENT '批次大小',
    `learning_rate` DECIMAL(10,8) DEFAULT 0.001 COMMENT '学习率',
    `loss` DECIMAL(10,6) DEFAULT 0.000000 COMMENT '损失值',
    `accuracy` DECIMAL(5,4) DEFAULT 0.0000 COMMENT '准确率',
    `start_time` DATETIME COMMENT '开始时间',
    `end_time` DATETIME COMMENT '结束时间',
    `created_by` BIGINT NOT NULL COMMENT '创建者ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`model_id`) REFERENCES `model`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`dataset_id`) REFERENCES `dataset`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`created_by`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_status` (`status`),
    INDEX `idx_model_id` (`model_id`),
    INDEX `idx_created_by` (`created_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='训练任务表';

-- 创建部署表
CREATE TABLE IF NOT EXISTS `deployment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部署ID',
    `name` VARCHAR(100) NOT NULL COMMENT '部署名称',
    `model_id` BIGINT NOT NULL COMMENT '模型ID',
    `endpoint` VARCHAR(500) COMMENT 'API端点',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-未部署 1-部署中 2-已部署 3-异常 4-已下线',
    `cpu_usage` DECIMAL(5,2) DEFAULT 0.00 COMMENT 'CPU使用率',
    `memory_usage` DECIMAL(5,2) DEFAULT 0.00 COMMENT '内存使用率',
    `request_count` BIGINT DEFAULT 0 COMMENT '请求次数',
    `error_count` BIGINT DEFAULT 0 COMMENT '错误次数',
    `last_request_time` DATETIME COMMENT '最后请求时间',
    `created_by` BIGINT NOT NULL COMMENT '创建者ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`model_id`) REFERENCES `model`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`created_by`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_status` (`status`),
    INDEX `idx_model_id` (`model_id`),
    INDEX `idx_created_by` (`created_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部署表';

-- 创建知识图谱表
CREATE TABLE IF NOT EXISTS `knowledge_graph` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图谱ID',
    `name` VARCHAR(100) NOT NULL COMMENT '图谱名称',
    `description` TEXT COMMENT '图谱描述',
    `node_count` INT DEFAULT 0 COMMENT '节点数量',
    `edge_count` INT DEFAULT 0 COMMENT '边数量',
    `file_path` VARCHAR(500) COMMENT '图谱文件路径',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `created_by` BIGINT NOT NULL COMMENT '创建者ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`created_by`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_status` (`status`),
    INDEX `idx_created_by` (`created_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识图谱表';

-- 创建RAG配置表
CREATE TABLE IF NOT EXISTS `rag_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `name` VARCHAR(100) NOT NULL COMMENT '配置名称',
    `description` TEXT COMMENT '配置描述',
    `knowledge_graph_id` BIGINT COMMENT '知识图谱ID',
    `embedding_model` VARCHAR(100) DEFAULT 'all-MiniLM-L6-v2' COMMENT '嵌入模型',
    `retrieval_top_k` INT DEFAULT 5 COMMENT '检索top-k',
    `similarity_threshold` DECIMAL(5,4) DEFAULT 0.7 COMMENT '相似度阈值',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `created_by` BIGINT NOT NULL COMMENT '创建者ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`knowledge_graph_id`) REFERENCES `knowledge_graph`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`created_by`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_status` (`status`),
    INDEX `idx_created_by` (`created_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='RAG配置表';

-- 创建Agent配置表
CREATE TABLE IF NOT EXISTS `agent_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `name` VARCHAR(100) NOT NULL COMMENT '配置名称',
    `description` TEXT COMMENT '配置描述',
    `type` VARCHAR(50) NOT NULL COMMENT 'Agent类型：CHAT, TOOL, WORKFLOW',
    `model_id` BIGINT COMMENT '模型ID',
    `rag_config_id` BIGINT COMMENT 'RAG配置ID',
    `tools` JSON COMMENT '工具配置',
    `workflow` JSON COMMENT '工作流配置',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `created_by` BIGINT NOT NULL COMMENT '创建者ID',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`model_id`) REFERENCES `model`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`rag_config_id`) REFERENCES `rag_config`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`created_by`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_by` (`created_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Agent配置表';

-- 插入初始数据
-- 插入默认管理员用户（密码：admin123，使用BCrypt加密）
INSERT IGNORE INTO `user` (`id`, `username`, `email`, `password`, `nickname`, `role`, `status`) VALUES 
(1, 'admin', 'admin@aicockpit.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVwjvO', '系统管理员', 'ADMIN', 1);

-- 插入示例模型数据
INSERT IGNORE INTO `model` (`id`, `name`, `description`, `type`, `framework`, `version`, `status`, `accuracy`, `created_by`) VALUES 
(1, '文本分类模型', '基于BERT的文本分类模型', 'TEXT_CLASSIFICATION', 'HUGGINGFACE', '1.0.0', 3, 0.9500, 1),
(2, '图像识别模型', '基于ResNet的图像分类模型', 'IMAGE_CLASSIFICATION', 'PYTORCH', '1.0.0', 3, 0.9200, 1),
(3, '文本生成模型', '基于GPT的文本生成模型', 'TEXT_GENERATION', 'HUGGINGFACE', '1.0.0', 2, 0.0000, 1);

-- 插入示例数据集数据
INSERT IGNORE INTO `dataset` (`id`, `name`, `description`, `type`, `data_count`, `status`, `created_by`) VALUES 
(1, '新闻分类数据集', '包含10个类别的新闻文本数据', 'TEXT', 10000, 1, 1),
(2, 'CIFAR-10图像数据集', '包含10个类别的图像数据', 'IMAGE', 60000, 1, 1),
(3, '对话数据集', '用于训练对话模型的数据集', 'TEXT', 5000, 1, 1);

-- 插入示例部署数据
INSERT IGNORE INTO `deployment` (`id`, `name`, `model_id`, `endpoint`, `status`, `created_by`) VALUES 
(1, '文本分类API服务', 1, 'http://localhost:8080/api/v1/models/1/predict', 2, 1),
(2, '图像识别API服务', 2, 'http://localhost:8080/api/v1/models/2/predict', 2, 1);

-- 创建索引优化查询性能
-- MySQL 5.7不支持CREATE INDEX IF NOT EXISTS语法，使用以下方式
CREATE INDEX idx_user_username ON `user`(username);
CREATE INDEX idx_user_email ON `user`(email);
CREATE INDEX idx_model_type_status ON `model`(type, status);
CREATE INDEX idx_training_task_status ON `training_task`(status);
CREATE INDEX idx_deployment_status ON `deployment`(status);

-- 显示表结构信息
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    DATA_LENGTH,
    INDEX_LENGTH,
    CREATE_TIME
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'ai_cockpit';