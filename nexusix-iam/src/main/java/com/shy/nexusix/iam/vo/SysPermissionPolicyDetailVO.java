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

    @Schema(description = "创建人ID", example = "100")
    private Long createBy;

    @Schema(description = "逻辑删除描述", example = "未删除")
    private String isDeleted;

}
