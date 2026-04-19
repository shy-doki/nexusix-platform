package com.shy.nexusix.common.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.Data;

@Data
public class PageCommonRTO {

    /**
     * 当前页码
     */
    @Min(value = 1, message = "页码最小为1")
    @Schema(description = "当前页码", example = "1", defaultValue = "1")
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    @Min(value = 1, message = "每页数量最小为1")
    @Max(value = 100, message = "每页数量最大为100")
    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer pageSize = 10;

}
