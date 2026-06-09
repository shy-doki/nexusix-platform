package com.shy.nexusix.tenant.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.shy.nexusix.common.constant.RegexConstant.Phone.CHINA_MOBILE;

/**
 * 租户自助注册请求对象
 *
 * @author shy
 * @since 2026-06-08
 */
@Data
@Schema(description = "租户自助注册请求对象")
public class SysTenantRegisterRTO {

    @NotBlank(message = "租户名称不能为空")
    @Size(min = 2, max = 100, message = "租户名称必须在2-100字符之间")
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

    @NotBlank(message = "租户类型不能为空")
    @Size(min = 2, max = 50, message = "租户类型必须在2-50字符之间")
    @Schema(description = "租户类型/行业", example = "互联网")
    private String tenantType;

    @NotBlank(message = "联系人姓名不能为空")
    @Size(min = 2, max = 20, message = "联系人姓名必须在2-20字符之间")
    @Schema(description = "联系人姓名", example = "张三")
    private String contactName;

    @NotBlank(message = "联系人电话不能为空")
    @Pattern(regexp = CHINA_MOBILE, message = "联系人电话格式不正确")
    @Schema(description = "联系人电话", example = "13812348000")
    private String contactPhone;

    @Schema(description = "父租户编码（可选，用于在指定租户下注册子租户）", example = "1987654321098765432")
    private String parentCode;

    @Schema(description = "扩展属性(JSONB，存储行业特定配置)", example = "{\"industry\": \"tech\", \"scale\": \"500+\"}")
    private String extAttributes;

}
