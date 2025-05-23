CREATE TABLE client (
                        uid BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID，主键',
                        username VARCHAR(255) COMMENT '用户名',
                        password VARCHAR(255) COMMENT '密码',
                        email VARCHAR(255) COMMENT '邮箱',
                        phone VARCHAR(255) COMMENT '电话'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
