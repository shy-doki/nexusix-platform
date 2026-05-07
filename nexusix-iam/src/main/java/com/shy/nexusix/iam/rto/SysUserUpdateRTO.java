package com.shy.nexusix.iam.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.shy.nexusix.common.constant.RegexConstant.Phone.CHINA_MOBILE;
import static com.shy.nexusix.common.constant.RegexConstant.Email.EMAIL;

/**
 * <p>
 * 用户更新请求对象
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@Schema(description = "用户更新请求对象")
public class SysUserUpdateRTO {

    /**
     * <p>主键Id</p>
     * <p>TODO 后续考虑加入加解密注解 这里就不加入正则判断 因为id默认加密传输</p>
     */
    @NotNull(message = "Id不能为空")
    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名必须在3-50字符之间")
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 加密密码
     */
    @Schema(description = "加密密码(为空则不修改)", example = "123456")
    private String password;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 50, message = "昵称必须在2-50字符之间")
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = EMAIL, message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = CHINA_MOBILE, message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址", example = "https://oss.example.com/avatar.jpg")
    private String avatar;

    /**
     * 全局状态 (1-正常 0-禁用)
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态(通过枚举转换)", example = "正常")
    private UserStatus status;

    /**
     * 创建人ID
     * 默认为当前登录用户
     * 超级管理员可以指定创建人ID
     */
    @Schema(description = "创建人ID", example = "100")
    private Long createBy;

    /**
     * 创建人姓名
     * 默认为创建人姓名
     * 超级管理员可以指定创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 更新人ID
     * 默认为当前登录用户
     * 超级管理员可以指定更新人ID
     */
    @Schema(description = "更新人ID", example = "100")
    private Long updateBy;

    /**
     * 更新人姓名
     * 默认为创建人姓名
     * 超级管理员可以指定更新人姓名
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 创建时间
     * 默认为当前时间
     * 超级管理员可以指定创建时间
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 默认为当前时间
     * 超级管理员可以指定更新时间
     */
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除 (0-正常 1-删除)
     * 默认为0
     * 超级管理员可以指定逻辑删除状态
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "未删除")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

}
