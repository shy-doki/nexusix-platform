package com.shy.nexusix.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shy.nexusix.common.constant.RegexConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.utils.IdUtils;
import com.shy.nexusix.common.utils.RegexUtils;
import com.shy.nexusix.system.config.FileStorageProperties;
import com.shy.nexusix.system.entity.SysFile;
import com.shy.nexusix.system.mapper.SysFileMapper;
import com.shy.nexusix.system.rto.FileUploadRTO;
import com.shy.nexusix.system.service.ISysFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * <p>
     * 上传单个文件
     * </p>
     * <p>
     * 执行流程：
     * <ul>
     *   <li>验证文件非空及大小限制</li>
     *   <li>检查文件名合法性，防止路径遍历攻击</li>
     *   <li>验证文件类型是否在允许的分类列表中</li>
     *   <li>生成唯一文件名和存储路径</li>
     *   <li>保存文件到磁盘并记录元数据到数据库</li>
     *   <li>若文件保存失败，回滚数据库记录</li>
     * </ul>
     * </p>
     *
     * @param file 上传的文件对象
     * @param param 文件上传请求参数，包含业务类型、租户信息等
     * @return 上传是否成功
     * @throws BusinessException 当文件为空、大小超限、类型不合法或保存失败时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean upload(MultipartFile file, FileUploadRTO param) {

        // 验证文件是否为空
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        // 验证原始文件名是否存在
        if (StringUtils.isBlank(file.getOriginalFilename())) {
            throw new BusinessException("文件名不能为空");
        }

        // 验证文件大小是否超过限制
        long fileSize = file.getSize();
        if (fileSize > FILE_STORAGE_PROPERTIES.getMaxSize()) {
            throw new BusinessException("文件大小超过限制，最大允许" + (FILE_STORAGE_PROPERTIES.getMaxSize() / 1024 / 1024) + "MB");
        }

        // TODO 获取上传人和租户信息

        String originalFilename = file.getOriginalFilename();

        String fileExt = "";

        // 安全检查 防止路径遍历攻击
        if (originalFilename.contains("..")) {
            throw new BusinessException("文件名包含非法路径遍历字符");
        }

        // 验证文件名是否符合规范（不包含非法字符）
        if (!RegexUtils.matches(originalFilename, RegexConstant.File.FILENAME)) {
            throw new BusinessException("文件名包含非法字符: " + originalFilename);
        }

        // 获取允许的文件类型分类列表
        List<String> categories = FILE_STORAGE_PROPERTIES.getAllowedCategories();

        boolean typeAllowed = false;

        // 遍历允许的分类，检查文件类型是否匹配
        for (String item : categories) {
            switch (item.toLowerCase()) {
                case "image":
                    if (RegexUtils.isImageFile(originalFilename)) typeAllowed = true;
                    break;
                case "document":
                    if (RegexUtils.isDocumentFile(originalFilename)) typeAllowed = true;
                    break;
                case "video":
                    if (RegexUtils.isVideoFile(originalFilename)) typeAllowed = true;
                    break;
                case "audio":
                    if (RegexUtils.isAudioFile(originalFilename)) typeAllowed = true;
                    break;
                case "archive":
                    if (RegexUtils.isArchiveFile(originalFilename)) typeAllowed = true;
                    break;
                default:
                    break;
            }
        }

        // 如果文件类型不在允许的范围内，抛出异常
        if (!typeAllowed) {
            throw new BusinessException("不支持的文件类型: " + fileExt);
        }

        // 提取文件扩展名
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExt = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }

        // 生成唯一文件名：短UUID + 时间戳 + 扩展名
        String uniqueFileName = IdUtils.uuidShort() + "_"
                + LocalDateTime.now().format(DATE_FORMATTER).replace(":", "-") + fileExt;

        // 构建相对路径：租户名称/业务类型/日期
        String relativePath = param.getTenantName() + "/" + param.getBizType() + "/" + LocalDateTime.now().format(DATE_FORMATTER);

        // 构建完整的上传目录路径
        Path uploadDir = Paths.get(FILE_STORAGE_PROPERTIES.getUploadRoot(), relativePath);

        // 构建目标文件的完整路径
        Path targetPath = uploadDir.resolve(uniqueFileName);

        // 构建文件访问URL
        String fileUrl = FILE_STORAGE_PROPERTIES.getUploadRoot() + "/" + relativePath + "/" + uniqueFileName;

        SysFile sysFile = new SysFile()
                .setTenantId(1L)
                .setTenantName("登录人租户")
                .setFileName(uniqueFileName)
                .setOriginalName(file.getOriginalFilename())
                .setFilePath(relativePath)
                .setFileUrl(fileUrl)
                .setFileSize(fileSize)
                .setFileType(fileExt)
                .setMimeType(file.getContentType())
                .setBizType(param.getBizType())
                .setUploadBy(1L)
                .setUploadName("登录人姓名")
                .setUploadTime(LocalDateTime.now())
                .setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 先保存数据库记录
        boolean result = this.save(sysFile);

        // 将文件保存到磁盘
        try {
            // 创建目录（如果不存在）
            Files.createDirectories(uploadDir);
            // 转移文件到目标路径
            file.transferTo(targetPath.toFile());
        } catch (IOException e) {
            // 如果文件保存失败，删除数据库记录以保证数据一致性
            this.removeById(sysFile.getId());
            throw new BusinessException("文件保存失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * <p>
     * 下载文件
     * </p>
     * <p>
     * 执行流程：
     * <ul>
     *   <li>根据文件名查询文件记录</li>
     *   <li>验证文件是否存在且未被删除</li>
     *   <li>验证文件路径合法性，防止路径遍历攻击</li>
     *   <li>返回文件元数据信息</li>
     * </ul>
     * </p>
     *
     * @param fileName 存储文件名（UUID重命名后的文件名）
     * @return 文件元数据实体对象
     * @throws BusinessException 当文件不存在、已被删除或路径非法时抛出
     */
    @Override
    public SysFile download(String fileName) {

        // 构建查询条件 根据文件名（不是原文件名）查询
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<SysFile>()
                .eq(SysFile::getFileName, fileName);

        // 查询文件记录
        SysFile fileInfo = this.getOne(wrapper);

        // 验证文件记录是否存在
        if (fileInfo == null) {
            throw new BusinessException("文件记录不存在");
        }

        // 验证文件是否已被逻辑删除
        if (GlobalEnum.Deleted.DELETED.getCode().equals(fileInfo.getIsDeleted())) {
            throw new BusinessException("文件已被删除");
        }

        // 获取基础路径用于安全验证
        Path basePath = Paths.get(FILE_STORAGE_PROPERTIES.getUploadRoot()).toAbsolutePath().normalize();

        // 构建文件的绝对路径并标准化
        Path filePath = Paths.get(fileInfo.getFileUrl()).toAbsolutePath().normalize();

        // 安全检查 验证文件路径是否在允许的根目录下，防止路径遍历攻击
        if (!filePath.startsWith(basePath)) {
            throw new BusinessException("非法文件路径");
        }

        // 创建文件系统资源对象
        Resource resource = new FileSystemResource(filePath.toFile());

        // 验证物理文件是否存在
        if (!resource.exists()) {
            throw new BusinessException("文件不存在");
        }

        return fileInfo;

    }


}
