package com.shy.nexusix.iam.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.shy.nexusix.common.constant.RegexConstant.Email.EMAIL;
import static com.shy.nexusix.common.constant.RegexConstant.Phone.CHINA_MOBILE;

/**
 * <p>用户更新请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "用户更新请求对象")
public class SysUserUpdateRTO {

    /**
     * 用户编码
     */
    @NotBlank(message = "用户编码不能为空")
    @Schema(description = "用户编码", example = "U001")
    private String userCode;

    /**
     * 用户名（登录名）
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名必须在3-20字符之间")
    @Schema(description = "用户名（登录名）", example = "admin")
    private String userName;

    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称不能超过50字符")
    @Schema(description = "昵称", example = "管理员")
    private String nickName;

    /**
     * 真实姓名
     */
    @Size(max = 50, message = "真实姓名不能超过50字符")
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 电子邮箱
     */
    @Pattern(regexp = EMAIL, message = "电子邮箱格式不正确")
    @Schema(description = "电子邮箱", example = "admin@example.com")
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = CHINA_MOBILE, message = "手机号码格式不正确")
    @Schema(description = "手机号码", example = "13812348000")
    private String phone;

    /**
     * 头像URL
     */
    @Schema(description = "头像URL", example = "/upload/user/avatar/2026/05/13/xxx.png")
    private String avatarUrl;

    /**
     * 性别：MALE, FEMALE, UNKNOWN
     */
    @Schema(description = "性别：MALE, FEMALE, UNKNOWN", example = "MALE")
    private String gender;

    /**
     * 出生日期
     */
    @Schema(description = "出生日期", example = "1990-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthday;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态", example = "启用")
    private GlobalEnum.UserStatus status;

    /**
     * 禁用原因
     */
    @Size(max = 200, message = "禁用原因不能超过200字符")
    @Schema(description = "禁用原因", example = "违规操作")
    private String disableReason;

    /**
     * 创建人编码
     * 超级管理员可填
     */
    @Schema(description = "创建人编码", example = "100")
    private String createByCode;

    /**
     * 创建人姓名
     * 超级管理员可填
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 更新人编码
     * 超级管理员可填
     */
    @Schema(description = "更新人编码", example = "100")
    private String updateByCode;

    /**
     * 更新人姓名
     * 超级管理员可填
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 创建时间
     * 超级管理员可填
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 超级管理员可填
     */
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     * 超级管理员可填
     */
    @Schema(description = "逻辑删除", example = "未删除")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

    /**
     * 删除时间
     * 超级管理员可填
     */
    @Schema(description = "删除时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
