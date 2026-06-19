package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>角色公共视图对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "角色公共视图对象")
public class SysRoleCommonVO {

    /**
     * 角色编码
     */
    @Schema(description = "角色编码", example = "SUPER_ADMIN")
    private String roleCode;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "超级管理员")
    private String roleName;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述", example = "系统超级管理员，拥有所有权限")
    private String roleDesc;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "1")
    private String status;

    /**
     * 禁用原因
     */
    @Schema(description = "禁用原因", example = "违规操作")
    private String disableReason;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "李四")
    private String createByName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间", example = "2026-04-07 15:45:30")
    private LocalDateTime createTime;

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人姓名", example = "100")
    private String updateByName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     * 超级管理员可见
     */
    @Schema(description = "逻辑删除", example = "0")
    private String isDeleted;

    /**
     * 删除时间
     * 超级管理员可见
     */
    @Schema(description = "删除时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
