package com.shy.nexusix.iam.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>角色更新请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "角色更新请求对象")
public class SysRoleUpdateRTO {

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Schema(description = "角色编码", example = "SUPER_ADMIN")
    private String roleCode;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 100, message = "角色名称必须在2-100字符之间")
    @Schema(description = "角色名称", example = "超级管理员")
    private String roleName;

    /**
     * 角色描述
     */
    @Size(max = 500, message = "角色描述不能超过500字符")
    @Schema(description = "角色描述", example = "系统超级管理员，拥有所有权限")
    private String roleDesc;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态", example = "启用")
    private GlobalEnum.RoleStatus status;

    /**
     * 禁用原因
     */
    @Size(max = 200, message = "禁用原因不能超过200字符")
    @Schema(description = "禁用原因", example = "违规操作")
    private String disableReason;

    /**
     * 创建人编码
     * 超级管理员可填
     */
    @Schema(description = "创建人编码", example = "100")
    private String createByCode;

    /**
     * 更新人编码
     * 超级管理员可填
     */
    @Schema(description = "更新人编码", example = "100")
    private String updateByCode;

    /**
     * 更新人姓名
     * 超级管理员可填
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 创建时间
     * 超级管理员可填
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 超级管理员可填
     */
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     * 超级管理员可填
     */
    @Schema(description = "逻辑删除", example = "未删除")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

    /**
     * 删除时间
     * 超级管理员可填
     */
    @Schema(description = "删除时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
