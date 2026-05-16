package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字段权限配置表
 * </p>
 *
 * @author shy
 * @since 2026-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_field_perm_config")
@Schema(name="SysFieldPermConfig对象", description="字段权限配置表")
public class SysFieldPermConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "权限名称", example = "用户姓名字段权限")
    @TableField(value = "perm_name")
    private String permName;

    @Schema(description = "权限描述", example = "控制用户姓名可见性")
    @TableField(value = "perm_desc")
    private String permDesc;

    @Schema(description = "权限标识", example = "user:name:field")
    @TableField(value = "perm_code")
    private String permCode;

    @Schema(description = "权限键", example = "user:name:field")
    @TableField(value = "perm_key")
    private String permKey;

    @Schema(description = "数据库字段名", example = "user_name")
    @TableField(value = "field_name")
    private String fieldName;

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

    @Schema(description = "创建时间", format = "date-time", example = "2026-05-16 10:00:00")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", format = "date-time", example = "2026-05-16 10:00:00")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除", example = "ACTIVE")
    @TableField(value = "is_deleted")
    private String isDeleted;


}
