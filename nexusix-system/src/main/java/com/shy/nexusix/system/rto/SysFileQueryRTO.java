package com.shy.nexusix.system.rto;

import com.shy.nexusix.common.rto.NumberRangeCommonRTO;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 文件查询请求对象
 * </p>
 * <p>
 * 用于接收前端查询文件列表时的筛选条件，支持多字段组合查询和分页
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@Schema(description = "文件查询请求参数")
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
     * 文件类型
     */
    @Schema(description = "文件类型", example = "application/pdf")
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
    private String uploadByName;

    /**
     * 上传时间范围
     */
    @Schema(description = "上传时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO uploadTime;

}
