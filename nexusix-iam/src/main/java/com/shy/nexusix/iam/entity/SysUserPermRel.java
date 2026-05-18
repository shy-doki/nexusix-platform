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
@Schema(name="SysUserPermRel对象", description="用户生效权限表(策略计算结果快照)")
public class SysUserPermRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "用户编码")
    private String userCode;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "权限编码")
    private String permCode;

    @Schema(description = "权限名称")
    private String permName;

    @Schema(description = "租户编码")
    private String tenantCode;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "生效动作: ALLOW / DENY")
    private String action;

    @Schema(description = "来源策略编码")
    private String policyCode;

    @Schema(description = "策略层级: SYSTEM / TENANT / ROLE / USER")
    private String policyLevel;

    @Schema(description = "数据库表名")
    private String tableName;

    @Schema(description = "表描述")
    private String tableDesc;

    @Schema(description = "访问类型")
    private String accessType;

    @Schema(description = "可操作字段")
    private String fieldOperates;

    @Schema(description = "不可操作字段")
    private String fieldUnOperate;

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
