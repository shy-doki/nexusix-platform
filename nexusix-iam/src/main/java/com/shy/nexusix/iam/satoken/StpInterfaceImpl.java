package com.shy.nexusix.iam.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.core.tenant.TenantContext;
import com.shy.nexusix.iam.entity.SysPermission;
import com.shy.nexusix.iam.entity.SysPermissionPolicy;
import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.entity.SysUserRoleRel;
import com.shy.nexusix.iam.service.ISysPermissionPolicyService;
import com.shy.nexusix.iam.service.ISysPermissionService;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.shy.nexusix.iam.service.ISysUserRoleRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * Sa-Token 权限/角色加载接口实现类
 * </p>
 * <p>
 * 实现Sa-Token的StpInterface接口，提供用户权限和角色列表查询。
 * 当调用StpUtil.getPermissionList()或StpUtil.getRoleList()时，Sa-Token会自动回调本类方法。
 * </p>
 * <p>
 * 采用二级缓存策略：
 * - 第一级：Redis缓存，优先从缓存读取
 * - 第二级：数据库查询，缓存未命中时查询数据库
 * 缓存过期时间 = 当前会话Token的剩余过期时间
 * </p>
 * <p>
 * 权限层级结构（四级）：系统 > 租户 > 角色 > 用户
 * 角色层级结构（三级）：系统 > 租户 > 用户
 * 上层禁用会影响所有下层，但级联禁用逻辑不在本类实现范围内。
 * 本类仅负责获取各层级权限/角色的有效状态与禁用状态。
 * </p>
 *
 * @author shy
 * @since 2026-05-12
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private ISysUserRoleRelService iSysUserRoleRelService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private ISysPermissionPolicyService iSysPermissionPolicyService;

    @Autowired
    private ISysPermissionService iSysPermissionService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * <p>
     * 获取指定用户的权限编码列表
     * </p>
     * <p>
     * 权限获取逻辑（按层级从高到低）：
     * 1. 系统级权限（targetType=1）：对所有用户生效
     * 2. 租户级权限（targetType=2）：对当前租户下所有用户生效
     * 3. 角色级权限（targetType=3）：通过用户角色关联，查询角色级权限
     * 4. 用户级权限（targetType=4）：针对特定用户配置的专属权限
     * </p>
     * <p>
     * 缓存策略：
     * - 优先从Redis缓存读取，缓存键格式：nexusix:perm:{targetType}:{targetId}:valid/invalid
     * - 缓存未命中时查询数据库，并将结果写入Redis
     * - 缓存过期时间 = 当前会话Token的剩余过期时间
     * </p>
     *
     * @param loginId   用户ID（Sa-Token会话标识）
     * @param loginType 登录类型（Sa-Token标识，本项目未使用）
     * @return 权限编码列表（已去重、过滤已禁用权限、过滤已删除记录）
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 解析用户ID
        Long userId = Long.parseLong(loginId.toString());
        // 获取当前租户ID
        Long tenantId = TenantContext.getCurrentTenantId();

        // 存储最终合并的权限编码集合
        Set<String> permissionCodeSet = new HashSet<>();

        // 尝试从Redis缓存读取系统级有效权限
        List<String> cachedSystemValid = (List<String>) StpUtil.getSession().get(GlobalConstant.RedisKey.PERM_SYSTEM_VALID);
        if (cachedSystemValid == null) {
            // 缓存未命中，查询数据库获取系统级权限策略
            LambdaQueryWrapper<SysPermissionPolicy> permPolicyWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                    .eq(SysPermissionPolicy::getTargetId, tenantId)
                    .eq(SysPermissionPolicy::getTargetType, GlobalEnum.PermType)

            // 如果存在权限策略，则查询对应的权限详情
            if () {

            } else {
                // 无权限策略时，缓存空列表避免缓存穿透

            }
        }
        // 缓存命中，直接使用缓存数据
        permissionCodeSet.addAll(cachedSystemValid);

    }

    /**
     * <p>
     * 获取指定用户的角色编码列表
     * </p>
     * <p>
     * 角色获取逻辑（按层级从高到低）：
     * 1. 系统级角色（roleLevel=1）：roleLevel=1且状态正常的角色对所有用户生效
     * 2. 租户级角色（roleLevel=2）：通过用户角色关联获取租户下分配的角色
     * 3. 用户级角色（roleLevel=3）：通过用户角色关联获取用户专属的临时角色
     * </p>
     * <p>
     * 缓存策略：
     * - 优先从Redis缓存读取，缓存键格式：nexusix:role:system:valid / nexusix:role:{roleId}
     * - 缓存未命中时查询数据库，并将结果写入Redis
     * - 缓存过期时间 = 当前会话Token的剩余过期时间
     * </p>
     *
     * @param loginId   用户ID（Sa-Token会话标识）
     * @param loginType 登录类型（Sa-Token标识，本项目未使用）
     * @return 角色编码列表（已去重、过滤已禁用角色、过滤已删除记录）
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 解析用户ID和获取当前租户ID
        Long userId = Long.parseLong(loginId.toString());
        Long tenantId = TenantContext.getCurrentTenantId();

        // 存储最终合并的角色编码集合（使用Set自动去重）
        Set<String> roleCodeSet = new HashSet<>();

        // 获取当前Token剩余过期时间（秒），用于设置缓存TTL
        long tokenTimeout = StpUtil.getTokenTimeout();
        // 若Token超时时间无效，使用默认30分钟作为兜底
        long cacheTtlSeconds = tokenTimeout > 0 ? tokenTimeout : 1800L;

        // ============================================================
        // 层级1：查询系统级角色（roleLevel=1，对所有用户生效）
        // ============================================================
        // 构建系统级角色有效/无效缓存键
        String systemRoleValidKey = GlobalConstant.RedisKey.ROLE_SYSTEM_VALID;
        String systemRoleInvalidKey = GlobalConstant.RedisKey.ROLE_SYSTEM_INVALID;

        // 尝试从Redis缓存读取系统级有效角色
        Object cachedSystemValid = redisTemplate.opsForValue().get(systemRoleValidKey);
        if (cachedSystemValid != null && cachedSystemValid instanceof List) {
            // 缓存命中，直接使用缓存数据
            roleCodeSet.addAll((List<String>) cachedSystemValid);
        } else {
            // 缓存未命中，查询数据库获取系统级角色
            LambdaQueryWrapper<SysRole> systemRoleWrapper = new LambdaQueryWrapper<SysRole>()
                    // 筛选系统级角色（roleLevel=1）
                    .eq(SysRole::getRoleLevel, GlobalConstant.Role.ROLE_LEVEL_SYSTEM)
                    // 过滤已删除的角色
                    .eq(SysRole::getIsDeleted, GlobalConstant.Status.NOT_DELETED);
            List<SysRole> allSystemRoles = iSysRoleService.list(systemRoleWrapper);

            // 按角色状态分类：有效角色（status=1）和无效/禁用角色（status=0）
            List<String> validSystemRoleCodes = allSystemRoles.stream()
                    .filter(r -> r.getStatus().equals(GlobalEnum.RoleStatus.ENABLE.getCode()))
                    .map(SysRole::getRoleCode)
                    .collect(Collectors.toList());
            List<String> invalidSystemRoleCodes = allSystemRoles.stream()
                    .filter(r -> r.getStatus().equals(GlobalEnum.RoleStatus.DISABLE.getCode()))
                    .map(SysRole::getRoleCode)
                    .collect(Collectors.toList());

            // 将有效角色编码写入Redis缓存，TTL等于Token剩余过期时间
            redisTemplate.opsForValue().set(systemRoleValidKey, validSystemRoleCodes, cacheTtlSeconds, TimeUnit.SECONDS);
            // 将无效角色编码也写入缓存（便于后续审计和排查）
            if (!invalidSystemRoleCodes.isEmpty()) {
                redisTemplate.opsForValue().set(systemRoleInvalidKey, invalidSystemRoleCodes, cacheTtlSeconds, TimeUnit.SECONDS);
            }

            // 合并有效角色到结果集
            roleCodeSet.addAll(validSystemRoleCodes);
        }

        // ============================================================
        // 层级2 & 层级3：查询租户级和用户级角色（通过用户角色关联表获取）
        // ============================================================
        if (tenantId != null) {
            // 查询用户在当前租户下关联的角色ID列表
            // 这些角色可能是租户级（roleLevel=2）或用户级（roleLevel=3）
            LambdaQueryWrapper<SysUserRoleRel> roleRelWrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                    .eq(SysUserRoleRel::getUserId, userId)
                    .eq(SysUserRoleRel::getTenantId, tenantId)
                    .eq(SysUserRoleRel::getIsDeleted, GlobalConstant.Status.NOT_DELETED);
            List<SysUserRoleRel> roleRelList = iSysUserRoleRelService.list(roleRelWrapper);

            // 如果用户有关联的角色，则查询角色详情
            if (!roleRelList.isEmpty()) {
                // 提取角色ID列表
                List<Long> roleIds = roleRelList.stream()
                        .map(SysUserRoleRel::getRoleId)
                        .collect(Collectors.toList());

                // 查询角色实体，过滤已删除的角色
                LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<SysRole>()
                        .in(SysRole::getId, roleIds)
                        .eq(SysRole::getIsDeleted, GlobalConstant.Status.NOT_DELETED);
                List<SysRole> userRoles = iSysRoleService.list(roleWrapper);

                // 遍历每个角色，按状态分类并写入缓存
                for (SysRole role : userRoles) {
                    // 构建单个角色的缓存键（每个角色独立缓存，便于单独失效）
                    String roleCacheKey = GlobalConstant.RedisKey.ROLE_PREFIX + role.getId();

                    if (role.getStatus().equals(GlobalEnum.RoleStatus.ENABLE.getCode())) {
                        // 角色状态为启用，添加到有效角色集合
                        roleCodeSet.add(role.getRoleCode());
                        // 将角色编码写入Redis缓存
                        redisTemplate.opsForValue().set(roleCacheKey, role.getRoleCode(), cacheTtlSeconds, TimeUnit.SECONDS);
                    } else {
                        // 角色状态为禁用，不添加到结果集
                        // 可考虑将禁用角色也缓存标记，避免重复查询
                        redisTemplate.opsForValue().set(roleCacheKey + ":disabled", true, cacheTtlSeconds, TimeUnit.SECONDS);
                    }
                }
            }
        }

        // 返回去重后的角色编码列表
        return new ArrayList<>(roleCodeSet);
    }

}