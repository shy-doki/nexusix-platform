package com.shy.nexusix.common.rto;

import com.shy.nexusix.common.constant.GlobalConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.Data;

/**
 * <p>分页查询公共参数</p>
 *
 * @author shy
 */
@Data
public class PageCommonRTO {

    /** 当前页码 */
    @Min(value = GlobalConstant.Page.MIN_PAGE_SIZE, message = "页码最小为1")
    @Schema(description = "当前页码", example = "1", defaultValue = "1")
    private Integer pageNum = 1;

    /** 每页数量 */
    @Min(value = GlobalConstant.Page.MIN_PAGE_SIZE, message = "每页数量最小为1")
    @Max(value = GlobalConstant.Page.MAX_PAGE_SIZE, message = "每页数量最大为100")
    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer pageSize = 10;

}
