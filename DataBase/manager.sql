CREATE TABLE manager (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID，主键',
                         username VARCHAR(255) COMMENT '用户名',
                         password VARCHAR(255) COMMENT '密码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
