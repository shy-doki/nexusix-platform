package com.shy.nexusix.system.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 文件存储配置属性类
 * </p>
 * <p>
 * 绑定 application.yml 中 file.* 配置项，
 * 支持运行时动态调整文件上传限制参数。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
@Schema(description = "文件存储配置属性")
public class FileStorageProperties {

    /**
     * 文件存储根目录
     */
    @Schema(description = "文件存储根目录", example = "/data/uploads")
    private String uploadRoot;

    /**
     * 单文件最大大小（字节），默认 50MB
     */
    @Schema(description = "单文件最大大小（字节）", example = "52428800")
    private long maxSize;

    /**
     * 批量上传单次最大文件数
     */
    @Schema(description = "批量上传单次最大文件数", example = "20")
    private int maxBatchCount;

    /**
     * 批量上传总大小上限（字节），默认 200MB
     */
    @Schema(description = "批量上传总大小上限（字节）", example = "209715200")
    private long maxBatchTotalSize;

    /**
     * 允许的文件类型分类（如 image、document、video、audio、archive）
     */
    @Schema(description = "允许的文件类型分类", example = "[\"image\", \"document\", \"archive\"]")
    private List<String> allowedCategories;

    /**
     * 是否启用 Magic Number 校验
     */
    @Schema(description = "是否启用 Magic Number 校验", example = "false")
    private boolean magicNumberCheck;

    /**
     * 访问 URL 前缀
     */
    @Schema(description = "访问 URL 前缀", example = "https://oss.example.com")
    private String urlPrefix;

    /**
     * 存储模式：local / oss / minio
     */
    @Schema(description = "存储模式：local / oss / minio", example = "local")
    private String storageMode;

}
