package com.example.traeapi.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutOutputDto {
    private String message;

    public static LogoutOutputDto success(String message) {
        return new LogoutOutputDto(message);
    }
}