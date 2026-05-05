package com.shy.nexusix.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件详情视图对象 - 包含文件的完整元数据信息
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文件详情视图对象")
public class SysFileDetailVO extends SysFileCommonVO {

    /**
     * 存储文件名（UUID 重命名，用于唯一标识和防冲突）
     */
    @Schema(description = "存储文件名（UUID 重命名）", example = "a1b2c3d4e5f6.pdf")
    private String fileName;

    /**
     * 文件存储相对路径
     */
    @Schema(description = "文件存储路径", example = "/uploads/2026/04/report.pdf")
    private String filePath;

    /**
     * 文件访问 URL（完整访问地址）
     */
    @Schema(description = "文件访问 URL", example = "https://oss.example.com/uploads/report.pdf")
    private String fileUrl;

    /**
     * MIME 类型（如 application/pdf、image/jpeg）
     */
    @Schema(description = "MIME 类型", example = "application/pdf")
    private String mimeType;

}
