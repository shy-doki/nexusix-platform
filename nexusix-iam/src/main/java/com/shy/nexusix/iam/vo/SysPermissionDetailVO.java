package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限详情视图对象")
public class SysPermissionDetailVO extends SysPermissionCommonVO {

    /**
     * 资源路径
     */
    @Schema(description = "资源路径", example = "/system/user")
    private String path;

}
