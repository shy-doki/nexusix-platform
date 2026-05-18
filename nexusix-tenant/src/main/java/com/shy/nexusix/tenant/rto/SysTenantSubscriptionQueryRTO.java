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
     * 租户编码
     */
    @Schema(description = "租户编码", example = "1987654321098765432")
    private String tenantCode;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

    /**
     * 套餐产品编码
     */
    @Schema(description = "套餐产品编码", example = "1001")
    private String packageCode;

    /**
     * 套餐名称
     */
    @Schema(description = "套餐名称", example = "企业版套餐")
    private String packageName;

    /**
     * 订阅类型
     */
    @Schema(description = "订阅类型", example = "自购")
    private String subscriptionType;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "生效")
    private String status;

    /**
     * 是否自动续费
     */
    @Schema(description = "是否自动续费", example = "true")
    private Boolean isAutoRenew;

    /**
     * 父租户分配记录编码
     */
    @Schema(description = "父租户编码", example = "1987654321098765432")
    private String parentCode;

    /**
     * 父租户名称
     */
    @Schema(description = "父租户名称", example = "阿里云")
    private String parentName;

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
     * 创建人编码
     */
    @Schema(description = "创建人编码", example = "100")
    private String createByCode;

    /**
     * 创建人名称
     */
    @Schema(description = "创建人名称", example = "张三")
    private String createByName;

    /**
     * 创建时间范围
     */
    @Schema(description = "创建时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO createTime;

    /**
     * 更新人编码
     */
    @Schema(description = "更新人编码", example = "100")
    private String updateByCode;

    /**
     * 创建人名称
     */
    @Schema(description = "更新人名称", example = "张三")
    private String updateByName;

    /**
     * 更新时间范围
     */
    @Schema(description = "更新时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO updateTime;

    /**
     * 逻辑删除
     * 超级管理员可填
     */
    @Schema(description = "逻辑删除", example = "0")
    private String isDeleted;

}
