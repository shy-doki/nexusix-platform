package com.shy.nexusix.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 文件信息视图对象 - 用于返回给前端的文件元数据
 * </p>
 * <p>
 * 包含文件的基本信息和上传记录，作为文件详情VO的基类使用
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@Schema(description = "文件基础信息响应对象")
public class SysFileCommonVO {

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "华东公司")
    private String tenantName;

    /**
     * 原始文件名（用户上传时的文件名）
     */
    @Schema(description = "原始文件名", example = "企业营业执照.pdf")
    private String originalName;

    /**
     * 文件大小（单位：字节）
     */
    @Schema(description = "文件大小（字节）", example = "2048576")
    private Long fileSize;

    /**
     * 文件格式（小写扩展名，如：pdf、jpg、png）
     */
    @Schema(description = "文件格式（小写扩展名）", example = "pdf")
    private String fileType;

    /**
     * 业务类型分类（如：logo、avatar、contract、license等）
     */
    @Schema(description = "业务类型分类", example = "license")
    private String bizType;

    /**
     * 上传人名称
     */
    @Schema(description = "上传人名称", example = "张三")
    private String uploadByName;

    /**
     * 上传时间（格式化为字符串）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "上传时间", example = "2026-04-29 10:30:00")
    private LocalDateTime uploadTime;

    /**
     * 逻辑删除标记（0-正常，1-已删除）
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    private String isDeleted;

}
