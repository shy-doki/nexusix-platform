package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "权限公共视图对象")
public class SysPermissionCommonVO {

    /**
     * 权限ID（雪花算法）
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
     * 采用 模块:资源:操作 格式
     */
    @Schema(description = "权限标识", example = "system:user:add")
    private String permCode;

    /**
     * 权限类型描述
     * 菜单/按钮/接口/数据字段
     */
    @Schema(description = "类型描述", example = "菜单")
    private String permType;

    /**
     * 父权限ID
     * 0表示顶级权限
     */
    @Schema(description = "父权限ID", example = "0")
    private Long parentId;

    /**
     * 父权限名称
     */
    @Schema(description = "父权限名称", example = "用户管理")
    private String parentName;

    /**
     * 状态描述
     * 正常/禁用
     */
    @Schema(description = "状态描述", example = "启用")
    private String status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    private LocalDateTime createTime;

    /**
     * 逻辑删除描述
     * 未删除/已删除
     */
    @Schema(description = "逻辑删除描述", example = "未删除")
    private String isDeleted;

}
