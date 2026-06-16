package com.example.traeapi.controller.user;

import com.example.traeapi.bean.user.UserInputBean;
import com.example.traeapi.bean.user.UserOutputBean;
import com.example.traeapi.dto.response.ApiResponse;
import com.example.traeapi.dto.user.UserInputDto;
import com.example.traeapi.dto.user.UserOutputDto;
import com.example.traeapi.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 添加用户
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<UserOutputDto>> addUser(
            @Valid @RequestBody UserInputDto request,
            HttpServletRequest httpRequest) {
        
        log.info("添加用户请求 - userId: {}", request.getUserId());
        
        HttpSession session = httpRequest.getSession(false);
        String operatorId = session != null ? (String) session.getAttribute("userId") : "system";
        
        UserInputBean requestBean = new UserInputBean();
        requestBean.setUserId(request.getUserId());
        requestBean.setUserName(request.getUserName());
        requestBean.setUserImage(request.getUserImage());
        requestBean.setRole(request.getRole());
        requestBean.setPassword(request.getPassword());
        requestBean.setCreatedBy(operatorId);
        requestBean.setUpdatedBy(operatorId);
        
        UserOutputBean resultBean = userService.addUser(requestBean);
        
        return ResponseEntity.ok(ApiResponse.success("添加成功", convertToDto(resultBean)));
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserOutputDto>> updateUser(
            @Valid @RequestBody UserInputDto request,
            HttpServletRequest httpRequest) {
        
        log.info("修改用户请求 - userId: {}", request.getUserId());
        
        HttpSession session = httpRequest.getSession(false);
        String operatorId = session != null ? (String) session.getAttribute("userId") : "system";
        
        UserInputBean requestBean = new UserInputBean();
        requestBean.setUserId(request.getUserId());
        requestBean.setUserName(request.getUserName());
        requestBean.setUserImage(request.getUserImage());
        requestBean.setRole(request.getRole());
        requestBean.setPassword(request.getPassword());
        requestBean.setUpdatedBy(operatorId);
        
        UserOutputBean resultBean = userService.updateUser(requestBean);
        
        return ResponseEntity.ok(ApiResponse.success("修改成功", convertToDto(resultBean)));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id) {
        log.info("删除用户请求 - id: {}", id);
        
        userService.deleteUser(id);
        
        return ResponseEntity.ok(ApiResponse.success("删除成功"));
    }

    /**
     * 查询所有用户列表
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<UserOutputDto>>> getAllUsers() {
        log.info("查询用户列表");
        
        List<UserOutputBean> userList = userService.getAllUsers();
        List<UserOutputDto> dtoList = userList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success("查询成功", dtoList));
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserOutputDto>> getUserById(@PathVariable Long id) {
        log.info("根据ID查询用户 - id: {}", id);
        
        UserOutputBean resultBean = userService.getUserById(id);
        
        return ResponseEntity.ok(ApiResponse.success("查询成功", convertToDto(resultBean)));
    }

    /**
     * 根据用户ID查询用户
     */
    @GetMapping("/detail/{userId}")
    public ResponseEntity<ApiResponse<UserOutputDto>> getUserByUserId(@PathVariable String userId) {
        log.info("根据用户ID查询用户 - userId: {}", userId);
        
        UserOutputBean resultBean = userService.getUserByUserId(userId);
        
        return ResponseEntity.ok(ApiResponse.success("查询成功", convertToDto(resultBean)));
    }

    private UserOutputDto convertToDto(UserOutputBean bean) {
        UserOutputDto dto = new UserOutputDto();
        dto.setId(bean.getId());
        dto.setUserId(bean.getUserId());
        dto.setUserName(bean.getUserName());
        dto.setUserImage(bean.getUserImage());
        dto.setRole(bean.getRole());
        dto.setFailCount(bean.getFailCount());
        dto.setCreatedBy(bean.getCreatedBy());
        dto.setCreatedAt(bean.getCreatedAt());
        dto.setUpdatedBy(bean.getUpdatedBy());
        dto.setUpdatedAt(bean.getUpdatedAt());
        return dto;
    }
}