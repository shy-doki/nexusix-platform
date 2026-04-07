package com.shy.nexusix.tenant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租户信息表 - 存储租户基础信息，支持无限层级
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tenant")
@Schema(name="SysTenant对象", description="租户信息表 - 存储租户基础信息，支持无限层级")
public class SysTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "租户名称", example = "某某科技有限公司")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "租户唯一编码", example = "TENANT_001")
    @TableField(value = "tenant_code")
    private String tenantCode;

    @Schema(description = "父租户 ID (0 为根租户)", example = "TENANT_006")
    @TableField(value = "parent_id")
    private Long parentId;

    @Schema(description = "祖级列表 (物化路径，如 0,100,200)", example = "0,100,200")
    @TableField(value = "ancestors")
    private String ancestors;

    @Schema(description = "联系人姓名", example = "张三")
    @TableField(value = "contact_name")
    private String contactName;

    @Schema(description = "联系人电话", example = "13800138000", pattern = "^1[3-9]\\d{9}$")
    @TableField(value = "contact_phone")
    private String contactPhone;

    @Schema(description = "状态 (1-正常 0-冻结)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "服务过期时间", example = "2026-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "expire_time")
    private LocalDateTime expireTime;

    @Schema(description = "当前主套餐 ID", example = "1001")
    @TableField(value = "package_id")
    private Long packageId;

    @Schema(description = "扩展属性 (JSONB，存储行业特定配置)", example = "{\"industry\": \"tech\", \"quota\": 100}")
    @TableField(value = "ext_attributes")
    private String extAttributes;

    @Schema(description = "创建人 ID", example = "100")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "更新人 ID", example = "100")
    @TableField(value = "update_by")
    private Long updateBy;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
