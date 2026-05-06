package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户公共视图对象
 * </p>
 * <p>
 * 注意：password 字段不得在视图对象中暴露，确保用户密码安全
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@Schema(description = "用户公共视图对象")
public class SysUserCommonVO {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1987654321098765432")
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址", example = "https://oss.example.com/avatar.jpg")
    private String avatar;

    /**
     * 全局状态
     * 通过枚举转换
     */
    @Schema(description = "状态", example = "启用")
    private String status;

    /**
     * 最后登录 IP
     */
    @Schema(description = "最后登录 IP", example = "192.168.1.100")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "最后登录时间", example = "2026-04-07 15:45:30")
    private LocalDateTime loginDate;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    private LocalDateTime createTime;

    /**
     * 逻辑删除 (0-正常 1-删除)
     * 超级管理员可见
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    private String isDeleted;

}
