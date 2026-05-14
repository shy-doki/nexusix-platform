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
 * 用户角色关联表 - 用户与角色的绑定关系
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_role_rel")
@Schema(name="SysUserRoleRel对象", description="用户角色关联表 - 用户与角色的绑定关系")
public class SysUserRoleRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "用户ID", example = "1001")
    @TableField(value = "user_id")
    private String userId;

    @Schema(description = "角色ID", example = "3001")
    @TableField(value = "role_id")
    private String roleId;

    @Schema(description = "租户ID", example = "TENANT_001")
    @TableField(value = "tenant_id")
    private String tenantId;

    @Schema(description = "角色生效时间", example = "2026-05-13 00:00:00")
    @TableField(value = "effective_time")
    private LocalDateTime effectiveTime;

    @Schema(description = "角色失效时间", example = "2027-05-13 23:59:59")
    @TableField(value = "expire_time")
    private LocalDateTime expireTime;

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
