package com.shy.nexusix.system.rto;

import com.shy.nexusix.common.rto.NumberRangeCommonRTO;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 文件条件查询请求对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "文件条件查询请求对象")
public class SysFileQueryRTO extends PageCommonRTO {

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "华东公司")
    private String tenantName;

    /**
     * 原始文件名
     */
    @Schema(description = "原始文件名", example = "企业营业执照.pdf")
    private String originalName;

    /**
     * 文件大小范围(字节)
     */
    @Schema(description = "文件大小范围(字节)", example = "{\"minValue\":1024,\"maxValue\":10485760}")
    private NumberRangeCommonRTO fileSize;

    /**
     * 文件类型（小写扩展名）
     */
    @Schema(description = "文件类型（小写扩展名）", example = ".pdf")
    private String fileType;

    /**
     * 业务类型分类
     */
    @Schema(description = "业务类型分类", example = "license")
    private String bizType;

    /**
     * 上传人名称
     */
    @Schema(description = "上传人名称", example = "张三")
    private String uploadName;

    /**
     * 上传时间范围
     */
    @Schema(description = "上传时间范围(一个/多个)[第一个参数为开始时间;第二个参数为结束时间]", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO uploadTime;

}
