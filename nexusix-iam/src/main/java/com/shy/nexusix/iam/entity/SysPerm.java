package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限/资源表-定义系统所有可授权资源
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm")
@Schema(name="SysPerm对象", description="权限/资源表-定义系统所有可授权资源")
public class SysPerm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "权限名称", example = "用户管理")
    @TableField(value = "perm_name")
    private String permName;

    @Schema(description = "权限描述", example = "用户管理权限")
    @TableField(value = "perm_desc")
    private String permDesc;

    @Schema(description = "权限标识", example = "system:user:manage")
    @TableField(value = "perm_code")
    private String permCode;

    @Schema(description = "权限键", example = "system:user:manage")
    @TableField(value = "perm_key")
    private String permKey;

    @Schema(description = "类型", example = "MENU")
    @TableField(value = "perm_type")
    private String permType;

    @Schema(description = "父权限ID", example = "1000")
    @TableField(value = "parent_id")
    private String parentId;

    @Schema(description = "父权限名称", example = "系统管理")
    @TableField(value = "parent_name")
    private String parentName;

    @Schema(description = "资源路径", example = "/system/user")
    @TableField(value = "path")
    private String path;

    @Schema(description = "关联数据库表名", example = "sys_user")
    @TableField(value = "table_name")
    private String tableName;

    @Schema(description = "关联数据库表名描述", example = "用户管理")
    @TableField(value = "table_desc")
    private String tableDesc;

    @Schema(description = "关联数据库字段名", example = "user_name")
    @TableField(value = "field_name")
    private String fieldName;

    @Schema(description = "关联数据库字段描述", example = "用户名")
    @TableField(value = "field_desc")
    private String fieldDesc;

    @Schema(description = "操作类型", example = "CREATE")
    @TableField(value = "operation_type")
    private String operationType;

    @Schema(description = "状态", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "创建人ID", example = "100")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "创建人姓名", example = "张三")
    @TableField(value = "create_by_name")
    private String createByName;

    @Schema(description = "更新人ID", example = "100")
    @TableField(value = "update_by")
    private String updateBy;

    @Schema(description = "更新人姓名", example = "张三")
    @TableField(value = "update_by_name")
    private String updateByName;

    @Schema(description = "创建时间", format = "date-time", example = "2026-05-13 15:45:30")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", format = "date-time", example = "2026-05-13 15:45:30")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除", example = "ACTIVE")
    @TableField(value = "is_deleted")
    private String isDeleted;

}
