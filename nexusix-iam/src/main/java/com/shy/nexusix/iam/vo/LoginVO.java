package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * <p>
 * 登录响应视图对象
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
@Schema(description = "登录响应结果")
public class LoginVO {

    @Schema(description = "Token令牌", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "用户ID", example = "1987654321098765432")
    private String userId;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "昵称", example = "管理员")
    private String nickname;

    @Schema(description = "当前租户ID", example = "1")
    private String tenantId;

    @Schema(description = "当前租户名称", example = "默认租户")
    private String tenantName;

    @Schema(description = "有效权限编码集合")
    private Set<String> validPerms;

    @Schema(description = "有效角色编码集合")
    private Set<String> validRoles;

}
