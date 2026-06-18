package com.shy.nexusix.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantAssignRTO;
import com.shy.nexusix.tenant.rto.SysTenantQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantRegisterRTO;
import com.shy.nexusix.tenant.rto.SysTenantReviewRTO;
import com.shy.nexusix.tenant.rto.SysTenantSwitchRTO;
import com.shy.nexusix.tenant.rto.SysTenantUpdateRTO;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * <p>租户信息服务接口</p>
 *
 * @author shy
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * <p>查询租户列表</p>
     *
     * @return 租户通用VO列表
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或查询失败时抛出
     */
    List<SysTenantCommonVO> queryTenantList();

    /**
     * <p>分页查询租户列表</p>
     *
     * @param page 分页参数
     * @return 分页后的租户通用VO列表
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限时抛出
     */
    IPage<SysTenantCommonVO> queryTenantPage(PageCommonRTO page);

    /**
     * <p>查询租户树形结构</p>
     *
     * @return 租户树形VO列表
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限时抛出
     */
    List<SysTenantTreeVO> queryTenantTreeList();

    /**
     * <p>分页查询租户树形结构</p>
     *
     * @param page 分页参数
     * @return 分页后的租户树形VO列表
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限时抛出
     */
    IPage<SysTenantTreeVO> queryTenantTreePage(PageCommonRTO page);

    /**
     * <p>查询指定租户的树形结构</p>
     *
     * @param id 租户ID
     * @return 以指定租户为根的树形结构
     * @throws com.shy.nexusix.common.exception.BusinessException 数据查询失败或数据异常时抛出
     */
    SysTenantTreeVO queryTenantTree(String id);

    /**
     * <p>条件查询租户列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的租户分页列表
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或查询失败时抛出
     */
    IPage<SysTenantCommonVO> queryTenant(SysTenantQueryRTO queryParam);

    /**
     * <p>查询租户详情</p>
     *
     * @param tenantCode 租户编码
     * @return 租户详情VO
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或查询失败时抛出
     */
    SysTenantDetailVO queryTenantDetail(String tenantCode);

    /**
     * <p>新增租户</p>
     *
     * @param addParam 新增租户信息
     * @return 新增结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或新增失败时抛出
     */
    Integer addTenant(SysTenantAddRTO addParam);

    /**
     * <p>修改租户</p>
     *
     * @param updateParam 修改租户信息
     * @return 修改结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限、租户不存在或修改失败时抛出
     */
    Integer updateTenant(SysTenantUpdateRTO updateParam);

    /**
     * <p>更新租户状态</p>
     *
     * @param id 租户ID
     * @param status 租户状态
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限、租户不存在或更新失败时抛出
     */
    Integer updateTenantStatus(String id, String status);

    /**
     * <p>删除租户</p>
     *
     * @param id 租户ID
     * @return 删除结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限、租户不存在或删除失败时抛出
     */
    Integer deleteTenant(@Valid String id);

    /**
     * <p>批量新增租户</p>
     *
     * @param addParamList 批量新增租户信息集合
     * @return 新增结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限、参数校验失败或新增失败时抛出
     */
    Integer batchAddTenant(List<SysTenantAddRTO> addParamList);

    /**
     * <p>批量修改租户</p>
     *
     * @param updateParamList 批量修改租户信息集合
     * @return 修改结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限、租户不存在或修改失败时抛出
     */
    Integer batchUpdateTenant(List<SysTenantUpdateRTO> updateParamList);

    /**
     * <p>批量更新租户状态</p>
     *
     * @param ids 租户ID集合
     * @param status 租户状态
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限、租户不存在或更新失败时抛出
     */
    Integer batchUpdateTenantStatus(List<String> ids, String status);

    /**
     * <p>批量删除租户</p>
     *
     * @param ids 租户ID集合
     * @return 删除结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限、租户不存在或删除失败时抛出
     */
    Integer batchDeleteTenant(List<String> ids);

    /**
     * <p>分配子租户</p>
     *
     * @param assignParam 子租户分配参数
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限、父租户不存在或分配失败时抛出
     */
    Integer assignSubTenant(SysTenantAssignRTO assignParam);

    /**
     * <p>分配父租户</p>
     *
     * @param assignParam 父租户分配参数
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限、参数非法或分配失败时抛出
     */
    Integer assignParentTenant(SysTenantAssignRTO assignParam);

    /**
     * <p>租户自助注册</p>
     *
     * @param registerParam 注册信息
     * @return 新增结果行数
     * @throws BusinessException 租户编码已存在或父租户不存在时抛出
     */
    Integer registerTenant(SysTenantRegisterRTO registerParam);

    /**
     * <p>审核租户注册</p>
     *
     * @param reviewParam 审核信息
     * @return 审核结果行数
     * @throws BusinessException 租户不存在或状态非PENDING时抛出
     */
    Integer reviewTenant(SysTenantReviewRTO reviewParam);

    /**
     * <p>切换租户</p>
     *
     * @param switchParam 切换参数
     * @return 切换后的租户信息
     * @throws BusinessException 用户不属于目标租户或租户状态异常时抛出
     */
    SysTenantCommonVO switchTenant(SysTenantSwitchRTO switchParam);

}
