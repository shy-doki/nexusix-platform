package com.shy.nexusix.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门策略表
 * </p>
 *
 * @author shy
 * @since 2026-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dept_policy")
@Schema(name = "SysDeptPolicy对象", description = "部门策略表")
public class SysDeptPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "策略编码", example = "POLICY_ADMIN")
    @TableField(value = "policy_code")
    private String policyCode;

    @Schema(description = "策略名称", example = "管理员策略")
    @TableField(value = "policy_name")
    private String policyName;

    @Schema(description = "授权目标ID（部门ID）", example = "1001")
    @TableField(value = "target_id")
    private Long targetId;

    @Schema(description = "授权目标类型", example = "DEPT")
    @TableField(value = "target_type")
    private String targetType;

    @Schema(description = "用户ID", example = "2001")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "是否主部门", example = "true")
    @TableField(value = "is_primary")
    private Boolean isPrimary;

    @Schema(description = "加入部门时间", example = "2026-06-11 09:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "join_time")
    private LocalDateTime joinTime;

    @Schema(description = "策略状态", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "禁用原因", example = "EXPIRED")
    @TableField(value = "disable_reason")
    private String disableReason;

    @Schema(description = "创建时所属租户ID", example = "1001")
    @TableField(value = "create_tenant")
    private Long createTenant;

    @Schema(description = "创建时所属部门ID", example = "100")
    @TableField(value = "create_dept")
    private Long createDept;

    @Schema(description = "创建时使用角色ID", example = "3001")
    @TableField(value = "create_role")
    private Long createRole;

    @Schema(description = "创建人ID", example = "4001")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "创建时间", example = "2026-06-11 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_at")
    private LocalDateTime createAt;

    @Schema(description = "更新人ID", example = "4001")
    @TableField(value = "update_by")
    private Long updateBy;

    @Schema(description = "更新时间", example = "2026-06-11 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_at")
    private LocalDateTime updateAt;

    @Schema(description = "逻辑删除标记", example = "NOT_DELETED")
    @TableField(value = "is_deleted")
    private String isDeleted;

    @Schema(description = "删除时间", example = "2026-06-11 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;
}