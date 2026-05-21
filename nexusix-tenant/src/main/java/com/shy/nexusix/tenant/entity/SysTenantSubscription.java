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

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "订阅编码", example = "1987654321098765432")
    @TableField(value = "subscription_code")
    private String subscriptionCode;

    @Schema(description = "租户ID", example = "1")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "租户编码", example = "EAST001")
    @TableField(value = "tenant_code")
    private String tenantCode;

    @Schema(description = "租户名称", example = "某科技公司")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "套餐产品ID", example = "1")
    @TableField(value = "package_id")
    private String packageId;

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

    @Schema(description = "来源类型", example = "DIRECT")
    @TableField(value = "source_type")
    private String sourceType;

    @Schema(description = "父租户ID", example = "1")
    @TableField(value = "parent_id")
    private String parentId;

    @Schema(description = "创建人ID", example = "100")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_at")
    private LocalDateTime createAt;

    @Schema(description = "更新人ID", example = "100")
    @TableField(value = "update_by")
    private String updateBy;

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
