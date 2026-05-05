package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "权限更新请求对象")
public class SysPermissionUpdateRTO {

    /**
     * 主键ID（雪花算法）
     */
    @NotNull(message = "Id不能为空")
    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    private Long id;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(min = 2, max = 100, message = "权限名称必须在2-100字符之间")
    @Schema(description = "权限名称", example = "用户管理")
    private String permName;

    /**
     * 权限标识
     * 采用 模块:资源:操作 格式，如 system:user:add
     */
    @NotBlank(message = "权限标识不能为空")
    @Size(min = 2, max = 200, message = "权限标识必须在2-200字符之间")
    @Schema(description = "权限标识 (如 system:user:add)", example = "system:user:add")
    private String permCode;

    /**
     * 权限类型
     * 1-菜单 2-按钮 3-接口 4-数据字段
     */
    @NotNull(message = "权限类型不能为空")
    @Schema(description = "类型 (1-菜单 2-按钮 3-接口 4-数据字段)", example = "1")
    private Integer permType;

    /**
     * 父权限ID
     * 0表示顶级权限
     */
    @Schema(description = "父权限 ID (0表示顶级)", example = "0")
    private Long parentId;

    /**
     * 父权限名称
     */
    @NotBlank(message = "父权限名称不能为空")
    @Schema(description = "父权限名称", example = "用户管理")
    private String parentName;

    /**
     * 资源路径
     * 菜单类型对应路由地址，接口类型对应API路径
     */
    @Schema(description = "资源路径", example = "/system/user")
    private String path;

    /**
     * 状态(通过枚举转换)
     */
    @NotNull(message = "状态不能为空")
    @EnumField
    @Schema(description = "状态(通过枚举转换)", example = "启用")
    private GlobalEnum.Status status;

}
