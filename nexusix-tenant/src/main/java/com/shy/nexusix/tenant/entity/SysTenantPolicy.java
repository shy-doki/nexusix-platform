package com.shy.nexusix.tenant.entity;

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
 * <p>租户策略实体，管理部门/角色与租户的绑定关系</p>
 *
 * @author shy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tenant_policy")
@Schema(name = "SysTenantPolicy对象", description = "租户策略表：将系统部门/角色绑定到租户")
public class SysTenantPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID（租户部门ID或租户角色ID）", example = "2001")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "策略编码", example = "TP_DEPT_001")
    @TableField(value = "policy_code")
    private String policyCode;

    @Schema(description = "策略名称", example = "财务部→万象集团")
    @TableField(value = "policy_name")
    private String policyName;

    @Schema(description = "源实体类型：DEPT, ROLE", example = "DEPT")
    @TableField(value = "source_type")
    private String sourceType;

    @Schema(description = "系统实体ID（系统部门ID或系统角色ID）", example = "301")
    @TableField(value = "source_id")
    private Long sourceId;

    @Schema(description = "目标租户ID（系统租户ID）", example = "101")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "绑定时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "bind_time")
    private LocalDateTime bindTime;

    @Schema(description = "策略状态：ACTIVE, DISABLED", example = "ACTIVE")
    @TableField(value = "status")
    private String status;

    @Schema(description = "禁用原因")
    @TableField(value = "disable_reason")
    private String disableReason;

    @Schema(description = "创建时所属租户ID")
    @TableField(value = "create_tenant")
    private Long createTenant;

    @Schema(description = "创建时所属部门ID")
    @TableField(value = "create_dept")
    private Long createDept;

    @Schema(description = "创建时使用角色ID")
    @TableField(value = "create_role")
    private Long createRole;

    @Schema(description = "创建人用户ID", example = "1")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "创建时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_at")
    private LocalDateTime createAt;

    @Schema(description = "最后更新人用户ID", example = "1")
    @TableField(value = "update_by")
    private Long updateBy;

    @Schema(description = "最后更新时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_at")
    private LocalDateTime updateAt;

    @Schema(description = "逻辑删除标记", example = "NOT_DELETED")
    @TableField(value = "is_deleted")
    private String isDeleted;

    @Schema(description = "删除时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;

}
