package com.shy.nexusix.core.user;

import cn.dev33.satoken.stp.StpUtil;
import com.shy.nexusix.common.constant.GlobalConstant;

import java.util.*;

/**
 * <p>
 * 用户上下文工具类 - 封装Sa-Token会话操作
 * </p>
 * <p>
 * 提供获取当前登录用户信息的快捷方法。
 * 所有数据从Sa-Token Session中读取，Session数据由sa-token-redis持久化到Redis。
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
public class UserContext {

    /**
     * 获取当前登录用户ID
     * 数据流：StpUtil → Redis Session → 返回userId
     *
     * @return 当前登录用户ID
     * @throws cn.dev33.satoken.exception.NotLoginException 未登录时抛出
     */
    public static String getCurrentUserId() {
        return StpUtil.getLoginIdAsString();
    }

    /**
     * 获取当前登录用户名称
     *
     * @return 用户名称，未设置时返回"未知用户"
     */
    public static String getCurrentUserName() {
        Object userName = StpUtil.getSession().get(GlobalConstant.Session.USER_NAME);
        return userName != null ? userName.toString() : "未知用户";
    }

    /**
     * 检查当前用户是否已登录
     *
     * @return true-已登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }

    /**
     * 获取当前用户的Token值
     *
     * @return Token值
     */
    public static String getCurrentToken() {
        return StpUtil.getTokenValue();
    }

    /**
     * 获取当前用户的权限编码列表 [有效+无效]
     * 数据流：StpUtil.getPermissionList() → StpInterfaceImpl → Redis缓存/DB
     *
     * @return 权限编码列表
     */
    public static Set<String> getCurrentPerm() {
        List<String> permList = StpUtil.getPermissionList();
        return permList != null ? new HashSet<>(permList) : Collections.emptySet();
    }

    /**
     * 获取当前用户的有效权限列表 [系统级]
     * 数据流：从 Sa-Token Session 中读取 validPermSystem
     *
     * @return 系统级有效权限编码列表，未设置时返回空列表
     */
    public static Set<String> getValidPermSystem() {
        Object permList = StpUtil.getSession().get(GlobalConstant.Session.VALID_PERM_SYSTEM);
        return permList != null ? (Set<String>) permList : Collections.emptySet();
    }

    /**
     * 获取当前用户的有效权限列表 [租户级]
     * 数据流：从 Sa-Token Session 中读取 validPermTenant
     *
     * @return 租户级有效权限编码列表，未设置时返回空列表
     */
    public static Set<String> getValidPermTenant() {
        Object permList = StpUtil.getSession().get(GlobalConstant.Session.VALID_PERM_TENANT);
        return permList != null ? (Set<String>) permList : Collections.emptySet();
    }

    /**
     * 获取当前用户的有效权限列表 [角色级]
     * 数据流：从 Sa-Token Session 中读取 validPermRole
     *
     * @return 角色级有效权限编码列表，未设置时返回空列表
     */
    public static Set<String> getValidPermRole() {
        Object permList = StpUtil.getSession().get(GlobalConstant.Session.VALID_PERM_ROLE);
        return permList != null ? (Set<String>) permList : Collections.emptySet();
    }

    /**
     * 获取当前用户的有效权限列表 [用户级]
     * 数据流：从 Sa-Token Session 中读取 validPermUser
     *
     * @return 用户级有效权限编码列表，未设置时返回空列表
     */
    public static Set<String> getValidPermUser() {
        Object permList = StpUtil.getSession().get(GlobalConstant.Session.VALID_PERM_USER);
        return permList != null ? (Set<String>) permList : Collections.emptySet();
    }

    /**
     * 获取当前用户的禁用权限列表 [租户级]
     * 数据流：从 Sa-Token Session 中读取 invalidPermTenant
     *
     * @return 租户级禁用权限编码列表，未设置时返回空列表
     */
    public static Set<String> getInvalidPermTenant() {
        Object permList = StpUtil.getSession().get(GlobalConstant.Session.INVALID_PERM_TENANT);
        return permList != null ? (Set<String>) permList : Collections.emptySet();
    }

    /**
     * 获取当前用户的禁用权限列表 [角色级]
     * 数据流：从 Sa-Token Session 中读取 invalidPermRole
     *
     * @return 角色级禁用权限编码列表，未设置时返回空列表
     */
    public static Set<String> getInvalidPermRole() {
        Object permList = StpUtil.getSession().get(GlobalConstant.Session.INVALID_PERM_ROLE);
        return permList != null ? (Set<String>) permList : Collections.emptySet();
    }

    /**
     * 获取当前用户的禁用权限列表 [用户级]
     * 数据流：从 Sa-Token Session 中读取 invalidPermUser
     *
     * @return 用户级禁用权限编码列表，未设置时返回空列表
     */
    public static Set<String> getInvalidPermUser() {
        Object permList = StpUtil.getSession().get(GlobalConstant.Session.INVALID_PERM_USER);
        return permList != null ? (Set<String>) permList : Collections.emptySet();
    }

    /**
     * 获取当前用户的所有有效权限列表（合并系统级+租户级+角色级+用户级）
     *
     * @return 合并后的有效权限编码列表
     */
    public static Set<String> getAllValidPerms() {
        Set<String> allPerms = new HashSet<>();
        allPerms.addAll(getValidPermSystem());
        allPerms.addAll(getValidPermTenant());
        allPerms.addAll(getValidPermRole());
        allPerms.addAll(getValidPermUser());
        return allPerms;
    }

    /**
     * 获取当前用户的所有禁用权限列表（合并租户级+角色级+用户级）
     *
     * @return 合并后的禁用权限编码列表
     */
    public static Set<String> getAllInvalidPerms() {
        Set<String> allPerms = new HashSet<>();
        allPerms.addAll(getInvalidPermTenant());
        allPerms.addAll(getInvalidPermRole());
        allPerms.addAll(getInvalidPermUser());
        return allPerms;
    }
    
    /**
     * 获取当前用户的角色编码列表[有效+无效]
     *
     * @return 角色编码列表
     */
    public static Set<String> getCurrentRoles() {
        List<String> roleList = StpUtil.getRoleList();
        return roleList != null ? new HashSet<>(roleList) : Collections.emptySet();
    }

    /**
     * 获取当前用户的有效角色列表 [租户级]
     * 数据流：从 Sa-Token Session 中读取 validRoleTenant
     *
     * @return 租户级有效角色编码列表，未设置时返回空列表
     */
    public static Set<String> getValidRoleTenant() {
        Object roleList = StpUtil.getSession().get(GlobalConstant.Session.VALID_ROLE_TENANT);
        return roleList != null ? (Set<String>) roleList : Collections.emptySet();
    }

    /**
     * 获取当前用户的有效角色列表 [用户级]
     * 数据流：从 Sa-Token Session 中读取 validRoleUser
     *
     * @return 用户级有效角色编码列表，未设置时返回空列表
     */
    public static Set<String> getValidRoleUser() {
        Object roleList = StpUtil.getSession().get(GlobalConstant.Session.VALID_ROLE_USER);
        return roleList != null ? (Set<String>) roleList : Collections.emptySet();
    }

    /**
     * 获取当前用户的禁用角色列表 [租户级]
     * 数据流：从 Sa-Token Session 中读取 invalidRoleTenant
     *
     * @return 租户级禁用角色编码列表，未设置时返回空列表
     */
    public static Set<String> getInvalidRoleTenant() {
        Object roleList = StpUtil.getSession().get(GlobalConstant.Session.INVALID_ROLE_TENANT);
        return roleList != null ? (Set<String>) roleList : Collections.emptySet();
    }

    /**
     * 获取当前用户的禁用角色列表 [用户级]
     * 数据流：从 Sa-Token Session 中读取 invalidRoleUser
     *
     * @return 用户级禁用角色编码列表，未设置时返回空列表
     */
    public static Set<String> getInvalidRoleUser() {
        Object roleList = StpUtil.getSession().get(GlobalConstant.Session.INVALID_ROLE_USER);
        return roleList != null ? (Set<String>) roleList : Collections.emptySet();
    }

    /**
     * 获取当前用户的所有有效角色列表（合并租户级+用户级）
     *
     * @return 合并后的有效角色编码列表
     */
    public static Set<String> getAllValidRoles() {
        Set<String> allRoles = new HashSet<>();
        allRoles.addAll(getValidRoleTenant());
        allRoles.addAll(getValidRoleUser());
        return allRoles;
    }

    /**
     * 获取当前用户的所有禁用角色列表（合并租户级+用户级）
     *
     * @return 合并后的禁用角色编码列表
     */
    public static Set<String> getAllInvalidRoles() {
        Set<String> allRoles = new HashSet<>();
        allRoles.addAll(getInvalidRoleTenant());
        allRoles.addAll(getInvalidRoleUser());
        return allRoles;
    }
    
    /**
     * 判断当前用户是否为系统级管理员
     * 数据流：StpUtil.getRoleList() → StpInterfaceImpl → Redis缓存/DB
     *
     * @return true-是系统管理员
     */
    public static boolean isSuperAdmin() {
        return StpUtil.getRoleList().contains(1);
    }

    /**
     * 判断当前用户是否为租户级管理员
     *
     * @return true-是租户管理员
     */
    public static boolean isTenantAdmin() {
        return StpUtil.getRoleList().contains(1);
    }

}
