package com.example.traeapi.service.login.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.traeapi.bean.login.LoginInputBean;
import com.example.traeapi.bean.login.LoginOutputBean;
import com.example.traeapi.entity.User;
import com.example.traeapi.exception.LoginException;
import com.example.traeapi.mapper.UserMapper;
import com.example.traeapi.service.login.LoginService;
import com.example.traeapi.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    // 默认失败次数上限（数据库中fail_count的初始值）
    @Value("${security.max-fail-count:3}")
    private int defaultFailCount;

    @Override
    @Transactional
    public LoginOutputBean login(LoginInputBean request) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId, request.getUserId());
        
        User user = userMapper.selectOne(queryWrapper);
        
        if (user == null) {
            throw new LoginException("用户不存在");
        }

        // 检查账户是否已锁定（fail_count为0表示已锁定）
        if (user.getFailCount() == null || user.getFailCount() == 0) {
            throw new LoginException("账户已锁定，请联系管理人员");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 失败次数减1
            int newFailCount = user.getFailCount() - 1;
            
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(User::getUserId, user.getUserId())
                         .set(User::getFailCount, newFailCount);
            userMapper.update(null, updateWrapper);
            
            if (newFailCount == 0) {
                // 次数用尽，账户被锁定
                throw new LoginException("密码错误，账户已被锁定，请联系管理人员");
            } else {
                // 还有剩余次数
                throw new LoginException(String.format("密码错误，剩余尝试次数: %d", newFailCount));
            }
        }

        // 登录成功，重置失败次数为默认值
        LambdaUpdateWrapper<User> resetWrapper = new LambdaUpdateWrapper<>();
        resetWrapper.eq(User::getUserId, user.getUserId())
                    .set(User::getFailCount, defaultFailCount);
        userMapper.update(null, resetWrapper);

        // 生成JWT令牌
        String token = jwtTokenUtil.generateToken(user.getUserId(), user.getUserName(), user.getRole());

        LoginOutputBean result = new LoginOutputBean();
        result.setAccessToken(token);
        result.setTokenType("Bearer");
        result.setExpiresAt(jwtTokenUtil.getExpirationTime());
        result.setUserId(user.getUserId());
        result.setUserName(user.getUserName());
        result.setUserImage(user.getUserImage());
        result.setRole(user.getRole());
        
        log.info("用户登录成功 - userId: {}, userName: {}, role: {}", 
                user.getUserId(), user.getUserName(), user.getRole());
        
        return result;
    }
}