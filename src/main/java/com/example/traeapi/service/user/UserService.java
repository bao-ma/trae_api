package com.example.traeapi.service.user;

import com.example.traeapi.bean.user.UserInputBean;
import com.example.traeapi.bean.user.UserOutputBean;

import java.util.List;

public interface UserService {

    /**
     * 添加用户
     */
    UserOutputBean addUser(UserInputBean request);

    /**
     * 修改用户信息
     */
    UserOutputBean updateUser(UserInputBean request);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 根据ID查询用户
     */
    UserOutputBean getUserById(Long id);

    /**
     * 根据用户ID查询用户
     */
    UserOutputBean getUserByUserId(String userId);

    /**
     * 查询所有用户列表
     */
    List<UserOutputBean> getAllUsers();
}