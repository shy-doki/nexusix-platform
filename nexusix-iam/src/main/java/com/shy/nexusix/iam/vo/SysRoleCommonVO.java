package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 角色公共视图对象
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@Schema(description = "角色公共视图对象")
public class SysRoleCommonVO {

    /**
     * 角色ID
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
     * 角色层级
     * 通过枚举转换
     */
    @Schema(description = "角色层级", example = "租户级")
    private String roleLevel;

    /**
     * 所属租户ID
     */
    @Schema(description = "所属租户ID", example = "1987654321098765432")
    private Long tenantId;

    /**
     * 所属租户名称
     */
    @Schema(description = "所属租户名称", example = "某科技公司")
    private String tenantName;

    /**
     * 数据范围
     * 通过枚举转换
     */
    @Schema(description = "数据范围", example = "全部")
    private String dataScope;

    /**
     * 状态
     * 通过枚举转换
     */
    @Schema(description = "状态", example = "启用")
    private String status;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    private LocalDateTime createTime;

    /**
     * 逻辑删除 (0-正常 1-删除)
     * 超级管理员可见
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    private String isDeleted;

}
