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
 * 权限表：系统级权限实体（全局）
 * </p>
 *
 * @author shy
 * @since 2026-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm")
@Schema(name = "SysPerm对象", description = "权限表：系统级权限实体（全局）")
public class SysPerm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID（系统权限ID）", example = "501")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "权限编码", example = "PERM_USER_MANAGE")
    @TableField(value = "perm_code")
    private String permCode;

    @Schema(description = "权限名称", example = "用户管理")
    @TableField(value = "perm_name")
    private String permName;

    @Schema(description = "权限描述")
    @TableField(value = "perm_desc")
    private String permDesc;

    @Schema(description = "权限类型：MENU, BUTTON, API, DATA", example = "MENU")
    @TableField(value = "perm_type")
    private String permType;

    @Schema(description = "父权限ID，0表示根权限", example = "0")
    @TableField(value = "parent_id")
    private Long parentId;

    @Schema(description = "权限层级路径", example = "/501")
    @TableField(value = "path")
    private String path;

    @Schema(description = "层级深度", example = "1")
    @TableField(value = "level")
    private Integer level;

    @Schema(description = "资源类型：URL, METHOD, TABLE等")
    @TableField(value = "resource_type")
    private String resourceType;

    @Schema(description = "资源路径（如API路径、表名）")
    @TableField(value = "resource_path")
    private String resourcePath;

    @Schema(description = "资源方法（如GET, POST, PUT, DELETE）")
    @TableField(value = "resource_method")
    private String resourceMethod;

    @Schema(description = "图标")
    @TableField(value = "icon")
    private String icon;

    @Schema(description = "排序序号", example = "0")
    @TableField(value = "sort_order")
    private Integer sortOrder;

    @Schema(description = "是否可见", example = "true")
    @TableField(value = "is_visible")
    private Boolean isVisible;

    @Schema(description = "状态：ENABLED, DISABLED", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "禁用原因")
    @TableField(value = "disable_reason")
    private String disableReason;

    @Schema(description = "创建时所属租户ID", example = "0")
    @TableField(value = "create_tenant")
    private Long createTenant;

    @Schema(description = "创建时所属部门ID", example = "0")
    @TableField(value = "create_dept")
    private Long createDept;

    @Schema(description = "创建时使用角色ID", example = "0")
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
