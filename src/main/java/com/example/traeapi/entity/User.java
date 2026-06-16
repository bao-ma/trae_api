package com.example.traeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName(value = "user", schema = "public")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private String userId;

    @TableField("user_name")
    private String userName;

    @TableField("user_image")
    private String userImage;

    @TableField("fail_count")
    private Integer failCount;

    @TableField("role")
    private String role;

    @TableField("del_flag")
    @TableLogic
    private String delFlag;

    @TableField("created_by")
    private String createdBy;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField("updated_by")
    private String updatedBy;

    @TableField(value = "updated_at", fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;

    @TableField("password")
    private String password;
}