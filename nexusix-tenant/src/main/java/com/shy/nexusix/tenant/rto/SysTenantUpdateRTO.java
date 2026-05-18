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
 * 租户更新请求对象
 * <p>
 * 用于接收前端传递的租户更新数据，包含完整的校验规则
 * </p>
 *
 * @author system
 * @since 2026-05-14
 */
@Data
@Schema(description = "租户更新请求对象")
public class SysTenantUpdateRTO {

    /**
     * 主键Id
     */
    @NotNull(message = "Id不能为空")
    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    private String id;

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
     * 租户类型
     */
    @NotBlank(message = "租户类型不能为空")
    @Size(min = 2, max = 50, message = "租户类型必须在2-50字符之间")
    @Schema(description = "租户类型", example = "餐饮、互联网")
    private String tenantType;

    /**
     * 租户logo路径
     */
    @Schema(description = "租户logo路径", example = "/upload/tenant/logo/2026/05/13/xxx.png")
    private String tenantLogoUrl;

    /**
     * 租户描述
     */
    @Size(max = 500, message = "租户描述不能超过500字符")
    @Schema(description = "租户描述", example = "这是...类型公司")
    private String tenantDesc;

    /**
     * 父租户ID
     */
    @NotNull(message = "父租户ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "父租户ID格式不正确")
    @Schema(description = "父租户ID", example = "1987654321098765432")
    private String parentCode;

    /**
     * 父租户名称
     */
    @NotBlank(message = "父租户名称不能为空")
    @Size(min = 2, max = 100, message = "父租户名称必须在2-100字符之间")
    @Schema(description = "父租户名称", example = "阿里云")
    private String parentName;

    /**
     * 祖级路径（系统内部计算，前端无需传入）
     */
    @Schema(description = "祖级列表", hidden = true)
    private String ancestors;

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
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态", example = "正常")
    private GlobalEnum.TenantStatus status;

    /**
     * 服务过期时间
     */
    @NotNull(message = "服务过期时间不能为空")
    @Future(message = "服务过期时间不能早于当前时间")
    @Schema(description = "服务过期时间", example = "2026-12-31 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireTime;

    /**
     * 套餐ID
     */
    @NotNull(message = "套餐ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "套餐ID格式不正确")
    @Schema(description = "套餐ID", example = "1001")
    private String packageCode;

    /**
     * 套餐名称
     */
    @NotBlank(message = "套餐名称不能为空")
    @Size(min = 2, max = 100, message = "套餐名称必须在2-100字符之间")
    @Schema(description = "套餐名称", example = "企业版套餐")
    private String packageName;

    /**
     * 扩展属性(JSONB，存储行业特定配置)
     */
    @Schema(description = "扩展属性(JSONB，存储行业特定配置)", example = "{\"industry\": \"tech\", \"quota\": 100}")
    private String extAttributes;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID", example = "100")
    private String createBy;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 更新人ID
     */
    @Schema(description = "更新人ID", example = "100")
    private String updateBy;

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
    @Schema(description = "逻辑删除", example = "未删除")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

}
