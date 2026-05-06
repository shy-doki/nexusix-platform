package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 权限公共视图对象
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@Schema(description = "权限公共视图对象")
public class SysPermissionCommonVO {

    /**
     * 权限ID
     */
    @Schema(description = "权限ID", example = "1987654321098765432")
    private Long id;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称", example = "用户管理")
    private String permName;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识", example = "system:user:add")
    private String permCode;

    /**
     * 类型
     */
    @Schema(description = "类型", example = "菜单")
    private String permType;

    /**
     * 父权限ID
     */
    @Schema(description = "父权限ID", example = "1987654321098765432")
    private Long parentId;

    /**
     * 父权限名称
     */
    @Schema(description = "父权限名称", example = "系统管理")
    private String parentName;

    /**
     * 资源路径
     */
    @Schema(description = "资源路径", example = "/system/user")
    private String path;

    /**
     * 状态 (1-启用 0-禁用)
     * 通过枚举转换
     */
    @Schema(description = "状态", example = "启用")
    private String status;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    private LocalDateTime createTime;

    /**
     * 逻辑删除 (0-正常 1-删除)
     * 超级管理员可见
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    private String isDeleted;

}
