package com.shy.nexusix.iam.dto;

import lombok.Data;

@Data
public class UserPermJoinDTO {

    /**
     * sys_perm_policy.id 权限策略主键，备用
     */
    private Long policyId;

    /**
     * sys_perm.id 权限资源主键，用于去重和分类
     */
    private Long permId;

    /**
     * sys_perm_policy.status 策略状态
     */
    private String policyStatus;

    /**
     * sys_perm_policy.access_type 字段访问类型
     */
    private String accessType;

    /**
     * sys_perm_policy.field_operates 允许操作的字段 JSON 数组
     */
    private String fieldOperates;

    /**
     * sys_perm_policy.table_name 控制的数据表名
     */
    private String tableName;

    /**
     * sys_perm.perm_code 权限编码，如 "SYS_USER"
     */
    private String permCode;

}
