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
 * 角色策略控制表-实现角色级联禁用及策略继承
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_policy")
@Schema(name="SysRolePolicy对象", description="角色策略控制表-实现角色级联禁用及策略继承")
public class SysRolePolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "目标类型", example = "USER")
    @TableField(value = "target_type")
    private String targetType;

    @Schema(description = "目标ID", example = "1001")
    @TableField(value = "target_id")
    private String targetId;

    @Schema(description = "目标名称", example = "张三")
    @TableField(value = "target_name")
    private String targetName;

    @Schema(description = "关联角色ID", example = "3001")
    @TableField(value = "role_id")
    private String roleId;

    @Schema(description = "关联角色名称", example = "系统管理员")
    @TableField(value = "role_name")
    private String roleName;

    @Schema(description = "动作", example = "ALLOW")
    @TableField(value = "action")
    private String action;

    @Schema(description = "优先级", example = "1")
    @TableField(value = "priority")
    private Integer priority;

    @Schema(description = "是否向下继承", example = "true")
    @TableField(value = "inheritance_enabled")
    private Boolean inheritanceEnabled;

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
