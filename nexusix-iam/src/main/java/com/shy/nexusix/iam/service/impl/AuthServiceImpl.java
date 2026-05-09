package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.util.IpUtil;
import com.shy.nexusix.core.tenant.TenantContext;
import com.shy.nexusix.core.user.UserContext;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.entity.SysUserTenantRel;
import com.shy.nexusix.iam.entity.SysUserToken;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.rto.RegisterRTO;
import com.shy.nexusix.iam.service.IAuthService;
import com.shy.nexusix.iam.service.ISysUserService;
import com.shy.nexusix.iam.service.ISysUserTenantRelService;
import com.shy.nexusix.iam.service.ISysUserTokenService;
import com.shy.nexusix.iam.vo.CurrentUserVO;
import com.shy.nexusix.iam.vo.LoginVO;
import com.shy.nexusix.iam.vo.RegisterVO;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.service.ISysTenantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysUserTenantRelService iSysUserTenantRelService;

    @Autowired
    private ISysTenantService iSysTenantService;

    @Autowired
    private ISysUserTokenService iSysUserTokenService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * <p>
     * 用户登录
     * </p>
     * <p>
     * 执行完整的用户登录流程：
     * 1. 校验用户名密码
     * 2. 校验用户状态
     * 3. 查询用户租户关联
     * 4. 校验租户状态
     * 5. 执行Sa-Token登录（Session写入Redis）
     * 6. 初始化租户上下文
     * 7. 预热权限缓存
     * 8. 更新登录信息
     * 9. 创建Token记录
     * </p>
     *
     * @param loginRTO 登录请求参数
     * @param request  HTTP请求对象
     * @return 登录结果VO
     * @throws com.shy.nexusix.common.exception.BusinessException 登录失败时抛出（用户名密码错误、用户禁用、租户冻结等）
     * @author shy
     * @since 2026-05-08
     */
    @Override
    public LoginVO login(LoginRTO loginRTO, HttpServletRequest request) {
        log.info("用户登录请求 - 用户名: {}", loginRTO.getUsername());

        // 查询用户
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, loginRTO.getUsername())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUser user = iSysUserService.getOne(userWrapper);

        if (user == null) {
            log.warn("登录失败 - 用户不存在: {}", loginRTO.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 校验用户状态
        if (!user.getStatus().equals(GlobalEnum.UserStatus.NORMAL.getCode())) {
            log.warn("登录失败 - 用户已被禁用: {}", loginRTO.getUsername());
            throw new BusinessException(403, "用户已被禁用，请联系管理员");
        }

        // 校验密码（BCrypt匹配）
        if (!BCrypt.checkpw(loginRTO.getPassword(), user.getPassword())) {
            log.warn("登录失败 - 密码错误: {}", loginRTO.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 查询用户租户关联（取默认）
        LambdaQueryWrapper<SysUserTenantRel> tenantRelWrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getUserId, user.getId())
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .eq(SysUserTenantRel::getIsDefault, GlobalEnum.Default.DEFAULT.getCode());
        SysUserTenantRel userTenantRel = iSysUserTenantRelService.getOne(tenantRelWrapper);

        if (userTenantRel == null) {
            log.warn("登录失败 - 用户未绑定租户: {}", loginRTO.getUsername());
            throw new BusinessException(403, "用户未绑定任何租户");
        }

        // 校验租户状态
        SysTenant tenant = iSysTenantService.getById(userTenantRel.getTenantId());
        if (tenant == null || !tenant.getStatus().equals(GlobalEnum.TenantStatus.NORMAL.getCode())) {
            log.warn("登录失败 - 租户已冻结或不存在: tenantId={}", userTenantRel.getTenantId());
            throw new BusinessException(403, "所属租户已被冻结或不存在");
        }

        // 执行登录 Sa-Token自动将会话写入Redis
        StpUtil.login(user.getId());
        log.info("用户登录成功 - userId={}, username={}", user.getId(), user.getUsername());

        // 将租户上下文写入Sa-Token Session → 自动持久化到Redis
        SaSession session = StpUtil.getSession();
        session.set(GlobalConstant.Session.TENANT_ID, tenant.getId());
        session.set(GlobalConstant.Session.TENANT_NAME, tenant.getTenantName());
        session.set(GlobalConstant.Session.USER_NAME, user.getNickname());

        // 预热权限/角色缓存到Redis（避免首次请求查库）
        List<String> permissions = StpUtil.getPermissionList();
        List<String> roles = StpUtil.getRoleList();

        // 更新登录信息
        String clientIp = IpUtil.getClientIp(request);
        LocalDateTime now = LocalDateTime.now();
        user.setLoginIp(clientIp);
        user.setLoginDate(now);
        iSysUserService.updateById(user);

        // 创建Token记录
        SysUserToken tokenRecord = new SysUserToken();
        tokenRecord.setUserId(user.getId());
        tokenRecord.setUserName(user.getNickname());
        tokenRecord.setTenantId(tenant.getId());
        tokenRecord.setTenantName(tenant.getTenantName());
        tokenRecord.setToken(StpUtil.getTokenValue());
        tokenRecord.setStatus(GlobalEnum.TokenStatus.VALID.getCode());
        tokenRecord.setLoginIp(clientIp);
        tokenRecord.setLoginTime(now);
        tokenRecord.setExpireTime(now.plusSeconds(StpUtil.getTokenTimeout()));
        iSysUserTokenService.save(tokenRecord);

        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(StpUtil.getTokenValue());
        loginVO.setTokenName(StpUtil.getTokenName());
        loginVO.setUserId(user.getId());
        loginVO.setUserName(user.getNickname());
        loginVO.setTenantId(tenant.getId());
        loginVO.setTenantName(tenant.getTenantName());
        loginVO.setPermissions(permissions);
        loginVO.setRoles(roles);

        return loginVO;
    }

    /**
     * <p>
     * 用户登出
     * </p>
     * <p>
     * 执行完整的用户登出流程：
     * 1. 清除用户权限/角色缓存
     * 2. 失效数据库Token记录
     * 3. 执行Sa-Token登出（清除Redis会话）
     * </p>
     *
     * @return 登出结果
     * @author shy
     * @since 2026-05-08
     */
    @Override
    public String logout() {
        if (!StpUtil.isLogin()) {
            log.info("登出请求 - 用户未登录");
            return "未登录状态，无需登出";
        }

        String tokenValue = StpUtil.getTokenValue();
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户登出 - userId={}", userId);

        // 清除业务缓存
        redisTemplate.delete(GlobalConstant.RedisKey.PERM_PREFIX + userId);
        redisTemplate.delete(GlobalConstant.RedisKey.ROLE_PREFIX + userId);

        // 失效Token记录
        LambdaUpdateWrapper<SysUserToken> updateWrapper = new LambdaUpdateWrapper<SysUserToken>()
                .eq(SysUserToken::getToken, tokenValue)
                .eq(SysUserToken::getUserId, userId)
                .set(SysUserToken::getStatus, GlobalEnum.TokenStatus.INVALID.getCode());
        iSysUserTokenService.update(updateWrapper);

        // 注销会话 → Sa-Token自动清除Redis数据
        StpUtil.logout();

        return "登出成功";
    }

    /**
     * <p>
     * 切换租户上下文
     * </p>
     * <p>
     * 执行租户切换流程：
     * 1. 校验用户是否属于目标租户
     * 2. 校验租户状态
     * 3. 更新Sa-Token Session中的租户信息
     * 4. 清除权限缓存（不同租户权限可能不同）
     * </p>
     *
     * @param tenantId 目标租户ID
     * @return 切换结果
     * @throws com.shy.nexusix.common.exception.BusinessException 切换失败时抛出（用户不属于该租户、租户冻结等）
     * @author shy
     * @since 2026-05-08
     */
    @Override
    public String switchTenant(Long tenantId) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("租户切换请求 - userId={}, targetTenantId={}", userId, tenantId);

        // 校验用户是否属于目标租户
        LambdaQueryWrapper<SysUserTenantRel> countWrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getUserId, userId)
                .eq(SysUserTenantRel::getTenantId, tenantId)
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long count = iSysUserTenantRelService.count(countWrapper);

        if (count == 0) {
            log.warn("租户切换失败 - 用户不属于该租户: userId={}, tenantId={}", userId, tenantId);
            throw new BusinessException(403, "您不属于该租户，无法切换");
        }

        // 校验目标租户状态
        SysTenant tenant = iSysTenantService.getById(tenantId);
        if (tenant == null || !tenant.getStatus().equals(GlobalEnum.TenantStatus.NORMAL.getCode())) {
            log.warn("租户切换失败 - 租户已冻结或不存在: tenantId={}", tenantId);
            throw new BusinessException(403, "目标租户已被冻结或不存在");
        }

        // 更新Sa-Token Session → 自动同步到Redis
        SaSession session = StpUtil.getSession();
        session.set(GlobalConstant.Session.TENANT_ID, tenant.getId());
        session.set(GlobalConstant.Session.TENANT_NAME, tenant.getTenantName());

        // 清除权限缓存（租户切换后权限可能不同）
        redisTemplate.delete(GlobalConstant.RedisKey.PERM_PREFIX + userId);
        redisTemplate.delete(GlobalConstant.RedisKey.ROLE_PREFIX + userId);

        log.info("租户切换成功 - userId={}, tenantId={}", userId, tenantId);
        return "租户切换成功";
    }

    /**
     * <p>
     * 获取当前登录用户信息
     * </p>
     * <p>
     * 从Sa-Token Session和权限缓存中获取当前用户的完整信息：
     * - 用户ID、用户名
     * - 租户ID、租户名称
     * - 权限列表
     * - 角色列表
     * </p>
     *
     * @return 当前用户信息VO
     * @author shy
     * @since 2026-05-08
     */
    @Override
    public CurrentUserVO getCurrentUser() {
        CurrentUserVO currentUserVO = new CurrentUserVO();
        currentUserVO.setUserId(UserContext.getCurrentUserId());
        currentUserVO.setUserName(UserContext.getCurrentUserName());
        currentUserVO.setTenantId(TenantContext.getCurrentTenantId());
        currentUserVO.setTenantName(TenantContext.getCurrentTenantName());
        currentUserVO.setPermissions(StpUtil.getPermissionList());
        currentUserVO.setRoles(StpUtil.getRoleList());
        return currentUserVO;
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
     * @return 注册结果VO
     * @throws com.shy.nexusix.common.exception.BusinessException 注册失败时抛出（密码不一致、用户名已存在、邮箱已注册、手机号已注册等）
     * @author shy
     * @since 2026-05-08
     */
    @Override
    public RegisterVO register(RegisterRTO registerRTO) {
        log.info("用户注册请求 - 用户名: {}", registerRTO.getUsername());

        // 校验两次密码输入是否一致
        if (!registerRTO.getPassword().equals(registerRTO.getConfirmPassword())) {
            log.warn("注册失败 - 两次密码输入不一致: {}", registerRTO.getUsername());
            throw new BusinessException(400, "两次密码输入不一致");
        }

        // 校验用户名是否已存在
        LambdaQueryWrapper<SysUser> usernameWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, registerRTO.getUsername());
        long usernameCount = iSysUserService.count(usernameWrapper);
        if (usernameCount > 0) {
            log.warn("注册失败 - 用户名已存在: {}", registerRTO.getUsername());
            throw new BusinessException(400, "用户名已存在");
        }

        // 校验邮箱是否已被注册
        LambdaQueryWrapper<SysUser> emailWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, registerRTO.getEmail());
        long emailCount = iSysUserService.count(emailWrapper);
        if (emailCount > 0) {
            log.warn("注册失败 - 邮箱已被注册: {}", registerRTO.getEmail());
            throw new BusinessException(400, "邮箱已被注册");
        }

        // 校验手机号是否已被注册
        LambdaQueryWrapper<SysUser> phoneWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, registerRTO.getPhone());
        long phoneCount = iSysUserService.count(phoneWrapper);
        if (phoneCount > 0) {
            log.warn("注册失败 - 手机号已被注册: {}", registerRTO.getPhone());
            throw new BusinessException(400, "手机号已被注册");
        }

        // 构建用户实体
        SysUser user = new SysUser();
        user.setUsername(registerRTO.getUsername());
        user.setPassword(BCrypt.hashpw(registerRTO.getPassword(), BCrypt.gensalt()));
        user.setNickname(registerRTO.getNickname());
        user.setEmail(registerRTO.getEmail());
        user.setPhone(registerRTO.getPhone());
        user.setStatus(GlobalEnum.UserStatus.NORMAL.getCode());
        user.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
        user.setCreateBy(0L);
        user.setCreateByName(registerRTO.getNickname());
        user.setUpdateBy(0L);
        user.setUpdateByName(registerRTO.getNickname());

        // 保存用户
        boolean saveResult = iSysUserService.save(user);
        if (!saveResult) {
            log.error("注册失败 - 数据库插入失败: {}", registerRTO.getUsername());
            throw new BusinessException(500, "注册失败，请稍后重试");
        }

        // 回填createBy/updateBy为自身ID
        user.setCreateBy(user.getId());
        user.setUpdateBy(user.getId());
        iSysUserService.updateById(user);

        log.info("用户注册成功 - userId={}, username={}", user.getId(), user.getUsername());

        // 构建返回结果（不含密码等敏感数据）
        RegisterVO registerVO = new RegisterVO();
        registerVO.setUserId(user.getId());
        registerVO.setUsername(user.getUsername());
        registerVO.setNickname(user.getNickname());
        registerVO.setEmail(user.getEmail());
        registerVO.setPhone(user.getPhone());

        return registerVO;
    }

}
