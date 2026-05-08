package com.shy.nexusix.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.service.ISafeAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 二级认证控制器 - 仅负责HTTP请求接收与响应处理
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
@RequestMapping("/auth/safe")
@Tag(name = "二级认证", description = "高风险操作前的密码二次验证")
public class SafeAuthController {

    @Autowired
    private ISafeAuthService iSafeAuthService;

    /**
     * 二级认证验证密码
     * Controller层职责：接收请求、参数校验、调用Service、封装响应
     */
    @PostMapping("/verify")
    @SaCheckLogin
    @Operation(summary = "二级认证验证")
    public ApiResponse verifySafePassword(@RequestBody String password) {
        // 直接调用Service层，业务逻辑完全封装在Service中
        String result = iSafeAuthService.verifySafePassword(password);
        return ApiResponse.success(result);
    }
}