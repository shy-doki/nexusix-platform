package com.shy.nexusix.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 登录日志表 - 记录用户登录信息
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_login_log")
@Schema(name="SysLoginLog对象", description="登录日志表 - 记录用户登录信息")
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户 ID", example = "1987654321098765432")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "用户名", example = "admin")
    @TableField(value = "username")
    private String username;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "登录 IP", example = "192.168.1.100")
    @TableField(value = "ip_address")
    private String ipAddress;

    @Schema(description = "登录地点", example = "北京市")
    @TableField(value = "login_location")
    private String loginLocation;

    @Schema(description = "浏览器", example = "Chrome 120")
    @TableField(value = "browser")
    private String browser;

    @Schema(description = "操作系统", example = "Windows 11")
    @TableField(value = "os")
    private String os;

    @Schema(description = "状态 (1-成功 0-失败)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "提示消息", example = "登录成功")
    @TableField(value = "msg")
    private String msg;

    @Schema(description = "登录时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "login_time")
    private LocalDateTime loginTime;


}
