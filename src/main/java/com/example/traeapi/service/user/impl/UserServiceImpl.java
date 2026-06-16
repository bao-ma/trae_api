package com.example.traeapi.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.traeapi.bean.user.UserInputBean;
import com.example.traeapi.bean.user.UserOutputBean;
import com.example.traeapi.entity.User;
import com.example.traeapi.exception.LoginException;
import com.example.traeapi.mapper.UserMapper;
import com.example.traeapi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.max-fail-count:3}")
    private int defaultFailCount;

    @Override
    @Transactional
    public UserOutputBean addUser(UserInputBean request) {
        // 检查用户是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId, request.getUserId());
        if (userMapper.selectOne(queryWrapper) != null) {
            throw new LoginException("用户ID已存在");
        }

        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserName(request.getUserName());
        user.setUserImage(request.getUserImage());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFailCount(defaultFailCount);
        user.setCreatedBy(request.getCreatedBy());
        user.setUpdatedBy(request.getUpdatedBy());

        userMapper.insert(user);
        log.info("新增用户成功 - userId: {}", request.getUserId());

        return convertToOutputBean(user);
    }

    @Override
    @Transactional
    public UserOutputBean updateUser(UserInputBean request) {
        User user = userMapper.selectById(request.getId());
        if (user == null) {
            throw new LoginException("用户不存在");
        }

        // 如果修改了用户ID，检查新ID是否已被使用
        if (!user.getUserId().equals(request.getUserId())) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserId, request.getUserId());
            if (userMapper.selectOne(queryWrapper) != null) {
                throw new LoginException("用户ID已存在");
            }
            user.setUserId(request.getUserId());
        }

        user.setUserName(request.getUserName());
        user.setUserImage(request.getUserImage());
        user.setRole(request.getRole());
        
        // 如果密码不为空，更新密码
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        user.setUpdatedBy(request.getUpdatedBy());

        userMapper.updateById(user);
        log.info("修改用户成功 - userId: {}", request.getUserId());

        return convertToOutputBean(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new LoginException("用户不存在");
        }

        userMapper.deleteById(id);
        log.info("删除用户成功 - userId: {}", user.getUserId());
    }

    @Override
    public UserOutputBean getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new LoginException("用户不存在");
        }
        return convertToOutputBean(user);
    }

    @Override
    public UserOutputBean getUserByUserId(String userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId, userId);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new LoginException("用户不存在");
        }
        return convertToOutputBean(user);
    }

    @Override
    public List<UserOutputBean> getAllUsers() {
        List<User> users = userMapper.selectList(null);
        return users.stream()
                .map(this::convertToOutputBean)
                .collect(Collectors.toList());
    }

    private UserOutputBean convertToOutputBean(User user) {
        UserOutputBean output = new UserOutputBean();
        output.setId(user.getId());
        output.setUserId(user.getUserId());
        output.setUserName(user.getUserName());
        output.setUserImage(user.getUserImage());
        output.setRole(user.getRole());
        output.setFailCount(user.getFailCount());
        output.setCreatedBy(user.getCreatedBy());
        output.setCreatedAt(user.getCreatedAt());
        output.setUpdatedBy(user.getUpdatedBy());
        output.setUpdatedAt(user.getUpdatedAt());
        return output;
    }
}