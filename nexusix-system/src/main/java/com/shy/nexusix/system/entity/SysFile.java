package com.shy.nexusix.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
 * @since 2026-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_file")
@Schema(name="SysFile对象", description="文件资源表-存储上传的文件信息")
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "所属租户ID", example = "tenant001")
    private String tenantId;

    @Schema(description = "所属租户名称", example = "示例租户")
    private String tenantName;

    @Schema(description = "文件名称", example = "document_20260513.pdf")
    private String fileName;

    @Schema(description = "原始文件名", example = "产品需求文档.pdf")
    private String originalName;

    @Schema(description = "文件存储路径", example = "/data/files/tenant001/2026-05-13/document_20260513.pdf")
    private String filePath;

    @Schema(description = "文件访问URL", example = "http://localhost:8080/files/document_20260513.pdf")
    private String fileUrl;

    @Schema(description = "文件大小(字节)", example = "1048576")
    private Long fileSize;

    @Schema(description = "文件类型", example = "pdf")
    private String fileType;

    @Schema(description = "MIME类型", example = "application/pdf")
    private String mimeType;

    @Schema(description = "业务类型分类", example = "contract")
    private String bizType;

    @Schema(description = "上传人ID", example = "user001")
    private String uploadBy;

    @Schema(description = "上传人名称", example = "张三")
    private String uploadByName;

    @Schema(description = "上传时间", example = "2026-05-13 10:30:00")
    private LocalDateTime uploadTime;

    @Schema(description = "更新人ID", example = "user001")
    private String updateBy;

    @Schema(description = "更新人名称", example = "张三")
    private String updateByName;

    @Schema(description = "更新时间", example = "2026-05-13 10:30:00")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除", example = "0")
    private String isDeleted;


}
