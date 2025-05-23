//package com.example.meetingmanage.controller;
//
//import com.example.meetingmanage.entities.Msg;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 简化控制器，仅显示管理员凭据，不调用任何服务
// */
//@RestController
//public class MinimalController {
//
//    /**
//     * 登录处理
//     */
//    @PostMapping("/manager/login")
//    public Msg login(@RequestBody Map<String, Object> para) {
//        displayCredentials();
//
//        // 提取登录信息
//        String username = (String) para.get("username");
//        String password = (String) para.get("password");
//
//        System.out.println("接收到登录请求: username=" + username + ", password=" + password);
//
//        // 始终返回成功，并包含token
//        Map<String, Object> data = new HashMap<>();
//        data.put("token", "admin-mock-token-12345");
//        data.put("username", "admin");
//
//        return Msg.success().add(data);
//    }
//
//    /**
//     * 专门显示管理员登录凭据的端点
//     */
//    @GetMapping({ "/admin-credentials", "/debug-creds" })
//    public Msg showCredentials() {
//        displayCredentials();
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("username", "admin");
//        data.put("password", "admin");
//
//        return Msg.success().add(data);
//    }
//
//    /**
//     * 拦截所有其他请求
//     */
//    @RequestMapping(value = "/**", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
//            RequestMethod.DELETE })
//    public Msg catchAll(@RequestBody(required = false) Map<String, Object> body,
//            @RequestParam(required = false) Map<String, Object> params) {
//        displayCredentials();
//
//        System.out.println("接收到请求，参数: " + (body != null ? body : params));
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("message", "服务器已接收请求，管理员账号为admin，密码为admin");
//        data.put("username", "admin");
//        data.put("password", "admin");
//
//        return Msg.success().add(data);
//    }
//
//    /**
//     * 在控制台显示凭据
//     */
//    private void displayCredentials() {
//        System.out.println("\n=========================================");
//        System.out.println("管理员凭据:");
//        System.out.println("用户名: admin");
//        System.out.println("密码: admin");
//        System.out.println("=========================================\n");
//    }
//}