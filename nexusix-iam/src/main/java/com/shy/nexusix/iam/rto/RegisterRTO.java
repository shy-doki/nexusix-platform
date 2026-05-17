package com.shy.nexusix.iam.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * <p>
 * 注册请求传输对象
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
@Schema(description = "注册请求参数")
public class RegisterRTO {

    @Schema(description = "用户名", example = "newuser", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    @Schema(description = "密码", example = "Admin@123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$", message = "密码必须包含字母和数字，长度6-20位")
    private String password;

    @Schema(description = "昵称", example = "新用户")
    private String nickname;

    @Schema(description = "邮箱", example = "user@example.com")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    private String email;

    @Schema(description = "手机号", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

}
