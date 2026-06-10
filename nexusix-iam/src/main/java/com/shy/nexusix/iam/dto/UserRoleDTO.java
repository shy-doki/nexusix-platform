package com.shy.nexusix.iam.dto;

import lombok.Data;

/**
 * <p>
 * 用户角色信息DTO，用于登录时查询用户在当前租户下的角色数据
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

}