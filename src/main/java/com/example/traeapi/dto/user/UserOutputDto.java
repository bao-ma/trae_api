package com.example.traeapi.dto.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserOutputDto {

    private Long id;

    private String userId;

    private String userName;

    private String userImage;

    private String role;

    private Integer failCount;

    private String createdBy;

    private LocalDateTime createdAt;

    private String updatedBy;

    private LocalDateTime updatedAt;
}