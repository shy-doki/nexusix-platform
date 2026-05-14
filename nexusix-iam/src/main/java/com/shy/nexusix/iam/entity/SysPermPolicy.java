package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限策略控制表-实现四层权限及禁用继承逻辑
 * </p>
 *
 * @author author
 * @since 2026-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm_policy")
@ApiModel(value="SysPermPolicy对象", description="权限策略控制表-实现四层权限及禁用继承逻辑")
public class SysPermPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "目标类型")
    private String targetType;

    @ApiModelProperty(value = "目标ID")
    private String targetId;

    @ApiModelProperty(value = "目标名称")
    private String targetName;

    @ApiModelProperty(value = "关联权限ID")
    private String permId;

    @ApiModelProperty(value = "关联权限名称")
    private String permName;

    @ApiModelProperty(value = "动作")
    private String action;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "是否向下继承")
    private Boolean inheritanceEnabled;

    @ApiModelProperty(value = "创建人ID")
    private String createBy;

    @ApiModelProperty(value = "创建人名称")
    private String createByName;

    @ApiModelProperty(value = "更新人ID")
    private String updateBy;

    @ApiModelProperty(value = "更新人名称")
    private String updateByName;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除")
    private String isDeleted;


}
