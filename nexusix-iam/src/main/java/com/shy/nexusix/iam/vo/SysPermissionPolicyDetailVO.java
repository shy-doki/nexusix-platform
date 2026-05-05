package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限策略详情视图对象")
public class SysPermissionPolicyDetailVO extends SysPermissionPolicyCommonVO {

    /**
     * 权限标识（关联查询填充）
     */
    @Schema(description = "权限标识", example = "system:user:add")
    private String permCode;

}
