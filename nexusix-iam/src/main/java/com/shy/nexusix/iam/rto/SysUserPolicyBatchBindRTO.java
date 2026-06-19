package com.shy.nexusix.iam.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * <p>用户策略批量绑定请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "用户策略批量绑定请求对象")
public class SysUserPolicyBatchBindRTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "501")
    private Long userId;

    /**
     * 目标类型
     */
    @NotNull(message = "目标类型不能为空")
    @Schema(description = "目标类型：TENANT, DEPT, ROLE", example = "TENANT")
    private String targetType;

    /**
     * 目标ID列表（租户ID、部门ID或角色ID列表）
     */
    @NotEmpty(message = "目标ID列表不能为空")
    @Schema(description = "目标ID列表", example = "[101, 102, 103]")
    private List<Long> targetIdList;

}
