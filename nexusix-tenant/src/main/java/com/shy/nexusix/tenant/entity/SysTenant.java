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
 * <p>租户信息实体，支持无限层级</p>
 *
 * @author shy
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
    private Long id;

    @Schema(description = "租户唯一编码", example = "TENANT_001")
    @TableField(value = "tenant_code")
    private String tenantCode;

    @Schema(description = "租户名称", example = "某某科技有限公司")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "租户类型", example = "餐饮、互联网")
    @TableField(value = "tenant_type")
    private String tenantType;

    @Schema(description = "租户描述", example = "这是...类型公司")
    @TableField(value = "tenant_desc")
    private String tenantDesc;

    @Schema(description = "租户logo路径", example = "/upload/tenant/logo/2026/05/13/xxx.png")
    @TableField(value = "tenant_logo_url")
    private String tenantLogoUrl;

    @Schema(description = "父租户ID", example = "1")
    @TableField(value = "parent_id")
    private Long parentId;

    @Schema(description = "父租户名称", example = "阿里云")
    @TableField(value = "parent_name")
    private String parentName;

    @Schema(description = "祖级列表", example = "rootTenant/GROUP001/EAST001")
    @TableField(value = "path")
    private String path;

    @Schema(description = "层级深度", example = "1")
    @TableField(value = "level")
    private Integer level;

    @Schema(description = "联系人姓名", example = "张三")
    @TableField(value = "contact_name")
    private String contactName;

    @Schema(description = "联系人电话", example = "13800138000", pattern = "^1[3-9]\\d{9}$")
    @TableField(value = "contact_phone")
    private String contactPhone;

    @Schema(description = "联系人邮箱", example = "zhangsan@example.com")
    @TableField(value = "contact_email")
    private String contactEmail;

    @Schema(description = "状态", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "禁用原因", example = "ADMIN_DISABLE")
    @TableField(value = "disable_reason")
    private String disableReason;

    @Schema(description = "服务过期时间", example = "2026-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "expire_time")
    private LocalDateTime expireTime;

    @Schema(description = "当前主套餐ID", example = "PKG_PREMIUM")
    @TableField(value = "package_id")
    private Long packageId;

    @Schema(description = "当前主套餐名称", example = "标准版")
    @TableField(value = "package_name")
    private String packageName;

    @Schema(description = "最大用户数限制", example = "1000")
    @TableField(value = "max_users")
    private Integer maxUsers;

    @Schema(description = "扩展属性(JSONB，存储行业特定配置)", example = "{\"industry\": \"tech\", \"quota\": 100}")
    @TableField(value = "ext_attributes")
    private String extAttributes;

    @Schema(description = "是否有子租户", example = "false")
    @TableField(value = "has_children")
    private Boolean hasChildren;

    @Schema(description = "创建时所属租户ID", example = "0")
    @TableField(value = "create_tenant")
    private Long createTenant;

    @Schema(description = "创建时所属部门ID", example = "0")
    @TableField(value = "create_dept")
    private Long createDept;

    @Schema(description = "创建时使用角色ID", example = "0")
    @TableField(value = "create_role")
    private Long createRole;

    @Schema(description = "创建人ID", example = "100")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_at")
    private LocalDateTime createAt;

    @Schema(description = "更新人ID", example = "100")
    @TableField(value = "update_by")
    private Long updateBy;

    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "update_at")
    private LocalDateTime updateAt;

    @Schema(description = "逻辑删除", example = "NOT_DELETED")
    @TableField(value = "is_deleted")
    private String isDeleted;

    @Schema(description = "删除时间", example = "2026-04-07 15:45:30")
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;


}
