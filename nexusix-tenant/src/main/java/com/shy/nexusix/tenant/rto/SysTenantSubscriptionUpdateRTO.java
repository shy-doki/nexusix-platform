package com.shy.nexusix.tenant.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.SubscriptionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.shy.nexusix.common.constant.RegexConstant.Code.SNOWFLAKE_ID;

/**
 * <p>
 * 租户套餐订阅更新请求对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "租户套餐订阅更新请求对象")
public class SysTenantSubscriptionUpdateRTO {

    /**
     * 租户编码
     */
    @NotBlank(message = "租户编码不能为空")
    @Schema(description = "租户编码", example = "1987654321098765432")
    private String tenantCode;

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    @Size(min = 2, max = 100, message = "租户名称必须在2-100字符之间")
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

    /**
     * 套餐产品编码
     */
    @NotBlank(message = "套餐产品编码不能为空")
    @Schema(description = "套餐产品编码", example = "1001")
    private String packageCode;

    /**
     * 套餐产品名称
     */
    @NotBlank(message = "套餐产品名称不能为空")
    @Size(min = 2, max = 100, message = "套餐产品名称必须在2-100字符之间")
    @Schema(description = "套餐产品名称", example = "高级套餐")
    private String packageName;

    /**
     * 订阅类型
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
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态", example = "生效")
    private GlobalEnum.SubscriptionStatus status;

    /**
     * 是否自动续费
     */
    @Schema(description = "是否自动续费", example = "false")
    private Boolean isAutoRenew;

    /**
     * 来源类型
     */
    @Schema(description = "来源类型", example = "1")
    private String sourceType;

    /**
     * 父租户编码
     */
    @Schema(description = "父租户编码", example = "1001")
    private String parentCode;

    /**
     * 父租户名称
     */
    @Schema(description = "父租户名称", example = "某某科技有限公司")
    private String parentName;

    /**
     * 创建人编码
     * 超级管理员可填
     */
    @Schema(description = "创建人编码", example = "100")
    private String createByCode;

    /**
     * 创建人姓名
     * 超级管理员可填
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 创建时间
     * 超级管理员可填
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新人编码
     * 超级管理员可填
     */
    @Schema(description = "更新人编码", example = "100")
    private String updateByCode;

    /**
     * 更新人姓名
     * 超级管理员可填
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 更新时间
     * 超级管理员可填
     */
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     * 超级管理员可填
     */
    @Schema(description = "逻辑删除", example = "未删除")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

    /**
     * 删除人时间
     * 超级管理员可填
     */
    @Schema(description = "删除人时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
