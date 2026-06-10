package com.shy.nexusix.core.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class UserContextDTO {

    /**
     * 租户信息，包含当前用户默认租户的基础信息
     */
    private TenantInfo tenantInfo;

    /**
     * 有效租户列表，状态为 ENABLED 的租户
     */
    private List<TenantItemInfo> validTenants;

    /**
     * 无效租户列表，状态为 DISABLED/EXPIRED/PENDING 的租户
     */
    private List<TenantItemInfo> invalidTenants;

    /**
     * 权限信息，包含用户全部权限编码、有效/无效权限、级联禁用详情及字段级权限
     */
    private PermInfo permInfo;

    /**
     * 角色信息列表，包含用户在当前登录租户下的角色编码及数据权限范围
     */
    private List<RoleInfo> roles;

    /**
     * <p>
     * 租户基础信息，缓存当前用户默认租户的标识与名称。
     * </p>
     *
     * <p>登录时从 sys_user_tenant_rel（默认租户关联）和 sys_tenant 表联合查询填充。</p>
     */
    @Data
    public static class TenantInfo {
        /**
         * 租户ID
         */
        private Long tenantId;
        /**
         * 租户编码，租户的唯一标识符
         */
        private String tenantCode;
        /**
         * 租户名称，用于前端展示
         */
        private String tenantName;
        /**
         * 租户状态（ENABLED/DISABLED/EXPIRED/PENDING）
         */
        private String tenantStatus;
    }

    /**
     * <p>
     * 租户列表项信息，用于缓存用户关联的所有租户。
     * </p>
     *
     * <p>登录时从 sys_user_tenant_rel 和 sys_tenant 表联合查询，按租户状态分类到 validTenants 或 invalidTenants。</p>
     */
    @Data
    public static class TenantItemInfo {
        /**
         * 租户ID
         */
        private Long tenantId;
        /**
         * 租户编码，租户的唯一标识符
         */
        private String tenantCode;
        /**
         * 租户名称，用于前端展示
         */
        private String tenantName;
        /**
         * 租户状态（ENABLED/DISABLED/EXPIRED/PENDING）
         */
        private String tenantStatus;
    }

    /**
     * <p>
     * 权限汇总信息，聚合用户全部权限数据。
     * </p>
     *
     * <p><b>字段说明：</b></p>
     * <ul>
     *   <li>perms - 全部权限编码列表（去重后），包含有效和无效权限</li>
     *   <li>validPerms - 有效权限编码列表，策略状态为 ACTIVE 的权限</li>
     *   <li>invalidPerms - 无效权限编码列表，被任一级别级联禁用的权限</li>
     *   <li>cascadeDisabled - 级联禁用详情，按系统/租户/角色/用户四级分类</li>
     *   <li>fieldPerm - 字段级权限配置，按查询/创建/更新操作分类</li>
     * </ul>
     */
    @Data
    public static class PermInfo {
        /**
         * 全部权限编码列表（已去重），包含有效和无效权限
         */
        private List<String> perms;
        /**
         * 有效权限编码列表，策略状态为 ACTIVE 且未被任何级别禁用的权限
         */
        private List<String> validPerms;
        /**
         * 无效权限编码列表，被系统/租户/角色/用户任一级别级联禁用的权限
         */
        private List<String> invalidPerms;
        /**
         * 级联禁用详情，按四级（系统/租户/角色/用户）分类记录被禁用的权限编码
         */
        private CascadeDisabled cascadeDisabled;
        /**
         * 字段级权限配置，按操作类型（查询/创建/更新）组织各数据表的字段可见性
         */
        private FieldPerm fieldPerm;
    }

    /**
     * <p>
     * 级联禁用详情，按四级分类记录被禁用的权限编码。
     * </p>
     *
     * <p><b>级联禁用优先级：</b>系统级 > 租户级 > 角色级 > 用户级</p>
     * <p>高级别的禁用不可被低级别覆盖，即系统级禁用的权限在租户/角色/用户层面无法恢复。</p>
     *
     * <p><b>用途：</b>前端可据此展示权限被禁用的具体原因和层级，便于权限审计与排查。</p>
     */
    @Data
    public static class CascadeDisabled {
        /**
         * 系统级禁用的权限编码列表，由系统管理员在系统层面直接禁用，优先级最高
         */
        private List<String> systemDisabled;
        /**
         * 租户级禁用的权限编码列表，由租户管理员在租户层面禁用
         */
        private List<String> tenantDisabled;
        /**
         * 角色级禁用的权限编码列表，由角色配置层面禁用
         */
        private List<String> roleDisabled;
        /**
         * 用户级禁用的权限编码列表，对特定用户单独禁用的权限，优先级最低
         */
        private List<String> userDisabled;
    }

    /**
     * <p>
     * 字段级权限配置，按操作类型组织各数据表的字段可见性。
     * </p>
     *
     * <p><b>数据结构：</b></p>
     * <p>每个 Map 的 key 为数据表名（如 "sys_tenant"），value 为该表在对应操作下的字段权限配置。</p>
     *
     * <p><b>使用场景：</b></p>
     * <ul>
     *   <li>query - 控制查询接口返回哪些字段列</li>
     *   <li>create - 控制创建接口允许提交哪些字段</li>
     *   <li>update - 控制更新接口允许修改哪些字段</li>
     * </ul>
     */
    @Data
    public static class FieldPerm {
        /**
         * 查询操作的字段权限映射，key 为表名，value 为该表的字段可见/不可见配置
         */
        private Map<String, EntityFieldPerm> query;
        /**
         * 创建操作的字段权限映射，key 为表名，value 为该表的字段可见/不可见配置
         */
        private Map<String, EntityFieldPerm> create;
        /**
         * 更新操作的字段权限映射，key 为表名，value 为该表的字段可见/不可见配置
         */
        private Map<String, EntityFieldPerm> update;
    }

    /**
     * <p>
     * 实体字段权限配置，定义单个数据表在特定操作下的字段可见性规则。
     * </p>
     *
     * <p><b>设计逻辑：</b></p>
     * <ul>
     *   <li>visibleFields - 策略状态为 ACTIVE 时配置的可见字段列表，表示用户有权查看/操作的字段</li>
     *   <li>invisibleFields - 策略状态为非 ACTIVE（各级别禁用）时配置的不可见字段列表，
     *       表示用户无权查看/操作的字段</li>
     * </ul>
     *
     * <p><b>使用示例：</b></p>
     * <p>若用户对 sys_tenant 表的查询操作有字段权限配置：
     * visibleFields = ["tenantCode", "tenantName"]，invisibleFields = ["contactPhone"]，
     * 则查询租户列表时仅返回 tenantCode 和 tenantName 字段，contactPhone 字段被过滤。</p>
     */
    @Data
    public static class EntityFieldPerm {
        /**
         * 可见字段列表，策略状态为 ACTIVE 时配置，表示用户有权访问的字段名
         */
        private List<String> visibleFields = new ArrayList<>();
        /**
         * 不可见字段列表，策略状态为非 ACTIVE（各级别禁用）时配置，表示用户无权访问的字段名
         */
        private List<String> invisibleFields = new ArrayList<>();
    }

    /**
     * <p>
     * 角色信息，缓存用户在当前登录租户下的角色编码及数据权限范围。
     * </p>
     *
     * <p><b>用途：</b>前端可据此判断用户角色类型，后端可用于数据权限过滤。</p>
     * <p><b>数据来源：</b>登录时从 sys_user_role_rel 和 sys_role 联合查询，筛选角色所属租户与当前登录租户一致。</p>
     */
    @Data
    public static class RoleInfo {
        /**
         * 角色编码，如 TENANT_ADMIN、EMPLOYEE、AUDITOR
         */
        private String roleCode;
        /**
         * 数据权限范围：ALL / DEPT / DEPT_AND_SUB / SELF
         */
        private String dataScope;
    }

}
