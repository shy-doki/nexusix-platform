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
 * <p>权限策略表，权限授予租户/部门/角色/用户</p>
 *
 * @author shy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm_policy")
@Schema(name = "SysPermPolicy对象", description = "权限策略表：权限授予租户/部门/角色/用户")
public class SysPermPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(description = "主键ID（系统权限授予租户时作为租户权限ID）", example = "5001")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 策略编码 */
    @Schema(description = "策略编码", example = "PP_001")
    @TableField(value = "policy_code")
    private String policyCode;

    /** 策略名称 */
    @Schema(description = "策略名称", example = "用户管理权限→万象集团")
    @TableField(value = "policy_name")
    private String policyName;

    /** 权限ID */
    @Schema(description = "权限ID（系统权限ID或租户权限ID）", example = "511")
    @TableField(value = "perm_id")
    private Long permId;

    /** 目标类型：TENANT, DEPT, ROLE, USER */
    @Schema(description = "目标类型：TENANT, DEPT, ROLE, USER", example = "TENANT")
    @TableField(value = "target_type")
    private String targetType;

    /** 目标ID */
    @Schema(description = "目标ID", example = "101")
    @TableField(value = "target_id")
    private Long targetId;

    /** 数据范围：ALL, DEPT_AND_SUB, DEPT, SELF */
    @Schema(description = "行级权限（数据范围）：ALL, DEPT_AND_SUB, DEPT, SELF", example = "ALL")
    @TableField(value = "data_scope")
    private String dataScope;

    /** 关联的数据表名 */
    @Schema(description = "关联的数据表名（用于字段级权限）", example = "sys_user")
    @TableField(value = "table_name")
    private String tableName;

    /** 字段级权限配置 */
    @Schema(description = "字段级权限配置 {\"field_name\": [\"READ\",\"CREATE\",\"UPDATE\",\"DELETE\"]}")
    @TableField(value = "field_operation")
    private String fieldPermissions;

    /** 策略状态：ACTIVE, DISABLED */
    @Schema(description = "策略状态：ACTIVE, DISABLED", example = "ACTIVE")
    @TableField(value = "status")
    private String status;

    /** 禁用原因 */
    @Schema(description = "禁用原因")
    @TableField(value = "disable_reason")
    private String disableReason;

    /** 创建时所属租户ID */
    @Schema(description = "创建时所属租户ID")
    @TableField(value = "create_tenant")
    private Long createTenant;

    /** 创建时所属部门ID */
    @Schema(description = "创建时所属部门ID")
    @TableField(value = "create_dept")
    private Long createDept;

    /** 创建时使用角色ID */
    @Schema(description = "创建时使用角色ID")
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
