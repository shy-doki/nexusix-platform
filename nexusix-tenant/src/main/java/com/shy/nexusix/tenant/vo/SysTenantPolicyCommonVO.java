package com.shy.nexusix.tenant.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>租户策略公共视图对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "租户策略公共视图对象")
public class SysTenantPolicyCommonVO {

    /**
     * 策略编码
     */
    @Schema(description = "策略编码", example = "TP_DEPT_001")
    private String policyCode;

    /**
     * 策略名称
     */
    @Schema(description = "策略名称", example = "财务部→万象集团")
    private String policyName;

    /**
     * 源实体类型
     */
    @Schema(description = "源实体类型：DEPT, ROLE", example = "DEPT")
    private String sourceType;

    /**
     * 源实体编码（部门编码或角色编码）
     */
    @Schema(description = "源实体编码", example = "DEPT_001")
    private String sourceCode;

    /**
     * 源实体名称（部门名称或角色名称）
     */
    @Schema(description = "源实体名称", example = "财务部")
    private String sourceName;

    /**
     * 目标租户编码
     */
    @Schema(description = "目标租户编码", example = "TEN_001")
    private String tenantCode;

    /**
     * 目标租户名称
     */
    @Schema(description = "目标租户名称", example = "万象集团")
    private String tenantName;

    /**
     * 绑定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "绑定时间", example = "2026-06-12 15:45:30")
    private LocalDateTime bindTime;

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
