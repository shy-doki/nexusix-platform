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
 * 用户与租户的关联关系
 * </p>
 *
 * @author shy
 * @since 2026-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_tenant_rel")
@Schema(name="SysUserTenantRel对象", description="用户与租户的关联关系")
public class SysUserTenantRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户ID", example = "1")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "租户ID", example = "1")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "部门ID", example = "100")
    @TableField(value = "dept_id")
    private Long deptId;

    @Schema(description = "是否为该租户管理员", example = "false")
    @TableField(value = "is_admin")
    private Boolean isAdmin;

    @Schema(description = "加入租户时间", format = "date-time", example = "2026-05-19 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "join_time")
    private LocalDateTime joinTime;

    @Schema(description = "是否默认租户", example = "true")
    @TableField(value = "is_default")
    private Boolean isDefault;

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
