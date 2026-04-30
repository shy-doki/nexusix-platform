package com.shy.nexusix.system.config;

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
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    /**
     * 文件存储根目录
     */
    private String uploadRoot;

    /**
     * 单文件最大大小（字节），默认 50MB
     */
    private long maxSize;

    /**
     * 批量上传单次最大文件数
     */
    private int maxBatchCount;

    /**
     * 批量上传总大小上限（字节），默认 200MB
     */
    private long maxBatchTotalSize;

    /**
     *  允许的文件类型分类
     */
    private List<String> allowedCategories;

    /**
     * 是否启用 Magic Number 校验
     */
    private boolean magicNumberCheck;

    /**
     *  访问 URL 前缀
     */
    private String urlPrefix;

    /**
     * 存储模式：local / oss / minio
     */
    private String storageMode;

}
