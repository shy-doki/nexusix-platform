package com.shy.nexusix.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author shy
 * @since 2026-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dept")
@Schema(name = "SysDept对象", description = "部门表")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "部门编码", example = "DEPT_001")
    @TableField(value = "dept_code")
    private String deptCode;

    @Schema(description = "部门名称", example = "技术部")
    @TableField(value = "dept_name")
    private String deptName;

    @Schema(description = "部门描述", example = "负责公司技术研发")
    @TableField(value = "dept_desc")
    private String deptDesc;

    @Schema(description = "父部门ID", example = "100")
    @TableField(value = "parent_id")
    private Long parentId;

    @Schema(description = "父部门编码", example = "DEPT_000")
    @TableField(value = "parent_code")
    private String parentCode;

    @Schema(description = "父部门名称", example = "总公司")
    @TableField(value = "parent_name")
    private String parentName;

    @Schema(description = "部门层级路径", example = "/总公司/技术部")
    @TableField(value = "path")
    private String path;

    @Schema(description = "所属租户ID", example = "1001")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "租户编码", example = "TENANT_001")
    @TableField(value = "tenant_code")
    private String tenantCode;

    @Schema(description = "租户名称", example = "某某科技有限公司")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "部门负责人ID", example = "2001")
    @TableField(value = "leader_id")
    private Long leaderId;

    @Schema(description = "部门负责人姓名", example = "张三")
    @TableField(value = "leader_name")
    private String leaderName;

    @Schema(description = "排序序号", example = "10")
    @TableField(value = "sort_order")
    private Integer sortOrder;

    @Schema(description = "部门状态", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "禁用原因", example = "ADMIN_DISABLE")
    @TableField(value = "disable_reason")
    private String disableReason;

    @Schema(description = "创建时所属租户ID", example = "1001")
    @TableField(value = "create_tenant")
    private Long createTenant;

    @Schema(description = "创建时所属部门ID", example = "100")
    @TableField(value = "create_dept")
    private Long createDept;

    @Schema(description = "创建时使用角色ID", example = "3001")
    @TableField(value = "create_role")
    private Long createRole;

    @Schema(description = "创建人ID", example = "4001")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "创建时间", example = "2026-06-11 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_at")
    private LocalDateTime createAt;

    @Schema(description = "更新人ID", example = "4001")
    @TableField(value = "update_by")
    private Long updateBy;

    @Schema(description = "更新时间", example = "2026-06-11 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_at")
    private LocalDateTime updateAt;

    @Schema(description = "逻辑删除标记", example = "NOT_DELETED")
    @TableField(value = "is_deleted")
    private String isDeleted;

    @Schema(description = "删除时间", example = "2026-06-11 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;
}