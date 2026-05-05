package com.shy.nexusix.iam.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 角色权限分配请求对象
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@Data
@Schema(description = "角色权限分配请求对象")
public class SysRolePermissionAssignRTO {

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    @Schema(description = "角色 ID", example = "1987654321098765432")
    private Long roleId;

    /**
     * 权限ID列表
     * 传入空列表表示清除该角色的所有权限
     */
    @NotEmpty(message = "权限ID列表不能为空")
    @Schema(description = "权限 ID 列表")
    private List<Long> permissionIds;

}
