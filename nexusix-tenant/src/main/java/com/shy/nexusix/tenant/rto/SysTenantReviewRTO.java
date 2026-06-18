package com.shy.nexusix.tenant.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>租户审核请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "租户审核请求对象")
public class SysTenantReviewRTO {

    /**
     * 租户ID
     */
    @NotBlank(message = "租户ID不能为空")
    @Schema(description = "租户ID", example = "1987654321098765432")
    private String tenantId;

    /**
     * 审核结果
     */
    @NotNull(message = "审核结果不能为空")
    @Schema(description = "审核结果：true-通过，false-拒绝", example = "true")
    private Boolean approved;

    /**
     * 审核备注
     */
    @Schema(description = "审核备注（拒绝时必填）", example = "企业信息不完整")
    private String reviewRemark;

}
