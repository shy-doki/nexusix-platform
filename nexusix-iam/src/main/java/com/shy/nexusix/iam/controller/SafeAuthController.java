package com.shy.nexusix.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.secure.BCrypt;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.service.ISysUserService;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 二级认证控制器 - 高风险操作前的密码二次验证
 * </p>
 * <p>
 * 验证成功后StpUtil.openSafe()将二级认证标记写入Redis，
 * 后续@SaCheckSafe校验时从Redis读取该标记。
 * </p>
 *
 * @author shy
 * @since 2026-05-07
 */
@RestController
@RequestMapping("/auth/safe")
@Tag(name = "二级认证", description = "高风险操作前的密码二次验证")
public class SafeAuthController {

    @Autowired
    private ISysUserService userService;

    /**
     * 二级认证验证密码
     * 验证成功后开启二级认证，有效期120秒，标记自动写入Redis
     */
    @PostMapping("/verify")
    @SaCheckLogin
    @Operation(summary = "二级认证验证")
    public ApiResponse verifySafePassword(@RequestBody String password) {
        if (password == null || password.isBlank()) {
            throw new BusinessException(400, "密码不能为空");
        }

        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException(401, "密码验证失败");
        }

        // 开启二级认证，有效期120秒 → 自动写入Redis
        StpUtil.openSafe(120);

        return ApiResponse.success("二级认证成功，有效期120秒");
    }
}