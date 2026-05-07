package com.shy.nexusix.iam.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.TokenStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.shy.nexusix.common.constant.RegexConstant.Code.SNOWFLAKE_ID;

/**
 * <p>
 * 用户Token新增请求对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "用户Token新增请求对象")
public class SysUserTokenAddRTO {

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "用户ID格式不正确")
    @Schema(description = "用户ID", example = "1987654321098765432")
    private String userId;

    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称不能为空")
    @Size(min = 2, max = 50, message = "用户名称必须在2-50字符之间")
    @Schema(description = "用户名称", example = "张三")
    private String userName;

    /**
     * 租户ID
     */
    @NotBlank(message = "租户ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "租户ID格式不正确")
    @Schema(description = "租户ID", example = "1987654321098765432")
    private String tenantId;

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    @Size(min = 2, max = 100, message = "租户名称必须在2-100字符之间")
    @Schema(description = "租户名称", example = "某科技公司")
    private String tenantName;

    /**
     * 登录令牌
     */
    @NotBlank(message = "登录令牌不能为空")
    @Schema(description = "登录令牌", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    /**
     * 设备信息
     */
    @Schema(description = "设备信息", example = "{\"device\": \"Chrome\", \"os\": \"Windows\"}")
    private String deviceInfo;

    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP", example = "192.168.1.100")
    private String loginIp;

    /**
     * 登录时间
     */
    @Schema(description = "登录时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime loginTime;

    /**
     * 过期时间
     */
    @NotNull(message = "过期时间不能为空")
    @Schema(description = "过期时间", example = "2026-04-08 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireTime;

    /**
     * 状态(通过枚举转换)
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态(通过枚举转换)", example = "有效")
    private TokenStatus status;

    /**
     * 逻辑删除
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

}
