package com.shy.nexusix.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.system.entity.SysFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.system.rto.SysFileQueryRTO;
import com.shy.nexusix.system.rto.SysFileUpdateRTO;
import com.shy.nexusix.system.rto.SysFileUploadRTO;
import com.shy.nexusix.system.vo.SysFileCommonVO;
import com.shy.nexusix.system.vo.SysFileDetailVO;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 文件资源表 - 存储上传的文件信息 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysFileService extends IService<SysFile> {

    List<SysFileCommonVO> queryFileList();

    IPage<SysFileCommonVO> queryFilePage(PageCommonRTO page);

    IPage<SysFileCommonVO> queryFile(SysFileQueryRTO queryParam);

    SysFileDetailVO queryFileDetail(String fileName);

    /**
     * <p>
     * 上传单个文件
     * </p>
     * <p>
     * 验证文件大小、类型合法性，生成唯一文件名并保存到指定路径，
     * 同时在数据库中记录文件元数据信息。
     * </p>
     *
     * @param file 上传的文件对象
     * @param param 文件上传请求参数，包含业务类型、租户信息等
     * @return 上传是否成功
     */
    boolean upload(MultipartFile file, SysFileUploadRTO param);

    Integer updateFile(SysFileUpdateRTO updateParam);

    Integer deleteFile(@Valid String id);

    /**
     * <p>
     * 下载文件
     * </p>
     * <p>
     * 根据文件名查询文件记录，验证文件存在性和删除状态，
     * 返回文件元数据信息用于后续下载操作。
     * </p>
     *
     * @param fileName 存储文件名（UUID重命名后的文件名）
     * @return 文件元数据实体对象
     */
    SysFile download(String fileName);

    Integer batchDeleteFile(List<String> ids);
}
