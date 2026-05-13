package com.shy.nexusix.system.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * <p>
 * 文件上传请求对象
 * </p>
 * <p>
 * 用于接收前端上传文件时的业务参数，包括业务类型、目标租户等信息
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@Schema(description = "文件上传请求参数")
public class SysFileUploadRTO {

    /**
     * 业务类型标识
     */
    @Schema(description = "业务类型标识", example = "tenant-export", allowableValues = {"logo", "avatar", "contract", "license", "attachment", "export"})
    @Size(max = 50, message = "业务类型不能超过50字符")
    private String bizType;

    /**
     * 目标租户编码（不传则取当前用户所属租户）
     */
    @Schema(description = "目标租户编码（不传则取当前用户所属租户）", example = "TEN0000001")
    @Size(max = 50, message = "租户编码不能超过50字符")
    private String tenantCode;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "华东公司")
    private String tenantName;

}
