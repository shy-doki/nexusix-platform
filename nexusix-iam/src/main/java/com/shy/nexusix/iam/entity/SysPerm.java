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
 * 权限/资源表-定义系统所有可授权资源（含字段级权限）
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm")
@Schema(name="SysPerm对象", description="权限/资源表-定义系统所有可授权资源（含字段级权限）")
public class SysPerm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "权限名称")
    private String permName;

    @Schema(description = "权限描述")
    private String permDesc;

    @Schema(description = "权限标识")
    private String permCode;

    @Schema(description = "权限键值(只可超级管理员变更，且变更后需要重启服务并修改相关常量)")
    private String permKey;

    @Schema(description = "权限类型：MENU-菜单 / BUTTON-按钮 / API-接口 / FIELD_GROUP-字段组 / FIELD-字段")
    private String permType;

    @Schema(description = "父权限编码")
    private String parentCode;

    @Schema(description = "父权限名称")
    private String parentName;

    @Schema(description = "资源路径")
    private String path;

    @Schema(description = "关联数据库表名")
    private String tableName;

    @Schema(description = "关联数据库表描述")
    private String tableDesc;

    @Schema(description = "关联数据库字段名")
    private String fieldName;

    @Schema(description = "关联数据库字段描述")
    private String fieldDesc;

    @Schema(description = "操作类型")
    private String operateType;

    @Schema(description = "状态")
    private String status;

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
