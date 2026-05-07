package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户租户关联通用视图对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "用户租户关联通用视图对象")
public class SysUserTenantRelCommonVO {

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    private Long id;

    @Schema(description = "用户ID", example = "1987654321098765432")
    private Long userId;

    @Schema(description = "租户ID", example = "1987654321098765432")
    private Long tenantId;

    @Schema(description = "主部门ID", example = "1001")
    private Long deptId;

    @Schema(description = "是否租户管理员", example = "false")
    private Boolean isAdmin;

    @Schema(description = "加入时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime joinTime;

    @Schema(description = "逻辑删除", example = "未删除")
    private String isDeleted;

    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
