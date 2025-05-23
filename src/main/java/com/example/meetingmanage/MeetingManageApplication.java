package com.example.meetingmanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * 会议管理系统主应用类
 * 禁用了所有数据库相关配置，使用mock服务实现
 */
//@SpringBootApplication(exclude = {
//        DataSourceAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class,
//        org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration.class,
//        org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration.class
//})
@SpringBootApplication
@MapperScan("com.example.meetingmanage.mapper")
// Removed MapperScan annotation since we're disabling database
public class MeetingManageApplication {

    public static void main(String[] args) {
        // 在启动时显示管理员凭据
        System.out.println("\n==================================================");
        System.out.println("管理员账号信息:");
        System.out.println("用户名: admin");
        System.out.println("密码: admin");
        System.out.println("==================================================\n");

        System.out.println("应用启动中，请稍后...");

        SpringApplication.run(MeetingManageApplication.class, args);

        System.out.println("\n==================================================");
        System.out.println("应用启动完成!");
        System.out.println("您可以使用以下账号登录:");
        System.out.println("用户名: admin");
        System.out.println("密码: admin");
        System.out.println("==================================================\n");
    }

}
