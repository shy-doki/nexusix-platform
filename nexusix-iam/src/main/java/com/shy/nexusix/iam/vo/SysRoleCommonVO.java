package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "角色公共视图对象")
public class SysRoleCommonVO {

    /**
     * 角色ID（雪花算法）
     */
    @Schema(description = "角色ID", example = "1987654321098765432")
    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码", example = "ROLE_ADMIN")
    private String roleCode;

    /**
     * 角色层级描述
     * 系统/租户/用户
     */
    @Schema(description = "角色层级描述", example = "租户")
    private String roleLevel;

    /**
     * 所属租户ID
     */
    @Schema(description = "所属租户ID", example = "0")
    private Long tenantId;

    /**
     * 数据范围描述
     * 全部/本部门/本人/自定义
     */
    @Schema(description = "数据范围描述", example = "全部")
    private String dataScope;

    /**
     * 状态描述
     * 正常/禁用
     */
    @Schema(description = "状态描述", example = "启用")
    private String status;

    /**
     * 排序
     */
    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    private LocalDateTime createTime;

}
