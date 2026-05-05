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
 * 权限策略控制表 - 实现四层权限及禁用继承逻辑
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission_policy")
@Schema(name="SysPermissionPolicy对象", description="权限策略控制表 - 实现四层权限及禁用继承逻辑")
public class SysPermissionPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "目标类型 (1-系统 2-租户 3-角色 4-用户)", example = "2")
    @TableField(value = "target_type")
    private Integer targetType;

    @Schema(description = "目标 ID (对应租户/角色/用户 ID)", example = "1987654321098765432")
    @TableField(value = "target_id")
    private Long targetId;

    @Schema(description = "目标名称 (对应租户/角色/用户名称)", example = "某科技公司")
    @TableField(value = "target_name")
    private String targetName;

    @Schema(description = "关联权限 ID", example = "1001")
    @TableField(value = "permission_id")
    private Long permissionId;

    @Schema(description = "关联权限名称", example = "用户管理")
    @TableField(value = "perm_name")
    private String permName;

    @Schema(description = "动作 (1-允许 2-拒绝)", example = "1")
    @TableField(value = "action")
    private Integer action;

    @Schema(description = "优先级 (数字越大优先级越高)", example = "100")
    @TableField(value = "priority")
    private Integer priority;

    @Schema(description = "是否向下继承", example = "true")
    @TableField(value = "inheritance_enabled")
    private Boolean inheritanceEnabled;

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
