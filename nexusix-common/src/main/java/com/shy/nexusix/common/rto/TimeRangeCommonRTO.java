package com.shy.nexusix.common.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeRangeCommonRTO {

    /**
     * 开始时间
     */
    @Schema(description = "开始时间", example = "2026-01-01 00:00:00")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间", example = "2026-12-31 23:59:59")
    private LocalDateTime endTime;
}
