package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色条件查询请求对象")
public class SysRoleQueryRTO extends PageCommonRTO {

    /**
     * 角色名称（模糊匹配）
     */
    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;

    /**
     * 角色编码（模糊匹配）
     */
    @Schema(description = "角色编码", example = "ROLE_ADMIN")
    private String roleCode;

    /**
     * 角色层级
     * 1-系统 2-租户 3-用户
     */
    @Schema(description = "角色层级 (1-系统 2-租户 3-用户)", example = "2")
    private Integer roleLevel;

    /**
     * 所属租户ID
     */
    @Schema(description = "所属租户ID", example = "0")
    private Long tenantId;

    /**
     * 状态
     * 1-正常 0-禁用
     */
    @Schema(description = "状态 (1-正常 0-禁用)", example = "1")
    private Integer status;

}
