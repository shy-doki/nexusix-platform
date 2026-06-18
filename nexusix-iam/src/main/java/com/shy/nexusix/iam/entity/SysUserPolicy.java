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
 * <p>用户策略表，用户加入租户、绑定部门和角色</p>
 *
 * @author shy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_policy")
@Schema(name = "SysUserPolicy对象", description = "用户策略表：用户加入租户、用户绑定部门和角色")
public class SysUserPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(description = "主键ID（绑定租户时作为租户用户ID）", example = "4001")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 策略编码 */
    @Schema(description = "策略编码", example = "UP_001")
    @TableField(value = "policy_code")
    private String policyCode;

    /** 策略名称 */
    @Schema(description = "策略名称", example = "张三→万象集团")
    @TableField(value = "policy_name")
    private String policyName;

    /** 用户ID */
    @Schema(description = "用户ID（系统用户ID或租户用户ID）", example = "2")
    @TableField(value = "user_id")
    private Long userId;

    /** 目标类型：TENANT, DEPT, ROLE */
    @Schema(description = "目标类型：TENANT, DEPT, ROLE", example = "TENANT")
    @TableField(value = "target_type")
    private String targetType;

    /** 目标ID */
    @Schema(description = "目标ID（系统租户ID/租户部门ID/租户角色ID）", example = "101")
    @TableField(value = "target_id")
    private Long targetId;

    /** 是否为主租户/主部门 */
    @Schema(description = "是否为主租户/主部门（仅TENANT和DEPT类型有效）", example = "true")
    @TableField(value = "is_primary")
    private Boolean isPrimary;

    /** 加入时间 */
    @Schema(description = "加入时间", format = "date-time", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "join_time")
    private LocalDateTime joinTime;

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
