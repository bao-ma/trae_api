package com.example.traeapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Slf4j
@Configuration
@Order(2)
public class StartupInfoConfig implements ApplicationRunner {

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${spring.application.name:trae-api}")
    private String applicationName;

    @Override
    public void run(ApplicationArguments args) {
        String baseUrl = "http://localhost:" + serverPort;

        System.out.println();
        System.out.println("==========================================================");
        System.out.println("               Trae API 启动成功");
        System.out.println("==========================================================");
        System.out.println();
        System.out.println("  应用信息:");
        System.out.println("  --------------------------------------------------------");
        System.out.println("  应用名称: " + applicationName);
        System.out.println("  服务端口: " + serverPort);
        System.out.println("  后台地址: " + baseUrl + "/" + applicationName);
        System.out.println();
        System.out.println("  可用接口:");
        System.out.println("  --------------------------------------------------------");
        System.out.println("  POST   " + baseUrl + "/api/auth/login    - 用户登录");
        System.out.println("  POST   " + baseUrl + "/api/auth/logout   - 用户登出");
        System.out.println("  GET    " + baseUrl + "/api/auth/session   - 获取会话信息");
        System.out.println();
        System.out.println("  测试账号:");
        System.out.println("  --------------------------------------------------------");
        System.out.println("  管理员: admin / admin123");
        System.out.println("  用户:   user / user123");
        System.out.println();
        System.out.println("==========================================================");
        System.out.println();
    }
}