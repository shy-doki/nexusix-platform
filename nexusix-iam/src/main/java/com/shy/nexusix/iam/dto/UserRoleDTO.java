package com.shy.nexusix.iam.dto;

import lombok.Data;

/**
 * <p>用户角色DTO - 用于查询用户在各租户下的角色信息</p>
 *
 * @author shy
 * @since 2026-06-12
 */
@Data
public class UserRoleDTO {

    /** 角色编码 */
    private String roleCode;

    /** 角色名称 */
    private String roleName;

    /** 租户编码 */
    private String tenantCode;

    /** 租户名称 */
    private String tenantName;

    /** 角色策略状态（ACTIVE/DISABLED） */
    private String rolePolicyStatus;

    /** 租户用户ID */
    private Long tenantUserId;
}
