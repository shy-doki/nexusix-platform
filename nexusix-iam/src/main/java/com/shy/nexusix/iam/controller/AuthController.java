package com.shy.nexusix.iam.controller;

import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.rto.RegisterRTO;
import com.shy.nexusix.iam.service.IAuthService;
import com.shy.nexusix.iam.vo.LoginVO;
import com.shy.nexusix.iam.vo.RegisterVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 认证控制器 - 提供登录、注册等认证接口
 * </p>
 * <p>
 * 该控制器下的接口路径已在WebConfig中配置为白名单，
 * 无需登录即可访问。
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户登录、注册等认证相关接口")
@Validated
public class AuthController {

    @Autowired
    private IAuthService iAuthService;

    /**
     * <p>
     * 用户登录
     * </p>
     * <p>
     * 验证用户名密码，登录成功后返回Token及用户权限角色信息。
     * 系统会自动加载用户权限到缓存，并选择默认租户。
     * </p>
     *
     * @param loginRTO 登录请求参数（用户名+密码）
     * @return 登录响应结果，包含Token、用户信息、权限和角色
     * @throws com.shy.nexusix.common.exception.BusinessException 用户不存在、密码错误、用户被禁用
     * @author shy
     * @since 2026-05-17
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "验证用户名密码，返回Token及用户权限角色信息")
    public ApiResponse login(@Valid @RequestBody LoginRTO loginRTO) {
        LoginVO loginVO = iAuthService.login(loginRTO);
        return ApiResponse.success("登录成功", loginVO);
    }

    /**
     * <p>
     * 用户注册
     * </p>
     * <p>
     * 创建新用户账号，密码使用BCrypt加密存储。
     * 注册成功后用户状态默认为启用，需重新登录获取Token。
     * </p>
     *
     * @param registerRTO 注册请求参数（用户名+密码+可选信息）
     * @return 注册响应结果，包含用户ID和用户名
     * @throws com.shy.nexusix.common.exception.BusinessException 用户名已存在、参数校验失败
     * @author shy
     * @since 2026-05-17
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "创建新用户账号，密码BCrypt加密存储")
    public ApiResponse register(@Valid @RequestBody RegisterRTO registerRTO) {
        RegisterVO registerVO = iAuthService.register(registerRTO);
        return ApiResponse.success("注册成功", registerVO);
    }

}
