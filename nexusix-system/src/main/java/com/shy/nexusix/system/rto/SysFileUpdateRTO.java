package com.shy.nexusix.system.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 文件更新请求对象
 * </p>
 * <p>
 * 用于接收前端更新文件信息时的参数，包含文件的完整属性信息
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@Schema(description = "文件更新请求参数")
public class SysFileUpdateRTO {

    /**
     * 主键 ID (雪花算法)
     */
    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    private Long id;

    /**
     * 所属租户 ID
     */
    @Schema(description = "所属租户 ID", example = "1987654321098765432")
    private Long tenantId;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称", example = "华东公司")
    private String tenantName;

    /**
     * 存储文件名（UUID 重命名）
     */
    @Schema(description = "存储文件名（UUID 重命名）", example = "a1b2c3d4e5f6.pdf")
    private String fileName;

    /**
     * 原始文件名
     */
    @Schema(description = "原始文件名", example = "企业营业执照.pdf")
    private String originalName;

    /**
     * 文件存储路径
     */
    @Schema(description = "文件存储路径", example = "/uploads/2026/04/report.pdf")
    private String filePath;

    /**
     * 文件访问 URL
     */
    @Schema(description = "文件访问 URL", example = "https://oss.example.com/uploads/report.pdf")
    private String fileUrl;

    /**
     * 文件大小 (字节)
     */
    @Schema(description = "文件大小 (字节)", example = "102400")
    private Long fileSize;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型", example = "application/pdf")
    private String fileType;

    /**
     * MIME 类型
     */
    @Schema(description = "MIME 类型", example = "application/pdf")
    private String mimeType;

    /**
     * 业务类型分类
     */
    @Schema(description = "业务类型分类", example = "license")
    private String bizType;

    /**
     * 上传人 ID
     */
    @Schema(description = "上传人 ID", example = "100")
    private Long uploadBy;

    /**
     * 上传人名称
     */
    @Schema(description = "上传人名称", example = "张三")
    private String uploadByName;

    /**
     * 上传时间
     */
    @Schema(description = "上传时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime uploadTime;

    /**
     * 逻辑删除 (0-正常 1-删除)
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    private Integer isDeleted;

}
