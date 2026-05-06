package com.shy.nexusix.iam.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.Action;
import com.shy.nexusix.common.enums.GlobalEnum.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.shy.nexusix.common.constant.RegexConstant.Code.SNOWFLAKE_ID;

/**
 * <p>
 * 权限策略新增请求对象
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@Schema(description = "权限策略新增请求对象")
public class SysPermissionPolicyAddRTO {

    /**
     * 目标类型 (1-系统 2-租户 3-角色 4-用户)
     */
    @NotNull(message = "目标类型不能为空")
    @EnumField
    @Schema(description = "目标类型(通过枚举转换)", example = "租户")
    private TargetType targetType;

    /**
     * 目标ID (对应租户/角色/用户 ID)
     */
    @NotBlank(message = "目标ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "目标ID格式不正确")
    @Schema(description = "目标ID", example = "1987654321098765432")
    private String targetId;

    /**
     * 目标名称(对应租户/角色/用户名称)
     */
    @NotBlank(message = "目标名称不能为空")
    @Size(min = 2, max = 100, message = "目标名称必须在2-100字符之间")
    @Schema(description = "目标名称", example = "某科技公司")
    private String targetName;

    /**
     * 关联权限ID
     */
    @NotBlank(message = "关联权限ID不能为空")
    @Pattern(regexp = SNOWFLAKE_ID, message = "关联权限ID格式不正确")
    @Schema(description = "关联权限ID", example = "1987654321098765432")
    private String permissionId;

    /**
     * 关联权限名称
     */
    @NotBlank(message = "关联权限名称不能为空")
    @Size(min = 2, max = 100, message = "关联权限名称必须在2-100字符之间")
    @Schema(description = "关联权限名称", example = "用户管理")
    private String permName;

    /**
     * 动作 (1-允许 2-拒绝)
     */
    @NotNull(message = "动作不能为空")
    @EnumField
    @Schema(description = "动作(通过枚举转换)", example = "允许")
    private Action action;

    /**
     * 优先级 (数字越大优先级越高)
     */
    @NotNull(message = "优先级不能为空")
    @Min(value = 0, message = "优先级不能为负数")
    @Max(value = 9999, message = "优先级不能超过9999")
    @Schema(description = "优先级 (数字越大优先级越高)", example = "100")
    private Integer priority;

    /**
     * 是否向下继承
     */
    @NotNull(message = "是否向下继承不能为空")
    @Schema(description = "是否向下继承", example = "true")
    private Boolean inheritanceEnabled;

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
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

}
