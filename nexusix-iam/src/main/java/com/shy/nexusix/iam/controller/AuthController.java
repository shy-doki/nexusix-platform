package com.shy.nexusix.iam.controller;

import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.rto.TenantRegisterRTO;
import com.shy.nexusix.iam.rto.UserRegisterRTO;
import com.shy.nexusix.iam.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>认证控制器</p>
 *
 * @author shy
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "认证服务相关接口")
public class AuthController {

    @Autowired
    private IAuthService iAuthService;

    /**
     * <p>用户登录认证</p>
     *
     * @param param 登录请求参数
     * @return 登录结果
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录认证", description = "用户登录认证接口")
    public ApiResponse login(@RequestBody LoginRTO param) {
        return iAuthService.login(param);
    }

    /**
     * <p>用户登出</p>
     *
     * @return 登出结果
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "清除当前登录会话")
    public ApiResponse logout() {
        return iAuthService.logout();
    }

    /**
     * <p>租户注册（含管理员账户）</p>
     * <p>创建租户、管理员用户、用户策略（关联租户），并初始化租户邀请码</p>
     *
     * @param param 租户注册请求参数
     * @return 注册结果
     */
    @PostMapping("/register/tenant")
    @Operation(summary = "租户注册", description = "租户注册接口，含管理员账户创建与邀请码初始化")
    public ApiResponse registerTenant(@Valid @RequestBody TenantRegisterRTO param) {
        return iAuthService.registerTenant(param);
    }

    /**
     * <p>用户注册（通过邀请码加入租户）</p>
     *
     * @param param 用户注册请求参数
     * @return 注册结果
     */
    @PostMapping("/register/user")
    @Operation(summary = "用户注册", description = "用户通过邀请码注册并加入对应租户")
    public ApiResponse registerUser(@Valid @RequestBody UserRegisterRTO param) {
        return iAuthService.registerUser(param);
    }

}
