package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "权限策略公共视图对象")
public class SysPermissionPolicyCommonVO {

    @Schema(description = "策略ID", example = "1987654321098765432")
    private Long id;

    @Schema(description = "目标类型描述", example = "租户")
    private String targetType;

    @Schema(description = "目标ID", example = "1987654321098765432")
    private Long targetId;

    @Schema(description = "关联权限ID", example = "1001")
    private Long permissionId;

    @Schema(description = "权限名称", example = "用户管理")
    private String permName;

    @Schema(description = "权限标识", example = "system:user:add")
    private String permCode;

    @Schema(description = "动作描述", example = "允许")
    private String action;

    @Schema(description = "优先级", example = "100")
    private Integer priority;

    @Schema(description = "是否向下继承", example = "true")
    private Boolean inheritanceEnabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    private LocalDateTime createTime;

}
