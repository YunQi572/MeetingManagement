package com.example.meetingmanage.controller;

import com.example.meetingmanage.entities.Msg;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 显示管理员凭据的控制器 - 任何请求都会显示admin凭据
 */
@RestController
@RequestMapping("/admin-credentials")
public class AdminCredentialsController {

    private static final Logger logger = Logger.getLogger(AdminCredentialsController.class);

    @GetMapping
    public Msg showCredentials() {
        displayCredentials();

        Map<String, Object> data = new HashMap<>();
        data.put("username", "admin");
        data.put("password", "admin");

        return Msg.success().add(data);
    }

    @PostMapping
    public Msg showCredentialsPost() {
        return showCredentials();
    }

    @RequestMapping("/**")
    public Msg anyPath() {
        return showCredentials();
    }

    private void displayCredentials() {
        logger.info("有人请求显示管理员凭据");

        System.out.println("\n=========================================");
        System.out.println("管理员凭据 (通过AdminCredentialsController):");
        System.out.println("用户名: admin");
        System.out.println("密码: admin");
        System.out.println("=========================================\n");

        logger.info("管理员凭据 - 用户名: admin, 密码: admin");
    }
}