package com.shy.nexusix.tenant.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 租户策略批量解绑请求对象
 * </p>
 * <p>
 * 用于批量解除部门/角色与租户的绑定关系
 * </p>
 *
 * @author shy
 * @since 2026-06-13
 */
@Data
@Schema(description = "租户策略批量解绑请求对象")
public class SysTenantPolicyBatchUnbindRTO {

    /**
     * 策略编码列表
     */
    @NotEmpty(message = "策略编码列表不能为空")
    @Schema(description = "策略编码列表", example = "[\"TP_DEPT_001\", \"TP_DEPT_002\"]")
    private List<String> policyCodeList;

}
