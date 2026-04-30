package com.shy.nexusix.system.vo;

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

    @Schema(description = "主键 ID（雪花算法）", example = "1987654321098765432")
    private Long id;

    @Schema(description = "所属租户 ID", example = "1987654321098765431")
    private Long tenantId;

    @Schema(description = "租户名称", example = "华东公司")
    private String tenantName;

    @Schema(description = "存储文件名（UUID 重命名）", example = "a1b2c3d4e5f6.pdf")
    private String fileName;

    @Schema(description = "原始文件名", example = "企业营业执照.pdf")
    private String originalName;

    @Schema(description = "文件存储路径", example = "/2026/04/29/a1b2c3d4e5f6.pdf")
    private String filePath;

    @Schema(description = "文件访问 URL", example = "/sys-file/access/2026/04/29/a1b2c3d4e5f6.pdf")
    private String fileUrl;

    @Schema(description = "文件大小（字节）", example = "2048576")
    private Long fileSize;

    @Schema(description = "文件格式（小写扩展名）", example = "pdf")
    private String fileType;

    @Schema(description = "MIME 类型", example = "application/pdf")
    private String mimeType;

    @Schema(description = "业务类型分类", example = "license")
    private String bizType;

    @Schema(description = "上传人 ID", example = "10001")
    private Long uploadBy;

    @Schema(description = "上传人名称", example = "张三")
    private String uploadName;

    @Schema(description = "上传时间", example = "2026-04-29 10:30:00")
    private LocalDateTime uploadTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    private Integer isDeleted;

}
