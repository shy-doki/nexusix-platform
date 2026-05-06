package com.shy.nexusix.iam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户详情视图对象
 * </p>
 * <p>
 * 注意：password 字段不得在视图对象中暴露，确保用户密码安全
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户详情视图对象")
public class SysUserDetailVO extends SysUserCommonVO {

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    private LocalDateTime updateTime;

}
