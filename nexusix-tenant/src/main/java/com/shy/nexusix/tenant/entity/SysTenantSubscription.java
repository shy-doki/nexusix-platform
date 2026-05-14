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
 * 租户套餐订阅表 - 记录租户购买的套餐及订阅状态
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tenant_subscription")
@Schema(name="SysTenantSubscription对象", description="租户套餐订阅表-记录租户购买的套餐及订阅状态")
public class SysTenantSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private String tenantId;

    @Schema(description = "租户名称", example = "某科技公司")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "套餐产品 ID", example = "1001")
    @TableField(value = "package_id")
    private String packageId;

    @Schema(description = "套餐产品名称", example = "高级套餐")
    @TableField(value = "package_name")
    private String packageName;

    @Schema(description = "订阅类型", example = "NEW")
    @TableField(value = "subscription_type")
    private String subscriptionType;

    @Schema(description = "订阅开始时间", format = "date-time", accessMode = Schema.AccessMode.READ_ONLY, example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    @Schema(description = "订阅结束时间", format = "date-time", accessMode = Schema.AccessMode.READ_ONLY, example = "2026-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    @Schema(description = "状态", example = "ACTIVE")
    @TableField(value = "status")
    private String status;

    @Schema(description = "是否自动续费", example = "false")
    @TableField(value = "is_auto_renew")
    private Boolean isAutoRenew;

    @Schema(description = "来源类型 (DIRECT-直接 INHERITED-继承)", example = "DIRECT")
    @TableField(value = "source_type")
    private String sourceType;

    @Schema(description = "父租户分配记录 ID", example = "1987654321098765432")
    @TableField(value = "parent_grant_id")
    private String parentGrantId;

    @Schema(description = "父租户名称", example = "某科技集团")
    @TableField(value = "parent_tenant_name")
    private String parentTenantName;

    @Schema(description = "创建人 ID", example = "100")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "创建人姓名(新增、更新、删除操作需要同步该字段)", example = "张三")
    @TableField(value = "create_by_name")
    private String createByName;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新人 ID", example = "100")
    @TableField(value = "update_by")
    private String updateBy;

    @Schema(description = "更新人姓名", example = "张三")
    @TableField(value = "update_by_name")
    private String updateByName;

    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除", example = "ACTIVE")
    @TableField(value = "is_deleted")
    private String isDeleted;


}
