package com.shy.nexusix.tenant.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.shy.nexusix.common.constant.RegexConstant.Character.ALPHANUMERIC;
import static com.shy.nexusix.common.constant.RegexConstant.Code.SNOWFLAKE_ID;
import static com.shy.nexusix.common.constant.RegexConstant.Phone.CHINA_MOBILE;

/**
 * <p>租户更新请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "租户更新请求对象")
public class SysTenantUpdateRTO {

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    @Size(min = 2, max = 100, message = "租户名称必须在2-100字符之间")
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

    /**
     * 租户编码
     */
    @NotBlank(message = "租户编码不能为空")
    @Schema(description = "租户编码", example = "TEN0000001")
    private String tenantCode;

    /**
     * 租户类型（同时存储行业类型）
     */
    @NotBlank(message = "租户类型不能为空")
    @Size(min = 2, max = 50, message = "租户类型必须在2-50字符之间")
    @Schema(description = "租户类型（同时存储行业类型）", example = "internet")
    private String tenantType;

    /**
     * 租户地址
     */
    @NotBlank(message = "租户地址不能为空")
    @Size(max = 500, message = "租户地址不能超过500字符")
    @Schema(description = "租户地址", example = "浙江省杭州市上城区万象大厦18层")
    private String tenantAddress;

    /**
     * 租户描述
     */
    @NotBlank(message = "租户描述不能为空")
    @Size(max = 500, message = "租户描述不能超过500字符")
    @Schema(description = "租户描述", example = "专注软件开发与技术服务")
    private String tenantDesc;

    /**
     * 租户企业规模
     */
    @NotBlank(message = "租户企业规模不能为空")
    @Size(max = 50, message = "租户企业规模不能超过50字符")
    @Schema(description = "租户企业规模", example = "51-200")
    private String tenantScale;

    /**
     * 租户logo路径
     */
    @Schema(description = "租户logo路径", example = "/upload/tenant/logo/2026/05/13/xxx.png")
    private String tenantLogoUrl;

    /**
     * 父租户编码
     */
    @NotNull(message = "父租户编码不能为空")
    @Schema(description = "父租户编码", example = "1987654321098765432")
    private String parentCode;

    /**
     * 祖级路径
     * 超级管理员可填
     */
    @Schema(description = "祖级列表", hidden = true)
    private String path;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空")
    @Size(min = 2, max = 20, message = "联系人姓名必须在2-20字符之间")
    @Schema(description = "联系人姓名", example = "张三")
    private String contactName;

    /**
     * 联系人电话
     */
    @NotBlank(message = "联系人电话不能为空")
    @Pattern(regexp = CHINA_MOBILE, message = "联系人电话格式不正确")
    @Schema(description = "联系人电话", example = "13812348000")
    private String contactPhone;

    /**
     * 联系人邮箱
     */
    @Email(message = "联系人邮箱格式不正确")
    @Size(max = 100, message = "联系人邮箱不能超过100字符")
    @Schema(description = "联系人邮箱", example = "zhangsan@example.com")
    private String contactEmail;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态", example = "正常")
    private GlobalEnum.TenantStatus status;

    /**
     * 服务过期时间
     * 超级管理员可填
     */
    @NotNull(message = "服务过期时间不能为空")
    @Future(message = "服务过期时间不能早于当前时间")
    @Schema(description = "服务过期时间", example = "2026-12-31 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireTime;

    /**
     * 套餐编码
     */
    @NotNull(message = "套餐编码不能为空")
    @Schema(description = "套餐编码", example = "1001")
    private String packageCode;

    /**
     * 扩展属性(JSONB，存储行业特定配置)
     */
    @Schema(description = "扩展属性(JSONB，存储行业特定配置)", example = "{\"industry\": \"tech\", \"quota\": 100}")
    private String extAttributes;

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
     * 创建时间
     * 超级管理员可填
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

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
     * 删除人编码
     * 超级管理员可填
     */
    @Schema(description = "删除时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
