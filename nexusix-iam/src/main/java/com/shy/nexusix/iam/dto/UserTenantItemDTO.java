package com.shy.nexusix.iam.dto;

import lombok.Data;

/**
 * <p>
 * 用户关联的所有租户信息DTO，用于登录时查询用户所属全部租户
 * </p>
 *
 * @author shy
 * @since 2026-06-09
 */
@Data
public class UserTenantItemDTO {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 租户状态（ENABLED/DISABLED/EXPIRED/PENDING）
     */
    private String tenantStatus;

}
