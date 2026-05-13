package com.shy.nexusix.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 文件详情视图对象
 * </p>
 * <p>
 * 用于展示文件的详细信息，包含文件存储信息和更新记录
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Data
@Schema(description = "文件详情信息响应对象")
public class SysFileDetailVO extends SysFileCommonVO {

    /**
     * 存储文件名（UUID 重命名）
     */
    @Schema(description = "存储文件名（UUID 重命名）", example = "a1b2c3d4e5f6.pdf")
    private String fileName;

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
     * MIME 类型
     */
    @Schema(description = "MIME 类型", example = "application/pdf")
    private String mimeType;

    /**
     * 更新人姓名
     */
    @Schema(description = "更新人姓名", example = "张三")
    private String updateByName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间", format = "date-time", example = "2026-04-07 15:45:30")
    private LocalDateTime updateTime;

}
