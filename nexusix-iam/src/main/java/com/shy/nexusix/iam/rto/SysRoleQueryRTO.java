package com.shy.nexusix.iam.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>角色条件查询请求对象</p>
 *
 * @author shy
 */
@Data
@Schema(description = "角色条件查询请求对象")
public class SysRoleQueryRTO extends PageCommonRTO {

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
     * 状态
     */
    @Schema(description = "状态", example = "1")
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
