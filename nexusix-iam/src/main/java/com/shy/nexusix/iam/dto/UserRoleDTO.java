package com.shy.nexusix.iam.dto;

import lombok.Data;

/**
 * <p>用户角色DTO，查询用户在各租户下的角色信息</p>
 *
 * @author shy
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

    /** 数据范围（ALL/DEPT_AND_SUB/DEPT/SELF） */
    private String dataScope;

    /** 租户用户ID */
    private Long tenantUserId;
}
