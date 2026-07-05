package com.shy.nexusix.iam.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * <p>用户注册请求传输对象</p>
 * <p>用户通过邀请码注册并加入对应租户，邮箱将作为登录账号</p>
 *
 * @author shy
 */
@Data
@Schema(description = "用户注册请求参数（通过邀请码加入租户）")
public class UserRegisterRTO {

    /** 用户姓名 */
    @Schema(description = "用户姓名（真实姓名）", example = "李四", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "姓名不能为空")
    @Size(min = 2, max = 20, message = "姓名长度必须在2-20个字符之间")
    private String userName;

    /** 手机号 */
    @Schema(description = "手机号", example = "13900139000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入有效的手机号码")
    private String userPhone;

    /** 邮箱（将作为登录账号） */
    @Schema(description = "邮箱（将作为登录账号）", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "请输入有效的邮箱地址")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String userEmail;

    /** 登录密码 */
    @Schema(description = "登录密码（8-32位，须含大小写字母和数字）", example = "User@123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 32, message = "密码长度必须在8-32个字符之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,32}$", message = "密码必须包含大小写字母和数字")
    private String userPassword;

    /** 租户邀请码 */
    @Schema(description = "租户邀请码（8位字符）", example = "AB12CD34", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "邀请码不能为空")
    @Size(min = 8, max = 8, message = "邀请码必须为8位字符")
    private String inviteCode;

}
