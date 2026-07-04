package com.shy.nexusix.iam.rto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * <p>重置用户密码请求传输对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "重置用户密码请求参数")
public class SysUserResetPasswordRTO {

    /**
     * 用户编码
     */
    @Schema(description = "用户编码", example = "USER001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户编码不能为空")
    private String userCode;

    /**
     * 新密码
     */
    @Schema(description = "新密码（含字母和数字，6-20位）", example = "newpass123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$", message = "密码必须含字母和数字，6-20位")
    private String newPassword;

}
