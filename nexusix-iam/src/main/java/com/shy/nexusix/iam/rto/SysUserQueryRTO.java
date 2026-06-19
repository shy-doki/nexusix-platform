package com.shy.nexusix.iam.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>用户条件查询请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "用户条件查询请求对象")
public class SysUserQueryRTO extends PageCommonRTO {

    /**
     * 用户编码
     */
    @Schema(description = "用户编码", example = "U001")
    private String userCode;

    /**
     * 用户名（登录名）
     */
    @Schema(description = "用户名（登录名）", example = "admin")
    private String userName;

    /**
     * 昵称
     */
    @Schema(description = "昵称", example = "管理员")
    private String nickName;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 电子邮箱
     */
    @Schema(description = "电子邮箱", example = "admin@example.com")
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码", example = "13800138000")
    private String phone;

    /**
     * 性别
     */
    @Schema(description = "性别", example = "MALE")
    private String gender;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "ENABLED")
    private String status;

    /**
     * 创建人编码
     */
    @Schema(description = "创建人编码", example = "100")
    private String createByCode;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "李四")
    private String createByName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private TimeRangeCommonRTO createTimeRange;

    /**
     * 更新人编码
     */
    @Schema(description = "更新人编码", example = "100")
    private String updateByCode;

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人姓名", example = "王五")
    private String updateByName;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间范围", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private TimeRangeCommonRTO updateTimeRange;

    /**
     * 逻辑删除
     * 超级管理员可填
     */
    @Schema(description = "逻辑删除", example = "0")
    private String isDeleted;

    /**
     * 删除时间
     * 超级管理员可填
     */
    @Schema(description = "删除时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
