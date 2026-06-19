package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>用户公共视图对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "用户公共视图对象")
public class SysUserCommonVO {

    /**
     * 用户编码
     */
    @Schema(description = "用户编码", example = "U001")
    private String userCode;

    /**
     * 用户名（登录名）
     */
    @Schema(description = "用户名（登录名）", example = "admin")
    private String userName;

    /**
     * 昵称
     */
    @Schema(description = "昵称", example = "管理员")
    private String nickName;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 电子邮箱
     */
    @Schema(description = "电子邮箱", example = "admin@example.com")
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码", example = "13800138000")
    private String phone;

    /**
     * 头像URL
     */
    @Schema(description = "头像URL", example = "/upload/user/avatar/2026/05/13/xxx.png")
    private String avatarUrl;

    /**
     * 性别
     */
    @Schema(description = "性别", example = "MALE")
    private String gender;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(description = "出生日期", example = "1990-01-01")
    private LocalDate birthday;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "启用")
    private String status;

    /**
     * 禁用原因
     */
    @Schema(description = "禁用原因", example = "违规操作")
    private String disableReason;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "最后登录时间", example = "2026-04-07 15:45:30")
    private LocalDateTime lastLoginAt;

    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP", example = "192.168.1.1")
    private String lastLoginIp;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "李四")
    private String createByName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    private LocalDateTime createTime;

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人姓名", example = "100")
    private String updateByName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     * 超级管理员可见
     */
    @Schema(description = "逻辑删除", example = "0")
    private String isDeleted;

    /**
     * 删除时间
     * 超级管理员可见
     */
    @Schema(description = "删除时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
