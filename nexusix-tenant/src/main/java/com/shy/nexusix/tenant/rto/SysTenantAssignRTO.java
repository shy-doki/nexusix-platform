package com.shy.nexusix.tenant.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 租户分配（父子租户分配）请求对象
 * </p>
 *
 * @author shy
 * @since 2026-05-04
 */
@Data
@Schema(description = "子租户分配请求对象")
public class SysTenantAssignRTO {

    /**
     * 父租户编码
     */
    @NotBlank(message = "父租户编码不能为空")
    @Size(max = 100, message = "父租户编码长度不能超过100")
    @Schema(description = "父租户编码", example = "PAREN_TENANT_001")
    private String parentCode;

    /**
     * 子租户编码
     */
    @NotEmpty(message = "子租户编码不能为空")
    @Size(max = 100, message = "子租户编码集合数量不能超过100")
    @Schema(description = "子租户编码", example = "SUB_TENANT_001")
    private List<String> subCode;

}
