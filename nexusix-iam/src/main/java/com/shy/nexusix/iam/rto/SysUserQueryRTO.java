package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 用户条件查询请求对象
 * </p>
 *
 * @author shy
 * @since 2026-05-06
 */
@Data
@Schema(description = "用户条件查询请求对象")
public class SysUserQueryRTO extends PageCommonRTO {

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 状态 (1-正常 0-禁用)
     * 支持数字编码、中文描述或枚举名称查询
     */
    @Schema(description = "状态 (1-正常 0-禁用)，支持数字编码、中文描述或枚举名称", example = "启用")
    private String status;

    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP", example = "192.168.1.100")
    private String lastLoginIp;

    /**
     * 最后登录时间范围
     */
    @Schema(description = "最后登录时间范围(一个/多个)[第一个参数为开始时间;第二个参数为结束时间]", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO lastLoginTime;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "张三")
    private String createByName;

    /**
     * 创建时间范围
     */
    @Schema(description = "创建时间范围(一个/多个)[第一个参数为开始时间;第二个参数为结束时间]", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO createTime;

}
