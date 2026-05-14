package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色表-定义系统/租户/用户级角色
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@Schema(name="SysRole对象", description="角色表-定义系统/租户/用户级角色")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "角色名称", example = "系统管理员")
    @TableField(value = "role_name")
    private String roleName;

    @Schema(description = "角色描述", example = "拥有系统所有权限")
    @TableField(value = "role_desc")
    private String roleDesc;

    @Schema(description = "角色编码", example = "ROLE_ADMIN")
    @TableField(value = "role_code")
    private String roleCode;

    @Schema(description = "所属租户ID", example = "TENANT_001")
    @TableField(value = "tenant_id")
    private String tenantId;

    @Schema(description = "租户名称", example = "某某科技有限公司")
    @TableField(value = "tenant_name")
    private String tenantName;

    @Schema(description = "数据范围", example = "ALL")
    @TableField(value = "data_scope")
    private String dataScope;

    @Schema(description = "状态", example = "ENABLED")
    @TableField(value = "status")
    private String status;

    @Schema(description = "排序顺序", example = "1")
    @TableField(value = "sort_order")
    private Integer sortOrder;

    @Schema(description = "创建人ID", example = "100")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "创建人姓名", example = "张三")
    @TableField(value = "create_by_name")
    private String createByName;

    @Schema(description = "更新人ID", example = "100")
    @TableField(value = "update_by")
    private String updateBy;

    @Schema(description = "更新人姓名", example = "张三")
    @TableField(value = "update_by_name")
    private String updateByName;

    @Schema(description = "创建时间", format = "date-time", example = "2026-05-13 15:45:30")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", format = "date-time", example = "2026-05-13 15:45:30")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除", example = "ACTIVE")
    @TableField(value = "is_deleted")
    private String isDeleted;


}
