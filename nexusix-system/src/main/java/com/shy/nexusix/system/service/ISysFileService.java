package com.shy.nexusix.system.service;

import com.shy.nexusix.system.entity.SysFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.system.rto.FileUploadRTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件资源表 - 存储上传的文件信息 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysFileService extends IService<SysFile> {

    boolean upload(MultipartFile file, FileUploadRTO param);

}
