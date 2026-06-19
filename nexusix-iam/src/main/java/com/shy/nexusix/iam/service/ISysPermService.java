package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysPerm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.iam.rto.SysPermAddRTO;
import com.shy.nexusix.iam.rto.SysPermQueryRTO;
import com.shy.nexusix.iam.rto.SysPermUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermCommonVO;
import com.shy.nexusix.iam.vo.SysPermDetailVO;
import com.shy.nexusix.iam.vo.SysPermTreeVO;
import com.shy.nexusix.common.exception.BusinessException;

import java.util.List;

/**
 * <p>系统权限服务接口</p>
 *
 * @author shy
 */
public interface ISysPermService extends IService<SysPerm> {

    /**
     * <p>查询权限列表</p>
     *
     * @return 权限通用VO列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    List<SysPermCommonVO> queryPermList();

    /**
     * <p>分页查询权限列表</p>
     *
     * @param page 分页参数
     * @return 分页后的权限通用VO列表
     * @throws BusinessException 无权限时抛出
     */
    IPage<SysPermCommonVO> queryPermPage(PageCommonRTO page);

    /**
     * <p>查询权限树形结构</p>
     *
     * @return 权限树形VO列表
     * @throws BusinessException 无权限时抛出
     */
    List<SysPermTreeVO> queryPermTreeList();

    /**
     * <p>分页查询权限树形结构</p>
     *
     * @param page 分页参数
     * @return 分页后的权限树形VO列表
     * @throws BusinessException 无权限时抛出
     */
    IPage<SysPermTreeVO> queryPermTreePage(PageCommonRTO page);

    /**
     * <p>查询指定权限的树形结构</p>
     *
     * @param id 权限ID
     * @return 以指定权限为根的树形结构
     * @throws BusinessException 数据查询失败或数据异常时抛出
     */
    SysPermTreeVO queryPermTree(String id);

    /**
     * <p>条件查询权限列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的权限分页列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    IPage<SysPermCommonVO> queryPerm(SysPermQueryRTO queryParam);

    /**
     * <p>查询权限详情</p>
     *
     * @param permCode 权限编码
     * @return 权限详情VO
     * @throws BusinessException 无权限或查询失败时抛出
     */
    SysPermDetailVO queryPermDetail(String permCode);

    /**
     * <p>新增权限</p>
     *
     * @param addParam 新增权限信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    Integer addPerm(SysPermAddRTO addParam);

    /**
     * <p>修改权限</p>
     *
     * @param updateParam 修改权限信息
     * @return 修改结果行数
     * @throws BusinessException 无权限、权限不存在或修改失败时抛出
     */
    Integer updatePerm(SysPermUpdateRTO updateParam);

    /**
     * <p>更新权限状态</p>
     *
     * @param id 权限ID
     * @param status 权限状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、权限不存在或更新失败时抛出
     */
    Integer updatePermStatus(String id, String status);

    /**
     * <p>删除权限</p>
     *
     * @param id 权限ID
     * @return 删除结果行数
     * @throws BusinessException 无权限、权限不存在或删除失败时抛出
     */
    Integer deletePerm(String id);

    /**
     * <p>批量新增权限</p>
     *
     * @param addParamList 批量新增权限信息集合
     * @return 新增结果行数
     * @throws BusinessException 无权限、参数校验失败或新增失败时抛出
     */
    Integer batchAddPerm(List<SysPermAddRTO> addParamList);

    /**
     * <p>批量修改权限</p>
     *
     * @param updateParamList 批量修改权限信息集合
     * @return 修改结果行数
     * @throws BusinessException 无权限、权限不存在或修改失败时抛出
     */
    Integer batchUpdatePerm(List<SysPermUpdateRTO> updateParamList);

    /**
     * <p>批量更新权限状态</p>
     *
     * @param ids 权限ID集合
     * @param status 权限状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、权限不存在或更新失败时抛出
     */
    Integer batchUpdatePermStatus(List<String> ids, String status);

    /**
     * <p>批量删除权限</p>
     *
     * @param ids 权限ID集合
     * @return 删除结果行数
     * @throws BusinessException 无权限、权限不存在或删除失败时抛出
     */
    Integer batchDeletePerm(List<String> ids);

}
