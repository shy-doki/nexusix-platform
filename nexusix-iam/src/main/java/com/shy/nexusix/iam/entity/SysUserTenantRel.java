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
 * 用户-租户关联表-实现用户与多租户绑定
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_tenant_rel")
@Schema(name="SysUserTenantRel对象", description="用户-租户关联表-实现用户与多租户绑定")
public class SysUserTenantRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "用户编码")
    private String userCode;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "租户编码")
    private String tenantCode;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "主部门编码")
    private String deptCode;

    @Schema(description = "是否租户管理员")
    private Boolean isAdmin;

    @Schema(description = "加入时间")
    private LocalDateTime joinTime;

    @Schema(description = "是否默认租户")
    private Boolean isDefault;

    @Schema(description = "创建人编码")
    private String createBy;

    @Schema(description = "创建人名称")
    private String createByName;

    @Schema(description = "更新人编码")
    private String updateBy;

    @Schema(description = "更新人名称")
    private String updateByName;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    private String isDeleted;


}
