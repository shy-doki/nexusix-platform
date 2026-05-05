package com.shy.nexusix.system.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysFileUpdateRTO {

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    private Long id;

    @Schema(description = "所属租户 ID", example = "1987654321098765432")
    private Long tenantId;

    @Schema(description = "租户名称", example = "华东公司")
    private String tenantName;

    @Schema(description = "存储文件名（UUID 重命名）", example = "a1b2c3d4e5f6.pdf")
    private String fileName;

    @Schema(description = "原始文件名", example = "企业营业执照.pdf")
    private String originalName;

    @Schema(description = "文件存储路径", example = "/uploads/2026/04/report.pdf")
    private String filePath;

    @Schema(description = "文件访问 URL", example = "https://oss.example.com/uploads/report.pdf")
    private String fileUrl;

    @Schema(description = "文件大小 (字节)", example = "102400")
    private Long fileSize;

    @Schema(description = "文件类型", example = "application/pdf")
    private String fileType;

    @Schema(description = "MIME 类型", example = "application/pdf")
    private String mimeType;

    @Schema(description = "业务类型分类", example = "license")
    private String bizType;

    @Schema(description = "上传人 ID", example = "100")
    private Long uploadBy;

    @Schema(description = "上传人名称", example = "张三")
    private String uploadName;

    @Schema(description = "上传时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime uploadTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    private Integer isDeleted;

}
