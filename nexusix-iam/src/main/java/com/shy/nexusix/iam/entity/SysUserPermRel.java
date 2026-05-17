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
 * 用户生效权限表(策略计算结果快照)
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_perm_rel")
@ApiModel(value="SysUserPermRel对象", description="用户生效权限表(策略计算结果快照)")
public class SysUserPermRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "用户编码")
    private String userCode;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "权限编码")
    private String permCode;

    @ApiModelProperty(value = "权限名称")
    private String permName;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "租户名称")
    private String tenantName;

    @ApiModelProperty(value = "生效动作: ALLOW / DENY")
    private String action;

    @ApiModelProperty(value = "来源策略编码")
    private String policyCode;

    @ApiModelProperty(value = "策略层级: SYSTEM / TENANT / ROLE / USER")
    private String policyLevel;

    @ApiModelProperty(value = "数据库表名")
    private String tableName;

    @ApiModelProperty(value = "表描述")
    private String tableDesc;

    @ApiModelProperty(value = "访问类型")
    private String accessType;

    @ApiModelProperty(value = "可操作字段")
    private String fieldOperates;

    @ApiModelProperty(value = "不可操作字段")
    private String fieldUnOperate;

    @ApiModelProperty(value = "创建人编码")
    private String createBy;

    @ApiModelProperty(value = "创建人名称")
    private String createByName;

    @ApiModelProperty(value = "更新人编码")
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
