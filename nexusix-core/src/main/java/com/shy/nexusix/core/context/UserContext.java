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
        // 权限信息
        UserContextDTO.PermissionInfo permissionInfo = dto.getPermissions();
        if (permissionInfo != null) {
            permissionInfo.setAll(nullToEmpty(permissionInfo.getAll()));
            permissionInfo.setValid(nullToEmpty(permissionInfo.getValid()));
            permissionInfo.setInvalid(nullToEmpty(permissionInfo.getInvalid()));

            UserContextDTO.DisabledDetail disabledDetail = permissionInfo.getDisabledDetail();
            if (disabledDetail != null) {
                disabledDetail.setSystem(nullToEmpty(disabledDetail.getSystem()));
                disabledDetail.setTenant(nullToEmpty(disabledDetail.getTenant()));
                disabledDetail.setRole(nullToEmpty(disabledDetail.getRole()));
                disabledDetail.setUser(nullToEmpty(disabledDetail.getUser()));
            }

            UserContextDTO.FieldPermission fieldPermission = permissionInfo.getFieldPermission();
            if (fieldPermission != null) {
                ensureFieldPermMapNonNull(fieldPermission.getQuery());
                ensureFieldPermMapNonNull(fieldPermission.getCreate());
                ensureFieldPermMapNonNull(fieldPermission.getUpdate());
            }
        }
    }

    private static void ensureFieldPermMapNonNull(Map<String, UserContextDTO.TableFieldPermission> map) {
        if (map == null) {
            return;
        }
        for (UserContextDTO.TableFieldPermission perm : map.values()) {
            if (perm != null) {
                perm.setOperable(nullToEmpty(perm.getOperable()));
                perm.setInoperable(nullToEmpty(perm.getInoperable()));
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