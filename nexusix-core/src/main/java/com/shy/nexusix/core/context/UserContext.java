package com.shy.nexusix.core.context;

import cn.dev33.satoken.stp.StpUtil;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.core.entity.dto.UserContextDTO;

import java.util.*;

public class UserContext {

    private UserContext() {
    }

    public static UserContextDTO getUserContext() {
        return (UserContextDTO) StpUtil.getSession().get(GlobalConstant.Session.USER_CONTEXT);
    }

    public static Long getCurrentUserId() {
        // TODO 待实现
        return null;
    }

    public static String getCurrentUserName() {
        // TODO 待实现
        return null;
    }

    public static boolean isLogin() {
        // TODO 待实现
        return false;
    }

    public static String getCurrentToken() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getCurrentPerm() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getValidPermSystem() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getValidPermTenant() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getValidPermRole() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getValidPermUser() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getInvalidPermTenant() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getInvalidPermRole() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getInvalidPermUser() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getCurrentRoles() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getValidRoleTenant() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getValidRoleUser() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getInvalidRoleTenant() {
        // TODO 待实现
        return null;
    }

    public static Set<String> getInvalidRoleUser() {
        // TODO 待实现
        return null;
    }

}
