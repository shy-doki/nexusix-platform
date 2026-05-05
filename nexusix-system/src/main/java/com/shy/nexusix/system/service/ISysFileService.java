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
 * 文件资源表 - 存储上传的文件信息，支持多租户隔离 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysFileService extends IService<SysFile> {

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
    List<SysFileCommonVO> queryFileList();

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
    IPage<SysFileCommonVO> queryFilePage(PageCommonRTO page);

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
    IPage<SysFileCommonVO> queryFile(SysFileQueryRTO queryParam);

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
     * @throws com.shy.nexusix.common.exception.BusinessException 当文件为空、大小超限、类型不合法或保存失败时抛出
     * @author shy
     * @since 2026-04-07
     */
    boolean upload(MultipartFile file, SysFileUploadRTO param);

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
    Integer updateFile(SysFileUpdateRTO updateParam);

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
    Integer deleteFile(@Valid String id);

    /**
     * <p>
     * 下载文件
     * </p>
     * <p>
     * 根据文件名查询文件记录，验证文件存在性和删除状态，
     * 同时校验文件路径合法性防止路径遍历攻击，
     * 返回文件元数据信息用于后续下载操作。
     * </p>
     *
     * @param fileName 存储文件名（UUID重命名后的文件名）
     * @return 文件元数据实体对象
     * @throws com.shy.nexusix.common.exception.BusinessException 当文件不存在、已被删除或路径非法时抛出
     * @author shy
     * @since 2026-04-07
     */
    SysFile download(String fileName);

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
    Integer batchDeleteFile(List<String> ids);

}
