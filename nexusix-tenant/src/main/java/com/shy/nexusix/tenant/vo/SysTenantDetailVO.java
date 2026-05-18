package com.shy.nexusix.tenant.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 租户详情视图对象
 * <p>
 * 继承自 {@link SysTenantCommonVO}，包含租户的详细信息
 * </p>
 *
 * @author system
 * @since 2026-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "租户详情视图对象")
public class SysTenantDetailVO extends SysTenantCommonVO {

    /**
     * 租户logo路径
     */
    @Schema(description = "租户logo路径", example = "/upload/tenant/logo/2026/05/13/xxx.png")
    private String tenantLogoUrl;

    /**
     * 租户描述
     */
    @Schema(description = "租户描述", example = "这是...类型公司")
    private String tenantDesc;

    /**
     * 祖级列表 (物化路径，如 0/100/200)
     */
    @Schema(description = "祖级列表 (物化路径，如 0/100/200)", example = "0/100/200")
    private String ancestors;

    /**
     * 扩展属性 (JSONB，存储行业特定配置)
     */
    @Schema(description = "扩展属性 (JSONB，存储行业特定配置)", example = "{\"industry\": \"tech\", \"quota\": 100}")
    private String extAttributes;

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
