package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>角色详情视图对象</p>
 *
 * @author shy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色详情视图对象")
public class SysRoleDetailVO extends SysRoleCommonVO {

    /**
     * 创建人编码
     */
    @Schema(description = "创建人编码", example = "USER_ADMIN")
    private String createByCode;

    /**
     * 更新人编码
     */
    @Schema(description = "更新人编码", example = "100")
    private String updateByCode;

}
