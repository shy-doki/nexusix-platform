package com.shy.nexusix.tenant.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "租户详情视图对象")
public class SysTenantDetailVO extends SysTenantCommonVO {

    @Schema(description = "租户类型", example = "餐饮、互联网")
    private String tenantType;

    @Schema(description = "租户logo路径", example = "/upload/tenant/logo/2026/05/13/xxx.png")
    private String tenantLogoUrl;

    @Schema(description = "租户描述", example = "这是...类型公司")
    private String tenantDesc;

    /**
     * 祖级列表 (物化路径，如 0/100/200)
     */
    @Schema(description = "祖级列表 (物化路径，如 0/100/200)", example = "0/100/200")
    private String ancestors;

    /**
     * 当前主套餐
     */
    @Schema(description = "当前主套餐", example = "1001")
    private String packageName;

    /**
     * 扩展属性 (JSONB，存储行业特定配置)
     */
    @Schema(description = "扩展属性 (JSONB，存储行业特定配置)", example = "{\"industry\": \"tech\", \"quota\": 100}")
    private String extAttributes;

    /**
     * 更新人
     */
    @Schema(description = "更新人姓名", example = "100")
    private String updateByName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    private LocalDateTime updateTime;

}
