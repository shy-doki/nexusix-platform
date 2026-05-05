package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "权限策略条件查询请求对象")
public class SysPermissionPolicyQueryRTO extends PageCommonRTO {

    @Schema(description = "目标类型 (1-系统 2-租户 3-角色 4-用户)", example = "2")
    private Integer targetType;

    @Schema(description = "目标 ID", example = "1987654321098765432")
    private Long targetId;

    @Schema(description = "关联权限 ID", example = "1001")
    private Long permissionId;

    @Schema(description = "动作 (1-允许 2-拒绝)", example = "1")
    private Integer action;

}
