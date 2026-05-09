package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 当前登录用户信息视图对象
 * </p>
 * <p>
 * 封装当前登录用户的完整信息，包括用户标识、租户上下文和权限数据。
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
@Data
@Schema(description = "当前登录用户信息视图对象")
public class CurrentUserVO {

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