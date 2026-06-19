package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>用户策略查询请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "用户策略查询请求对象")
public class SysUserPolicyQueryRTO {

    /**
     * 策略编码（模糊匹配）
     */
    @Schema(description = "策略编码", example = "UP_TENANT")
    private String policyCode;

    /**
     * 策略名称（模糊匹配）
     */
    @Schema(description = "策略名称", example = "张三")
    private String policyName;

    /**
     * 目标类型（精确匹配）
     */
    @Schema(description = "目标类型：TENANT, DEPT, ROLE", example = "TENANT")
    private String targetType;

    /**
     * 用户ID（精确匹配）
     */
    @Schema(description = "用户ID", example = "501")
    private Long userId;

    /**
     * 目标ID（精确匹配）
     */
    @Schema(description = "目标ID", example = "101")
    private Long targetId;

    /**
     * 策略状态（精确匹配）
     */
    @Schema(description = "策略状态", example = "ACTIVE")
    private String status;

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
