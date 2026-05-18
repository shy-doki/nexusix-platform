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
 * 权限策略控制表-实现四层权限及禁用继承逻辑
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm_policy")
@Schema(name="SysPermPolicy对象", description="权限策略控制表-实现四层权限及禁用继承逻辑")
public class SysPermPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "策略编码")
    private String policyCode;

    @Schema(description = "目标类型")
    private String targetType;

    @Schema(description = "目标编码")
    private String targetCode;

    @Schema(description = "目标名称")
    private String targetName;

    @Schema(description = "关联权限编码")
    private String permCode;

    @Schema(description = "关联权限名称")
    private String permName;

    @Schema(description = "动作")
    private String action;

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
