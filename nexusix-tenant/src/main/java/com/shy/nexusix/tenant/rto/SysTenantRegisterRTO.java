package com.shy.nexusix.tenant.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.shy.nexusix.common.constant.RegexConstant.Phone.CHINA_MOBILE;

/**
 * <p>租户自助注册请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "租户自助注册请求对象")
public class SysTenantRegisterRTO {

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    @Size(min = 2, max = 100, message = "租户名称必须在2-100字符之间")
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

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
    @Schema(description = "租户logo路径（可选）", example = "/upload/tenant/logo/2026/05/13/xxx.png")
    private String tenantLogoUrl;

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
     * 父租户编码
     */
    @Schema(description = "父租户编码（可选，用于在指定租户下注册子租户）", example = "1987654321098765432")
    private String parentCode;

    /**
     * 扩展属性
     */
    @Schema(description = "扩展属性(JSONB，存储行业特定配置)", example = "{\"industry\": \"tech\", \"scale\": \"500+\"}")
    private String extAttributes;

}
