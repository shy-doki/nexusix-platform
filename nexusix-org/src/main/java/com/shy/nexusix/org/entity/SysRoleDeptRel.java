package com.shy.nexusix.org.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色数据权限关联表 - 角色自定义数据范围时关联的部门
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_dept_rel")
@Schema(name="SysRoleDeptRel对象", description="角色数据权限关联表 - 角色自定义数据范围时关联的部门")
public class SysRoleDeptRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "角色 ID", example = "1001")
    @TableField(value = "role_id")
    private Long roleId;

    @Schema(description = "部门 ID（针对数据可见度为自定义时解决如何知道该用户能看哪些部门）", example = "1001")
    @TableField(value = "dept_id")
    private Long deptId;

    @Schema(description = "租户 ID（这里解决用户每次登录，只能选择一个租户进入，进入租户 A，就只能看到租户 A 的数据，进入租户 B，就只能看到租户 B 的数据，绝对不允许在租户 A 里看到租户 B 的部门 / 角色 / 权限）", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


}
