package com.shy.nexusix.system.controller;


import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.system.rto.FileUploadRTO;
import com.shy.nexusix.system.service.ISysFileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    // 查询文件列表

    // 分页查询文件列表

    // 查询文件详情

    // 单文件上传
    @PostMapping("upload")
    @Operation(summary = "单文件上传", description = "上传单个文件，支持图片/文档/压缩包等格式")
    public ApiResponse upload(@RequestParam("file")MultipartFile file, FileUploadRTO param) {
        boolean result = iSysFileService.upload(file, param);
        return result ? ApiResponse.success("上传成功") : ApiResponse.error("上传失败");
    }

    // 批量文件上传

    // 更新文件信息

    // 删除文件

    // 文件下载

}
