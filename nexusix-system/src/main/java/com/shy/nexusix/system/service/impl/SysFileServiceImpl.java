package com.shy.nexusix.system.service.impl;

import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.system.config.FileStorageProperties;
import com.shy.nexusix.system.entity.SysFile;
import com.shy.nexusix.system.mapper.SysFileMapper;
import com.shy.nexusix.system.rto.FileUploadRTO;
import com.shy.nexusix.system.service.ISysFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.shy.nexusix.common.utils.DateUtils.DATE_FORMATTER;

/**
 * <p>
 * 文件资源表 - 存储上传的文件信息 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    @Autowired
    private FileStorageProperties FILE_STORAGE_PROPERTIES;

    @Override
    public boolean upload(MultipartFile file, FileUploadRTO param) {

        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        if (StringUtils.isBlank(file.getOriginalFilename())) {
            throw new BusinessException("文件名不能为空");
        }

        long fileSize = file.getSize();
        if (fileSize > FILE_STORAGE_PROPERTIES.getMaxSize()) {
            throw new BusinessException("文件大小超过限制，最大允许" + (FILE_STORAGE_PROPERTIES.getMaxSize() / 1024 / 1024) + "MB");
        }

        // TODO 获取上传人和租户信息

        String originalFilename = file.getOriginalFilename();

        String fileExt = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            fileExt = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }

        String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + "_"
                + LocalDateTime.now().format(DATE_FORMATTER).replace(":", "-") + fileExt;

        String relativePath = param.getTenantName() + "/" + param.getBizType() + "/" + LocalDateTime.now().format(DATE_FORMATTER);

        Path uploadDir = Paths.get(FILE_STORAGE_PROPERTIES.getUploadRoot(), relativePath);

        Path targetPath = uploadDir.resolve(uniqueFileName);

        try {
            Files.createDirectories(uploadDir);

            file.transferTo(targetPath.toFile());

        } catch (IOException e) {
            throw new BusinessException("文件保存失败: " + e.getMessage());
        }

        SysFile sysFile = new SysFile();
        sysFile.setOriginalName(file.getOriginalFilename());
        sysFile.setFileName(uniqueFileName);
        sysFile.setFilePath(relativePath);
        sysFile.setFileSize(fileSize);
        sysFile.setFileType(fileExt);
        sysFile.setMimeType(file.getContentType());
        sysFile.setTenantId(1L);
        sysFile.setTenantName("登录人租户");
        sysFile.setBizType(param.getBizType());
        sysFile.setUploadBy(1L);
        sysFile.setUploadName("登录人姓名");
        sysFile.setUploadTime(LocalDateTime.now());
        sysFile.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());

        return this.save(sysFile);
    }

}
