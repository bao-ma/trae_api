package com.example.traeapi.dto.login;

import lombok.Data;

@Data
public class LoginOutputDto {
    private String accessToken;
    private String tokenType;
    private String expiresAt;
    private String userId;
    private String userName;
    private String userImage;
    private String role;
}