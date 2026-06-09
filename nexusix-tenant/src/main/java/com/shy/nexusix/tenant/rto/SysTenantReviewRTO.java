package com.shy.nexusix.tenant.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 租户审核请求对象
 *
 * @author shy
 * @since 2026-06-08
 */
@Data
@Schema(description = "租户审核请求对象")
public class SysTenantReviewRTO {

    @NotBlank(message = "租户ID不能为空")
    @Schema(description = "租户ID", example = "1987654321098765432")
    private String tenantId;

    @NotNull(message = "审核结果不能为空")
    @Schema(description = "审核结果：true-通过，false-拒绝", example = "true")
    private Boolean approved;

    @Schema(description = "审核备注（拒绝时必填）", example = "企业信息不完整")
    private String reviewRemark;

}
