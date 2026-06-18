package com.shy.nexusix.iam.dto;

import lombok.Data;

/**
 * <p>用户部门关联DTO，查询用户在各租户下的部门信息</p>
 *
 * @author shy
 */
@Data
public class UserDeptDTO {

    /** 部门编码 */
    private String deptCode;

    /** 部门名称 */
    private String deptName;

    /** 部门路径 */
    private String path;

    /** 层级深度 */
    private Integer level;

    /** 租户编码 */
    private String tenantCode;

    /** 租户名称 */
    private String tenantName;

    /** 是否主部门 */
    private Boolean isPrimary;

    /** 用户策略状态 */
    private String userPolicyStatus;

    /** 租户用户ID */
    private Long tenantUserId;
}
