package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 权限策略公共视图对象
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@Schema(description = "权限策略公共视图对象")
public class SysPermissionPolicyCommonVO {

    /**
     * 策略ID
     */
    @Schema(description = "策略ID", example = "1987654321098765432")
    private Long id;

    /**
     * 目标类型 (1-系统 2-租户 3-角色 4-用户)
     */
    @Schema(description = "目标类型", example = "租户")
    private String targetType;

    /**
     * 目标ID
     */
    @Schema(description = "目标ID", example = "1987654321098765432")
    private Long targetId;

    /**
     * 目标名称
     */
    @Schema(description = "目标名称", example = "某科技公司")
    private String targetName;

    /**
     * 关联权限ID
     */
    @Schema(description = "关联权限ID", example = "1987654321098765432")
    private Long permissionId;

    /**
     * 关联权限名称
     */
    @Schema(description = "关联权限名称", example = "用户管理")
    private String permName;

    /**
     * 动作 (1-允许 2-拒绝)
     * 通过枚举转换
     */
    @Schema(description = "动作", example = "允许")
    private String action;

    /**
     * 优先级 (数字越大优先级越高)
     */
    @Schema(description = "优先级", example = "100")
    private Integer priority;

    /**
     * 是否向下继承
     */
    @Schema(description = "是否向下继承", example = "true")
    private Boolean inheritanceEnabled;

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
