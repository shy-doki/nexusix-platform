package com.shy.nexusix.tenant.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysTenantCommonVO {

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "某某科技有限公司")
    private String tenantName;

    /**
     * 租户编码（脱敏）
     */
    @Schema(description = "租户编码（脱敏）", example = "TEN******001")
    private String tenantCode;

    /**
     * 父租户名称
     * 新增、更新、删除操作需要同步该字段
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
     * 状态（0-禁用 1-启用）
     * 通过枚举转换
     */
    @Schema(description = "状态（0-禁用 1-启用）", example = "1")
    private String status;

    /**
     * 服务过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "服务过期时间", example = "2026-12-31 23:59:59")
    private LocalDateTime expireTime;

    /**
     * 创建人姓名
     * 新增、更新、删除操作需要同步该字段
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
     * 逻辑删除 (0-正常 1-删除)
     * 超级管理员可见
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    private String isDeleted;

}
