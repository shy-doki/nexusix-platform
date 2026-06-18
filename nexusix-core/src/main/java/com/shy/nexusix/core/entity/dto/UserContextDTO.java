package com.shy.nexusix.core.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>用户上下文DTO，登录时构建并存入Sa-Token Session</p>
 * @author shy
 */
@Data
public class UserContextDTO {

    /** 用户基本信息 */
    private UserInfo userInfo;

    /** 租户分组（当前/全部/有效/无效） */
    private TenantGroup tenantInfo;

    /** 权限汇总信息 */
    private PermissionInfo permInfo;

    /** 角色分组（当前租户/全部/有效/无效），含租户标注 */
    private RoleGroup roleInfo;

    /** 部门分组（当前租户/全部/有效/无效），含租户标注 */
    private DeptGroup deptInfo;

    // 用户信息

    /** <p>用户基本信息</p> */
    @Data
    public static class UserInfo {
        /** 用户ID */
        private Long userId;
        /** 用户编码 */
        private String userCode;
        /** 用户名（登录名） */
        private String userName;
        /** 昵称 */
        private String nickName;
        /** 邮箱 */
        private String email;
        /** 手机号 */
        private String phone;
        /** 头像URL */
        private String avatar;
    }

    // 租户相关

    /** <p>租户项</p> */
    @Data
    public static class TenantItem {
        /** 租户编码 */
        private String tenantCode;
        /** 租户名称 */
        private String tenantName;
        /** 租户状态（ENABLED/DISABLED/EXPIRED/PENDING） */
        private String status;
        /** 服务过期时间 */
        private String expireTime;
        /** 是否主租户 */
        private Boolean isPrimary;
    }

    /** <p>租户分组（当前/全部/有效/无效）</p> */
    @Data
    public static class TenantGroup {
        /** 当前登录租户 */
        private List<TenantItem> current;
        /** 全部租户（有效+无效） */
        private List<TenantItem> all;
        /** 有效租户（用户策略ENABLED 且 租户ENABLED） */
        private List<TenantItem> valid;
        /** 无效租户（用户策略或租户状态非ENABLED） */
        private List<TenantItem> invalid;
    }

    // 权限相关

    /** <p>权限项</p> */
    @Data
    public static class PermItem {
        /** 权限编码 */
        private String permCode;
        /** 权限名称 */
        private String permName;
        /** 权限类型：MENU, BUTTON, API, DATA */
        private String permType;
        /** 所属租户编码 */
        private String tenantCode;
        /** 所属租户名称 */
        private String tenantName;
        /** 权限策略状态（ACTIVE/DISABLED） */
        private String permPolicyStatus;
    }

    /** <p>权限汇总信息</p> */
    @Data
    public static class PermissionInfo {
        /** 当前登录租户下的权限（分启用/禁用） */
        private CurrentPermissions current = new CurrentPermissions();
        /** 全部权限（按租户分组） */
        private List<TenantPermissions> all = new ArrayList<>();
        /** 有效权限（按租户分组） */
        private List<TenantPermissions> valid = new ArrayList<>();
        /** 无效权限（按租户分组） */
        private List<TenantPermissions> invalid = new ArrayList<>();
        /** 级联禁用详情（按租户分组） */
        private Map<String, DisabledDetail> disabledDetailByTenant;
        /** 字段级权限（按租户分组） */
        private Map<String, FieldPermission> fieldPermissionByTenant;
    }

    /** <p>当前租户权限（启用/禁用）</p> */
    @Data
    public static class CurrentPermissions {
        /** 当前租户启用的权限 */
        private List<PermItem> enabled = new ArrayList<>();
        /** 当前租户禁用的权限 */
        private List<PermItem> disabled = new ArrayList<>();
    }

    /** <p>按租户分组的权限列表</p> */
    @Data
    public static class TenantPermissions {
        /** 租户编码 */
        private String tenantCode;
        /** 租户名称 */
        private String tenantName;
        /** 该租户下的权限列表 */
        private List<PermItem> permissions = new ArrayList<>();
    }

    /** <p>级联禁用详情</p> */
    @Data
    public static class DisabledDetail {
        /** 系统级禁用的权限编码 */
        private List<String> system;
        /** 租户级禁用的权限编码 */
        private List<String> tenant;
        /** 部门级禁用的权限编码 */
        private List<String> dept;
        /** 角色级禁用的权限编码 */
        private List<String> role;
        /** 用户级禁用的权限编码 */
        private List<String> user;
    }

    /** <p>字段级权限</p> */
    @Data
    public static class FieldPermission {
        /** 查询操作的字段权限，key=表名 */
        private Map<String, TableFieldPermission> query;
        /** 创建操作的字段权限，key=表名 */
        private Map<String, TableFieldPermission> create;
        /** 更新操作的字段权限，key=表名 */
        private Map<String, TableFieldPermission> update;
    }

    /** <p>表字段权限</p> */
    @Data
    public static class TableFieldPermission {
        /** 可操作字段列表（策略ACTIVE时配置） */
        private List<String> operable = new ArrayList<>();
        /** 不可操作字段列表（策略非ACTIVE时配置） */
        private List<String> inoperable = new ArrayList<>();
    }

    // 角色相关

    /** <p>角色项</p> */
    @Data
    public static class RoleItem {
        /** 角色编码 */
        private String roleCode;
        /** 角色名称 */
        private String roleName;
        /** 数据权限范围（ALL/DEPT/DEPT_AND_SUB/SELF） */
        private String dataScope;
        /** 所属租户编码 */
        private String tenantCode;
        /** 所属租户名称 */
        private String tenantName;
    }

    /** <p>角色分组（当前租户/全部/有效/无效）</p> */
    @Data
    public static class RoleGroup {
        /** 当前登录租户下的角色（分启用/禁用） */
        private CurrentRoles current = new CurrentRoles();
        /** 全部角色（按租户分组） */
        private List<TenantRoles> all = new ArrayList<>();
        /** 有效角色（按租户分组） */
        private List<TenantRoles> valid = new ArrayList<>();
        /** 无效角色（按租户分组） */
        private List<TenantRoles> invalid = new ArrayList<>();
    }

    /** <p>当前租户角色（启用/禁用）</p> */
    @Data
    public static class CurrentRoles {
        /** 当前租户启用的角色 */
        private List<RoleItem> enabled = new ArrayList<>();
        /** 当前租户禁用的角色 */
        private List<RoleItem> disabled = new ArrayList<>();
    }

    /** <p>按租户分组的角色列表</p> */
    @Data
    public static class TenantRoles {
        /** 租户编码 */
        private String tenantCode;
        /** 租户名称 */
        private String tenantName;
        /** 该租户下的角色列表 */
        private List<RoleItem> roles = new ArrayList<>();
    }

    // 部门相关

    /** <p>部门项</p> */
    @Data
    public static class DeptItem {
        /** 部门编码 */
        private String deptCode;
        /** 部门名称 */
        private String deptName;
        /** 部门路径 */
        private String path;
        /** 层级深度 */
        private Integer level;
        /** 所属租户编码 */
        private String tenantCode;
        /** 所属租户名称 */
        private String tenantName;
        /** 是否主部门 */
        private Boolean isPrimary;
        /** 用户策略状态（ACTIVE/DISABLED） */
        private String userPolicyStatus;
    }

    /** <p>部门分组（当前租户/全部/有效/无效）</p> */
    @Data
    public static class DeptGroup {
        /** 当前登录租户下的部门（分启用/禁用） */
        private CurrentDepts current = new CurrentDepts();
        /** 全部部门（按租户分组） */
        private List<TenantDepts> all = new ArrayList<>();
        /** 有效部门（按租户分组） */
        private List<TenantDepts> valid = new ArrayList<>();
        /** 无效部门（按租户分组） */
        private List<TenantDepts> invalid = new ArrayList<>();
    }

    /** <p>当前租户部门（启用/禁用）</p> */
    @Data
    public static class CurrentDepts {
        /** 当前租户启用的部门 */
        private List<DeptItem> enabled = new ArrayList<>();
        /** 当前租户禁用的部门 */
        private List<DeptItem> disabled = new ArrayList<>();
    }

    /** <p>按租户分组的部门列表</p> */
    @Data
    public static class TenantDepts {
        /** 租户编码 */
        private String tenantCode;
        /** 租户名称 */
        private String tenantName;
        /** 该租户下的部门列表 */
        private List<DeptItem> depts = new ArrayList<>();
    }

}
