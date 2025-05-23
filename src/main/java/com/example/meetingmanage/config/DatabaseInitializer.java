package com.example.meetingmanage.config;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        // 直接输出管理员账户信息，不尝试连接数据库
        try {
            logger.info("初始化管理员账户信息");

            // 输出到日志
            logger.info("=========================================");
            logger.info("管理员账户信息:");
            logger.info("用户名: admin");
            logger.info("密码: admin");
            logger.info("=========================================");

            // 输出到控制台
            System.out.println("=========================================");
            System.out.println("管理员账户信息 (无数据库模式):");
            System.out.println("用户名: admin");
            System.out.println("密码: admin");
            System.out.println("=========================================");

        } catch (Exception e) {
            logger.error("显示管理员账户信息时出错: " + e.getMessage(), e);
            System.out.println("显示管理员账户信息时出错: " + e.getMessage());
        }
    }
}