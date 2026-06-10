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
 * 角色表 - 存储租户下的角色定义
 * </p>
 *
 * @author shy
 * @since 2026-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@Schema(name = "SysRole对象", description = "角色表 - 存储租户下的角色定义")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "角色名称", example = "租户管理员")
    @TableField(value = "role_name")
    private String roleName;

    @Schema(description = "角色描述", example = "租户最高权限管理员")
    @TableField(value = "role_desc")
    private String roleDesc;

    @Schema(description = "角色编码", example = "TENANT_ADMIN")
    @TableField(value = "role_code")
    private String roleCode;

    @Schema(description = "角色层级", example = "TENANT")
    @TableField(value = "role_level")
    private String roleLevel;

    @Schema(description = "所属租户ID", example = "100")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "租户编码", example = "TENANT_001")
    @TableField(value = "tenant_code")
    private String tenantCode;

    @Schema(description = "租户名称", example = "某某科技有限公司")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "数据权限范围 (ALL/DEPT/DEPT_AND_SUB/SELF)", example = "ALL")
    @TableField(value = "data_scope")
    private String dataScope;

    @Schema(description = "排序序号", example = "1")
    @TableField(value = "sort_order")
    private Integer sortOrder;

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
