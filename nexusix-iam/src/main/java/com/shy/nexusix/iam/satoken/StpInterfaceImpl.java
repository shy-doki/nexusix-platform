package com.shy.nexusix.iam.satoken;

import cn.dev33.satoken.stp.StpInterface;
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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * Sa-Token 权限/角色加载接口实现类
 * </p>
 * <p>
 * 实现Sa-Token的StpInterface接口，提供用户权限和角色列表查询。
 * 当调用StpUtil.getPermissionList()或StpUtil.getRoleList()时，Sa-Token会自动回调本类方法。
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

    /**
     * <p>
     * 获取指定用户的权限编码列表
     * </p>
     * <p>
     * 权限获取逻辑（按优先级排序）：
     * 1. 查询系统级权限策略（targetType=1），所有用户生效
     * 2. 查询租户级权限策略（targetType=2），当前租户下所有用户生效
     * 3. 查询用户角色关联，再通过角色查询角色级权限策略（targetType=3）
     * 4. 查询用户级权限策略（targetType=4），针对特定用户配置
     * </p>
     *
     * @param loginId   用户ID（Sa-Token会话标识）
     * @param loginType 登录类型（Sa-Token标识，本项目未使用）
     * @return 权限编码列表（已去重、过滤已禁用权限、过滤已删除记录）
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        try {
            Long userId = Long.parseLong(loginId.toString());
            Long tenantId = TenantContext.getCurrentTenantId();

            Set<String> permissionCodeSet = new HashSet<>();

            // 查询系统级权限策略（对所有用户生效）
            List<String> systemPerms = queryPermissionCodesByTargetType(GlobalConstant.Permission.TARGET_TYPE_SYSTEM, null);
            permissionCodeSet.addAll(systemPerms);

            // 查询租户级权限策略（对当前租户下所有用户生效）
            if (tenantId != null) {
                List<String> tenantPerms = queryPermissionCodesByTargetType(GlobalConstant.Permission.TARGET_TYPE_TENANT, tenantId);
                permissionCodeSet.addAll(tenantPerms);

                // 查询用户角色关联，再查询角色级权限策略
                List<Long> roleIds = getUserRoleIds(userId, tenantId);
                for (Long roleId : roleIds) {
                    List<String> rolePerms = queryPermissionCodesByTargetType(GlobalConstant.Permission.TARGET_TYPE_ROLE, roleId);
                    permissionCodeSet.addAll(rolePerms);
                }
            }

            // 查询用户级权限策略（针对特定用户配置）
            List<String> userPerms = queryPermissionCodesByTargetType(GlobalConstant.Permission.TARGET_TYPE_USER, userId);
            permissionCodeSet.addAll(userPerms);

            return new ArrayList<>(permissionCodeSet);
        } catch (Exception e) {
            log.error("获取用户权限列表失败, loginId: {}", loginId, e);
            return Collections.emptyList();
        }
    }

    /**
     * <p>
     * 获取指定用户的角色编码列表
     * </p>
     * <p>
     * 角色获取逻辑：
     * 1. 查询系统级角色（roleLevel=1且状态正常的角色对所有用户生效）
     * 2. 查询用户角色关联的角色编码（租户级角色和用户级角色）
     * </p>
     *
     * @param loginId   用户ID（Sa-Token会话标识）
     * @param loginType 登录类型（Sa-Token标识，本项目未使用）
     * @return 角色编码列表（已去重、过滤已禁用角色、过滤已删除记录）
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        try {
            Long userId = Long.parseLong(loginId.toString());
            Long tenantId = TenantContext.getCurrentTenantId();

            Set<String> roleCodeSet = new HashSet<>();

            // 1. 查询系统级角色（roleLevel=1且状态正常的角色对所有用户生效）
            List<String> systemRoles = querySystemRoleCodes();
            roleCodeSet.addAll(systemRoles);

            // 2. 查询用户角色关联的角色编码（租户级角色和用户级角色）
            if (tenantId != null) {
                LambdaQueryWrapper<SysUserRoleRel> relWrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                        .eq(SysUserRoleRel::getUserId, userId)
                        .eq(SysUserRoleRel::getTenantId, tenantId)
                        .eq(SysUserRoleRel::getIsDeleted, GlobalConstant.Status.NOT_DELETED);

                List<SysUserRoleRel> relList = iSysUserRoleRelService.list(relWrapper);
                if (!relList.isEmpty()) {
                    List<Long> roleIds = relList.stream()
                            .map(SysUserRoleRel::getRoleId)
                            .collect(Collectors.toList());

                    LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<SysRole>()
                            .in(SysRole::getId, roleIds)
                            .eq(SysRole::getStatus, GlobalEnum.UserStatus.NORMAL.getCode())
                            .eq(SysRole::getIsDeleted, GlobalConstant.Status.NOT_DELETED);

                    List<SysRole> roleList = iSysRoleService.list(roleWrapper);
                    List<String> roleCodes = roleList.stream()
                            .map(SysRole::getRoleCode)
                            .collect(Collectors.toList());
                    roleCodeSet.addAll(roleCodes);
                }
            }

            return new ArrayList<>(roleCodeSet);
        } catch (Exception e) {
            log.error("获取用户角色列表失败, loginId: {}", loginId, e);
            return Collections.emptyList();
        }
    }

    /**
     * <p>
     * 根据目标类型查询关联的权限编码列表
     * </p>
     *
     * @param targetType 目标类型（1-系统 2-租户 3-角色 4-用户）
     * @param targetId   目标ID
     * @return 权限编码列表
     */
    private List<String> queryPermissionCodesByTargetType(int targetType, Long targetId) {
        LambdaQueryWrapper<SysPermissionPolicy> policyWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getTargetType, targetType)
                .eq(targetId != null, SysPermissionPolicy::getTargetId, targetId)
                .eq(SysPermissionPolicy::getAction, GlobalConstant.Permission.ACTION_ALLOW)
                .eq(SysPermissionPolicy::getIsDeleted, GlobalConstant.Status.NOT_DELETED)
                .orderByDesc(SysPermissionPolicy::getPriority);

        List<SysPermissionPolicy> policyList = iSysPermissionPolicyService.list(policyWrapper);
        if (policyList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> permissionIds = policyList.stream()
                .map(SysPermissionPolicy::getPermissionId)
                .collect(Collectors.toList());

        LambdaQueryWrapper<SysPermission> permWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, permissionIds)
                .eq(SysPermission::getStatus, GlobalEnum.UserStatus.NORMAL.getCode())
                .eq(SysPermission::getIsDeleted, GlobalConstant.Status.NOT_DELETED);

        List<SysPermission> permList = iSysPermissionService.list(permWrapper);
        return permList.stream()
                .map(SysPermission::getPermCode)
                .collect(Collectors.toList());
    }

    /**
     * <p>
     * 查询系统级角色编码列表
     * </p>
     *
     * @return 系统级角色编码列表
     */
    private List<String> querySystemRoleCodes() {
        LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleLevel, GlobalConstant.Role.ROLE_LEVEL_SYSTEM)
                .eq(SysRole::getStatus, GlobalEnum.UserStatus.NORMAL.getCode())
                .eq(SysRole::getIsDeleted, GlobalConstant.Status.NOT_DELETED);

        List<SysRole> roleList = iSysRoleService.list(roleWrapper);
        return roleList.stream()
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList());
    }

    /**
     * <p>
     * 获取用户在指定租户下的角色ID列表
     * </p>
     *
     * @param userId   用户ID
     * @param tenantId 租户ID
     * @return 角色ID列表
     */
    private List<Long> getUserRoleIds(Long userId, Long tenantId) {
        LambdaQueryWrapper<SysUserRoleRel> relWrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .eq(SysUserRoleRel::getUserId, userId)
                .eq(SysUserRoleRel::getTenantId, tenantId)
                .eq(SysUserRoleRel::getIsDeleted, GlobalConstant.Status.NOT_DELETED);

        List<SysUserRoleRel> relList = iSysUserRoleRelService.list(relWrapper);
        return relList.stream()
                .map(SysUserRoleRel::getRoleId)
                .collect(Collectors.toList());
    }
}
