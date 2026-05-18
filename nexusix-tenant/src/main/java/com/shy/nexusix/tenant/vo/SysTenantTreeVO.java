package com.shy.nexusix.tenant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 租户树形视图对象
 * </p>
 *
 * @author shy
 * @since 2026-04-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "租户树形视图对象")
public class SysTenantTreeVO extends SysTenantCommonVO {

    /**
     * 父租户编码
     */
    @Schema(description = "父租户编码", example = "1987654321098765432")
    private String parentCode;


    /**
     * 子租户
     */
    @Schema(description = "子租户列表")
    private List<SysTenantTreeVO> childTenant;

}
