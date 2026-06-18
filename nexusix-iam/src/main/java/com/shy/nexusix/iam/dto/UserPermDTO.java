package com.shy.nexusix.iam.dto;

import lombok.Data;

/**
 * <p>用户权限关联DTO，查询用户在各租户下的权限信息</p>
 *
 * @author shy
 */
@Data
public class UserPermDTO {

    /** 权限编码 */
    private String permCode;

    /** 权限名称 */
    private String permName;

    /** 权限类型：MENU, BUTTON, API, DATA */
    private String permType;

    /** 租户编码 */
    private String tenantCode;

    /** 租户名称 */
    private String tenantName;

    /** 权限策略状态（ACTIVE/DISABLED） */
    private String permPolicyStatus;

    /** 目标类型：TENANT, DEPT, ROLE, USER */
    private String targetType;

    /** 数据范围：ALL, DEPT_AND_SUB, DEPT, SELF */
    private String dataScope;

    /** 关联的数据表名 */
    private String tableName;

    /** 字段级权限配置（JSON字符串） */
    private String fieldPermissions;

    /** 租户用户ID */
    private Long tenantUserId;
}
