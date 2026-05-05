package com.shy.nexusix.system.rto;

import com.shy.nexusix.common.rto.NumberRangeCommonRTO;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysFileQueryRTO extends PageCommonRTO {

    @Schema(description = "租户名称", example = "华东公司")
    private String tenantName;

    @Schema(description = "原始文件名", example = "企业营业执照.pdf")
    private String originalName;

    @Schema(description = "文件大小范围(字节)", example = "{\"minValue\":1024,\"maxValue\":10485760}")
    private NumberRangeCommonRTO fileSize;

    @Schema(description = "文件类型", example = "application/pdf")
    private String fileType;

    @Schema(description = "业务类型分类", example = "license")
    private String bizType;

    @Schema(description = "上传人名称", example = "张三")
    private String uploadName;

    @Schema(description = "上传时间范围(一个/多个)[第一个参数为开始时间;第二个参数为结束时间]", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO uploadTime;

}
