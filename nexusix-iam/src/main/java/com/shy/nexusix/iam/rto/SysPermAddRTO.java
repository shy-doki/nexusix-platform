package com.shy.nexusix.iam.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>权限新增请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "权限新增请求对象")
public class SysPermAddRTO {

    /**
     * 权限编码
     */
    @NotBlank(message = "权限编码不能为空")
    @Schema(description = "权限编码", example = "PERM_USER_MANAGE")
    private String permCode;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(min = 2, max = 100, message = "权限名称必须在2-100字符之间")
    @Schema(description = "权限名称", example = "用户管理")
    private String permName;

    /**
     * 权限描述
     */
    @Size(max = 500, message = "权限描述不能超过500字符")
    @Schema(description = "权限描述", example = "用户管理相关权限")
    private String permDesc;

    /**
     * 权限类型
     */
    @NotBlank(message = "权限类型不能为空")
    @Schema(description = "权限类型：MENU, BUTTON, API, DATA", example = "MENU")
    private String permType;

    /**
     * 父权限编码
     */
    @NotNull(message = "父权限编码不能为空")
    @Schema(description = "父权限编码", example = "0")
    private String parentCode;

    /**
     * 父权限名称
     */
    @Schema(description = "父权限名称", example = "系统管理")
    private String parentName;

    /**
     * 权限层级路径
     * 超级管理员可填
     */
    @Schema(description = "权限层级路径", example = "/501")
    private String path;

    /**
     * 资源类型
     */
    @Schema(description = "资源类型：URL, METHOD, TABLE等", example = "URL")
    private String resourceType;

    /**
     * 资源路径
     */
    @Schema(description = "资源路径", example = "/api/user/list")
    private String resourcePath;

    /**
     * 资源方法
     */
    @Schema(description = "资源方法", example = "GET")
    private String resourceMethod;

    /**
     * 图标
     */
    @Schema(description = "图标", example = "icon-user")
    private String icon;

    /**
     * 排序序号
     */
    @Schema(description = "排序序号", example = "0")
    private Integer sortOrder;

    /**
     * 是否可见
     */
    @Schema(description = "是否可见", example = "true")
    private Boolean isVisible;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态", example = "启用")
    private GlobalEnum.PermStatus status;

    /**
     * 禁用原因
     */
    @Size(max = 200, message = "禁用原因不能超过200字符")
    @Schema(description = "禁用原因", example = "权限调整中")
    private String disableReason;

    /**
     * 创建人编码
     * 超级管理员可填
     */
    @Schema(description = "创建人编码", example = "100")
    private String createByCode;

    /**
     * 创建人姓名
     * 超级管理员可填
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 创建时间
     * 超级管理员可填
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

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
    @Schema(description = "逻辑删除", example = "0")
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
