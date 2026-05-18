package com.shy.nexusix.tenant.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 租户套餐订阅通用视图对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "租户套餐订阅通用视图对象")
public class SysTenantSubscriptionCommonVO {

    /**
     * 订阅编码
     */
    @Schema(description = "订阅编码", example = "SUB_001")
    private String subscriptionCode;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

    /**
     * 套餐产品名称
     */
    @Schema(description = "套餐产品名称", example = "高级套餐")
    private String packageName;

    /**
     * 订阅类型
     */
    @Schema(description = "订阅类型", example = "自购")
    private String subscriptionType;

    /**
     * 订阅开始时间
     */
    @Schema(description = "订阅开始时间", example = "2026-01-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 订阅结束时间
     */
    @Schema(description = "订阅结束时间", example = "2026-12-31 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 来源类型
     */
    @Schema(description = "来源类型", example = "1")
    private Integer sourceType;

    /**
     * 父租户名称
     */
    @Schema(description = "父租户名称", example = "某科技集团")
    private String parentName;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "生效")
    private String status;

    /**
     * 是否自动续费
     */
    @Schema(description = "是否自动续费", example = "false")
    private Boolean isAutoRenew;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 逻辑删除
     * 超级管理员可见
     */
    @Schema(description = "逻辑删除", example = "未删除")
    private String isDeleted;

}
