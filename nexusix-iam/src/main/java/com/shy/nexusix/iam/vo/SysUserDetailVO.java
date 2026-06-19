package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>用户详情视图对象</p>
 *
 * @author shy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户详情视图对象")
public class SysUserDetailVO extends SysUserCommonVO {

    /**
     * 创建人编码
     */
    @Schema(description = "创建人编码", example = "100")
    private String createByCode;

    /**
     * 更新人编码
     */
    @Schema(description = "更新人编码", example = "100")
    private String updateByCode;

}
