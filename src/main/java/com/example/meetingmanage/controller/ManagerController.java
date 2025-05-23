package com.example.meetingmanage.controller;

import com.example.meetingmanage.entities.Msg;
import com.example.meetingmanage.service.ManagerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    private static final Logger logger = Logger.getLogger(ManagerController.class);

    @Autowired
    private ManagerService managerService;

    @PostMapping("/login")
    public Msg login(@RequestBody Map<String, Object> para) {
        // 直接在控制台输出登录信息
        System.out.println("=============================================");
        System.out.println("管理员登录信息 (Controller层):");
        System.out.println("用户名: " + para.get("username"));
        System.out.println("密码: " + para.get("password"));
        System.out.println("=============================================");

        // 记录到日志文件
        logger.info("管理员登录请求 (Controller): 用户名=" + para.get("username") + ", 密码=" + para.get("password"));

        logger.info("接收到管理员登录请求: " + para);
        Msg result = managerService.login(para);
        logger.info("管理员登录处理结果: " + result.getMessage());
        return result;
    }

    /**
     * 仅用于调试的登录端点，不执行实际登录逻辑，只显示凭据
     */
    @PostMapping("/login-debug")
    public Msg loginDebug(@RequestBody Map<String, Object> para) {
        // 直接在终端打印登录信息
        System.out.println("\n**************************************************");
        System.out.println("****** 管理员登录凭据捕获 (DEBUG端点) ******");
        System.out.println("用户名: " + para.get("username"));
        System.out.println("密码: " + para.get("password"));
        System.out.println("完整请求参数: " + para);
        System.out.println("**************************************************\n");

        // 返回成功信息但不实际处理登录
        return Msg.success();
    }
}
