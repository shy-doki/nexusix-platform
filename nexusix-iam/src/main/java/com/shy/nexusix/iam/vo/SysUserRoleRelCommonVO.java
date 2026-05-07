package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户角色关联通用视图对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "用户角色关联通用视图对象")
public class SysUserRoleRelCommonVO {

    /**
     * 主键Id
     */
    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1987654321098765432")
    private Long userId;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID", example = "1001")
    private Long roleId;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID", example = "1987654321098765432")
    private Long tenantId;

    /**
     * 逻辑删除(中文描述)
     */
    @Schema(description = "逻辑删除", example = "未删除")
    private String isDeleted;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
