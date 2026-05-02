package com.shy.nexusix.system.controller;


import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.system.config.FileStorageProperties;
import com.shy.nexusix.system.entity.SysFile;
import com.shy.nexusix.system.rto.FileUploadRTO;
import com.shy.nexusix.system.service.ISysFileService;
import io.swagger.v3.oas.annotations.Operation;
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

/**
 * <p>
 * 文件资源表 - 存储上传的文件信息 前端控制器
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@RestController
@RequestMapping("/sys-file")
public class SysFileController {

    @Autowired
    private ISysFileService iSysFileService;

    @Autowired
    private FileStorageProperties FILE_STORAGE_PROPERTIES;

    // 查询文件列表

    // 分页查询文件列表

    // 查询文件详情

    /**
     * <p>
     * 单文件上传
     * </p>
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
    public ApiResponse upload(@RequestPart MultipartFile file, @ParameterObject FileUploadRTO param) {
        boolean result = iSysFileService.upload(file, param);
        return result ? ApiResponse.success("上传成功") : ApiResponse.error("上传失败");
    }

    // 更新文件信息

    // 删除文件

    /**
     * <p>
     * 文件下载
     * </p>
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

}
