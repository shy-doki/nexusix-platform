package com.shy.nexusix.core.context;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.core.entity.dto.UserContextDTO;

import java.util.*;

/**
 * <p>用户上下文工具类，提供从Sa-Token Session获取用户上下文的快捷方法</p>
 * @author shy
 */
public class UserContext {

    /** <p>私有构造</p> */
    private UserContext() {
    }

    /**
     * <p>获取当前用户上下文DTO</p>
     * @return 用户上下文DTO
     */
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
     * <p>确保DTO中所有List字段不为null，防止反序列化NPE</p>
     * @param dto 用户上下文DTO
     */
    private static void ensureNonNull(UserContextDTO dto) {
        if (dto == null) {
            return;
        }
        // 权限信息
        UserContextDTO.PermissionInfo permissionInfo = dto.getPermInfo();
        if (permissionInfo != null) {
            // 当前租户权限
            UserContextDTO.CurrentPermissions currentPerms = permissionInfo.getCurrent();
            if (currentPerms != null) {
                currentPerms.setEnabled(nullToEmpty(currentPerms.getEnabled()));
                currentPerms.setDisabled(nullToEmpty(currentPerms.getDisabled()));
            }

            // 全部/有效/无效权限
            permissionInfo.setAll(nullToEmpty(permissionInfo.getAll()));
            permissionInfo.setValid(nullToEmpty(permissionInfo.getValid()));
            permissionInfo.setInvalid(nullToEmpty(permissionInfo.getInvalid()));

            // 级联禁用详情
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

            // 字段级权限
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
            // 当前租户部门
            UserContextDTO.CurrentDepts currentDepts = deptGroup.getCurrent();
            if (currentDepts != null) {
                currentDepts.setEnabled(nullToEmpty(currentDepts.getEnabled()));
                currentDepts.setDisabled(nullToEmpty(currentDepts.getDisabled()));
            }

            // 全部/有效/无效部门
            deptGroup.setAll(nullToEmpty(deptGroup.getAll()));
            deptGroup.setValid(nullToEmpty(deptGroup.getValid()));
            deptGroup.setInvalid(nullToEmpty(deptGroup.getInvalid()));
        }

        // 角色分组
        UserContextDTO.RoleGroup roleGroup = dto.getRoleInfo();
        if (roleGroup != null) {
            // 当前租户角色
            UserContextDTO.CurrentRoles currentRoles = roleGroup.getCurrent();
            if (currentRoles != null) {
                currentRoles.setEnabled(nullToEmpty(currentRoles.getEnabled()));
                currentRoles.setDisabled(nullToEmpty(currentRoles.getDisabled()));
            }

            // 全部/有效/无效角色
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

    /**
     * <p>确保字段权限Map中所有TableFieldPermission的List不为null</p>
     * @param map 表字段权限Map
     */
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

    /**
     * <p>null转空列表</p>
     * @param list 原列表
     * @return 非null列表
     */
    private static <T> List<T> nullToEmpty(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }

    /**
     * <p>获取当前用户ID</p>
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取当前用户名</p>
     * @return 用户名
     */
    public static String getCurrentUserName() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>判断当前是否已登录</p>
     * @return 是否登录
     */
    public static boolean isLogin() {
        // TODO 待实现
        return false;
    }

    /**
     * <p>获取当前Token</p>
     * @return Token值
     */
    public static String getCurrentToken() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取当前用户权限编码集合</p>
     * @return 权限编码集合
     */
    public static Set<String> getCurrentPerm() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取系统级有效权限编码集合</p>
     * @return 权限编码集合
     */
    public static Set<String> getValidPermSystem() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取租户级有效权限编码集合</p>
     * @return 权限编码集合
     */
    public static Set<String> getValidPermTenant() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取角色级有效权限编码集合</p>
     * @return 权限编码集合
     */
    public static Set<String> getValidPermRole() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取用户级有效权限编码集合</p>
     * @return 权限编码集合
     */
    public static Set<String> getValidPermUser() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取租户级无效权限编码集合</p>
     * @return 权限编码集合
     */
    public static Set<String> getInvalidPermTenant() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取角色级无效权限编码集合</p>
     * @return 权限编码集合
     */
    public static Set<String> getInvalidPermRole() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取用户级无效权限编码集合</p>
     * @return 权限编码集合
     */
    public static Set<String> getInvalidPermUser() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取当前用户角色编码集合</p>
     * @return 角色编码集合
     */
    public static Set<String> getCurrentRoles() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取租户级有效角色编码集合</p>
     * @return 角色编码集合
     */
    public static Set<String> getValidRoleTenant() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取用户级有效角色编码集合</p>
     * @return 角色编码集合
     */
    public static Set<String> getValidRoleUser() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取租户级无效角色编码集合</p>
     * @return 角色编码集合
     */
    public static Set<String> getInvalidRoleTenant() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取用户级无效角色编码集合</p>
     * @return 角色编码集合
     */
    public static Set<String> getInvalidRoleUser() {
        // TODO 待实现
        return null;
    }

    /**
     * <p>获取当前租户的字段权限</p>
     * @return 字段权限，不存在时返回null
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

        // 按租户编码查找字段权限
        Map<String, UserContextDTO.FieldPermission> fieldPermByTenant =
            userContext.getPermInfo().getFieldPermissionByTenant();

        if (fieldPermByTenant == null) {
            return null;
        }

        return fieldPermByTenant.get(currentTenantCode);
    }

    /**
     * <p>获取当前租户编码</p>
     * @return 租户编码，不存在时返回null
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
