package com.shy.nexusix.common.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "数值范围查询参数")
public class NumberRangeCommonRTO {

    /**
     * 最小值
     */
    @Schema(description = "最小值", example = "1024")
    private Long minValue;

    /**
     * 最大值
     */
    @Schema(description = "最大值", example = "10485760")
    private Long maxValue;

}
