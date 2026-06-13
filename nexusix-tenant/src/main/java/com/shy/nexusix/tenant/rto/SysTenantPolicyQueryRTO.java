package com.shy.nexusix.tenant.rto;

import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 * 租户策略查询请求对象
 * </p>
 * <p>
 * 用于接收前端传递的租户策略查询条件，支持多条件组合查询和分页
 * </p>
 *
 * @author shy
 * @since 2026-06-13
 */
@Data
@Schema(description = "租户策略查询请求对象")
public class SysTenantPolicyQueryRTO {

    /**
     * 策略编码（模糊匹配）
     */
    @Schema(description = "策略编码", example = "TP_DEPT")
    private String policyCode;

    /**
     * 策略名称（模糊匹配）
     */
    @Schema(description = "策略名称", example = "财务部")
    private String policyName;

    /**
     * 源实体类型（精确匹配）
     */
    @Schema(description = "源实体类型：DEPT, ROLE", example = "DEPT")
    private String sourceType;

    /**
     * 源实体ID（精确匹配）
     */
    @Schema(description = "源实体ID", example = "301")
    private Long sourceId;

    /**
     * 目标租户ID（精确匹配）
     */
    @Schema(description = "目标租户ID", example = "101")
    private Long tenantId;

    /**
     * 策略状态（精确匹配）
     */
    @Schema(description = "策略状态", example = "ACTIVE")
    private String status;

    /**
     * 绑定时间范围查询
     */
    @Schema(description = "绑定时间范围")
    private TimeRangeCommonRTO bindTimeRange;

    /**
     * 创建时间范围查询
     */
    @Schema(description = "创建时间范围")
    private TimeRangeCommonRTO createTimeRange;

    /**
     * 更新时间范围查询
     */
    @Schema(description = "更新时间范围")
    private TimeRangeCommonRTO updateTimeRange;

    /**
     * 页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码", example = "1")
    private Long pageNum;

    /**
     * 每页数量
     */
    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量必须大于0")
    @Schema(description = "每页数量", example = "10")
    private Long pageSize;

}
