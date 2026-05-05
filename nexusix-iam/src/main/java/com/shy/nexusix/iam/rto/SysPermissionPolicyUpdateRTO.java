package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "权限策略更新请求对象")
public class SysPermissionPolicyUpdateRTO {

    @NotNull(message = "Id不能为空")
    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    private Long id;

    @NotNull(message = "目标类型不能为空")
    @EnumField
    @Schema(description = "目标类型 (1-系统 2-租户 3-角色 4-用户)", example = "2")
    private GlobalEnum.TargetType targetType;

    @NotNull(message = "目标ID不能为空")
    @Schema(description = "目标 ID (对应租户/角色/用户 ID)", example = "1987654321098765432")
    private Long targetId;

    @NotNull(message = "关联权限ID不能为空")
    @Schema(description = "关联权限 ID", example = "1001")
    private Long permissionId;

    @NotNull(message = "动作不能为空")
    @EnumField
    @Schema(description = "动作 (1-允许 2-拒绝)", example = "1")
    private GlobalEnum.Action action;

    @NotNull(message = "优先级不能为空")
    @Schema(description = "优先级 (数字越大优先级越高)", example = "100")
    private Integer priority;

    @NotNull(message = "是否向下继承不能为空")
    @Schema(description = "是否向下继承", example = "true")
    private Boolean inheritanceEnabled;

}
