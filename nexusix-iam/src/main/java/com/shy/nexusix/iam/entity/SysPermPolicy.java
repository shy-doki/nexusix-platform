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
 * 权限策略表
 * </p>
 *
 * @author shy
 * @since 2026-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm_policy")
@Schema(name="SysPermPolicy对象", description="权限策略表")
public class SysPermPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "策略编码", example = "POLICY_001")
    @TableField(value = "policy_code")
    private String policyCode;

    @Schema(description = "策略名称", example = "用户数据读写权限")
    @TableField(value = "policy_name")
    private String policyName;

    @Schema(description = "授权目标ID", example = "100")
    @TableField(value = "target_id")
    private Long targetId;

    @Schema(description = "授权目标类型", example = "USER")
    @TableField(value = "target_type")
    private String targetType;

    @Schema(description = "关联权限ID", example = "1")
    @TableField(value = "perm_id")
    private Long permId;

    @Schema(description = "租户ID", example = "1")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "控制的数据表名", example = "sys_user")
    @TableField(value = "table_name")
    private String tableName;

    @Schema(description = "数据表描述", example = "系统用户表")
    @TableField(value = "table_desc")
    private String tableDesc;

    @Schema(description = "访问类型", example = "READ_WRITE")
    @TableField(value = "access_type")
    private String accessType;

    @Schema(description = "允许操作的字段，JSON格式", example = "[\"id\", \"name\", \"email\"]")
    @TableField(value = "field_operates")
    private String fieldOperates;

    @Schema(description = "策略状态", example = "ACTIVE")
    @TableField(value = "status")
    private String status;

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
