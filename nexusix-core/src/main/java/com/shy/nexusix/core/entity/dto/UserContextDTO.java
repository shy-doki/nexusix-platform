package com.shy.nexusix.core.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>用户上下文DTO，登录时构建并存入Sa-Token Session</p>
 *
 * <pre>
 * 结构概览：
 *   currentTenant     — 当前登录租户基础信息
 *   tenants           — 租户分组（全部/有效/无效）
 *   permissions       — 权限汇总（全部/有效/无效 + 级联禁用详情 + 字段级权限）
 *   roles             — 角色分组（当前租户/全部/有效/无效），每条角色标注所属租户
 * </pre>
 *
 * @author shy
 * @since 2026-06-11
 */
@Data
public class UserContextDTO {

    /** 当前登录租户信息 */
    private TenantInfo currentTenant;

    /** 租户分组（全部/有效/无效） */
    private TenantGroup tenants;

    /** 权限汇总信息 */
    private PermissionInfo permissions;

    /** 角色分组（当前租户/全部/有效/无效），含租户标注 */
    private RoleGroup roles;

    /** 部门分组（当前租户/全部/有效/无效），含租户标注 */
    private DeptGroup depts;

    // ==================== 租户相关 ====================

    @Data
    public static class TenantInfo {
        /** 租户编码 */
        private String tenantCode;
        /** 租户名称 */
        private String tenantName;
        /** 租户状态（ENABLED/DISABLED/EXPIRED/PENDING） */
        private String status;
    }

    @Data
    public static class TenantItem {
        /** 租户编码 */
        private String tenantCode;
        /** 租户名称 */
        private String tenantName;
        /** 租户状态（ENABLED/DISABLED/EXPIRED/PENDING） */
        private String status;
        /** 是否主租户 */
        private Boolean isPrimary;
    }

    @Data
    public static class TenantGroup {
        /** 全部租户（有效+无效） */
        private List<TenantItem> all;
        /** 有效租户（用户策略ENABLED 且 租户ENABLED） */
        private List<TenantItem> valid;
        /** 无效租户（用户策略或租户状态非ENABLED） */
        private List<TenantItem> invalid;
    }

    // ==================== 权限相关 ====================

    @Data
    public static class PermissionInfo {
        /** 全部权限编码（有效+无效，已去重） */
        private List<String> all;
        /** 有效权限编码（策略ACTIVE且未被任何级别禁用） */
        private List<String> valid;
        /** 无效权限编码（被系统/租户/角色/用户任一级别禁用） */
        private List<String> invalid;
        /** 级联禁用详情，按四级分类 */
        private DisabledDetail disabledDetail;
        /** 字段级权限，按操作类型分组 */
        private FieldPermission fieldPermission;
    }

    @Data
    public static class DisabledDetail {
        /** 系统级禁用的权限编码 */
        private List<String> system;
        /** 租户级禁用的权限编码 */
        private List<String> tenant;
        /** 角色级禁用的权限编码 */
        private List<String> role;
        /** 用户级禁用的权限编码 */
        private List<String> user;
    }

    @Data
    public static class FieldPermission {
        /** 查询操作的字段权限，key=表名 */
        private Map<String, TableFieldPermission> query;
        /** 创建操作的字段权限，key=表名 */
        private Map<String, TableFieldPermission> create;
        /** 更新操作的字段权限，key=表名 */
        private Map<String, TableFieldPermission> update;
    }

    @Data
    public static class TableFieldPermission {
        /** 可操作字段列表（策略ACTIVE时配置） */
        private List<String> operable = new ArrayList<>();
        /** 不可操作字段列表（策略非ACTIVE时配置） */
        private List<String> inoperable = new ArrayList<>();
    }

    // ==================== 角色相关 ====================

    @Data
    public static class RoleItem {
        /** 角色编码 */
        private String roleCode;
        /** 数据权限范围（ALL/DEPT/DEPT_AND_SUB/SELF） */
        private String dataScope;
        /** 所属租户编码 */
        private String tenantCode;
        /** 所属租户名称 */
        private String tenantName;
    }

    @Data
    public static class RoleGroup {
        /** 当前登录租户下的角色列表 */
        private List<RoleItem> current = new ArrayList<>();
        /** 全部角色（跨所有租户，含有效+无效） */
        private List<RoleItem> all = new ArrayList<>();
        /** 有效角色（角色策略状态为ACTIVE） */
        private List<RoleItem> valid = new ArrayList<>();
        /** 无效角色（角色策略状态非ACTIVE） */
        private List<RoleItem> invalid = new ArrayList<>();
    }

    // ==================== 部门相关 ====================

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

    @Data
    public static class DeptGroup {
        /** 当前登录租户下的部门列表 */
        private List<DeptItem> current = new ArrayList<>();
        /** 全部部门（跨所有租户，含有效+无效） */
        private List<DeptItem> all = new ArrayList<>();
        /** 有效部门（用户策略状态为ACTIVE） */
        private List<DeptItem> valid = new ArrayList<>();
        /** 无效部门（用户策略状态非ACTIVE） */
        private List<DeptItem> invalid = new ArrayList<>();
    }

}