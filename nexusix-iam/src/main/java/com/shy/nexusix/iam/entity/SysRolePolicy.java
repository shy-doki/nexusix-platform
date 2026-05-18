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
 * @since 2026-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_policy")
@Schema(name="SysRolePolicy对象", description="角色策略控制表-实现角色级联禁用及策略继承")
public class SysRolePolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "目标类型")
    private String targetType;

    @Schema(description = "目标编码")
    private String targetCode;

    @Schema(description = "目标名称")
    private String targetName;

    @Schema(description = "关联角色编码")
    private String roleCode;

    @Schema(description = "关联角色名称")
    private String roleName;

    @Schema(description = "动作")
    private String action;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "是否向下继承")
    private Boolean inheritanceEnabled;

    @Schema(description = "创建人编码")
    private String createBy;

    @Schema(description = "创建人名称")
    private String createByName;

    @Schema(description = "更新人编码")
    private String updateBy;

    @Schema(description = "更新人名称")
    private String updateByName;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    private String isDeleted;


}
