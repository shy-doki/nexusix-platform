package com.shy.nexusix.iam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>注册响应视图对象</p>
 * <p>注册成功后返回登录账号、租户编码等关键信息</p>
 *
 * @author shy
 */
@Data
@Schema(description = "注册响应视图对象")
public class RegisterVO {

    /** 用户ID */
    @Schema(description = "用户ID", example = "1789456123456789")
    private Long userId;

    /** 用户编码 */
    @Schema(description = "用户编码", example = "U0001")
    private String userCode;

    /** 登录账号（邮箱） */
    @Schema(description = "登录账号（邮箱）", example = "admin@example.com")
    private String loginAccount;

    /** 用户姓名 */
    @Schema(description = "用户姓名", example = "张三")
    private String realName;

    /** 租户编码（租户注册时返回，用户注册时返回绑定的租户编码） */
    @Schema(description = "租户编码", example = "T0001")
    private String tenantCode;

    /** 租户名称 */
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

}
