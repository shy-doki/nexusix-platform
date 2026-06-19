package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>权限详情视图对象</p>
 *
 * @author shy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限详情视图对象")
public class SysPermDetailVO extends SysPermCommonVO {

    /**
     * 权限描述
     */
    @Schema(description = "权限描述", example = "用户管理相关权限")
    private String permDesc;

    /**
     * 权限层级路径
     */
    @Schema(description = "权限层级路径", example = "/501")
    private String path;

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
