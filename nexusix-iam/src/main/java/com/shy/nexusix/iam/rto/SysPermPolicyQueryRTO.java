package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>权限策略查询请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "权限策略查询请求对象")
public class SysPermPolicyQueryRTO {

    /**
     * 策略编码（模糊匹配）
     */
    @Schema(description = "策略编码", example = "PP")
    private String policyCode;

    /**
     * 策略名称（模糊匹配）
     */
    @Schema(description = "策略名称", example = "用户管理")
    private String policyName;

    /**
     * 目标类型（精确匹配）
     */
    @Schema(description = "目标类型：TENANT, DEPT, ROLE, USER", example = "TENANT")
    private String targetType;

    /**
     * 权限ID（精确匹配）
     */
    @Schema(description = "权限ID", example = "511")
    private Long permId;

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
