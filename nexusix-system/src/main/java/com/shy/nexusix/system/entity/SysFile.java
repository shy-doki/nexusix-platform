package com.shy.nexusix.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件资源表 - 存储上传的文件信息
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_file")
@Schema(name="SysFile对象", description="文件资源表 - 存储上传的文件信息")
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "所属租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "文件名称", example = "report.pdf")
    @TableField(value = "file_name")
    private String fileName;

    @Schema(description = "文件存储路径", example = "/uploads/2026/04/report.pdf")
    @TableField(value = "file_path")
    private String filePath;

    @Schema(description = "文件访问 URL", example = "https://oss.example.com/uploads/report.pdf")
    @TableField(value = "file_url")
    private String fileUrl;

    @Schema(description = "文件大小 (字节)", example = "102400")
    @TableField(value = "file_size")
    private Long fileSize;

    @Schema(description = "文件类型", example = "application/pdf")
    @TableField(value = "file_type")
    private String fileType;

    @Schema(description = "上传人 ID", example = "100")
    @TableField(value = "upload_by")
    private Long uploadBy;

    @Schema(description = "上传时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "upload_time")
    private LocalDateTime uploadTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
