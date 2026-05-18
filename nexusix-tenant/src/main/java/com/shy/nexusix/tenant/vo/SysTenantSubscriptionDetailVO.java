package com.shy.nexusix.tenant.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 租户套餐订阅详情视图对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "租户套餐订阅详情视图对象")
public class SysTenantSubscriptionDetailVO {

    /**
     * 租户编码
     */
    @Schema(description = "租户编码", example = "1987654321098765432")
    private String tenantCode;

    /**
     * 套餐产品编码
     */
    @Schema(description = "套餐产品编码", example = "1001")
    private String packageCode;

    /**
     * 父租户编码
     */
    @Schema(description = "父租户编码", example = "1987654321098765432")
    private String parentCode;

    /**
     * 创建人编码
     */
    @Schema(description = "创建人编码", example = "USER_ADMIN")
    private String createByCode;

    /**
     * 更新人编码
     */
    @Schema(description = "更新人编码", example = "100")
    private Long updateByCode;

}
