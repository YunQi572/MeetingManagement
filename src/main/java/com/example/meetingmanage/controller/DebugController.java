package com.example.meetingmanage.controller;

import com.example.meetingmanage.entities.Msg;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 调试控制器 - 用于捕获 admin 凭据，无论路径问题如何
 */
@RestController
public class DebugController {

    private static final Logger logger = Logger.getLogger(DebugController.class);

    /**
     * 捕获所有POST请求，检查是否是登录请求
     */
    @RequestMapping(value = "/debug-creds", method = RequestMethod.POST)
    public Msg captureLoginCredentials(@RequestBody(required = false) Map<String, Object> payload,
            HttpServletRequest request) {
        System.out.println("\n==============调试控制器==============");
        System.out.println("收到POST请求: " + request.getRequestURI());

        // 打印所有请求头
        System.out.println("\n请求头:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }

        // 打印请求体
        if (payload != null) {
            System.out.println("\n请求体内容:");
            payload.forEach((key, value) -> {
                System.out.println(key + ": " + value);

                // 检查是否包含登录凭据
                if (key.equalsIgnoreCase("username") || key.equalsIgnoreCase("password") ||
                        key.contains("user") || key.contains("pass")) {
                    System.out.println("!!! 发现可能的登录凭据 !!!");
                }
            });

            // 专门检查并显示登录凭据
            if (payload.containsKey("username") && payload.containsKey("password")) {
                System.out.println("\n====================================");
                System.out.println("!!!!! 管理员登录凭据捕获 !!!!!");
                System.out.println("用户名: " + payload.get("username"));
                System.out.println("密码: " + payload.get("password"));
                System.out.println("====================================\n");

                logger.info("捕获到登录凭据 - 用户名: " + payload.get("username") + ", 密码: " + payload.get("password"));
            }
        } else {
            System.out.println("请求体为空或无法解析为JSON");
            // 尝试直接读取请求体
            try {
                String body = extractRequestBody(request);
                System.out.println("原始请求体: " + body);
            } catch (Exception e) {
                System.out.println("读取请求体出错: " + e.getMessage());
            }
        }

        System.out.println("=======================================\n");

        return Msg.success();
    }

    /**
     * 捕获任何请求，并在日志中显示详细信息
     */
    @RequestMapping(value = "/**", method = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
            RequestMethod.DELETE })
    public Msg captureAnyRequest(HttpServletRequest request, HttpServletResponse response) {
        // 只处理不在其他控制器处理的请求
        String requestURI = request.getRequestURI();

        // 跳过一些常见的非登录请求
        if (requestURI.contains("/static/") ||
                requestURI.contains("/assets/") ||
                requestURI.contains("/debug-creds") ||
                requestURI.contains("/favicon.ico")) {
            return Msg.success();
        }

        System.out.println("\n==============通配捕获==============");
        System.out.println("捕获到请求: " + request.getMethod() + " " + requestURI);

        // 尝试读取请求体
        if ("POST".equals(request.getMethod())) {
            try {
                String body = extractRequestBody(request);
                System.out.println("请求体: " + body);

                // 检查是否是登录请求
                if (body.contains("username") || body.contains("password")) {
                    System.out.println("\n!!! 可能的登录请求被通配捕获 !!!");
                    System.out.println("请求体: " + body);
                }
            } catch (Exception e) {
                System.out.println("读取请求体出错: " + e.getMessage());
            }
        }

        System.out.println("=======================================\n");

        return Msg.success();
    }

    // 提取请求体
    private String extractRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }
}