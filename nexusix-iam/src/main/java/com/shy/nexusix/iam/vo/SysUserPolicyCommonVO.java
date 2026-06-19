package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>用户策略公共视图对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "用户策略公共视图对象")
public class SysUserPolicyCommonVO {

    /**
     * 策略编码
     */
    @Schema(description = "策略编码", example = "UP_TENANT_001")
    private String policyCode;

    /**
     * 策略名称
     */
    @Schema(description = "策略名称", example = "张三→万象集团")
    private String policyName;

    /**
     * 用户编码
     */
    @Schema(description = "用户编码", example = "USR_001")
    private String userCode;

    /**
     * 用户名称
     */
    @Schema(description = "用户名称", example = "张三")
    private String userName;

    /**
     * 目标类型
     */
    @Schema(description = "目标类型：TENANT, DEPT, ROLE", example = "TENANT")
    private String targetType;

    /**
     * 目标编码（租户编码、部门编码或角色编码）
     */
    @Schema(description = "目标编码", example = "TEN_001")
    private String targetCode;

    /**
     * 目标名称（租户名称、部门名称或角色名称）
     */
    @Schema(description = "目标名称", example = "万象集团")
    private String targetName;

    /**
     * 是否为主租户/主部门
     */
    @Schema(description = "是否为主租户/主部门", example = "false")
    private Boolean isPrimary;

    /**
     * 策略状态
     */
    @Schema(description = "策略状态", example = "ACTIVE")
    private String status;

    /**
     * 禁用原因
     */
    @Schema(description = "禁用原因", example = "ADMIN_DISABLE")
    private String disableReason;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "李四")
    private String createByName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间", example = "2026-06-12 15:45:30")
    private LocalDateTime createTime;

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人姓名", example = "王五")
    private String updateByName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间", example = "2026-06-12 15:45:30")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     * 超级管理员可见
     */
    @Schema(description = "逻辑删除", example = "NOT_DELETED")
    private String isDeleted;

    /**
     * 删除时间
     * 超级管理员可见
     */
    @Schema(description = "删除时间", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
