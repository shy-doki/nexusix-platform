package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.iam.rto.SysPermissionAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermissionCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionDetailVO;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;

import java.util.List;

/**
 * <p>
 * 权限资源表 - 服务接口
 * </p>
 * <p>
 * 提供权限资源的CRUD、树形结构、批量操作、状态切换等业务能力。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
public interface ISysPermissionService extends IService<SysPermission> {

    /**
     * 查询权限列表
     *
     * @return 权限公共视图对象列表
     */
    List<SysPermissionCommonVO> queryPermissionList();

    /**
     * 分页查询权限
     *
     * @param page 分页参数
     * @return 分页后的权限公共视图对象
     */
    IPage<SysPermissionCommonVO> queryPermissionPage(PageCommonRTO page);

    /**
     * 查询权限树形列表
     * <p>
     * 返回按parentId层级关系构建的树形结构
     * </p>
     *
     * @return 权限树形视图对象列表
     */
    List<SysPermissionTreeVO> queryPermissionTreeList();

    /**
     * 条件查询权限
     *
     * @param queryParam 查询条件（支持权限名称、标识、类型、状态筛选）
     * @return 分页后的权限公共视图对象
     */
    IPage<SysPermissionCommonVO> queryPermission(SysPermissionQueryRTO queryParam);

    /**
     * 查询权限详情
     *
     * @param id 权限ID
     * @return 权限详情视图对象
     * @throws com.shy.nexusix.common.exception.BusinessException 当权限不存在时抛出
     */
    SysPermissionDetailVO queryPermissionDetail(String id);

    /**
     * 新增权限
     * <p>
     * 会校验权限标识的唯一性，parentId为空时默认设为0（顶级权限）
     * </p>
     *
     * @param addParam 新增参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当权限标识已存在时抛出
     */
    Integer addPermission(SysPermissionAddRTO addParam);

    /**
     * 修改权限
     * <p>
     * 会校验权限标识的唯一性（排除自身），parentId为空时默认设为0
     * </p>
     *
     * @param updateParam 修改参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当权限不存在或标识已被占用时抛出
     */
    Integer updatePermission(SysPermissionUpdateRTO updateParam);

    /**
     * 更新权限状态
     *
     * @param id     权限ID
     * @param status 状态值（1-启用 0-禁用）
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当权限不存在或状态未变更时抛出
     */
    Integer updatePermissionStatus(String id, String status);

    /**
     * 删除权限
     * <p>
     * 逻辑删除，删除前会校验是否存在子权限
     * </p>
     *
     * @param id 权限ID
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当权限不存在或存在子权限时抛出
     */
    Integer deletePermission(String id);

    /**
     * 批量新增权限
     * <p>
     * 单次上限100条，会校验权限标识的唯一性（含批量内部去重）
     * </p>
     *
     * @param addParamList 新增参数列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当存在重复标识或超过上限时抛出
     */
    Integer batchAddPermission(List<SysPermissionAddRTO> addParamList);

    /**
     * 批量修改权限
     * <p>
     * 单次上限100条，会校验ID存在性和标识唯一性
     * </p>
     *
     * @param updateParamList 修改参数列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当部分权限不存在或标识冲突时抛出
     */
    Integer batchUpdatePermission(List<SysPermissionUpdateRTO> updateParamList);

    /**
     * 批量删除权限
     * <p>
     * 单次上限100条，逻辑删除，删除前会校验是否存在子权限
     * </p>
     *
     * @param ids 权限ID列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当存在子权限或超过上限时抛出
     */
    Integer batchDeletePermission(List<String> ids);

}
