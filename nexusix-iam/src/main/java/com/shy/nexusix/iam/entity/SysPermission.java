package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限/资源表 - 定义系统所有可授权资源
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
@Schema(name="SysPermission对象", description="权限/资源表 - 定义系统所有可授权资源")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "权限名称", example = "用户管理")
    @TableField(value = "perm_name")
    private String permName;

    @Schema(description = "权限标识 (如 system:user:add)", example = "system:user:add")
    @TableField(value = "perm_code")
    private String permCode;

    @Schema(description = "类型 (1-菜单 2-按钮 3-接口 4-数据字段)", example = "1")
    @TableField(value = "perm_type")
    private Integer permType;

    @Schema(description = "父权限 ID", example = "0")
    @TableField(value = "parent_id")
    private Long parentId;

    @Schema(description = "父权限名称", example = "用户管理")
    @TableField(value = "parent_name")
    private String parentName;

    @Schema(description = "资源路径", example = "/system/user")
    @TableField(value = "path")
    private String path;

    @Schema(description = "状态 (1-正常 0-禁用)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "创建人 ID", example = "100")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "创建人姓名(新增、更新、删除操作需要同步该字段)", example = "张三")
    @TableField(value = "create_by_name")
    private String createByName;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "更新人 ID", example = "100")
    @TableField(value = "update_by")
    private Long updateBy;

    @Schema(description = "更新人姓名(新增、更新、删除操作需要同步该字段)", example = "张三")
    @TableField(value = "update_by_name")
    private String updateByName;

    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
