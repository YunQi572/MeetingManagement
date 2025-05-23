package com.example.meetingmanage.config;

import com.example.meetingmanage.entities.*;
import com.example.meetingmanage.mapper.*;
import com.example.meetingmanage.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 全局Mock配置
 * 提供所有需要的Mapper和Service接口的模拟实现
 */
@Configuration
public class GlobalMockConfig {

    /**
     * 创建一个通用的Mapper提供者，可以动态生成任何类型的Mapper
     */
    @Bean
    @ConditionalOnMissingBean(name = "anyMapperProvider")
    public Object anyMapperProvider() {
        System.out.println("创建通用Mapper提供者");

        // 这里可以通过代理动态创建任何类型的Mapper
        // 但简单起见，我们在MinimalController中处理所有请求
        return new Object();
    }

    /**
     * 全局打印管理员凭据的Bean
     */
    @Bean
    public Object credentialsPrinter() {
        System.out.println("\n==================================================");
        System.out.println("【全局Bean】管理员账号信息:");
        System.out.println("用户名: admin");
        System.out.println("密码: admin");
        System.out.println("==================================================\n");
        return new Object();
    }

    /**
     * 管理员服务，主要用于登录验证
     */
    @Bean
    @Primary
    public ManagerService managerService() {
        return new ManagerService() {
            @Override
            public Msg login(Map<String, Object> para) {
                System.out.println("模拟 ManagerService.login: " + para);

                // 打印管理员登录凭据
                System.out.println("\n============================================");
                System.out.println("管理员登录凭据 (MockService):");
                System.out.println("用户名: admin");
                System.out.println("密码: admin");
                System.out.println("============================================\n");

                // 构造返回数据
                Map<String, Object> data = new java.util.HashMap<>();
                data.put("token", "mock-admin-token");

                return Msg.success().add(data);
            }
        };
    }
}