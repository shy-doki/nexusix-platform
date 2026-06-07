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
        UserContextDTO dto;
        if (obj instanceof UserContextDTO) {
            dto = (UserContextDTO) obj;
        } else if (obj instanceof JSONObject) {
            dto = ((JSONObject) obj).toJavaObject(UserContextDTO.class);
        } else {
            dto = (UserContextDTO) obj;
        }
        ensureNonNull(dto);
        return dto;
    }

    /**
     * 确保 DTO 中所有 List 类型字段不为 null，防止反序列化时 null 值导致 NPE
     */
    private static void ensureNonNull(UserContextDTO dto) {
        if (dto == null) {
            return;
        }
        UserContextDTO.PermInfo permInfo = dto.getPermInfo();
        if (permInfo == null) {
            return;
        }
        permInfo.setPerms(nullToEmpty(permInfo.getPerms()));
        permInfo.setValidPerms(nullToEmpty(permInfo.getValidPerms()));
        permInfo.setInvalidPerms(nullToEmpty(permInfo.getInvalidPerms()));

        UserContextDTO.CascadeDisabled cascadeDisabled = permInfo.getCascadeDisabled();
        if (cascadeDisabled != null) {
            cascadeDisabled.setSystemDisabled(nullToEmpty(cascadeDisabled.getSystemDisabled()));
            cascadeDisabled.setTenantDisabled(nullToEmpty(cascadeDisabled.getTenantDisabled()));
            cascadeDisabled.setRoleDisabled(nullToEmpty(cascadeDisabled.getRoleDisabled()));
            cascadeDisabled.setUserDisabled(nullToEmpty(cascadeDisabled.getUserDisabled()));
        }

        UserContextDTO.FieldPerm fieldPerm = permInfo.getFieldPerm();
        if (fieldPerm != null) {
            ensureFieldPermMapNonNull(fieldPerm.getQuery());
            ensureFieldPermMapNonNull(fieldPerm.getCreate());
            ensureFieldPermMapNonNull(fieldPerm.getUpdate());
        }
    }

    private static void ensureFieldPermMapNonNull(Map<String, UserContextDTO.EntityFieldPerm> map) {
        if (map == null) {
            return;
        }
        for (UserContextDTO.EntityFieldPerm perm : map.values()) {
            if (perm != null) {
                perm.setVisibleFields(nullToEmpty(perm.getVisibleFields()));
                perm.setInvisibleFields(nullToEmpty(perm.getInvisibleFields()));
            }
        }
    }

    private static <T> List<T> nullToEmpty(List<T> list) {
        return list != null ? list : Collections.emptyList();
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
