package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "权限公共视图对象")
public class SysPermissionCommonVO {

    @Schema(description = "权限ID", example = "1987654321098765432")
    private Long id;

    @Schema(description = "权限名称", example = "用户管理")
    private String permName;

    @Schema(description = "权限标识", example = "system:user:add")
    private String permCode;

    @Schema(description = "类型描述", example = "菜单")
    private String permType;

    @Schema(description = "父权限ID", example = "0")
    private Long parentId;

    @Schema(description = "资源路径", example = "/system/user")
    private String path;

    @Schema(description = "状态描述", example = "启用")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    private LocalDateTime createTime;

}
