package com.shy.nexusix.iam.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * <p>租户注册请求传输对象</p>
 * <p>包含租户基本信息、管理员信息和安全设置，用于一体化注册租户和管理员账户</p>
 * <p>租户行业类型存储于 tenantType 字段（数据库 sys_tenant.tenant_type）</p>
 *
 * @author shy
 */
@Data
@Schema(description = "租户注册请求参数（包含租户信息+管理员信息+安全设置）")
public class TenantRegisterRTO {

    // ==================== 租户基本信息 ====================

    /** 租户名称 */
    @Schema(description = "租户名称（企业/组织名称）", example = "某某科技有限公司", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "租户名称不能为空")
    @Size(min = 2, max = 50, message = "租户名称长度必须在2-50个字符之间")
    private String tenantName;

    /** 行业类型（存储于 sys_tenant.tenant_type） */
    @Schema(description = "行业类型", example = "internet", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "行业类型不能为空")
    private String tenantType;

    /** 租户地址 */
    @Schema(description = "租户地址", example = "浙江省杭州市上城区万象大厦18层", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "租户地址不能为空")
    @Size(max = 500, message = "租户地址长度不能超过500个字符")
    private String tenantAddress;

    /** 租户描述 */
    @Schema(description = "租户描述", example = "专注软件开发与技术服务", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "租户描述不能为空")
    @Size(max = 500, message = "租户描述长度不能超过500个字符")
    private String tenantDesc;

    /** 租户LOGO路径 */
    @Schema(description = "租户LOGO路径（可选）", example = "https://logo.example.com/xxx.png")
    @Size(max = 500, message = "租户LOGO路径长度不能超过500个字符")
    private String tenantLogoUrl;

    /** 企业规模 */
    @Schema(description = "企业规模", example = "51-200", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "企业规模不能为空")
    private String tenantScale;

    /** 联系电话 */
    @Schema(description = "联系电话（企业联系电话）", example = "13800138000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^[\\d\\-+() ]{7,20}$", message = "请输入有效的联系电话")
    private String contactPhone;

    // ==================== 管理员信息 ====================

    /** 管理员用户名（登录账号） */
    @Schema(description = "管理员用户名（登录账号）", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "管理员用户名不能为空")
    @Size(min = 4, max = 30, message = "用户名长度必须在4-30个字符之间")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "用户名须以字母开头，仅可包含字母、数字、下划线")
    private String adminUserName;

    /** 管理员昵称 */
    @Schema(description = "管理员昵称", example = "管理员", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "管理员昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String adminNickName;

    /** 管理员真实姓名 */
    @Schema(description = "管理员真实姓名", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "管理员真实姓名不能为空")
    @Size(min = 2, max = 20, message = "真实姓名长度必须在2-20个字符之间")
    private String adminRealName;

    /** 管理员性别：MALE, FEMALE, UNKNOWN */
    @Schema(description = "管理员性别：MALE, FEMALE, UNKNOWN", example = "MALE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "管理员性别不能为空")
    @Pattern(regexp = "^(MALE|FEMALE|UNKNOWN)$", message = "性别必须为 MALE/FEMALE/UNKNOWN 之一")
    private String adminGender;

    /** 管理员出生日期（格式：yyyy-MM-dd） */
    @Schema(description = "管理员出生日期（格式：yyyy-MM-dd）", example = "1990-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "管理员出生日期不能为空")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "出生日期格式必须为 yyyy-MM-dd")
    private String adminBirthday;

    /** 管理员手机号 */
    @Schema(description = "管理员手机号", example = "13800138000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "管理员手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入有效的手机号码")
    private String adminPhone;

    /** 管理员电子邮箱 */
    @Schema(description = "管理员电子邮箱", example = "admin@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "管理员邮箱不能为空")
    @Email(message = "请输入有效的邮箱地址")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String adminEmail;

    // ==================== 安全设置 ====================

    /** 管理员登录密码 */
    @Schema(description = "管理员登录密码（8-32位，须含大小写字母和数字）", example = "Admin@123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 32, message = "密码长度必须在8-32个字符之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,32}$", message = "密码必须包含大小写字母和数字")
    private String adminPassword;

}
