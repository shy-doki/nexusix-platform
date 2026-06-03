package com.shy.nexusix.iam.dto;

import lombok.Data;

@Data
public class UserLoginJoinDTO {

    /**
     * sys_user.id 用户主键ID，用于 Sa-Token 登录
     */
    private Long userId;

    /**
     * sys_user.password 加密密码，用于密码比对
     */
    private String password;

    /**
     * sys_user_tenant_rel.id 用户-租户关联关系ID
     * 该ID将被用作第二次查询中 sys_user_perm_rel.user_id 的查询条件
     */
    private Long userTenantRelId;

    /**
     * sys_tenant.id 租户ID
     * 备用字段，后续扩展可能需要
     */
    private Long tenantId;

    /**
     * sys_tenant.tenant_name 租户名称，用于缓存到 UserContext.TenantInfo
     */
    private String tenantName;

    /**
     * sys_tenant.tenant_code 租户编码，用于缓存到 UserContext.TenantInfo
     */
    private String tenantCode;

    /**
     * sys_tenant.status 租户状态，用于校验是否停用/过期
     */
    private String tenantStatus;

}
