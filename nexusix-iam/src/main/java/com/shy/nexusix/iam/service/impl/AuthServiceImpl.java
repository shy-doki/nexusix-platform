package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.core.tenant.TenantContext;
import com.shy.nexusix.core.user.UserContext;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.entity.SysUserTenantRel;
import com.shy.nexusix.iam.entity.SysUserToken;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.mapper.SysUserTenantRelMapper;
import com.shy.nexusix.iam.mapper.SysUserTokenMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.IAuthService;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.mapper.SysTenantMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 认证服务实现类 - 封装完整的认证业务逻辑
 * </p>
 * <p>
 * 本类实现所有认证相关的业务逻辑，与Controller层解耦，便于单元测试和代码复用。
 * 所有业务异常通过BusinessException抛出，由GlobalExceptionHandler统一处理。
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
@Service
public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private static final String SESSION_TENANT_ID_KEY = "tenantId";
    private static final String SESSION_TENANT_NAME_KEY = "tenantName";
    private static final String SESSION_USER_NAME_KEY = "userName";

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserTenantRelMapper userTenantRelMapper;

    @Autowired
    private SysTenantMapper tenantMapper;

    @Autowired
    private SysUserTokenMapper userTokenMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录 - 完整业务逻辑实现
     */
    @Override
    public Map<String, Object> login(LoginRTO loginRTO, HttpServletRequest request) {
        logger.info("用户登录请求 - 用户名: {}", loginRTO.getUsername());

        // 步骤1: 查询用户
        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, loginRTO.getUsername())
                        .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
        );

        if (user == null) {
            logger.warn("登录失败 - 用户不存在: {}", loginRTO.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 步骤2: 校验用户状态
        if (!user.getStatus().equals(GlobalEnum.UserStatus.NORMAL.getCode())) {
            logger.warn("登录失败 - 用户已被禁用: {}", loginRTO.getUsername());
            throw new BusinessException(403, "用户已被禁用，请联系管理员");
        }

        // 步骤3: 校验密码（BCrypt匹配）
        if (!BCrypt.checkpw(loginRTO.getPassword(), user.getPassword())) {
            logger.warn("登录失败 - 密码错误: {}", loginRTO.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 步骤4: 查询用户租户关联（取默认/第一个有效租户）
        SysUserTenantRel userTenantRel = userTenantRelMapper.selectOne(
                new LambdaQueryWrapper<SysUserTenantRel>()
                        .eq(SysUserTenantRel::getUserId, user.getId())
                        .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                        .orderByDesc(SysUserTenantRel::getCreateTime)
                        .last("LIMIT 1")
        );

        if (userTenantRel == null) {
            logger.warn("登录失败 - 用户未绑定租户: {}", loginRTO.getUsername());
            throw new BusinessException(403, "用户未绑定任何租户");
        }

        // 步骤5: 校验租户状态
        SysTenant tenant = tenantMapper.selectById(userTenantRel.getTenantId());
        if (tenant == null || !tenant.getStatus().equals(GlobalEnum.TenantStatus.NORMAL.getCode())) {
            logger.warn("登录失败 - 租户已冻结或不存在: tenantId={}", userTenantRel.getTenantId());
            throw new BusinessException(403, "所属租户已被冻结或不存在");
        }

        // 步骤6: 执行登录 → Sa-Token自动将会话写入Redis
        StpUtil.login(user.getId());
        logger.info("用户登录成功 - userId={}, username={}", user.getId(), user.getUsername());

        // 步骤7: 将租户上下文写入Sa-Token Session → 自动持久化到Redis
        SaSession session = StpUtil.getSession();
        session.set(SESSION_TENANT_ID_KEY, tenant.getId());
        session.set(SESSION_TENANT_NAME_KEY, tenant.getTenantName());
        session.set(SESSION_USER_NAME_KEY, user.getNickname());

        // 步骤8: 预热权限/角色缓存到Redis（避免首次请求查库）
        List<String> permissions = StpUtil.getPermissionList();
        List<String> roles = StpUtil.getRoleList();

        // 步骤9: 更新登录信息
        user.setLoginIp(getClientIp(request));
        user.setLoginDate(LocalDateTime.now());
        userMapper.updateById(user);

        // 步骤10: 创建Token记录
        SysUserToken tokenRecord = new SysUserToken();
        tokenRecord.setUserId(user.getId());
        tokenRecord.setToken(StpUtil.getTokenValue());
        tokenRecord.setStatus(GlobalEnum.TokenStatus.VALID.getCode());
        tokenRecord.setLoginIp(getClientIp(request));
        tokenRecord.setExpireTime(LocalDateTime.now().plusSeconds(StpUtil.getTokenTimeout()));
        userTokenMapper.insert(tokenRecord);

        // 步骤11: 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", StpUtil.getTokenValue());
        result.put("tokenName", StpUtil.getTokenName());
        result.put("userId", user.getId());
        result.put("userName", user.getNickname());
        result.put("tenantId", tenant.getId());
        result.put("tenantName", tenant.getTenantName());
        result.put("permissions", permissions);
        result.put("roles", roles);

        return result;
    }

    /**
     * 用户登出 - 完整业务逻辑实现
     */
    @Override
    public String logout() {
        if (!StpUtil.isLogin()) {
            logger.info("登出请求 - 用户未登录");
            return "未登录状态，无需登出";
        }

        String tokenValue = StpUtil.getTokenValue();
        Long userId = StpUtil.getLoginIdAsLong();

        logger.info("用户登出 - userId={}", userId);

        // 步骤1: 清除业务缓存
        redisTemplate.delete("nexusix:perm:" + userId);
        redisTemplate.delete("nexusix:role:" + userId);

        // 步骤2: 失效Token记录
        userTokenMapper.update(
                null,
                new LambdaUpdateWrapper<SysUserToken>()
                        .eq(SysUserToken::getToken, tokenValue)
                        .eq(SysUserToken::getUserId, userId)
                        .set(SysUserToken::getStatus, GlobalEnum.TokenStatus.INVALID.getCode())
        );

        // 步骤3: 注销会话 → Sa-Token自动清除Redis数据
        StpUtil.logout();

        return "登出成功";
    }

    /**
     * 切换租户上下文 - 完整业务逻辑实现
     */
    @Override
    public String switchTenant(Long tenantId) {
        Long userId = StpUtil.getLoginIdAsLong();
        logger.info("租户切换请求 - userId={}, targetTenantId={}", userId, tenantId);

        // 步骤1: 校验用户是否属于目标租户
        long count = userTenantRelMapper.selectCount(
                new LambdaQueryWrapper<SysUserTenantRel>()
                        .eq(SysUserTenantRel::getUserId, userId)
                        .eq(SysUserTenantRel::getTenantId, tenantId)
                        .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
        );

        if (count == 0) {
            logger.warn("租户切换失败 - 用户不属于该租户: userId={}, tenantId={}", userId, tenantId);
            throw new BusinessException(403, "您不属于该租户，无法切换");
        }

        // 步骤2: 校验目标租户状态
        SysTenant tenant = tenantMapper.selectById(tenantId);
        if (tenant == null || !tenant.getStatus().equals(GlobalEnum.TenantStatus.NORMAL.getCode())) {
            logger.warn("租户切换失败 - 租户已冻结或不存在: tenantId={}", tenantId);
            throw new BusinessException(403, "目标租户已被冻结或不存在");
        }

        // 步骤3: 更新Sa-Token Session → 自动同步到Redis
        SaSession session = StpUtil.getSession();
        session.set(SESSION_TENANT_ID_KEY, tenant.getId());
        session.set(SESSION_TENANT_NAME_KEY, tenant.getTenantName());

        // 步骤4: 清除权限缓存（租户切换后权限可能不同）
        redisTemplate.delete("nexusix:perm:" + userId);
        redisTemplate.delete("nexusix:role:" + userId);

        logger.info("租户切换成功 - userId={}, tenantId={}", userId, tenantId);
        return "租户切换成功";
    }

    /**
     * 获取当前登录用户信息 - 完整业务逻辑实现
     */
    @Override
    public Map<String, Object> getCurrentUser() {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", UserContext.getCurrentUserId());
        result.put("userName", UserContext.getCurrentUserName());
        result.put("tenantId", TenantContext.getCurrentTenantId());
        result.put("tenantName", TenantContext.getCurrentTenantName());
        result.put("permissions", StpUtil.getPermissionList());
        result.put("roles", StpUtil.getRoleList());
        return result;
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