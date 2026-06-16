package com.example.traeapi.bean.login;

import lombok.Data;

@Data
public class LoginOutputBean {
    private String accessToken;
    private String tokenType;
    private String expiresAt;
    private String userId;
    private String userName;
    private String userImage;
    private String role;
}