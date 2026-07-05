package com.shy.nexusix.tenant.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>租户公共视图对象</p>
 *
 * @author shy
 */
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
     * 租户类型（同时存储行业类型）
     */
    @Schema(description = "租户类型（同时存储行业类型）", example = "internet")
    private String tenantType;

    /**
     * 租户地址
     */
    @Schema(description = "租户地址", example = "浙江省杭州市上城区万象大厦18层")
    private String tenantAddress;

    /**
     * 租户描述
     */
    @Schema(description = "租户描述", example = "专注软件开发与技术服务")
    private String tenantDesc;

    /**
     * 租户企业规模
     */
    @Schema(description = "租户企业规模", example = "51-200")
    private String tenantScale;

    /**
     * 租户logo路径
     */
    @Schema(description = "租户logo路径", example = "/upload/tenant/logo/2026/05/13/xxx.png")
    private String tenantLogoUrl;

    /**
     * 租户邀请码
     */
    @Schema(description = "租户邀请码", example = "AB12CD34")
    private String inviteCode;

    /**
     * 父租户ID
     */
    @Schema(description = "父租户ID", example = "0")
    private Long parentId;

    /**
     * 层级深度
     */
    @Schema(description = "层级深度", example = "1")
    private Integer level;

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
     * 联系人邮箱
     */
    @Schema(description = "联系人邮箱", example = "zhangsan@example.com")
    private String contactEmail;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "1")
    private String status;

    /**
     * 禁用原因
     */
    @Schema(description = "禁用原因", example = "ADMIN_DISABLE")
    private String disableReason;

    /**
     * 服务过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "服务过期时间", example = "2026-12-31 23:59:59")
    private LocalDateTime expireTime;

    /**
     * 套餐ID
     */
    @Schema(description = "套餐ID", example = "1")
    private Long packageId;

    /**
     * 是否有子租户
     */
    @Schema(description = "是否有子租户", example = "true")
    private Boolean hasChildren;

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

    /**
     * 删除时间
     * 超级管理员可见
     */
    @Schema(description = "删除时间", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

}
