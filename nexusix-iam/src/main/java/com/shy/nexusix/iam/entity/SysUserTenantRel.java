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
 * 用户-租户关联表-实现用户与多租户绑定
 * </p>
 *
 * @author author
 * @since 2026-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_tenant_rel")
@ApiModel(value="SysUserTenantRel对象", description="用户-租户关联表-实现用户与多租户绑定")
public class SysUserTenantRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "主部门ID")
    private String deptId;

    @ApiModelProperty(value = "是否租户管理员")
    private Boolean isAdmin;

    @ApiModelProperty(value = "加入时间")
    private LocalDateTime joinTime;

    @ApiModelProperty(value = "是否默认租户")
    private Boolean isDefault;

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
