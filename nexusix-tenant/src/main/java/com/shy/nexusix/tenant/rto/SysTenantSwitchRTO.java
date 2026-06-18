package com.shy.nexusix.tenant.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>租户切换请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "租户切换请求对象")
public class SysTenantSwitchRTO {

    /**
     * 目标租户编码
     */
    @NotBlank(message = "目标租户编码不能为空")
    @Schema(description = "目标租户编码", example = "TEN00000001")
    private String targetTenantCode;

}
