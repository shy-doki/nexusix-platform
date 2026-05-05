package com.shy.nexusix.iam.rto;

import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "权限条件查询请求对象")
public class SysPermissionQueryRTO extends PageCommonRTO {

    /**
     * 权限名称（模糊匹配）
     */
    @Schema(description = "权限名称", example = "用户管理")
    private String permName;

    /**
     * 权限标识（模糊匹配）
     */
    @Schema(description = "权限标识", example = "system:user")
    private String permCode;

    /**
     * 权限类型
     * 1-菜单 2-按钮 3-接口 4-数据字段
     */
    @Schema(description = "权限类型 (1-菜单 2-按钮 3-接口 4-数据字段)", example = "菜单")
    private String permType;

    /**
     * 状态
     * 1-正常 0-禁用
     */
    @Schema(description = "状态 (1-正常 0-禁用)", example = "正常")
    private String status;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "李四")
    private String createByName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间范围(一个/多个)[第一个参数为开始时间;第二个参数为结束时间]", example = "{\"startTime\":\"2026-01-01 00:00:00\",\"endTime\":\"2026-12-31 23:59:59\"}")
    private TimeRangeCommonRTO createTime;

}
