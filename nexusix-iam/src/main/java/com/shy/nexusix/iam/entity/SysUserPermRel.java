package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户与权限策略直接关联表
 * </p>
 *
 * @author shy
 * @since 2026-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_perm_rel")
@Schema(name="SysUserPermRel对象", description="用户与权限策略直接关联表")
public class SysUserPermRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户ID", example = "1")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "权限策略ID", example = "1")
    @TableField(value = "policy_id")
    private Long policyId;

    @Schema(description = "创建人ID", example = "100")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "创建时间", format = "date-time", example = "2026-05-19 15:45:30")
    @TableField(value = "create_at")
    private LocalDateTime createAt;

    @Schema(description = "更新人ID", example = "100")
    @TableField(value = "update_by")
    private String updateBy;

    @Schema(description = "更新时间", format = "date-time", example = "2026-05-19 15:45:30")
    @TableField(value = "update_at")
    private LocalDateTime updateAt;

    @Schema(description = "逻辑删除", example = "NOT_DELETED")
    @TableField(value = "is_deleted")
    private String isDeleted;

    @Schema(description = "删除时间", example = "2026-05-19 15:45:30")
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;


}
