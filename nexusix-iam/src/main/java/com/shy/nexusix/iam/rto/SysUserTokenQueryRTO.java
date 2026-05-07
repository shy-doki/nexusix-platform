package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 用户Token条件查询请求对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "用户Token条件查询请求对象")
public class SysUserTokenQueryRTO extends PageCommonRTO {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1987654321098765432")
    private String userId;

    /**
     * 用户名称
     */
    @Schema(description = "用户名称", example = "张三")
    private String userName;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID", example = "1987654321098765432")
    private String tenantId;

    /**
     * 状态 (1-有效 0-无效)
     */
    @Schema(description = "状态 (1-有效 0-无效)，支持数字编码、中文描述或枚举名称", example = "有效")
    private String status;

    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP", example = "192.168.1.100")
    private String loginIp;

    /**
     * 登录时间范围
     */
    @Schema(description = "登录时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO loginTime;

    /**
     * 过期时间范围
     */
    @Schema(description = "过期时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO expireTime;

    /**
     * 创建时间范围
     */
    @Schema(description = "创建时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO createTime;

}
