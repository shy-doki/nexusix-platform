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
 * <p>角色表，系统级角色实体（全局）</p>
 *
 * @author shy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@Schema(name = "SysRole对象", description = "角色表：系统级角色实体（全局）")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(description = "主键ID（系统角色ID）", example = "401")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 角色编码 */
    @Schema(description = "角色编码", example = "SUPER_ADMIN")
    @TableField(value = "role_code")
    private String roleCode;

    /** 角色名称 */
    @Schema(description = "角色名称", example = "超级管理员")
    @TableField(value = "role_name")
    private String roleName;

    /** 角色描述 */
    @Schema(description = "角色描述")
    @TableField(value = "role_desc")
    private String roleDesc;

    /** 状态：ENABLED, DISABLED */
    @Schema(description = "状态：ENABLED, DISABLED", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    /** 禁用原因 */
    @Schema(description = "禁用原因")
    @TableField(value = "disable_reason")
    private String disableReason;

    /** 创建时所属租户ID */
    @Schema(description = "创建时所属租户ID", example = "0")
    @TableField(value = "create_tenant")
    private Long createTenant;

    /** 创建时所属部门ID */
    @Schema(description = "创建时所属部门ID", example = "0")
    @TableField(value = "create_dept")
    private Long createDept;

    /** 创建时使用角色ID */
    @Schema(description = "创建时使用角色ID", example = "0")
    @TableField(value = "create_role")
    private Long createRole;

    /** 创建人用户ID */
    @Schema(description = "创建人用户ID", example = "1")
    @TableField(value = "create_by")
    private Long createBy;

    /** 创建时间 */
    @Schema(description = "创建时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_at")
    private LocalDateTime createAt;

    /** 最后更新人用户ID */
    @Schema(description = "最后更新人用户ID", example = "1")
    @TableField(value = "update_by")
    private Long updateBy;

    /** 最后更新时间 */
    @Schema(description = "最后更新时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_at")
    private LocalDateTime updateAt;

    /** 逻辑删除标记 */
    @Schema(description = "逻辑删除标记", example = "NOT_DELETED")
    @TableField(value = "is_deleted")
    private String isDeleted;

    /** 删除时间 */
    @Schema(description = "删除时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;

}
