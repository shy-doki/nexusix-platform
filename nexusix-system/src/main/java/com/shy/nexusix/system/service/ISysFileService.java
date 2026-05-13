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
 * 文件资源管理服务接口
 * </p>
 * <p>
 * 提供文件的上传、下载、查询、更新、删除等业务逻辑处理功能。
 * 继承 MyBatis-Plus 的 IService 接口，获得基础 CRUD 能力。
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
public interface ISysFileService extends IService<SysFile> {

    /**
     * 查询文件列表
     *
     * @return 文件通用视图对象列表
     */
    List<SysFileCommonVO> queryFileList();

    /**
     * 分页查询文件列表
     *
     * @param page 分页参数对象，包含页码和每页大小
     * @return 分页后的文件通用视图对象
     */
    IPage<SysFileCommonVO> queryFilePage(PageCommonRTO page);

    /**
     * 条件查询文件列表（分页）
     *
     * @param queryParam 查询条件参数，支持租户名称、文件名、文件大小范围、文件类型、业务类型、上传人、上传时间范围等条件
     * @return 满足条件的分页文件通用视图对象
     */
    IPage<SysFileCommonVO> queryFile(SysFileQueryRTO queryParam);

    /**
     * 查询文件详情
     *
     * @param fileName 存储文件名（UUID重命名后的文件名）
     * @return 文件详情视图对象
     */
    SysFileDetailVO queryFileDetail(String fileName);

    /**
     * 上传单个文件
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

    /**
     * 更新文件信息
     *
     * @param updateParam 文件更新参数对象，包含需要更新的文件信息
     * @return 受影响的行数
     */
    Integer updateFile(SysFileUpdateRTO updateParam);

    /**
     * 删除文件（逻辑删除）
     *
     * @param id 文件ID
     * @return 受影响的行数
     */
    Integer deleteFile(@Valid String id);

    /**
     * 下载文件
     * <p>
     * 根据文件名查询文件记录，验证文件存在性和删除状态，
     * 返回文件元数据信息用于后续下载操作。
     * </p>
     *
     * @param fileName 存储文件名（UUID重命名后的文件名）
     * @return 文件元数据实体对象
     */
    SysFile download(String fileName);

    /**
     * 批量删除文件（逻辑删除）
     *
     * @param ids 文件ID列表，单次不超过100条
     * @return 受影响的行数
     */
    Integer batchDeleteFile(List<String> ids);

}
