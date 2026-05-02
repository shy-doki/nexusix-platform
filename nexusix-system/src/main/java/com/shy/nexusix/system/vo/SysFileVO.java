package com.shy.nexusix.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 文件信息视图对象 - 用于返回给前端的文件元数据
 * </p>
 */
@Data
@Schema(description = "文件信息 VO")
public class SysFileVO {

    /**
     * 主键 ID（雪花算法生成）
     */
    @Schema(description = "主键 ID（雪花算法）", example = "1987654321098765432")
    private Long id;

    /**
     * 所属租户 ID
     */
    @Schema(description = "所属租户 ID", example = "1987654321098765431")
    private Long tenantId;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "华东公司")
    private String tenantName;

    /**
     * 存储文件名（UUID重命名后的文件名，用于唯一标识）
     */
    @Schema(description = "存储文件名（UUID 重命名）", example = "a1b2c3d4e5f6.pdf")
    private String fileName;

    /**
     * 原始文件名（用户上传时的文件名）
     */
    @Schema(description = "原始文件名", example = "企业营业执照.pdf")
    private String originalName;

    /**
     * 文件存储相对路径（不包含根目录）
     */
    @Schema(description = "文件存储路径", example = "/2026/04/29/a1b2c3d4e5f6.pdf")
    private String filePath;

    /**
     * 文件访问完整URL（可用于直接访问或下载）
     */
    @Schema(description = "文件访问 URL", example = "/sys-file/access/2026/04/29/a1b2c3d4e5f6.pdf")
    private String fileUrl;

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
     * MIME类型（用于标识文件的媒体类型）
     */
    @Schema(description = "MIME 类型", example = "application/pdf")
    private String mimeType;

    /**
     * 业务类型分类（如：logo、avatar、contract、license等）
     */
    @Schema(description = "业务类型分类", example = "license")
    private String bizType;

    /**
     * 上传人 ID
     */
    @Schema(description = "上传人 ID", example = "10001")
    private Long uploadBy;

    /**
     * 上传人名称
     */
    @Schema(description = "上传人名称", example = "张三")
    private String uploadName;

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
    private Integer isDeleted;

}
