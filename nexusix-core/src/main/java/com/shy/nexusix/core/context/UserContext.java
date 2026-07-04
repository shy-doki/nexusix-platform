package com.shy.nexusix.core.context;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.core.entity.dto.UserContextDTO.CurrentDepts;
import com.shy.nexusix.core.entity.dto.UserContextDTO.CurrentRoles;
import com.shy.nexusix.core.entity.dto.UserContextDTO.FieldPermission;
import com.shy.nexusix.core.entity.dto.UserContextDTO.TenantItem;
import com.shy.nexusix.core.entity.dto.UserContextDTO.UserInfo;

/**
 * <p>用户上下文工具类，提供从Sa-Token Session获取用户上下文的快捷方法</p>
 *
 * @author shy
 */
public class UserContext {

    /** <p>私有构造</p> */
    private UserContext() {
    }

    /**
     * <p>获取当前用户上下文DTO</p>
     *
     * @return 用户上下文DTO；未登录或Session无数据时返回null
     */
    public static UserContextDTO getUserContext() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        Object obj = StpUtil.getSession().get(GlobalConstant.Session.USER_CONTEXT);
        if (obj == null) {
            return null;
        }
        if (obj instanceof UserContextDTO) {
            return (UserContextDTO) obj;
        }
        return JSON.parseObject(JSON.toJSONString(obj), UserContextDTO.class);
    }

    /**
     * <p>获取当前登录租户的字段级权限</p>
     *
     * @return 当前租户的字段权限对象；无法获取时返回null
     */
    public static FieldPermission getCurrentPerm() {
        UserContextDTO userContext = getUserContext();
        if (userContext == null
            || userContext.getTenantInfo() == null
            || userContext.getTenantInfo().getCurrent() == null
            || userContext.getTenantInfo().getCurrent().isEmpty()
            || userContext.getPermInfo() == null
            || userContext.getPermInfo().getFieldPermissionByTenant() == null) {
            return null;
        }
        // 当前租户编码（tenantInfo.current 是 List，登录时确定，租户切换时更新）
        String currentTenantCode = userContext.getTenantInfo().getCurrent().get(0).getTenantCode();
        return userContext.getPermInfo().getFieldPermissionByTenant().get(currentTenantCode);
    }

    /**
     * <p>获取当前登录租户信息</p>
     *
     * @return 当前租户项；无法获取时返回null
     */
    public static TenantItem getCurrentTenantInfo() {
        UserContextDTO userContext = getUserContext();
        if (userContext == null
            || userContext.getTenantInfo() == null
            || userContext.getTenantInfo().getCurrent() == null
            || userContext.getTenantInfo().getCurrent().isEmpty()) {
            return null;
        }
        return userContext.getTenantInfo().getCurrent().get(0);
    }

    /**
     * <p>获取当前登录租户下的部门信息</p>
     *
     * @return 当前租户部门信息（含启用/禁用分组）；无法获取时返回null
     */
    public static CurrentDepts getCurrentDeptInfo() {
        UserContextDTO userContext = getUserContext();
        if (userContext == null || userContext.getDeptInfo() == null) {
            return null;
        }
        return userContext.getDeptInfo().getCurrent();
    }

    /**
     * <p>获取当前登录租户下的角色信息</p>
     *
     * @return 当前租户角色信息（含启用/禁用分组）；无法获取时返回null
     */
    public static CurrentRoles getCurrentRoleInfo() {
        UserContextDTO userContext = getUserContext();
        if (userContext == null || userContext.getRoleInfo() == null) {
            return null;
        }
        return userContext.getRoleInfo().getCurrent();
    }

    /**
     * <p>获取当前登录用户的基本信息</p>
     *
     * @return 用户基本信息；无法获取时返回null
     */
    public static UserInfo getCurrentUserInfo() {
        UserContextDTO userContext = getUserContext();
        if (userContext == null || userContext.getUserInfo() == null) {
            return null;
        }
        return userContext.getUserInfo();
    }

}
