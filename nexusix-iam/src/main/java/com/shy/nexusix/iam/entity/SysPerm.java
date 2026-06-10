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
 * 权限表 - 存储系统权限资源定义
 * </p>
 *
 * @author shy
 * @since 2026-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm")
@Schema(name = "SysPerm对象", description = "权限表 - 存储系统权限资源定义")
public class SysPerm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "权限名称", example = "用户管理")
    @TableField(value = "perm_name")
    private String permName;

    @Schema(description = "权限描述", example = "系统用户管理权限")
    @TableField(value = "perm_desc")
    private String permDesc;

    @Schema(description = "权限编码", example = "SYS_USER")
    @TableField(value = "perm_code")
    private String permCode;

    @Schema(description = "权限标识", example = "sys:user:list")
    @TableField(value = "perm_key")
    private String permKey;

    @Schema(description = "权限类型", example = "MENU")
    @TableField(value = "perm_type")
    private String permType;

    @Schema(description = "父权限ID (0为顶级)", example = "0")
    @TableField(value = "parent_id")
    private Long parentId;

    @Schema(description = "父权限名称", example = "系统管理")
    @TableField(value = "parent_name")
    private String parentName;

    @Schema(description = "权限路径", example = "/system/user")
    @TableField(value = "path")
    private String path;

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
