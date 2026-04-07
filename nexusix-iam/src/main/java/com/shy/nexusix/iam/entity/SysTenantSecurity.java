package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租户安全策略配置表 - 定义租户密码策略和登录限制
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tenant_security")
@Schema(name="SysTenantSecurity对象", description="租户安全策略配置表 - 定义租户密码策略和登录限制")
public class SysTenantSecurity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "密码最小长度", example = "8")
    @TableField(value = "pwd_min_length")
    private Integer pwdMinLength;

    @Schema(description = "密码复杂度 (0-无 1-字母 + 数字 2-字母 + 数字 + 特殊字符)", example = "2")
    @TableField(value = "pwd_complexity")
    private Integer pwdComplexity;

    @Schema(description = "密码过期天数 (0-永不过期)", example = "90")
    @TableField(value = "pwd_expire_days")
    private Integer pwdExpireDays;

    @Schema(description = "登录失败锁定次数", example = "5")
    @TableField(value = "login_fail_limit")
    private Integer loginFailLimit;

    @Schema(description = "锁定时长 (分钟)", example = "30")
    @TableField(value = "lock_duration")
    private Integer lockDuration;

    @Schema(description = "更新人 ID", example = "100")
    @TableField(value = "update_by")
    private Long updateBy;

    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;


}
