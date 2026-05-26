package com.shy.nexusix.iam.dto;

import lombok.Data;

@Data
public class LoginUserTenantDTO {

    // ===== sys_user 字段 =====
    private Long userId;
    private String userCode;
    private String userName;
    private String password;
    private String nickName;
    private String email;
    private String phone;
    private String avatar;
    private String userStatus;

    // ===== sys_user_tenant_rel 字段 =====
    private Long relId;
    private Long relUserId;
    private Long relTenantId;
    private Long relDeptId;
    private Boolean relIsAdmin;
    private Boolean relIsDefault;

    // ===== sys_tenant 字段 =====
    private Long tenantId;
    private String tenantCode;
    private String tenantName;
    private String tenantStatus;

}
