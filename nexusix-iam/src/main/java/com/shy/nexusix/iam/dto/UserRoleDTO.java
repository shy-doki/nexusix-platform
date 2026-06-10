package com.shy.nexusix.iam.dto;

import lombok.Data;

/**
 * <p>
 * 用户角色信息DTO，用于登录时查询用户在当前租户下或全部租户下的角色数据
 * </p>
 *
 * @author shy
 * @since 2026-06-10
 */
@Data
public class UserRoleDTO {

    /**
     * sys_role.role_code 角色编码，如 TENANT_ADMIN、EMPLOYEE
     */
    private String roleCode;

    /**
     * sys_role.data_scope 数据权限范围，如 ALL / DEPT / DEPT_AND_SUB / SELF
     */
    private String dataScope;

    /**
     * 所属租户ID
     */
    private Long tenantId;

    /**
     * 所属租户编码
     */
    private String tenantCode;

    /**
     * 所属租户名称
     */
    private String tenantName;

    /**
     * 角色策略ID（sys_role_policy.id）
     */
    private Long rolePolicyId;

    /**
     * 角色策略状态（ACTIVE/DISABLED），用于判定该角色分配是否有效
     */
    private String rolePolicyStatus;

}