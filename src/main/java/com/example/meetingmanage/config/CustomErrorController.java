package com.example.meetingmanage.config;

import com.example.meetingmanage.entities.Msg;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController extends BasicErrorController {

    private static final Logger logger = Logger.getLogger(CustomErrorController.class);

    public CustomErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes, serverProperties.getError());
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);

        // 记录请求信息
        logger.error("请求错误 - 状态码: " + status.value() + ", 路径: " + request.getRequestURI());
        System.out.println("=============================================");
        System.out.println("错误请求捕获:");
        System.out.println("状态码: " + status.value());
        System.out.println("请求URI: " + request.getRequestURI());
        System.out.println("原始URI: " + request.getAttribute("javax.servlet.error.request_uri"));
        System.out.println("请求方法: " + request.getMethod());

        // 打印请求头
        System.out.println("\n请求头信息:");
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            System.out.println(headerName + ": " + request.getHeader(headerName));
        });

        // 尝试读取请求体
        try {
            // 尝试读取请求体
            if (request.getReader() != null) {
                String requestBody = extractRequestBody(request);
                System.out.println("\n请求体内容:");
                System.out.println(requestBody);

                // 如果请求体包含username或password，可能是登录请求
                if (requestBody.contains("username") || requestBody.contains("password")) {
                    System.out.println("\n!!! 登录凭据捕获(ERROR) !!!");
                    System.out.println(requestBody);
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            }
        } catch (Exception e) {
            System.out.println("无法读取请求体: " + e.getMessage());
        }

        System.out.println("=============================================");

        Msg responseMsg = Msg.fault();
        responseMsg.setMessage("请求处理出错，请检查API路径和参数");

        return new ResponseEntity<>(Collections.singletonMap("error", responseMsg), status);
    }

    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        // 记录错误信息
        HttpStatus status = getStatus(request);
        logger.error("HTML错误请求 - 状态码: " + status.value() + ", 路径: " + request.getRequestURI());

        return super.errorHtml(request, response);
    }

    // 提取请求体内容
    private String extractRequestBody(HttpServletRequest request) {
        try {
            if (request.getContentType() != null &&
                    request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                // 读取请求体
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
                    return reader.lines().collect(Collectors.joining("\n"));
                }
            }
        } catch (IOException e) {
            logger.error("读取请求体时出错: " + e.getMessage());
        }
        return "无法读取请求体或请求体为空";
    }
}