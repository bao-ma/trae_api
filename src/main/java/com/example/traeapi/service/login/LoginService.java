package com.example.traeapi.service.login;

import com.example.traeapi.bean.login.LoginInputBean;
import com.example.traeapi.bean.login.LoginOutputBean;

public interface LoginService {

    LoginOutputBean login(LoginInputBean request);
}