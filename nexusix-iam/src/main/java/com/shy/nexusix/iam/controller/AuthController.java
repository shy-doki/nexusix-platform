package com.shy.nexusix.iam.controller;

import com.shy.nexusix.common.annotation.RateLimit;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.rto.RegisterRTO;
import com.shy.nexusix.iam.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 认证管理控制器 - 仅负责HTTP请求接收与响应处理
 * </p>
 * <p>
 * 本控制器不包含任何业务逻辑，所有业务逻辑委托给IAuthService处理。
 * 职责：
 * 1. 接收HTTP请求，校验参数
 * 2. 调用Service层方法
 * 3. 封装响应返回给前端
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "登录、登出、会话管理")
public class AuthController {

    @Autowired
    private IAuthService authService;

    /**
     * 用户注册
     * Controller层职责：接收请求、参数校验、调用Service、封装响应
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    @RateLimit(key = "auth:register", limit = 5, period = 60,
            limitType = RateLimit.LimitType.IP,
            message = "注册尝试过于频繁，请60秒后再试")
    public ApiResponse register(@Valid @RequestBody RegisterRTO registerRTO) {
        Map<String, Object> result = authService.register(registerRTO);
        return ApiResponse.success(result);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @RateLimit(key = "auth:login", limit = 5, period = 60,
            limitType = RateLimit.LimitType.IP,
            message = "登录尝试过于频繁，请60秒后再试")
    public ApiResponse login(@Valid @RequestBody LoginRTO loginRTO, HttpServletRequest request) {
        Map<String, Object> result = authService.login(loginRTO, request);
        return ApiResponse.success(result);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public ApiResponse logout() {
        String result = authService.logout();
        return ApiResponse.success(result);
    }

    /**
     * 切换租户上下文
     */
    @PutMapping("/switch-tenant")
    @Operation(summary = "切换租户上下文")
    public ApiResponse switchTenant(@RequestParam Long tenantId) {
        String result = authService.switchTenant(tenantId);
        return ApiResponse.success(result);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current-user")
    @Operation(summary = "获取当前登录用户信息")
    public ApiResponse getCurrentUser() {
        Map<String, Object> result = authService.getCurrentUser();
        return ApiResponse.success(result);
    }
}