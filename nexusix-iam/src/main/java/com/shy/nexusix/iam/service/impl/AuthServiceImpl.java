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
import com.shy.nexusix.iam.rto.RegisterRTO;
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
     * 用户登录
     */
    @Override
    public Map<String, Object> login(LoginRTO loginRTO, HttpServletRequest request) {
        logger.info("用户登录请求 - 用户名: {}", loginRTO.getUsername());

        // 查询用户
        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, loginRTO.getUsername())
                        .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
        );

        if (user == null) {
            logger.warn("登录失败 - 用户不存在: {}", loginRTO.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 校验用户状态
        if (!user.getStatus().equals(GlobalEnum.UserStatus.NORMAL.getCode())) {
            logger.warn("登录失败 - 用户已被禁用: {}", loginRTO.getUsername());
            throw new BusinessException(403, "用户已被禁用，请联系管理员");
        }

        // 校验密码（BCrypt匹配）
        if (!BCrypt.checkpw(loginRTO.getPassword(), user.getPassword())) {
            logger.warn("登录失败 - 密码错误: {}", loginRTO.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 查询用户租户关联（取默认/第一个有效租户）
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

        // 校验租户状态
        SysTenant tenant = tenantMapper.selectById(userTenantRel.getTenantId());
        if (tenant == null || !tenant.getStatus().equals(GlobalEnum.TenantStatus.NORMAL.getCode())) {
            logger.warn("登录失败 - 租户已冻结或不存在: tenantId={}", userTenantRel.getTenantId());
            throw new BusinessException(403, "所属租户已被冻结或不存在");
        }

        // 执行登录 Sa-Token自动将会话写入Redis
        StpUtil.login(user.getId());
        logger.info("用户登录成功 - userId={}, username={}", user.getId(), user.getUsername());

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
        userMapper.updateById(user);

        // 创建Token记录
        SysUserToken tokenRecord = new SysUserToken();
        tokenRecord.setUserId(user.getId());
        tokenRecord.setUserName(user.getNickname());
        tokenRecord.setTenantId(tenant.getId());
        tokenRecord.setTenantName(tenant.getTenantName());
        tokenRecord.setToken(StpUtil.getTokenValue());
        tokenRecord.setStatus(GlobalEnum.TokenStatus.VALID.getCode());
        tokenRecord.setLoginIp(getClientIp(request));
        tokenRecord.setLoginTime(LocalDateTime.now());
        tokenRecord.setExpireTime(LocalDateTime.now().plusSeconds(StpUtil.getTokenTimeout()));
        userTokenMapper.insert(tokenRecord);

        // 构建返回结果
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
     * 用户登出
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

        // 清除业务缓存
        redisTemplate.delete("nexusix:perm:" + userId);
        redisTemplate.delete("nexusix:role:" + userId);

        // 失效Token记录
        userTokenMapper.update(
                null,
                new LambdaUpdateWrapper<SysUserToken>()
                        .eq(SysUserToken::getToken, tokenValue)
                        .eq(SysUserToken::getUserId, userId)
                        .set(SysUserToken::getStatus, GlobalEnum.TokenStatus.INVALID.getCode())
        );

        // 注销会话 → Sa-Token自动清除Redis数据
        StpUtil.logout();

        return "登出成功";
    }

    /**
     * 切换租户上下文
     */
    @Override
    public String switchTenant(Long tenantId) {
        Long userId = StpUtil.getLoginIdAsLong();
        logger.info("租户切换请求 - userId={}, targetTenantId={}", userId, tenantId);

        // 校验用户是否属于目标租户
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

        // 校验目标租户状态
        SysTenant tenant = tenantMapper.selectById(tenantId);
        if (tenant == null || !tenant.getStatus().equals(GlobalEnum.TenantStatus.NORMAL.getCode())) {
            logger.warn("租户切换失败 - 租户已冻结或不存在: tenantId={}", tenantId);
            throw new BusinessException(403, "目标租户已被冻结或不存在");
        }

        // 更新Sa-Token Session → 自动同步到Redis
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
     * 获取当前登录用户信息
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
     * <p>
     * 用户注册
     * </p>
     * <p>
     * 执行完整的用户注册流程：
     * 1. 校验两次密码输入是否一致
     * 2. 校验用户名是否已存在
     * 3. 校验邮箱是否已被注册
     * 4. 校验手机号是否已被注册
     * 5. BCrypt加密密码
     * 6. 构建用户实体并保存
     * 7. 返回注册结果（不含敏感数据）
     * </p>
     *
     * @param registerRTO 注册请求参数
     * @return 注册结果Map
     * @throws BusinessException 注册失败时抛出
     * @author shy
     * @since 2026-05-08
     */
    @Override
    public Map<String, Object> register(RegisterRTO registerRTO) {
        logger.info("用户注册请求 - 用户名: {}", registerRTO.getUsername());

        // 步骤1: 校验两次密码输入是否一致
        if (!registerRTO.getPassword().equals(registerRTO.getConfirmPassword())) {
            logger.warn("注册失败 - 两次密码输入不一致: {}", registerRTO.getUsername());
            throw new BusinessException(400, "两次密码输入不一致");
        }

        // 步骤2: 校验用户名是否已存在
        long usernameCount = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, registerRTO.getUsername())
        );
        if (usernameCount > 0) {
            logger.warn("注册失败 - 用户名已存在: {}", registerRTO.getUsername());
            throw new BusinessException(400, "用户名已存在");
        }

        // 步骤3: 校验邮箱是否已被注册
        long emailCount = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getEmail, registerRTO.getEmail())
        );
        if (emailCount > 0) {
            logger.warn("注册失败 - 邮箱已被注册: {}", registerRTO.getEmail());
            throw new BusinessException(400, "邮箱已被注册");
        }

        // 步骤4: 校验手机号是否已被注册
        long phoneCount = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getPhone, registerRTO.getPhone())
        );
        if (phoneCount > 0) {
            logger.warn("注册失败 - 手机号已被注册: {}", registerRTO.getPhone());
            throw new BusinessException(400, "手机号已被注册");
        }

        // 步骤5: 构建用户实体
        SysUser user = new SysUser();
        user.setUsername(registerRTO.getUsername());
        // BCrypt加密密码（强度因子10）
        user.setPassword(BCrypt.hashpw(registerRTO.getPassword(), BCrypt.gensalt()));
        user.setNickname(registerRTO.getNickname());
        user.setEmail(registerRTO.getEmail());
        user.setPhone(registerRTO.getPhone());
        // 注册用户默认状态为正常
        user.setStatus(GlobalEnum.UserStatus.NORMAL.getCode());
        // 逻辑删除默认为0（正常）
        user.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
        // 审计字段：注册场景下createBy为自身（保存后获取ID），先置为0
        user.setCreateBy(0L);
        user.setCreateByName(registerRTO.getNickname());
        user.setUpdateBy(0L);
        user.setUpdateByName(registerRTO.getNickname());

        // 步骤6: 保存用户
        int insertResult = userMapper.insert(user);
        if (insertResult <= 0) {
            logger.error("注册失败 - 数据库插入失败: {}", registerRTO.getUsername());
            throw new BusinessException(500, "注册失败，请稍后重试");
        }

        // 回填createBy/updateBy为自身ID
        user.setCreateBy(user.getId());
        user.setUpdateBy(user.getId());
        userMapper.updateById(user);

        logger.info("用户注册成功 - userId={}, username={}", user.getId(), user.getUsername());

        // 步骤7: 构建返回结果（不含密码等敏感数据）
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("email", user.getEmail());
        result.put("phone", user.getPhone());

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