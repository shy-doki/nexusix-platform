package com.shy.nexusix.tenant.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysTenantQueryRTO extends PageCommonRTO {

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

    /**
     * 租户编码（脱敏）
     */
    @Schema(description = "租户编码（脱敏）", example = "TEN******001")
    private String tenantCode;

    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名", example = "张三")
    private String contactName;

    /**
     * 联系人电话
     */
    @Schema(description = "联系人电话", example = "13800138000")
    private String contactPhone;

    /**
     * 状态（0-禁用 1-启用）
     */
    @Schema(description = "状态（0-禁用 1-启用）", example = "1")
    private String status;

    /**
     * 服务过期时间
     */
    @Schema(description = "服务过期时间", example = "2026-12-31 23:59:59")
    private String expireTime;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "李四")
    private String createByName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    private String createTime;

}
