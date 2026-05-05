package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限策略详情视图对象")
public class SysPermissionPolicyDetailVO extends SysPermissionPolicyCommonVO {

    /**
     * 权限标识（关联查询填充）
     */
    @Schema(description = "权限标识", example = "system:user:add")
    private String permCode;

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    private LocalDateTime updateTime;

}
