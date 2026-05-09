package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 登录响应视图对象
 * </p>
 * <p>
 * 封装用户登录成功后返回的Token信息、用户信息、租户信息和权限数据。
 * 替代原Map<String, Object>返回类型，提供类型安全和明确的API契约。
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
@Data
@Schema(description = "登录响应视图对象")
public class LoginVO {

    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "令牌名称", example = "Authorization")
    private String tokenName;

    @Schema(description = "用户ID", example = "1987654321098765432")
    private Long userId;

    @Schema(description = "用户名称", example = "张三")
    private String userName;

    @Schema(description = "租户ID", example = "1987654321098765432")
    private Long tenantId;

    @Schema(description = "租户名称", example = "某科技公司")
    private String tenantName;

    @Schema(description = "权限编码列表")
    private List<String> permissions;

    @Schema(description = "角色编码列表")
    private List<String> roles;

}