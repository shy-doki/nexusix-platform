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
 * 用户策略表 - 存储用户与租户的关联策略
 * </p>
 *
 * @author shy
 * @since 2026-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_policy")
@Schema(name = "SysUserPolicy对象", description = "用户策略表 - 存储用户与租户的关联策略")
public class SysUserPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户ID", example = "1001")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "策略编码", example = "UP_001")
    @TableField(value = "policy_code")
    private String policyCode;

    @Schema(description = "策略名称", example = "默认租户策略")
    @TableField(value = "policy_name")
    private String policyName;

    @Schema(description = "授权目标ID（系统租户ID）", example = "100")
    @TableField(value = "target_id")
    private Long targetId;

    @Schema(description = "授权目标类型", example = "USER")
    @TableField(value = "target_type")
    private String targetType;

    @Schema(description = "是否为该租户管理员", example = "false")
    @TableField(value = "is_admin")
    private Boolean isAdmin;

    @Schema(description = "是否默认租户", example = "true")
    @TableField(value = "is_default")
    private Boolean isDefault;

    @Schema(description = "加入租户时间", example = "2026-06-10 15:30:00")
    @TableField(value = "join_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime joinTime;

    @Schema(description = "策略状态 (ENABLED/DISABLED)", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "禁用原因", example = "租户管理员手动禁用")
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
