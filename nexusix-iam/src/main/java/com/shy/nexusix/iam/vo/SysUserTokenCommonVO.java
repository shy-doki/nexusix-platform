package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户Token通用视图对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "用户Token通用视图对象")
public class SysUserTokenCommonVO {

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
     * 用户名称
     */
    @Schema(description = "用户名称", example = "张三")
    private String userName;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID", example = "1987654321098765432")
    private Long tenantId;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "某科技公司")
    private String tenantName;

    /**
     * 设备信息
     */
    @Schema(description = "设备信息", example = "{\"device\": \"Chrome\", \"os\": \"Windows\"}")
    private String deviceInfo;

    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP", example = "192.168.1.100")
    private String loginIp;

    /**
     * 登录时间
     */
    @Schema(description = "登录时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime loginTime;

    /**
     * 过期时间
     */
    @Schema(description = "过期时间", example = "2026-04-08 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireTime;

    /**
     * 状态(中文描述)
     */
    @Schema(description = "状态", example = "有效")
    private String status;

    /**
     * 逻辑删除(中文描述)
     */
    @Schema(description = "逻辑删除", example = "未删除")
    private String isDeleted;

}
