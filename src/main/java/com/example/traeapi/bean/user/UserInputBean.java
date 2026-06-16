package com.example.traeapi.bean.user;

import lombok.Data;

@Data
public class UserInputBean {

    private Long id;

    private String userId;

    private String userName;

    private String userImage;

    private String role;

    private String password;

    private String createdBy;

    private String updatedBy;
}