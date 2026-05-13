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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * <p>
 * 文件资源管理控制器
 * </p>
 * <p>
 * 提供文件的上传、下载、查询、更新、删除等基础管理功能。
 * 支持单文件上传、批量删除、条件查询和分页查询等操作。
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@RestController
@RequestMapping("/file")
@Tag(name = "文件管理", description = "文件基础信息管理相关接口")
public class SysFileController {

    @Autowired
    private ISysFileService iSysFileService;

    @Autowired
    private FileStorageProperties FILE_STORAGE_PROPERTIES;

    /**
     * 查询文件列表
     *
     * @return 统一的API响应结果，包含所有文件列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询文件列表", description = "返回所有文件列表")
    public ApiResponse queryFileList() {
        List<SysFileCommonVO> fileList = iSysFileService.queryFileList();
        return ApiResponse.success(fileList);
    }

    /**
     * 分页查询文件列表
     *
     * @param page 分页参数对象，包含页码和每页大小
     * @return 统一的API响应结果，包含分页后的文件列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询文件列表", description = "返回分页后的文件列表")
    public ApiResponse queryFilePage(PageCommonRTO page) {
        IPage<SysFileCommonVO> filePage = iSysFileService.queryFilePage(page);
        return ApiResponse.success(filePage);
    }

    /**
     * 条件查询文件列表（分页）
     *
     * @param queryParam 查询条件参数，支持租户名称、文件名、文件大小范围、文件类型、业务类型、上传人、上传时间范围等条件
     * @return 统一的API响应结果，包含满足条件的分页文件列表
     */
    @PostMapping("/query")
    @Operation(summary = "条件查询文件列表", description = "返回满足条件的文件列表")
    public ApiResponse queryFile(@RequestBody SysFileQueryRTO queryParam) {
        IPage<SysFileCommonVO> filePage = iSysFileService.queryFile(queryParam);
        return ApiResponse.success(filePage);
    }

    /**
     * 查询文件详情
     *
     * @param fileName 存储文件名（UUID重命名后的文件名）
     * @return 统一的API响应结果，包含指定文件的详情信息
     */
    @GetMapping("/detail/{fileName}")
    @Operation(summary = "查询文件详情", description = "返回指定文件的详情信息")
    public ApiResponse queryFileDetail(@PathVariable String fileName) {
        SysFileDetailVO fileDetail = iSysFileService.queryFileDetail(fileName);
        return ApiResponse.success(fileDetail);
    }

    /**
     * 单文件上传
     * <p>
     * 接收单个文件上传请求，验证文件格式和大小后保存到服务器，
     * 返回上传结果信息。
     * </p>
     *
     * @param file 上传的文件对象
     * @param param 文件上传请求参数，包含业务类型、租户信息等
     * @return 统一的API响应结果，包含上传成功或失败信息
     */
    @PostMapping("/upload")
    @Operation(summary = "单文件上传", description = "上传单个文件，支持图片/文档/压缩包等格式")
    public ApiResponse upload(@RequestPart MultipartFile file, @ParameterObject SysFileUploadRTO param) {
        boolean result = iSysFileService.upload(file, param);
        return result ? ApiResponse.success("上传成功") : ApiResponse.error("上传失败");
    }

    /**
     * 更新文件信息
     *
     * @param updateParam 文件更新参数对象，包含需要更新的文件信息
     * @return 统一的API响应结果，包含受影响的行数
     */
    @PutMapping("/update")
    @Operation(summary = "修改文件", description = "修改文件信息")
    public ApiResponse updateFile(@RequestBody SysFileUpdateRTO updateParam) {
        Integer affectedRows = iSysFileService.updateFile(updateParam);
        return ApiResponse.success(affectedRows);
    }

    /**
     * 删除文件
     *
     * @param id 文件ID
     * @return 统一的API响应结果，包含受影响的行数
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除文件", description = "删除文件信息")
    public ApiResponse deleteFile(@RequestParam @Valid String id) {
        Integer affectedRows = iSysFileService.deleteFile(id);
        return ApiResponse.success(affectedRows);
    }

    /**
     * 文件下载
     * <p>
     * 根据文件名查询文件信息，构建文件资源响应对象，
     * 设置合适的Content-Type和Content-Disposition头信息，
     * 返回文件流供客户端下载。
     * </p>
     *
     * @param fileName 存储文件名（UUID重命名后的文件名）
     * @return ResponseEntity封装的文件资源，包含文件内容和下载头信息
     */
    @GetMapping("/download")
    @Operation(summary = "文件下载", description = "根据文件名称下载文件，返回文件流")
    public ResponseEntity<Resource> download(@RequestParam String fileName) {
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
     * 批量删除文件
     *
     * @param ids 文件ID列表，单次不超过100条
     * @return 统一的API响应结果，包含受影响的行数
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除文件", description = "批量逻辑删除文件，单次不超过100条")
    public ApiResponse batchDeleteFile(@RequestBody List<String> ids) {
        Integer affectedRows = iSysFileService.batchDeleteFile(ids);
        return ApiResponse.success(affectedRows);
    }

}
