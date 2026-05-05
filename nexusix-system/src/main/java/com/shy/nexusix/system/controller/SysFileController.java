package com.shy.nexusix.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.system.config.FileStorageProperties;
import com.shy.nexusix.system.entity.SysFile;
import com.shy.nexusix.system.rto.SysFileQueryRTO;
import com.shy.nexusix.system.rto.SysFileUpdateRTO;
import com.shy.nexusix.system.rto.SysFileUploadRTO;
import com.shy.nexusix.system.service.ISysFileService;
import com.shy.nexusix.system.vo.SysFileCommonVO;
import com.shy.nexusix.system.vo.SysFileDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * <p>
 * 文件资源表 - 存储上传的文件信息 前端控制器
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@RestController
@RequestMapping("/file")
@Tag(name = "文件管理", description = "文件基础信息管理相关接口")
@Validated
public class SysFileController {

    @Autowired
    private ISysFileService iSysFileService;

    @Autowired
    private FileStorageProperties FILE_STORAGE_PROPERTIES;

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
    @GetMapping("/list")
    @Operation(summary = "查询文件列表", description = "返回所有文件列表")
    public ApiResponse queryFileList() {
        List<SysFileCommonVO> fileList = iSysFileService.queryFileList();
        return ApiResponse.success(fileList);
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
    @GetMapping("/page")
    @Operation(summary = "分页查询文件列表", description = "返回分页后的文件列表")
    public ApiResponse queryFilePage(@Valid PageCommonRTO page) {
        IPage<SysFileCommonVO> filePage = iSysFileService.queryFilePage(page);
        return ApiResponse.success(filePage);
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
    @PostMapping("/query")
    @Operation(summary = "条件查询文件列表", description = "返回满足条件的文件列表")
    public ApiResponse queryFile(@Valid @RequestBody SysFileQueryRTO queryParam) {
        IPage<SysFileCommonVO> filePage = iSysFileService.queryFile(queryParam);
        return ApiResponse.success(filePage);
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
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或文件不存在时抛出
     * @author shy
     * @since 2026-04-07
     */
    @GetMapping("/detail/{fileName}")
    @Operation(summary = "查询文件详情", description = "返回指定文件的详情信息")
    public ApiResponse queryFileDetail(@NotBlank(message = "文件名称不能为空") @PathVariable String fileName) {
        SysFileDetailVO fileDetail = iSysFileService.queryFileDetail(fileName);
        return ApiResponse.success(fileDetail);
    }

    /**
     * <p>
     * 单文件上传
     * </p>
     * <p>
     * 接收单个文件上传请求，验证文件格式和大小后保存到服务器，
     * 返回上传结果信息。支持图片/文档/压缩包等格式。
     * 需要登录并具备文件上传权限才能访问。
     * </p>
     *
     * @param file 上传的文件对象
     * @param param 文件上传请求参数，包含业务类型、租户信息等
     * @return 统一的API响应结果，包含上传成功或失败信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当文件为空、大小超限、类型不合法或保存失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @PostMapping("/upload")
    @Operation(summary = "单文件上传", description = "上传单个文件，支持图片/文档/压缩包等格式")
    public ApiResponse upload(@RequestPart MultipartFile file, @ParameterObject SysFileUploadRTO param) {
        boolean result = iSysFileService.upload(file, param);
        return result ? ApiResponse.success("上传成功") : ApiResponse.error("上传失败");
    }

    /**
     * <p>
     * 修改文件信息
     * </p>
     * <p>
     * 修改文件元数据信息，需要登录并具备文件修改权限才能访问。
     * 仅允许修改文件的业务类型、原始文件名等元数据，不允许修改文件存储路径和唯一标识。
     * </p>
     *
     * @param updateParam 修改文件信息
     * @return 修改结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、文件不存在或修改失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @PutMapping("/update")
    @Operation(summary = "修改文件", description = "修改文件信息")
    public ApiResponse updateFile(@Valid @RequestBody SysFileUpdateRTO updateParam) {
        Integer affectedRows = iSysFileService.updateFile(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 删除文件
     * </p>
     * <p>
     * 删除指定文件信息，需要登录并具备文件删除权限才能访问。
     * 删除操作为逻辑删除，删除后文件标记为已删除状态。
     * </p>
     *
     * @param id 文件ID
     * @return 删除结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、文件不存在或删除失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除文件", description = "删除文件信息")
    public ApiResponse deleteFile(@NotBlank(message = "Id不能为空") @RequestParam String id) {
        Integer affectedRows = iSysFileService.deleteFile(id);
        return ApiResponse.success(affectedRows);
    }

    /**
     * <p>
     * 文件下载
     * </p>
     * <p>
     * 根据文件名查询文件信息，构建文件资源响应对象，
     * 设置合适的Content-Type和Content-Disposition头信息，
     * 返回文件流供客户端下载。
     * 需要登录并具备文件下载权限才能访问。
     * </p>
     *
     * @param fileName 存储文件名（UUID重命名后的文件名）
     * @return ResponseEntity封装的文件资源，包含文件内容和下载头信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当文件不存在、已被删除或路径非法时抛出
     * @author shy
     * @since 2026-04-07
     */
    @GetMapping("/download")
    @Operation(summary = "文件下载", description = "根据文件名称下载文件，返回文件流")
    public ResponseEntity<Resource> download(@NotBlank(message = "文件名称不能为空") @RequestParam String fileName) {
        SysFile fileInfo = iSysFileService.download(fileName);

        // 构建文件的完整路径：根目录 + 相对路径 + 文件名
        Path filePath = Paths.get(FILE_STORAGE_PROPERTIES.getUploadRoot())
                .resolve(fileInfo.getFilePath() + "/" + fileInfo.getFileName())
                .normalize();

        // 创建文件系统资源对象
        Resource resource = new FileSystemResource(filePath.toFile());

        // 设置内容类型为二进制流（通用下载类型）
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        // 对原始文件名进行URL编码，处理中文等特殊字符
        String encodedFileName = URLEncoder.encode(fileInfo.getOriginalName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        // 构建Content-Disposition头，指定为附件下载方式
        String contentDisposition = String.format("attachment; filename*=%s", encodedFileName);

        // 构建HTTP响应，设置内容类型、下载头信息和文件资源
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    /**
     * <p>
     * 批量删除文件
     * </p>
     * <p>
     * 批量逻辑删除多个指定文件信息，需要登录并具备文件删除权限才能访问。
     * 单次批量删除数量不能超过100条。
     * 删除操作为逻辑删除，删除后文件标记为已删除状态。
     * </p>
     *
     * @param ids 文件ID集合
     * @return 删除结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、文件不存在或删除失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除文件", description = "批量逻辑删除文件，单次不超过100条")
    public ApiResponse batchDeleteFile(@NotEmpty(message = "文件ID集合不能为空") @RequestBody List<String> ids) {
        Integer affectedRows = iSysFileService.batchDeleteFile(ids);
        return ApiResponse.success(affectedRows);
    }

}
