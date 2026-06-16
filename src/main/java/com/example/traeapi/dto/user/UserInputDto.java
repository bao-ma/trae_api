package com.example.traeapi.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserInputDto {

    @NotBlank(message = "用户ID不能为空")
    @Size(max = 50, message = "用户ID长度不能超过50")
    private String userId;

    @NotBlank(message = "用户名称不能为空")
    @Size(max = 100, message = "用户名称长度不能超过100")
    private String userName;

    private String userImage;

    @NotBlank(message = "角色不能为空")
    private String role;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100之间")
    private String password;
}