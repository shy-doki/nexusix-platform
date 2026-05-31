package com.shy.nexusix.core.context;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.core.entity.dto.UserContextDTO;

import java.util.*;

public class UserContext {

    private UserContext() {
    }

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

    public static Long getCurrentUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    public static String getCurrentUserName() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getTenantInfo() != null ? ctx.getTenantInfo().getTenantName() : "未知用户";
    }

    public static boolean isLogin() {
        return StpUtil.isLogin();
    }

    public static String getCurrentToken() {
        return StpUtil.getTokenValue();
    }

    public static Set<String> getCurrentPerm() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getPerms())
                : Collections.emptySet();
    }

    public static Set<String> getValidPermSystem() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null && ctx.getPermInfo().getCascadeDisabled() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getCascadeDisabled().getSystemDisabled())
                : Collections.emptySet();
    }

    public static Set<String> getValidPermTenant() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null && ctx.getPermInfo().getCascadeDisabled() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getCascadeDisabled().getTenantDisabled())
                : Collections.emptySet();
    }

    public static Set<String> getValidPermRole() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null && ctx.getPermInfo().getCascadeDisabled() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getCascadeDisabled().getRoleDisabled())
                : Collections.emptySet();
    }

    public static Set<String> getValidPermUser() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null && ctx.getPermInfo().getCascadeDisabled() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getCascadeDisabled().getUserDisabled())
                : Collections.emptySet();
    }

    public static Set<String> getInvalidPermTenant() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getInvalidPerms())
                : Collections.emptySet();
    }

    public static Set<String> getInvalidPermRole() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getInvalidPerms())
                : Collections.emptySet();
    }

    public static Set<String> getInvalidPermUser() {
        UserContextDTO ctx = getUserContext();
        return ctx != null && ctx.getPermInfo() != null
                ? new LinkedHashSet<>(ctx.getPermInfo().getInvalidPerms())
                : Collections.emptySet();
    }

    public static Set<String> getCurrentRoles() {
        return Collections.emptySet();
    }

    public static Set<String> getValidRoleTenant() {
        return Collections.emptySet();
    }

    public static Set<String> getValidRoleUser() {
        return Collections.emptySet();
    }

    public static Set<String> getInvalidRoleTenant() {
        return Collections.emptySet();
    }

    public static Set<String> getInvalidRoleUser() {
        return Collections.emptySet();
    }

}
