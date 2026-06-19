package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>权限策略详情视图对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "权限策略详情视图对象")
public class SysPermPolicyDetailVO {

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
     * 权限ID
     */
    @Schema(description = "权限ID", example = "511")
    private Long permId;

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
     * 目标ID
     */
    @Schema(description = "目标ID", example = "101")
    private Long targetId;

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
     * 字段级权限配置
     */
    @Schema(description = "字段级权限配置", example = "{\"field_name\": [\"READ\",\"CREATE\",\"UPDATE\",\"DELETE\"]}")
    private String fieldPermissions;

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
     * 创建租户ID
     */
    @Schema(description = "创建租户ID", example = "101")
    private Long createTenant;

    /**
     * 创建部门ID
     */
    @Schema(description = "创建部门ID", example = "201")
    private Long createDept;

    /**
     * 创建角色ID
     */
    @Schema(description = "创建角色ID", example = "401")
    private Long createRole;

    /**
     * 创建人编码
     */
    @Schema(description = "创建人编码", example = "1")
    private Long createByCode;

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
     * 更新人编码
     */
    @Schema(description = "更新人编码", example = "1")
    private Long updateByCode;

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
     */
    @Schema(description = "逻辑删除", example = "NOT_DELETED")
    private String isDeleted;

    /**
     * 删除时间
     */
    @Schema(description = "删除时间", example = "2026-06-12 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
