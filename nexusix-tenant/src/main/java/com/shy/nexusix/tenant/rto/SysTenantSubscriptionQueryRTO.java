package com.shy.nexusix.tenant.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 租户套餐订阅条件查询请求对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "租户套餐订阅条件查询请求对象")
public class SysTenantSubscriptionQueryRTO extends PageCommonRTO {

    /**
     * 租户ID
     */
    @Schema(description = "租户ID", example = "1987654321098765432")
    private String tenantId;

    /**
     * 套餐产品ID
     */
    @Schema(description = "套餐产品ID", example = "1001")
    private String packageId;

    /**
     * 订阅类型 (1-自购 2-父租户分配)
     */
    @Schema(description = "订阅类型 (1-自购 2-父租户分配)，支持数字编码、中文描述或枚举名称", example = "自购")
    private String subscriptionType;

    /**
     * 状态 (1-生效 0-过期)
     */
    @Schema(description = "状态 (1-生效 0-过期)，支持数字编码、中文描述或枚举名称", example = "生效")
    private String status;

    /**
     * 是否自动续费
     */
    @Schema(description = "是否自动续费", example = "true")
    private Boolean isAutoRenew;

    /**
     * 订阅开始时间范围
     */
    @Schema(description = "订阅开始时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO startTime;

    /**
     * 订阅结束时间范围
     */
    @Schema(description = "订阅结束时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO endTime;

    /**
     * 创建时间范围
     */
    @Schema(description = "创建时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO createTime;

    /**
     * 逻辑删除
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    private String isDeleted;

}
