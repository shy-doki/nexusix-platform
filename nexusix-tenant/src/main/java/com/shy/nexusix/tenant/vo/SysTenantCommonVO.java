package com.shy.nexusix.tenant.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "租户公共视图对象")
public class SysTenantCommonVO {

    /**
     * 租户编码
     */
    @Schema(description = "租户编码", example = "TEN00000001")
    private String tenantCode;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

    /**
     * 租户类型
     */
    @Schema(description = "租户类型", example = "餐饮、互联网")
    private String tenantType;

    /**
     * 父租户名称
     */
    @Schema(description = "父租户名称", example = "阿里云")
    private String parentName;

    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名", example = "张三")
    private String contactName;

    /**
     * 联系人电话
     */
    @Schema(description = "联系人电话", example = "13800138000")
    private String contactPhone;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "1")
    private String status;

    /**
     * 服务过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "服务过期时间", example = "2026-12-31 23:59:59")
    private LocalDateTime expireTime;

    /**
     * 是否有子租户
     */
    @Schema(description = "是否有子租户", example = "true")
    private Boolean hasChildren;

    /**
     * 当前主套餐
     */
    @Schema(description = "当前主套餐", example = "1001")
    private String packageName;

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

}
