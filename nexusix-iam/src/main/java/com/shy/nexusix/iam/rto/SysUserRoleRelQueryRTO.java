package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 用户角色关联条件查询请求对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "用户角色关联条件查询请求对象")
public class SysUserRoleRelQueryRTO extends PageCommonRTO {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1987654321098765432")
    private String userId;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID", example = "1001")
    private String roleId;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID", example = "1987654321098765432")
    private String tenantId;

    /**
     * 创建时间范围
     */
    @Schema(description = "创建时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO createTime;

}
