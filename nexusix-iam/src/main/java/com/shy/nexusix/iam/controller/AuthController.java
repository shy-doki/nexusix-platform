package com.shy.nexusix.iam.controller;

import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

}
