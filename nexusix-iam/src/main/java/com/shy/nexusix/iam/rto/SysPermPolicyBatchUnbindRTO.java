package com.shy.nexusix.iam.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * <p>权限策略批量解绑请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "权限策略批量解绑请求对象")
public class SysPermPolicyBatchUnbindRTO {

    /**
     * 策略编码列表
     */
    @NotEmpty(message = "策略编码列表不能为空")
    @Schema(description = "策略编码列表", example = "[\"PP_001\", \"PP_002\"]")
    private List<String> policyCodeList;

}
