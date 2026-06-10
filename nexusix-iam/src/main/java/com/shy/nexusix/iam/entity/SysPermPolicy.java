package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限策略表 - 存储权限的分配策略 (TENANT能力边界/ROLE角色权限/USER个人权限)
 * </p>
 *
 * @author shy
 * @since 2026-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm_policy")
@Schema(name = "SysPermPolicy对象", description = "权限策略表 - 存储权限的分配策略 (TENANT能力边界/ROLE角色权限/USER个人权限)")
public class SysPermPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "策略编码", example = "PP_001")
    @TableField(value = "policy_code")
    private String policyCode;

    @Schema(description = "策略名称", example = "租户基础权限策略")
    @TableField(value = "policy_name")
    private String policyName;

    @Schema(description = "授权目标ID（角色策略ID、用户策略ID、系统租户ID）", example = "100")
    @TableField(value = "target_id")
    private Long targetId;

    @Schema(description = "授权目标类型 (TENANT/ROLE/USER)", example = "TENANT")
    @TableField(value = "target_type")
    private String targetType;

    @Schema(description = "关联权限ID", example = "1001")
    @TableField(value = "perm_id")
    private Long permId;

    @Schema(description = "控制的数据表名", example = "sys_tenant")
    @TableField(value = "table_name")
    private String tableName;

    @Schema(description = "数据表描述", example = "租户信息表")
    @TableField(value = "table_desc")
    private String tableDesc;

    @Schema(description = "访问类型 (QUERY/CREATE/UPDATE)", example = "QUERY")
    @TableField(value = "access_type")
    private String accessType;

    @Schema(description = "允许操作的字段列表 (JSON数组)", example = "[\"tenantCode\",\"tenantName\"]")
    @TableField(value = "field_operates")
    private String fieldOperates;

    @Schema(description = "策略状态 (ACTIVE/DISABLED_SYSTEM_LEVEL/DISABLED_TENANT_LEVEL/DISABLED_ROLE_LEVEL/DISABLED_USER_LEVEL)", example = "ACTIVE")
    @TableField(value = "status")
    private String status;

    @Schema(description = "禁用原因", example = "系统管理员全局禁用")
    @TableField(value = "disable_reason")
    private String disableReason;

    @Schema(description = "创建人ID", example = "100")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @TableField(value = "create_at", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createAt;

    @Schema(description = "更新人ID", example = "100")
    @TableField(value = "update_by")
    private Long updateBy;

    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @TableField(value = "update_at", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateAt;

    @Schema(description = "逻辑删除标记 (NOT_DELETED/DELETED)", example = "NOT_DELETED")
    @TableField(value = "is_deleted")
    private String isDeleted;

    @Schema(description = "删除时间", example = "2026-04-07 15:45:30")
    @TableField(value = "deleted_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deletedAt;

}
