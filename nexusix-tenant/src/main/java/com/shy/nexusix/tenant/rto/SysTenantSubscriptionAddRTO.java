package com.shy.nexusix.tenant.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.SubscriptionStatus;
import com.shy.nexusix.common.enums.GlobalEnum.SubscriptionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.shy.nexusix.common.constant.RegexConstant.Code.SNOWFLAKE_ID;

/**
 * <p>
 * 租户套餐订阅新增请求对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "租户套餐订阅新增请求对象")
public class SysTenantSubscriptionAddRTO {

    /**
     * 租户ID
     */
    @NotBlank(message = "租户ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "租户ID格式不正确")
    @Schema(description = "租户ID", example = "1987654321098765432")
    private String tenantId;

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    @Size(min = 2, max = 100, message = "租户名称必须在2-100字符之间")
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

    /**
     * 套餐产品ID
     */
    @NotBlank(message = "套餐产品ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "套餐产品ID格式不正确")
    @Schema(description = "套餐产品ID", example = "1001")
    private String packageId;

    /**
     * 套餐产品名称
     */
    @NotBlank(message = "套餐产品名称不能为空")
    @Size(min = 2, max = 100, message = "套餐产品名称必须在2-100字符之间")
    @Schema(description = "套餐产品名称", example = "高级套餐")
    private String packageName;

    /**
     * 订阅类型(通过枚举转换)
     */
    @NotNull(message = "订阅类型不能为空")
    @EnumField
    @Schema(description = "订阅类型(通过枚举转换)", example = "自购")
    private SubscriptionType subscriptionType;

    /**
     * 订阅开始时间
     */
    @NotNull(message = "订阅开始时间不能为空")
    @Schema(description = "订阅开始时间", example = "2026-01-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 订阅结束时间
     */
    @NotNull(message = "订阅结束时间不能为空")
    @Schema(description = "订阅结束时间", example = "2026-12-31 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 状态(通过枚举转换)
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态(通过枚举转换)", example = "生效")
    private SubscriptionStatus status;

    /**
     * 是否自动续费
     */
    @Schema(description = "是否自动续费", example = "false")
    private Boolean isAutoRenew;

    /**
     * 来源类型
     */
    @Schema(description = "来源类型", example = "1")
    private Integer sourceType;

    /**
     * 父租户分配记录ID
     */
    @Schema(description = "父租户分配记录ID", example = "1987654321098765432")
    private Long parentGrantId;

    /**
     * 父租户名称
     */
    @Schema(description = "父租户名称", example = "某科技集团")
    private String parentTenantName;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID", example = "100")
    private Long createBy;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 更新人ID
     */
    @Schema(description = "更新人ID", example = "100")
    private Long updateBy;

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

}
