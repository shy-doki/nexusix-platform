package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 注册响应视图对象
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
@Schema(description = "注册响应结果")
public class RegisterVO {

    @Schema(description = "用户ID", example = "1987654321098765432")
    private String userId;

    @Schema(description = "用户名", example = "newuser")
    private String username;

}
