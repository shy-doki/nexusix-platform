package com.shy.nexusix.system.entity;

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
 * 菜单表 - 前端导航与按钮权限映射
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
@Schema(name="SysMenu对象", description="菜单表 - 前端导航与按钮权限映射")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "菜单名称", example = "用户管理")
    @TableField(value = "menu_name")
    private String menuName;

    @Schema(description = "类型 (1-目录 2-菜单 3-按钮)", example = "2")
    @TableField(value = "menu_type")
    private Integer menuType;

    @Schema(description = "父菜单 ID", example = "0")
    @TableField(value = "parent_id")
    private Long parentId;

    @Schema(description = "路由地址", example = "/system/user")
    @TableField(value = "path")
    private String path;

    @Schema(description = "组件路径", example = "system/user/index")
    @TableField(value = "component")
    private String component;

    @Schema(description = "权限标识", example = "system:user:list")
    @TableField(value = "perm_code")
    private String permCode;

    @Schema(description = "是否可见", example = "true")
    @TableField(value = "visible")
    private Boolean visible;

    @Schema(description = "状态 (1-正常 0-停用)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "排序", example = "1")
    @TableField(value = "sort_order")
    private Integer sortOrder;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
