package com.shy.nexusix.core.entity.dto;

import lombok.Data;

@Data
public class UserPermDetailDTO {

    // ===== sys_perm =====
    private Long permId;
    private String permCode;
    private String permName;

    // ===== sys_perm_policy =====
    private Long policyId;
    private Long policyPermId;
    private String policyStatus;
    private String policyTableName;
    private String policyAccessType;
    private String policyFieldOperates;

    // ===== sys_user_perm_rel =====
    private Long relId;

}
