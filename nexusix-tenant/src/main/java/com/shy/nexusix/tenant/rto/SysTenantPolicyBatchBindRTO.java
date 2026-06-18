package com.shy.nexusix.tenant.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * <p>租户策略批量绑定请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "租户策略批量绑定请求对象")
public class SysTenantPolicyBatchBindRTO {

    /**
     * 源实体类型
     */
    @NotNull(message = "源实体类型不能为空")
    @Schema(description = "源实体类型：DEPT, ROLE", example = "DEPT")
    private String sourceType;

    /**
     * 源实体ID列表（部门ID或角色ID列表）
     */
    @NotEmpty(message = "源实体ID列表不能为空")
    @Schema(description = "源实体ID列表", example = "[301, 302, 303]")
    private List<Long> sourceIdList;

    /**
     * 目标租户ID
     */
    @NotNull(message = "目标租户ID不能为空")
    @Schema(description = "目标租户ID", example = "101")
    private Long tenantId;

}
