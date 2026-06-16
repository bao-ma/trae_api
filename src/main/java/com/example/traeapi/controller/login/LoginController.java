package com.example.traeapi.controller.login;

import com.example.traeapi.bean.login.LoginInputBean;
import com.example.traeapi.bean.login.LoginOutputBean;
import com.example.traeapi.dto.login.LoginInputDto;
import com.example.traeapi.dto.login.LoginOutputDto;
import com.example.traeapi.dto.login.LogoutOutputDto;
import com.example.traeapi.dto.response.ApiResponse;
import com.example.traeapi.service.login.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginOutputDto>> login(
            @Valid @RequestBody LoginInputDto request,
            HttpServletRequest httpRequest) {
        
        log.info("用户登录请求 - userId: {}", request.getUserId());
        
        LoginInputBean requestBean = new LoginInputBean();
        requestBean.setUserId(request.getUserId());
        requestBean.setPassword(request.getPassword());
        
        LoginOutputBean resultBean = loginService.login(requestBean);
        
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("userId", resultBean.getUserId());
        session.setAttribute("userName", resultBean.getUserName());
        session.setAttribute("userImage", resultBean.getUserImage());
        session.setAttribute("role", resultBean.getRole());
        session.setAttribute("token", resultBean.getAccessToken());
        
        LoginOutputDto response = new LoginOutputDto();
        response.setAccessToken(resultBean.getAccessToken());
        response.setTokenType(resultBean.getTokenType());
        response.setExpiresAt(resultBean.getExpiresAt());
        response.setUserId(resultBean.getUserId());
        response.setUserName(resultBean.getUserName());
        response.setUserImage(resultBean.getUserImage());
        response.setRole(resultBean.getRole());
        
        log.info("用户登录成功 - userId: {}, userName: {}, role: {}", 
                resultBean.getUserId(), resultBean.getUserName(), resultBean.getRole());
        
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutOutputDto>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            String userId = (String) session.getAttribute("userId");
            session.invalidate();
            log.info("用户登出 - userId: {}", userId);
            return ResponseEntity.ok(ApiResponse.success("登出成功", LogoutOutputDto.success("已成功退出登录")));
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("未找到有效会话"));
    }

    @GetMapping("/session")
    public ResponseEntity<ApiResponse<Object>> getSessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("未登录"));
        }
        
        String userId = (String) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");
        String userImage = (String) session.getAttribute("userImage");
        String role = (String) session.getAttribute("role");
        
        return ResponseEntity.ok(ApiResponse.success(new SessionInfo(userId, userName, userImage, role)));
    }

    private record SessionInfo(String userId, String userName, String userImage, String role) {}
}