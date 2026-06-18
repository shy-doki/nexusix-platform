package com.shy.nexusix.tenant.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>租户策略新增请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "租户策略新增请求对象")
public class SysTenantPolicyAddRTO {

    /**
     * 策略编码
     */
    @NotBlank(message = "策略编码不能为空")
    @Size(max = 100, message = "策略编码不能超过100字符")
    @Schema(description = "策略编码", example = "TP_DEPT_001")
    private String policyCode;

    /**
     * 策略名称
     */
    @NotBlank(message = "策略名称不能为空")
    @Size(min = 2, max = 100, message = "策略名称必须在2-100字符之间")
    @Schema(description = "策略名称", example = "财务部→万象集团")
    private String policyName;

    /**
     * 源实体类型
     */
    @NotBlank(message = "源实体类型不能为空")
    @Schema(description = "源实体类型：DEPT, ROLE", example = "DEPT")
    private String sourceType;

    /**
     * 源实体ID（系统部门ID或系统角色ID）
     */
    @NotNull(message = "源实体ID不能为空")
    @Schema(description = "源实体ID", example = "301")
    private Long sourceId;

    /**
     * 目标租户ID（系统租户ID）
     */
    @NotNull(message = "目标租户ID不能为空")
    @Schema(description = "目标租户ID", example = "101")
    private Long tenantId;

    /**
     * 绑定时间
     */
    @Schema(description = "绑定时间", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime bindTime;

    /**
     * 策略状态
     */
    @Schema(description = "策略状态：ACTIVE, DISABLED", example = "ACTIVE")
    private String status;

    /**
     * 禁用原因
     */
    @Size(max = 200, message = "禁用原因不能超过200字符")
    @Schema(description = "禁用原因", example = "ADMIN_DISABLE")
    private String disableReason;

    /**
     * 创建租户ID
     * 超级管理员可填
     */
    @Schema(description = "创建租户ID", example = "101")
    private Long createTenant;

    /**
     * 创建部门ID
     * 超级管理员可填
     */
    @Schema(description = "创建部门ID", example = "201")
    private Long createDept;

    /**
     * 创建角色ID
     * 超级管理员可填
     */
    @Schema(description = "创建角色ID", example = "401")
    private Long createRole;

    /**
     * 创建人编码
     * 超级管理员可填
     */
    @Schema(description = "创建人编码", example = "1")
    private String createByCode;

    /**
     * 创建时间
     * 超级管理员可填
     */
    @Schema(description = "创建时间", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新人编码
     * 超级管理员可填
     */
    @Schema(description = "更新人编码", example = "1")
    private String updateByCode;

    /**
     * 更新时间
     * 超级管理员可填
     */
    @Schema(description = "更新时间", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     * 超级管理员可填
     */
    @Schema(description = "逻辑删除", example = "NOT_DELETED")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

    /**
     * 删除时间
     * 超级管理员可填
     */
    @Schema(description = "删除时间", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
