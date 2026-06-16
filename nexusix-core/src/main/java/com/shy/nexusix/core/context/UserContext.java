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
        UserContextDTO.PermissionInfo permissionInfo = dto.getPermInfo();
        if (permissionInfo != null) {
            // current 现在是 CurrentPermissions 类型
            UserContextDTO.CurrentPermissions currentPerms = permissionInfo.getCurrent();
            if (currentPerms != null) {
                currentPerms.setEnabled(nullToEmpty(currentPerms.getEnabled()));
                currentPerms.setDisabled(nullToEmpty(currentPerms.getDisabled()));
            }

            // all/valid/invalid 现在是 List<TenantPermissions> 类型
            permissionInfo.setAll(nullToEmpty(permissionInfo.getAll()));
            permissionInfo.setValid(nullToEmpty(permissionInfo.getValid()));
            permissionInfo.setInvalid(nullToEmpty(permissionInfo.getInvalid()));

            // disabledDetail 现在是按租户分组的 Map
            Map<String, UserContextDTO.DisabledDetail> disabledDetailMap = permissionInfo.getDisabledDetailByTenant();
            if (disabledDetailMap != null) {
                for (UserContextDTO.DisabledDetail detail : disabledDetailMap.values()) {
                    if (detail != null) {
                        detail.setSystem(nullToEmpty(detail.getSystem()));
                        detail.setTenant(nullToEmpty(detail.getTenant()));
                        detail.setRole(nullToEmpty(detail.getRole()));
                        detail.setUser(nullToEmpty(detail.getUser()));
                    }
                }
            }

            // fieldPermission 现在是按租户分组的 Map
            Map<String, UserContextDTO.FieldPermission> fieldPermMap = permissionInfo.getFieldPermissionByTenant();
            if (fieldPermMap != null) {
                for (UserContextDTO.FieldPermission fp : fieldPermMap.values()) {
                    if (fp != null) {
                        ensureFieldPermMapNonNull(fp.getQuery());
                        ensureFieldPermMapNonNull(fp.getCreate());
                        ensureFieldPermMapNonNull(fp.getUpdate());
                    }
                }
            }
        }

        // 部门分组
        UserContextDTO.DeptGroup deptGroup = dto.getDeptInfo();
        if (deptGroup != null) {
            // current 现在是 CurrentDepts 类型
            UserContextDTO.CurrentDepts currentDepts = deptGroup.getCurrent();
            if (currentDepts != null) {
                currentDepts.setEnabled(nullToEmpty(currentDepts.getEnabled()));
                currentDepts.setDisabled(nullToEmpty(currentDepts.getDisabled()));
            }

            // all/valid/invalid 现在是 List<TenantDepts> 类型
            deptGroup.setAll(nullToEmpty(deptGroup.getAll()));
            deptGroup.setValid(nullToEmpty(deptGroup.getValid()));
            deptGroup.setInvalid(nullToEmpty(deptGroup.getInvalid()));
        }

        // 角色分组
        UserContextDTO.RoleGroup roleGroup = dto.getRoleInfo();
        if (roleGroup != null) {
            // current 现在是 CurrentRoles 类型
            UserContextDTO.CurrentRoles currentRoles = roleGroup.getCurrent();
            if (currentRoles != null) {
                currentRoles.setEnabled(nullToEmpty(currentRoles.getEnabled()));
                currentRoles.setDisabled(nullToEmpty(currentRoles.getDisabled()));
            }

            // all/valid/invalid 现在是 List<TenantRoles> 类型
            roleGroup.setAll(nullToEmpty(roleGroup.getAll()));
            roleGroup.setValid(nullToEmpty(roleGroup.getValid()));
            roleGroup.setInvalid(nullToEmpty(roleGroup.getInvalid()));
        }

        // 租户分组
        UserContextDTO.TenantGroup tenantGroup = dto.getTenantInfo();
        if (tenantGroup != null) {
            tenantGroup.setCurrent(nullToEmpty(tenantGroup.getCurrent()));
            tenantGroup.setAll(nullToEmpty(tenantGroup.getAll()));
            tenantGroup.setValid(nullToEmpty(tenantGroup.getValid()));
            tenantGroup.setInvalid(nullToEmpty(tenantGroup.getInvalid()));
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

    /**
     * 获取当前租户的字段权限
     * 兼容新的按租户分组结构
     */
    public static UserContextDTO.FieldPermission getCurrentTenantFieldPermission() {
        UserContextDTO userContext = getUserContext();
        if (userContext == null || userContext.getPermInfo() == null) {
            return null;
        }

        // 获取当前租户编码
        String currentTenantCode = getCurrentTenantCode();
        if (currentTenantCode == null) {
            return null;
        }

        // 从按租户分组的字段权限中获取当前租户的权限
        Map<String, UserContextDTO.FieldPermission> fieldPermByTenant =
            userContext.getPermInfo().getFieldPermissionByTenant();

        if (fieldPermByTenant == null) {
            return null;
        }

        return fieldPermByTenant.get(currentTenantCode);
    }

    /**
     * 获取当前租户编码
     */
    public static String getCurrentTenantCode() {
        UserContextDTO userContext = getUserContext();
        if (userContext == null || userContext.getTenantInfo() == null) {
            return null;
        }

        List<UserContextDTO.TenantItem> current = userContext.getTenantInfo().getCurrent();
        if (current == null || current.isEmpty()) {
            return null;
        }

        return current.get(0).getTenantCode();
    }

}