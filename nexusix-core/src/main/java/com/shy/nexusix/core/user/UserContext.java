package com.shy.nexusix.core.user;

import cn.dev33.satoken.stp.StpUtil;
import com.shy.nexusix.common.constant.GlobalConstant;

import java.util.List;

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

    /**
     * 获取当前用户的权限编码列表
     * 数据流：StpUtil.getPermissionList() → StpInterfaceImpl → Redis缓存/DB
     *
     * @return 权限编码列表
     */
    public static List<String> getCurrentPermissions() {
        return StpUtil.getPermissionList();
    }

    /**
     * 获取当前用户的角色编码列表
     *
     * @return 角色编码列表
     */
    public static List<String> getCurrentRoles() {
        return StpUtil.getRoleList();
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

}
