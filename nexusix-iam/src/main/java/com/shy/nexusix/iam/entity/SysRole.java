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
 * 角色表-定义系统/租户/用户级角色
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@Schema(name="SysRole对象", description="角色表-定义系统/租户/用户级角色")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色描述")
    private String roleDesc;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "租户编码")
    private String tenantCode;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "数据范围")
    private String dataScope;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "排序顺序")
    private Integer sortOrder;

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
