package com.shy.nexusix.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.constant.RegexConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.NumberRangeCommonRTO;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.common.utils.IdUtils;
import com.shy.nexusix.common.utils.RegexUtils;
import com.shy.nexusix.system.config.FileStorageProperties;
import com.shy.nexusix.system.converter.SysFileConverter;
import com.shy.nexusix.system.entity.SysFile;
import com.shy.nexusix.system.mapper.SysFileMapper;
import com.shy.nexusix.system.rto.SysFileQueryRTO;
import com.shy.nexusix.system.rto.SysFileUpdateRTO;
import com.shy.nexusix.system.rto.SysFileUploadRTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.system.service.ISysFileService;
import com.shy.nexusix.system.vo.SysFileCommonVO;
import com.shy.nexusix.system.vo.SysFileDetailVO;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.shy.nexusix.common.utils.DateUtils.DATE_FORMATTER;

/**
 * <p>
 * 文件资源表 - 存储上传的文件信息，支持多租户隔离 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    // TODO 业务校验 + 软删除考虑使用aop结合注解去做

    // TODO 权限校验 控制器层/aop处理

    // TODO 租户隔离校验

    @Autowired
    private FileStorageProperties FILE_STORAGE_PROPERTIES;

    @Autowired
    private SysFileConverter sysFileConverter;

    /**
     * <p>
     * 查询文件列表
     * </p>
     * <p>
     * 返回所有文件的平铺列表，按上传时间倒序排列。
     * 需要登录并具备文件查看权限才能访问。
     * </p>
     *
     * @return 文件列表，包含租户名称、原始文件名、文件大小、文件类型、业务类型、上传人等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @Override
    public List<SysFileCommonVO> queryFileList() {

        // 构建查询条件：仅查询未删除的文件，按上传时间倒序排列
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<SysFile>()
                .eq(SysFile::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysFile::getUploadTime);

        // 执行查询获取文件列表
        List<SysFile> fileList = this.list(wrapper);

        // 转换为VO对象并返回
        return sysFileConverter.toVoList(fileList);

    }

    /**
     * <p>
     * 分页查询文件列表
     * </p>
     * <p>
     * 返回分页后的文件列表，按上传时间倒序排列。
     * 需要登录并具备文件查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的文件列表，包含租户名称、原始文件名、文件大小、文件类型、业务类型、上传人等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @Override
    public IPage<SysFileCommonVO> queryFilePage(PageCommonRTO page) {

        // 构建分页参数
        Page<SysFile> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        // 构建查询条件：仅查询未删除的文件，按上传时间倒序排列
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<SysFile>()
                .eq(SysFile::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysFile::getUploadTime);

        // 执行分页查询
        IPage<SysFile> filePage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysFileConverter.toVOPage(filePage);

    }

    /**
     * <p>
     * 条件查询\筛选文件列表
     * </p>
     * <p>
     * 返回满足条件的文件列表，支持按租户名称、原始文件名、文件大小范围、
     * 文件类型、业务类型、上传人名称、上传时间范围等条件筛选。
     * 需要登录并具备文件条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的文件列表，包含租户名称、原始文件名、文件大小、文件类型、业务类型、上传人等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @Override
    public IPage<SysFileCommonVO> queryFile(SysFileQueryRTO queryParam) {

        // 构建分页参数
        Page<SysFile> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        // 构建动态查询条件
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<>();

        // 租户名称精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getTenantName()),
                SysFile::getTenantName, queryParam.getTenantName());

        // 原始文件名精确查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getOriginalName()),
                SysFile::getOriginalName, queryParam.getOriginalName());

        // 文件大小范围查询
        NumberRangeCommonRTO fileSize = queryParam.getFileSize();
        if (fileSize != null) {
            Long startSize = fileSize.getMinValue();
            Long endSize = fileSize.getMaxValue();

            // 校验大小范围合法性
            if (startSize != null && endSize != null && startSize > endSize) {
                throw new BusinessException(400, "文件最小大小不能超过最大大小");
            }

            // 根据大小范围构建查询条件
            if (startSize != null && endSize != null) {
                wrapper.between(SysFile::getFileSize, startSize, endSize);
            } else if (startSize != null) {
                wrapper.ge(SysFile::getFileSize, startSize);
            } else if (endSize != null) {
                wrapper.le(SysFile::getFileSize, endSize);
            }
        }

        // 文件类型条件查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getFileType()),
                SysFile::getFileType, queryParam.getFileType());

        // 业务类型分类条件查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getBizType()),
                SysFile::getBizType, queryParam.getBizType());

        // 上传人名称条件查询
        wrapper.eq(StringUtils.isNotBlank(queryParam.getUploadName()),
                SysFile::getUploadName, queryParam.getUploadName());

        // 上传时间范围查询
        TimeRangeCommonRTO uploadTime = queryParam.getUploadTime();
        if (uploadTime != null) {
            LocalDateTime startTime = uploadTime.getStartTime();
            LocalDateTime endTime = uploadTime.getEndTime();

            // 校验时间范围合法性
            if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
                throw new BusinessException(400, "开始时间不能晚于结束时间");
            }
            if (startTime != null && endTime != null && endTime.isBefore(startTime)) {
                throw new BusinessException(400, "结束时间不能早于开始时间");
            }

            // 根据时间范围构建查询条件
            if (startTime != null && endTime != null) {
                wrapper.between(SysFile::getUploadTime, startTime, endTime);
            } else if (startTime != null) {
                wrapper.ge(SysFile::getUploadTime, startTime);
            } else if (endTime != null) {
                wrapper.le(SysFile::getUploadTime, endTime);
            }
        }

        // 条件分页查询
        IPage<SysFile> fileQueryPage = this.page(pageParam, wrapper);

        // 转换为VO分页对象并返回
        return sysFileConverter.toVOPage(fileQueryPage);

    }

    /**
     * <p>
     * 查询文件详情
     * </p>
     * <p>
     * 返回指定文件的详情信息，包含文件存储路径、访问URL、MIME类型等。
     * 需要登录并具备文件详情查询权限才能访问。
     * </p>
     *
     * @param fileName 存储文件名（UUID重命名后的文件名）
     * @return 文件详情信息，包含存储文件名、文件路径、访问URL、MIME类型等
     * @throws com.shy.nexusix.common.exception.BusinessException 当文件不存在或查询失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @Override
    public SysFileDetailVO queryFileDetail(String fileName) {

        // 参数校验：文件名称不能为空
        if (StringUtils.isBlank(fileName)) {
            throw new BusinessException(400, "文件名称不能为空");
        }

        // 构建查询条件：根据存储文件名查询未删除的文件
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<SysFile>()
                .eq(SysFile::getFileName, fileName)
                .eq(SysFile::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行查询获取文件详情
        SysFile fileDetail = this.getOne(wrapper);

        // 文件不存在时抛出异常
        if (fileDetail == null) {
            throw new BusinessException(404, "文件不存在");
        }

        // 转换为详情VO对象并返回
        return sysFileConverter.toDetailVO(fileDetail);

    }

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
     * @author shy
     * @since 2026-04-07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean upload(MultipartFile file, SysFileUploadRTO param) {

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

        // 安全检查：防止路径遍历攻击
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

        // 构建文件实体对象
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
     * 修改文件信息
     * </p>
     * <p>
     * 修改文件元数据信息，需要登录并具备文件修改权限才能访问。
     * 仅允许修改文件的业务类型、原始文件名等元数据，不允许修改文件存储路径和唯一标识。
     * 修改原始文件名时会自动更新文件类型扩展名。
     * </p>
     *
     * @param updateParam 修改文件信息
     * @return 修改结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、文件不存在或修改失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateFile(SysFileUpdateRTO updateParam) {

        // 查询待修改的文件是否存在
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<SysFile>()
                .eq(SysFile::getId, updateParam.getId())
                .eq(SysFile::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysFile existFile = this.getOne(wrapper);

        if (existFile == null) {
            throw new BusinessException(404, "文件不存在");
        }

        // 如果修改了租户ID，校验目标租户是否存在
        if (!existFile.getTenantId().equals(updateParam.getTenantId())) {
            LambdaQueryWrapper<SysFile> tenantWrapper = new LambdaQueryWrapper<SysFile>()
                    .eq(SysFile::getTenantId, updateParam.getTenantId())
                    .eq(SysFile::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long tenantFileCount = this.count(tenantWrapper);
            if (tenantFileCount == 0) {
                // TODO 后续接入租户模块后，校验目标租户是否存在于 sys_tenant 表
            }
        }

        // 如果修改了原始文件名，需校验新文件名合法性
        if (!existFile.getOriginalName().equals(updateParam.getOriginalName())) {
            // 安全检查：防止路径遍历攻击
            if (updateParam.getOriginalName().contains("..")) {
                throw new BusinessException(400, "文件名包含非法路径遍历字符");
            }

            // 验证文件名是否符合规范
            if (!RegexUtils.matches(updateParam.getOriginalName(), RegexConstant.File.FILENAME)) {
                throw new BusinessException(400, "文件名包含非法字符: " + updateParam.getOriginalName());
            }

            // 校验该租户下是否已存在同名文件
            LambdaQueryWrapper<SysFile> nameWrapper = new LambdaQueryWrapper<SysFile>()
                    .eq(SysFile::getTenantId, updateParam.getTenantId())
                    .eq(SysFile::getOriginalName, updateParam.getOriginalName())
                    .eq(SysFile::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .ne(SysFile::getId, updateParam.getId());
            long nameCount = this.count(nameWrapper);
            if (nameCount > 0) {
                throw new BusinessException(400, "该租户下已存在同名文件");
            }
        }

        // 如果修改了业务类型，校验业务类型是否合法
        if (!existFile.getBizType().equals(updateParam.getBizType())) {
            List<String> allowedBizTypes = List.of(
                    "logo", "avatar", "contract", "license", "attachment", "export"
            );
            if (!allowedBizTypes.contains(updateParam.getBizType())) {
                throw new BusinessException(400, "不支持的文件业务类型: " + updateParam.getBizType());
            }
        }

        // 转换并更新文件信息
        SysFile updateEntity = sysFileConverter.toEntityUpdate(updateParam);

        // 如果修改了原始文件名，同步更新文件类型扩展名
        if (!existFile.getOriginalName().equals(updateParam.getOriginalName())) {
            String newOriginalName = updateParam.getOriginalName();
            if (newOriginalName != null && newOriginalName.contains(".")) {
                String fileExt = newOriginalName.substring(
                        newOriginalName.lastIndexOf(".")
                ).toLowerCase();
                updateEntity.setFileType(fileExt);
            }
        }

        boolean result = this.updateById(updateEntity);

        if (!result) {
            throw new BusinessException(500, "修改文件信息失败");
        }

        return 1;

    }

    /**
     * <p>
     * 删除文件
     * </p>
     * <p>
     * 删除指定文件信息，需要登录并具备文件删除权限才能访问。
     * 删除操作为逻辑删除，删除后文件标记为已删除状态。
     * 同时校验文件路径合法性，防止路径遍历攻击。
     * </p>
     *
     * @param id 文件ID
     * @return 删除结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、文件不存在或删除失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteFile(String id) {

        // 参数校验：文件ID不能为空
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "文件ID不能为空");
        }

        // 查询待删除的文件是否存在
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<SysFile>()
                .eq(SysFile::getId, id)
                .eq(SysFile::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysFile existFile = this.getOne(wrapper);

        if (existFile == null) {
            throw new BusinessException(404, "文件不存在");
        }

        // TODO 租户隔离校验

        // 安全检查：验证文件路径是否在允许的根目录下，防止路径遍历攻击
        Path basePath = Paths.get(FILE_STORAGE_PROPERTIES.getUploadRoot())
                .toAbsolutePath().normalize();
        Path filePath = Paths.get(existFile.getFileUrl())
                .toAbsolutePath().normalize();

        if (!filePath.startsWith(basePath)) {
            throw new BusinessException(400, "非法文件路径，拒绝删除");
        }

        // 执行逻辑删除：设置is_deleted标志位
        SysFile deleteFile = new SysFile();
        deleteFile.setId(Long.parseLong(id));
        deleteFile.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());

        boolean result = this.updateById(deleteFile);

        if (!result) {
            throw new BusinessException(500, "删除文件失败");
        }

        return 1;

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
     *   <li>验证物理文件是否存在</li>
     *   <li>返回文件元数据信息</li>
     * </ul>
     * </p>
     *
     * @param fileName 存储文件名（UUID重命名后的文件名）
     * @return 文件元数据实体对象
     * @throws BusinessException 当文件不存在、已被删除或路径非法时抛出
     * @author shy
     * @since 2026-04-07
     */
    @Override
    public SysFile download(String fileName) {

        // 构建查询条件：根据文件名（不是原文件名）查询
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

        // 安全检查：验证文件路径是否在允许的根目录下，防止路径遍历攻击
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

    /**
     * <p>
     * 批量删除文件
     * </p>
     * <p>
     * 批量逻辑删除多个指定文件信息，需要登录并具备文件删除权限才能访问。
     * 单次批量删除数量不能超过100条。
     * 删除操作为逻辑删除，删除后文件标记为已删除状态。
     * 同时校验每个文件路径的合法性，防止路径遍历攻击。
     * </p>
     *
     * @param ids 文件ID集合
     * @return 删除结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、文件不存在或删除失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteFile(List<String> ids) {

        // 校验批量删除数量限制
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        // 校验ID格式并转换为Long类型，同时进行重复检测
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "批量删除中存在ID为空的记录");
            }
            // 重复检测
            Long parsedId = Long.parseLong(id);
            if (idSet.contains(parsedId)) {
                throw new BusinessException(400, "批量删除中存在重复的文件ID: " + id);
            }
            idSet.add(parsedId);
        }

        // 查询待删除的文件是否存在且未被删除
        LambdaQueryWrapper<SysFile> existWrapper = new LambdaQueryWrapper<SysFile>()
                .in(SysFile::getId, idSet)
                .eq(SysFile::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysFile> existFiles = this.list(existWrapper);

        if (existFiles.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的文件");
        }

        // 获取基础路径用于安全验证
        Path basePath = Paths.get(FILE_STORAGE_PROPERTIES.getUploadRoot())
                .toAbsolutePath().normalize();

        // TODO 租户隔离校验

        // 安全检查：验证每个文件路径是否在允许的根目录下
        for (SysFile file : existFiles) {
            Path filePath = Paths.get(file.getFileUrl())
                    .toAbsolutePath().normalize();
            if (!filePath.startsWith(basePath)) {
                throw new BusinessException(400, "文件[" + file.getOriginalName() + "]路径非法，拒绝删除");
            }
        }

        // 构建批量逻辑删除的数据列表
        List<SysFile> deleteFileList = new ArrayList<>();
        for (SysFile file : existFiles) {
            SysFile deleteFile = new SysFile();
            deleteFile.setId(file.getId());
            deleteFile.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            deleteFileList.add(deleteFile);
        }

        // 执行批量逻辑删除
        boolean batch = this.updateBatchById(deleteFileList);

        if (!batch) {
            throw new BusinessException(500, "批量删除文件失败");
        }

        return deleteFileList.size();

    }


}
