package com.shy.nexusix.iam.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import static com.shy.nexusix.common.constant.RegexConstant.Email.EMAIL;
import static com.shy.nexusix.common.constant.RegexConstant.Password.PASSWORD_MEDIUM;
import static com.shy.nexusix.common.constant.RegexConstant.Phone.CHINA_MOBILE;
import static com.shy.nexusix.common.constant.RegexConstant.Username.USERNAME_LETTER_START;

/**
 * <p>
 * 用户注册请求对象
 * </p>
 * <p>
 * 用于用户自主注册场景，与SysUserAddRTO（管理员创建用户）的区别：
 * - 无需提供status字段（注册用户默认为正常状态）
 * - 无需提供createBy/createByName等审计字段（系统自动填充）
 * - 密码采用中等强度校验（必须包含字母和数字）
 * - 用户名采用字母开头校验
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
@Data
@Schema(description = "用户注册请求对象")
public class RegisterRTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名必须在4-20字符之间")
    @Pattern(regexp = USERNAME_LETTER_START, message = "用户名必须字母开头，仅支持字母、数字和下划线")
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码必须在6-20字符之间")
    @Pattern(regexp = PASSWORD_MEDIUM, message = "密码必须包含字母和数字")
    @Schema(description = "密码", example = "admin123")
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码", example = "admin123")
    private String confirmPassword;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 50, message = "昵称必须在2-50字符之间")
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = EMAIL, message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = CHINA_MOBILE, message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

}
