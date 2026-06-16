package com.example.traeapi.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.traeapi.entity.User;
import com.example.traeapi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Order(1)
    public CommandLineRunner initData() {
        return args -> {
            // 使用Lambda条件构造器检查用户是否存在
            LambdaQueryWrapper<User> adminQuery = new LambdaQueryWrapper<>();
            adminQuery.eq(User::getUserId, "admin");
            
            if (userMapper.selectCount(adminQuery) == 0) {
                User admin = new User();
                admin.setUserId("admin");
                admin.setUserName("管理员");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("A");
                admin.setCreatedBy("system");
                userMapper.insert(admin);
            }

            LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
            userQuery.eq(User::getUserId, "user");
            
            if (userMapper.selectCount(userQuery) == 0) {
                User user = new User();
                user.setUserId("user");
                user.setUserName("普通用户");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole("U");
                user.setCreatedBy("system");
                userMapper.insert(user);
            }
        };
    }
}