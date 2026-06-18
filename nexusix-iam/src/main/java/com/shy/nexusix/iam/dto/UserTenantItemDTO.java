package com.shy.nexusix.iam.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>用户租户关联DTO，查询用户所有租户信息</p>
 *
 * @author shy
 */
@Data
public class UserTenantItemDTO {

    /** 租户编码 */
    private String tenantCode;

    /** 租户名称 */
    private String tenantName;

    /** 租户状态 */
    private String status;

    /** 是否主租户 */
    private Boolean isPrimary;

    /** 服务过期时间 */
    private LocalDateTime expireTime;

    /** 租户用户ID */
    private Long tenantUserId;

    /** 系统租户ID */
    private Long tenantId;
}
