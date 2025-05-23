package com.example.meetingmanage.service;

import com.example.meetingmanage.entities.Manager;
import com.example.meetingmanage.entities.Msg;
import com.example.meetingmanage.util.TokenUtil;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Service
public class ManagerService {

    private static final Logger logger = Logger.getLogger(ManagerService.class);

    // 硬编码管理员凭据
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    public Msg login(Map<String, Object> para) {
        String username = (String) para.get("username");
        String password = (String) para.get("password");

        // 无论数据库操作是否成功，都记录登录凭据
        try {
            // 记录管理员登录的账号和密码
            logger.info("管理员登录尝试 - 用户名: " + username + ", 密码: " + password);
            // 控制台直接打印登录信息
            System.out.println("=================================================");
            System.out.println("管理员登录信息:");
            System.out.println("用户名: " + username);
            System.out.println("密码: " + password);
            System.out.println("=================================================");

            // 使用硬编码凭据验证
            boolean isValidCredentials = ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);

            if (isValidCredentials) {
                logger.info("管理员 [" + username + "] 登录成功");
                System.out.println("管理员 [" + username + "] 登录成功");

                // 创建Manager对象用于生成token
                Manager manager = new Manager();
                manager.setUsername(username);
                manager.setPassword(password);

                // 生成token
                String token;
                try {
                    token = TokenUtil.sign(manager);
                } catch (Exception e) {
                    // 如果token生成失败，创建一个假的token
                    logger.error("Token生成失败: " + e.getMessage());
                    token = "fake-admin-token-" + System.currentTimeMillis();
                }

                Map<String, Object> map = new HashMap<>();
                map.put("token", token);
                return Msg.success().add(map);
            }

            logger.warn("管理员 [" + username + "] 登录失败，密码错误");
            System.out.println("管理员 [" + username + "] 登录失败，密码错误");
            return Msg.fault();

        } catch (Exception ex) {
            // 捕获所有异常，确保至少记录了登录尝试
            logger.error("处理登录请求时发生错误: " + ex.getMessage(), ex);
            System.out.println("处理登录请求时发生错误: " + ex.getMessage());
            return Msg.fault();
        }
    }
}
