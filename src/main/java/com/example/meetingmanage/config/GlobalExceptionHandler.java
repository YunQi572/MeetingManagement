package com.example.meetingmanage.config;

import com.example.meetingmanage.entities.Msg;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Msg> handleException(Exception e, HttpServletRequest request) {
        logger.error("请求处理出错 [" + request.getMethod() + " " + request.getRequestURI() + "]: " + e.getMessage(), e);
        System.out.println("全局异常: " + e.getMessage());

        // 打印请求头
        System.out.println("请求头信息:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            System.out.println(headerName + ": " + request.getHeader(headerName));
        });

        // 打印请求参数
        System.out.println("请求参数:");
        request.getParameterMap().forEach((key, value) -> {
            System.out.println(key + ": " + String.join(", ", value));
        });

        // 检查是否是登录请求
        if (request.getRequestURI().contains("/login") ||
                request.getRequestURI().contains("/manager")) {
            System.out.println("========================");
            System.out.println("登录请求出错，请检查参数和路径配置");
            System.out.println("请求URI: " + request.getRequestURI());
            System.out.println("请求方法: " + request.getMethod());
            System.out.println("========================");
        }

        Msg responseMsg = Msg.fault();
        responseMsg.setMessage("请求处理出错: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMsg);
    }
}