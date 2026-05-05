package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色详情视图对象")
public class SysRoleDetailVO extends SysRoleCommonVO {

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID", example = "100")
    private Long createBy;

    /**
     * 逻辑删除描述
     * 未删除/已删除
     */
    @Schema(description = "逻辑删除描述", example = "未删除")
    private String isDeleted;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    private LocalDateTime updateTime;

}
