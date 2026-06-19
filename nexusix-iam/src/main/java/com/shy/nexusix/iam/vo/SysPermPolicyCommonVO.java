package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>权限策略公共视图对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "权限策略公共视图对象")
public class SysPermPolicyCommonVO {

    /**
     * 策略编码
     */
    @Schema(description = "策略编码", example = "PP_001")
    private String policyCode;

    /**
     * 策略名称
     */
    @Schema(description = "策略名称", example = "用户管理权限→万象集团")
    private String policyName;

    /**
     * 权限编码
     */
    @Schema(description = "权限编码", example = "PERM_USER_MGR")
    private String permCode;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称", example = "用户管理")
    private String permName;

    /**
     * 目标类型
     */
    @Schema(description = "目标类型：TENANT, DEPT, ROLE, USER", example = "TENANT")
    private String targetType;

    /**
     * 目标编码
     */
    @Schema(description = "目标编码", example = "TEN_001")
    private String targetCode;

    /**
     * 目标名称
     */
    @Schema(description = "目标名称", example = "万象集团")
    private String targetName;

    /**
     * 数据范围
     */
    @Schema(description = "数据范围：ALL, DEPT_AND_SUB, DEPT, SELF", example = "ALL")
    private String dataScope;

    /**
     * 关联的数据表名
     */
    @Schema(description = "关联的数据表名", example = "sys_user")
    private String tableName;

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
