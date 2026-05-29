package com.shy.nexusix.core.context;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.core.entity.dto.UserContextDTO;

import java.util.*;

/**
 * <p>
 * 用户上下文工具类，提供当前登录用户上下文信息的静态访问入口。
 * </p>
 *
 * <p><b>设计意图：</b></p>
 * <p>本类作为用户上下文的统一访问层，封装了 Sa-Token Session 的读取逻辑，
 * 使业务代码无需直接操作 Session API，通过静态方法即可获取当前用户的
 * 权限信息、租户信息、登录状态等上下文数据。</p>
 *
 * <p><b>核心职责：</b></p>
 * <ul>
 *   <li>从 Sa-Token Session 中读取并反序列化 UserContextDTO</li>
 *   <li>提供当前用户ID、用户名、Token等基础信息的快捷获取</li>
 *   <li>提供权限编码集合（全部/有效/无效/各级别禁用）的快捷获取</li>
 *   <li>提供角色信息的快捷获取（当前为占位实现）</li>
 * </ul>
 *
 * <p><b>数据来源说明：</b></p>
 * <p>用户上下文数据在登录时由 {@link com.shy.nexusix.iam.service.Impl.AuthServiceImpl}
 * 构建并写入 Sa-Token Session，本类仅负责读取，不负责写入。</p>
 *
 * <p><b>类型兼容处理：</b></p>
 * <p>由于 Sa-Token Session 在某些序列化场景下可能将 UserContextDTO 存储为 JSONObject，
 * 本类在 {@link #getUserContext()} 方法中兼容了 UserContextDTO 和 JSONObject 两种类型，
 * 确保反序列化的健壮性。</p>
 *
 * @author shy
 * @since 2026-04-07
 * @see UserContextDTO
 * @see GlobalConstant.Session#USER_CONTEXT
 */
public class UserContext {

    /**
     * 私有构造函数，防止实例化。本类仅提供静态方法。
     */
    private UserContext() {
    }

    /**
     * <p>
     * 获取当前登录用户的上下文信息。
     * </p>
     *
     * <p><b>实现逻辑：</b></p>
     * <ol>
     *   <li>从 Sa-Token Session 中读取以 {@link GlobalConstant.Session#USER_CONTEXT} 为键的对象</li>
     *   <li>若对象为 null，返回 null</li>
     *   <li>若对象为 UserContextDTO 类型，直接返回</li>
     *   <li>若对象为 JSONObject 类型（Sa-Token 序列化场景），反序列化为 UserContextDTO</li>
     *   <li>其他类型抛出 IllegalStateException</li>
     * </ol>
     *
     * @return 当前用户的上下文信息，若未登录或 Session 中无数据则返回 null
     * @throws IllegalStateException 当 Session 中的对象类型既不是 UserContextDTO 也不是 JSONObject 时抛出
     */
    public static UserContextDTO getUserContext() {
        Object obj = StpUtil.getSession().get(GlobalConstant.Session.USER_CONTEXT);
        if (obj == null) {
            return null;
        }
        // 直接类型匹配：Session 中存储的即为 UserContextDTO 实例
        if (obj instanceof UserContextDTO) {
            return (UserContextDTO) obj;
        }
        // JSON 反序列化兼容：Sa-Token 在某些序列化配置下会将对象存储为 JSONObject
        if (obj instanceof JSONObject) {
            return ((JSONObject) obj).toJavaObject(UserContextDTO.class);
        }
        throw new IllegalStateException("Session中userContext类型异常: " + obj.getClass().getName());
    }

    /**
     * <p>
     * 获取当前登录用户的ID。
     * </p>
     *
     * @return 当前用户的ID（Long 类型），由 Sa-Token 的登录标识转换而来
     */
    public static Long getCurrentUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    /**
     * <p>
     * 获取当前登录用户的名称。
     * </p>
     *
     * <p><b>注意：</b>当前实现返回的是租户名称（tenantName）而非用户名，
     * 这可能是一个待修正的逻辑，后续应改为返回用户的 nickName 或 userName。</p>
     *
     * @return 当前用户的租户名称，若上下文为空则返回 "未知用户"
     */
    public static String getCurrentUserName() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getTenantInfo() != null ? ctx.getTenantInfo().getTenantName() : "未知用户";
    }

    /**
     * <p>
     * 判断当前请求是否已登录。
     * </p>
     *
     * @return true 表示当前请求携带有效 Token 且已登录，false 表示未登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }

    /**
     * <p>
     * 获取当前请求的 Token 值。
     * </p>
     *
     * @return 当前请求中携带的 Sa-Token Token 字符串
     */
    public static String getCurrentToken() {
        return StpUtil.getTokenValue();
    }

    /**
     * <p>
     * 获取当前用户的全部权限编码集合（含有效和无效权限）。
     * </p>
     *
     * <p>返回的集合包含用户被授予的所有权限编码，不区分是否被级联禁用。
     * 使用 LinkedHashSet 保持插入顺序的同时去重。</p>
     *
     * @return 全部权限编码集合，若上下文为空则返回空集合
     */
    public static Set<String> getCurrentPerm() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getPerms())
                : Collections.emptySet();
    }

    /**
     * <p>
     * 获取被系统级禁用的权限编码集合。
     * </p>
     *
     * <p>系统级禁用是最高优先级的权限禁用，由系统管理员在系统层面直接禁用，
     * 不可被低级别（租户/角色/用户）覆盖。</p>
     *
     * @return 系统级禁用的权限编码集合，若上下文为空则返回空集合
     */
    public static Set<String> getValidPermSystem() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null && ctx.getPermInfo().getCascadeDisabled() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getCascadeDisabled().getSystemDisabled())
                : Collections.emptySet();
    }

    /**
     * <p>
     * 获取被租户级禁用的权限编码集合。
     * </p>
     *
     * <p>租户级禁用由租户管理员在租户层面禁用，优先级低于系统级，
     * 但高于角色级和用户级。</p>
     *
     * @return 租户级禁用的权限编码集合，若上下文为空则返回空集合
     */
    public static Set<String> getValidPermTenant() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null && ctx.getPermInfo().getCascadeDisabled() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getCascadeDisabled().getTenantDisabled())
                : Collections.emptySet();
    }

    /**
     * <p>
     * 获取被角色级禁用的权限编码集合。
     * </p>
     *
     * <p>角色级禁用由角色配置层面禁用，优先级低于租户级，但高于用户级。</p>
     *
     * @return 角色级禁用的权限编码集合，若上下文为空则返回空集合
     */
    public static Set<String> getValidPermRole() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null && ctx.getPermInfo().getCascadeDisabled() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getCascadeDisabled().getRoleDisabled())
                : Collections.emptySet();
    }

    /**
     * <p>
     * 获取被用户级禁用的权限编码集合。
     * </p>
     *
     * <p>用户级禁用是最低优先级的权限禁用，由对特定用户单独禁用权限产生。</p>
     *
     * @return 用户级禁用的权限编码集合，若上下文为空则返回空集合
     */
    public static Set<String> getValidPermUser() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null && ctx.getPermInfo().getCascadeDisabled() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getCascadeDisabled().getUserDisabled())
                : Collections.emptySet();
    }

    /**
     * <p>
     * 获取租户级别的无效权限编码集合。
     * </p>
     *
     * <p>返回所有因任一级别级联禁用而失效的权限编码汇总。
     * 当前实现直接返回 invalidPerms 列表，未按租户级别单独过滤。</p>
     *
     * @return 无效权限编码集合，若上下文为空则返回空集合
     */
    public static Set<String> getInvalidPermTenant() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getInvalidPerms())
                : Collections.emptySet();
    }

    /**
     * <p>
     * 获取角色级别的无效权限编码集合。
     * </p>
     *
     * <p>当前实现直接返回 invalidPerms 列表，未按角色级别单独过滤，
     * 与 {@link #getInvalidPermTenant()} 返回结果一致，后续可能需要细分。</p>
     *
     * @return 无效权限编码集合，若上下文为空则返回空集合
     */
    public static Set<String> getInvalidPermRole() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getInvalidPerms())
                : Collections.emptySet();
    }

    /**
     * <p>
     * 获取用户级别的无效权限编码集合。
     * </p>
     *
     * <p>当前实现直接返回 invalidPerms 列表，未按用户级别单独过滤，
     * 与 {@link #getInvalidPermTenant()} 返回结果一致，后续可能需要细分。</p>
     *
     * @return 无效权限编码集合，若上下文为空则返回空集合
     */
    public static Set<String> getInvalidPermUser() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getInvalidPerms())
                : Collections.emptySet();
    }

    /**
     * <p>
     * 获取当前用户的角色编码集合。
     * </p>
     *
     * <p>当前为占位实现，始终返回空集合，待角色模块开发后补充。</p>
     *
     * @return 角色编码集合（当前始终为空）
     */
    public static Set<String> getCurrentRoles() {
        return Collections.emptySet();
    }

    /**
     * <p>
     * 获取租户级别的有效角色编码集合。
     * </p>
     *
     * <p>当前为占位实现，始终返回空集合，待角色模块开发后补充。</p>
     *
     * @return 租户级别有效角色编码集合（当前始终为空）
     */
    public static Set<String> getValidRoleTenant() {
        return Collections.emptySet();
    }

    /**
     * <p>
     * 获取用户级别的有效角色编码集合。
     * </p>
     *
     * <p>当前为占位实现，始终返回空集合，待角色模块开发后补充。</p>
     *
     * @return 用户级别有效角色编码集合（当前始终为空）
     */
    public static Set<String> getValidRoleUser() {
        return Collections.emptySet();
    }

    /**
     * <p>
     * 获取租户级别的无效角色编码集合。
     * </p>
     *
     * <p>当前为占位实现，始终返回空集合，待角色模块开发后补充。</p>
     *
     * @return 租户级别无效角色编码集合（当前始终为空）
     */
    public static Set<String> getInvalidRoleTenant() {
        return Collections.emptySet();
    }

    /**
     * <p>
     * 获取用户级别的无效角色编码集合。
     * </p>
     *
     * <p>当前为占位实现，始终返回空集合，待角色模块开发后补充。</p>
     *
     * @return 用户级别无效角色编码集合（当前始终为空）
     */
    public static Set<String> getInvalidRoleUser() {
        return Collections.emptySet();
    }

}
