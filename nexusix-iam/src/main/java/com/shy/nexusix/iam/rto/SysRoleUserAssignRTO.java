package com.shy.nexusix.iam.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "角色用户分配请求对象")
public class SysRoleUserAssignRTO {

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    @Schema(description = "角色 ID", example = "1987654321098765432")
    private Long roleId;

    /**
     * 租户ID
     * 角色必须属于此租户
     */
    @NotNull(message = "租户ID不能为空")
    @Schema(description = "租户 ID", example = "1987654321098765432")
    private Long tenantId;

    /**
     * 用户ID列表
     * 传入空列表表示清除该角色的所有用户关联
     */
    @NotEmpty(message = "用户ID列表不能为空")
    @Schema(description = "用户 ID 列表")
    private List<Long> userIds;

}
