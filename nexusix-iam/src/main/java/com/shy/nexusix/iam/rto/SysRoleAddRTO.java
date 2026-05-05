package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * <p>
 * 角色新增请求对象
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@Data
@Schema(description = "角色新增请求对象")
public class SysRoleAddRTO {

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 100, message = "角色名称必须在2-100字符之间")
    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;

    /**
     * 角色编码
     * 采用大写+下划线格式，如 ROLE_ADMIN
     */
    @NotBlank(message = "角色编码不能为空")
    @Size(min = 2, max = 100, message = "角色编码必须在2-100字符之间")
    @Schema(description = "角色编码", example = "ROLE_ADMIN")
    private String roleCode;

    /**
     * 角色层级(通过枚举转换)
     * 1-系统 2-租户 3-用户
     */
    @NotNull(message = "角色层级不能为空")
    @EnumField
    @Schema(description = "角色层级(通过枚举转换)", example = "2")
    private GlobalEnum.RoleLevel roleLevel;

    /**
     * 所属租户ID
     * 系统级角色填0
     */
    @Schema(description = "所属租户ID (系统级为0)", example = "0")
    private Long tenantId;

    /**
     * 数据范围(通过枚举转换)
     * 1-全部 2-本部门 3-本人 4-自定义
     */
    @NotNull(message = "数据范围不能为空")
    @EnumField
    @Schema(description = "数据范围(通过枚举转换)", example = "1")
    private GlobalEnum.DataScope dataScope;

    /**
     * 状态(通过枚举转换)
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态(通过枚举转换)", example = "启用")
    private GlobalEnum.Status status;

    /**
     * 排序
     */
    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

}
