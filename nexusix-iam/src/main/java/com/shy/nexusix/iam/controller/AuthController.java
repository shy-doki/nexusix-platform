package com.shy.nexusix.iam.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shy.nexusix.common.annotation.RateLimit;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.core.tenant.TenantContext;
import com.shy.nexusix.core.user.UserContext;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.entity.SysUserTenantRel;
import com.shy.nexusix.iam.entity.SysUserToken;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.ISysUserService;
import com.shy.nexusix.iam.service.ISysUserTenantRelService;
import com.shy.nexusix.iam.service.ISysUserTokenService;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.service.ISysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 认证管理控制器 - 登录、登出、会话管理
 * </p>
 * <p>
 * 登录时将租户上下文写入Sa-Token Session，Session数据由sa-token-redis持久化到Redis。
 * 登出时清除业务缓存并调用StpUtil.logout()，Sa-Token自动清除Redis中的会话数据。
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
    private ISysUserService userService;

    @Autowired
    private ISysUserTenantRelService userTenantRelService;

    @Autowired
    private ISysTenantService tenantService;

    @Autowired
    private ISysUserTokenService userTokenService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String SESSION_TENANT_ID_KEY = "tenantId";
    private static final String SESSION_TENANT_NAME_KEY = "tenantName";
    private static final String SESSION_USER_NAME_KEY = "userName";

    /**
     * 用户登录
     * 登录成功后Sa-Token自动将会话写入Redis，租户上下文写入Session也同步到Redis
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @RateLimit(key = "auth:login", limit = 5, period = 60,
            limitType = RateLimit.LimitType.IP,
            message = "登录尝试过于频繁，请60秒后再试")
    public ApiResponse login(@Valid @RequestBody LoginRTO loginRTO, HttpServletRequest request) {

        // 查询用户
        SysUser user = userService.getOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, loginRTO.getUsername())
                        .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
        );
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 校验用户状态
        if (!user.getStatus().equals(GlobalEnum.UserStatus.NORMAL.getCode())) {
            throw new BusinessException(403, "用户已被禁用，请联系管理员");
        }

        // 校验密码（BCrypt匹配）
        if (!BCrypt.checkpw(loginRTO.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 查询用户租户关联（取默认/第一个有效租户）
        SysUserTenantRel userTenantRel = userTenantRelService.getOne(
                new LambdaQueryWrapper<SysUserTenantRel>()
                        .eq(SysUserTenantRel::getUserId, user.getId())
                        .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                        .orderByDesc(SysUserTenantRel::getCreateTime)
                        .last("LIMIT 1")
        );
        if (userTenantRel == null) {
            throw new BusinessException(403, "用户未绑定任何租户");
        }

        // 校验租户状态
        SysTenant tenant = tenantService.getById(userTenantRel.getTenantId());
        if (tenant == null || !tenant.getStatus().equals(GlobalEnum.TenantStatus.NORMAL.getCode())) {
            throw new BusinessException(403, "所属租户已被冻结或不存在");
        }

        // 执行登录 → Sa-Token自动将会话写入Redis
        StpUtil.login(user.getId());

        // 将租户上下文写入Sa-Token Session → 自动持久化到Redis
        SaSession session = StpUtil.getSession();
        session.set(SESSION_TENANT_ID_KEY, tenant.getId());
        session.set(SESSION_TENANT_NAME_KEY, tenant.getTenantName());
        session.set(SESSION_USER_NAME_KEY, user.getNickname());

        // 预热权限/角色缓存到Redis（避免首次请求查库）
        List<String> permissions = StpUtil.getPermissionList();
        List<String> roles = StpUtil.getRoleList();

        // 更新登录信息
        user.setLoginIp(getClientIp(request));
        user.setLoginDate(LocalDateTime.now());
        userService.updateById(user);

        // 创建Token记录
        SysUserToken tokenRecord = new SysUserToken();
        tokenRecord.setUserId(user.getId());
        tokenRecord.setToken(StpUtil.getTokenValue());
        tokenRecord.setStatus(GlobalEnum.TokenStatus.VALID.getCode());
        tokenRecord.setLoginIp(getClientIp(request));
        tokenRecord.setExpireTime(LocalDateTime.now().plusSeconds(StpUtil.getTokenTimeout()));
        userTokenService.save(tokenRecord);

        return ApiResponse.success(Map.of(
                "token", StpUtil.getTokenValue(),
                "tokenName", StpUtil.getTokenName(),
                "userId", user.getId(),
                "userName", user.getNickname(),
                "tenantId", tenant.getId(),
                "tenantName", tenant.getTenantName(),
                "permissions", permissions,
                "roles", roles
        ));
    }

    /**
     * 用户登出
     * 清除业务缓存 + Sa-Token自动清除Redis会话数据
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public ApiResponse logout() {
        if (!StpUtil.isLogin()) {
            return ApiResponse.success("未登录状态，无需登出");
        }

        String tokenValue = StpUtil.getTokenValue();
        Long userId = StpUtil.getLoginIdAsLong();

        // 清除业务缓存
        redisTemplate.delete("nexusix:perm:" + userId);
        redisTemplate.delete("nexusix:role:" + userId);

        // 失效Token记录
        userTokenService.update(
                new LambdaUpdateWrapper<SysUserToken>()
                        .eq(SysUserToken::getToken, tokenValue)
                        .eq(SysUserToken::getUserId, userId)
                        .set(SysUserToken::getStatus, GlobalEnum.TokenStatus.INVALID.getCode())
        );

        // 注销会话 → Sa-Token自动清除Redis数据
        StpUtil.logout();

        return ApiResponse.success("登出成功");
    }

    /**
     * 切换租户上下文
     * 更新Session后Sa-Token自动同步到Redis，清除权限缓存（不同租户可能有不同权限）
     */
    @PutMapping("/switch-tenant")
    @Operation(summary = "切换租户上下文")
    public ApiResponse switchTenant(@RequestParam Long tenantId) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 校验用户是否属于目标租户
        long count = userTenantRelService.count(
                new LambdaQueryWrapper<SysUserTenantRel>()
                        .eq(SysUserTenantRel::getUserId, userId)
                        .eq(SysUserTenantRel::getTenantId, tenantId)
                        .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
        );
        if (count == 0) {
            throw new BusinessException(403, "您不属于该租户，无法切换");
        }

        // 校验目标租户状态
        SysTenant tenant = tenantService.getById(tenantId);
        if (tenant == null || !tenant.getStatus().equals(GlobalEnum.TenantStatus.NORMAL.getCode())) {
            throw new BusinessException(403, "目标租户已被冻结或不存在");
        }

        // 更新Sa-Token Session → 自动同步到Redis
        SaSession session = StpUtil.getSession();
        session.set(SESSION_TENANT_ID_KEY, tenant.getId());
        session.set(SESSION_TENANT_NAME_KEY, tenant.getTenantName());

        // 清除权限缓存（租户切换后权限可能不同）
        redisTemplate.delete("nexusix:perm:" + userId);
        redisTemplate.delete("nexusix:role:" + userId);

        return ApiResponse.success("租户切换成功");
    }

    /**
     * 获取当前登录用户信息
     * 所有数据从Redis缓存获取
     */
    @GetMapping("/current-user")
    @Operation(summary = "获取当前登录用户信息")
    public ApiResponse getCurrentUser() {
        return ApiResponse.success(Map.of(
                "userId", UserContext.getCurrentUserId(),
                "userName", UserContext.getCurrentUserName(),
                "tenantId", TenantContext.getCurrentTenantId(),
                "tenantName", TenantContext.getCurrentTenantName(),
                "permissions", StpUtil.getPermissionList(),
                "roles", StpUtil.getRoleList()
        ));
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

}
