package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 注册响应视图对象
 * </p>
 * <p>
 * 封装用户注册成功后返回的基本信息，不含密码等敏感数据。
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
@Data
@Schema(description = "注册响应视图对象")
public class RegisterVO {

    @Schema(description = "用户ID", example = "1987654321098765432")
    private Long userId;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "昵称", example = "张三")
    private String nickname;

    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

}