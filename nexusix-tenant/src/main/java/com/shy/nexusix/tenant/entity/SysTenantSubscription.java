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
@Schema(name="SysTenantSubscription对象", description="租户套餐订阅表 - 记录租户购买的套餐及订阅状态")
public class SysTenantSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "套餐产品 ID", example = "1001")
    @TableField(value = "package_id")
    private Long packageId;

    @Schema(description = "订阅类型 (1-自购 2-父租户分配)", example = "1")
    @TableField(value = "subscription_type")
    private Integer subscriptionType;

    @Schema(description = "订阅开始时间", format = "date-time", accessMode = Schema.AccessMode.READ_ONLY, example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    @Schema(description = "订阅结束时间", format = "date-time", accessMode = Schema.AccessMode.READ_ONLY, example = "2026-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    @Schema(description = "状态 (1-生效 0-过期)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "是否自动续费", example = "false")
    @TableField(value = "is_auto_renew")
    private Boolean isAutoRenew;

    @Schema(description = "来源类型", example = "1")
    @TableField(value = "source_type")
    private Integer sourceType;

    @Schema(description = "父租户分配记录 ID", example = "1987654321098765432")
    @TableField(value = "parent_grant_id")
    private Long parentGrantId;

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
