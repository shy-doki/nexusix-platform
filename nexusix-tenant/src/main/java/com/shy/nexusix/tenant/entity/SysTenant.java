package com.shy.nexusix.tenant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
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
@Schema(name="SysTenant对象", description="租户信息表-存储租户基础信息，支持无限层级")
public class SysTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "租户名称", example = "某某科技有限公司")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "租户类型", example = "餐饮、互联网")
    @TableField(value = "tenant_type")
    private String tenantType;

    @Schema(description = "租户logo路径", example = "/upload/tenant/logo/2026/05/13/xxx.png")
    @TableField(value = "tenant_logo_url")
    private String tenantLogoUrl;

    @Schema(description = "租户描述", example = "这是...类型公司")
    @TableField(value = "tenant_desc")
    private String tenantDesc;

    @Schema(description = "租户唯一编码", example = "TENANT_001")
    @TableField(value = "tenant_code")
    private String tenantCode;

    @Schema(description = "父租户ID", example = "TENANT_006")
    @TableField(value = "parent_id")
    private String parentId;

    @Schema(description = "父租户名称", example = "阿里云")
    @TableField(value = "parent_name")
    private String parentName;

    @Schema(description = "祖级列表", example = "0/100/200")
    @TableField(value = "ancestors")
    private String ancestors;

    @Schema(description = "联系人姓名", example = "张三")
    @TableField(value = "contact_name")
    private String contactName;

    @Schema(description = "联系人电话", example = "13800138000", pattern = "^1[3-9]\\d{9}$")
    @TableField(value = "contact_phone")
    private String contactPhone;

    @Schema(description = "状态", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "服务过期时间", example = "2026-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "expire_time")
    private LocalDateTime expireTime;

    @Schema(description = "当前主套餐ID", example = "1001")
    @TableField(value = "package_id")
    private String packageId;

    @Schema(description = "当前主套餐名称", example = "标准版")
    @TableField(value = "package_name")
    private String packageName;

    @Schema(description = "扩展属性(JSONB，存储行业特定配置)", example = "{\"industry\": \"tech\", \"quota\": 100}")
    @TableField(value = "ext_attributes")
    private String extAttributes;

    @Schema(description = "是否有子租户", example = "有/无")
    @TableField(value = "has_children")
    private Boolean hasChildren;

    @Schema(description = "创建人ID", example = "100")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "创建人姓名", example = "张三")
    @TableField(value = "create_by_name")
    private String createByName;

    @Schema(description = "更新人ID", example = "100")
    @TableField(value = "update_by")
    private String updateBy;

    @Schema(description = "更新人姓名", example = "张三")
    @TableField(value = "update_by_name")
    private String updateByName;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除", example = "ACTIVE")
    @TableField(value = "is_deleted")
    private String isDeleted;


}
