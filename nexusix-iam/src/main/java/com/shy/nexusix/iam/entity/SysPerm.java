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
 * 权限/资源表-定义系统所有可授权资源
 * </p>
 *
 * @author author
 * @since 2026-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm")
@ApiModel(value="SysPerm对象", description="权限/资源表-定义系统所有可授权资源")
public class SysPerm implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "权限名称")
    private String permName;

    @ApiModelProperty(value = "权限标识")
    private String permCode;

    @ApiModelProperty(value = "类型")
    private String permType;

    @ApiModelProperty(value = "父权限ID")
    private String parentId;

    @ApiModelProperty(value = "父权限名称")
    private String parentName;

    @ApiModelProperty(value = "资源路径")
    private String path;

    @ApiModelProperty(value = "状态")
    private String status;

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
