package com.shy.nexusix.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.entity.SysTenantPolicy;
import com.shy.nexusix.tenant.rto.SysTenantPolicyAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantPolicyBatchBindRTO;
import com.shy.nexusix.tenant.rto.SysTenantPolicyBatchUnbindRTO;
import com.shy.nexusix.tenant.rto.SysTenantPolicyQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantPolicyUpdateRTO;
import com.shy.nexusix.tenant.vo.SysTenantPolicyCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantPolicyDetailVO;

import java.util.List;

/**
 * <p>租户策略服务接口</p>
 *
 * @author shy
 */
public interface ISysTenantPolicyService extends IService<SysTenantPolicy> {

    /**
     * <p>查询租户策略列表</p>
     *
     * @return 租户策略通用VO列表
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或查询失败时抛出
     */
    List<SysTenantPolicyCommonVO> queryPolicyList();

    /**
     * <p>分页查询租户策略列表</p>
     *
     * @param page 分页参数
     * @return 分页后的租户策略通用VO列表
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限时抛出
     */
    IPage<SysTenantPolicyCommonVO> queryPolicyPage(PageCommonRTO page);

    /**
     * <p>条件查询租户策略列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的租户策略分页列表
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或查询失败时抛出
     */
    IPage<SysTenantPolicyCommonVO> queryPolicy(SysTenantPolicyQueryRTO queryParam);

    /**
     * <p>查询租户策略详情</p>
     *
     * @param policyCode 策略编码
     * @return 租户策略详情VO
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或查询失败时抛出
     */
    SysTenantPolicyDetailVO queryPolicyDetail(String policyCode);

    /**
     * <p>新增租户策略</p>
     *
     * @param addParam 新增租户策略信息
     * @return 新增结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或新增失败时抛出
     */
    Integer addPolicy(SysTenantPolicyAddRTO addParam);

    /**
     * <p>修改租户策略</p>
     *
     * @param updateParam 修改租户策略信息
     * @return 修改结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或策略不存在或修改失败时抛出
     */
    Integer updatePolicy(SysTenantPolicyUpdateRTO updateParam);

    /**
     * <p>更新租户策略状态</p>
     *
     * @param policyCode 策略编码
     * @param status 策略状态
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或策略不存在或更新失败时抛出
     */
    Integer updatePolicyStatus(String policyCode, String status);

    /**
     * <p>删除租户策略</p>
     *
     * @param policyCode 策略编码
     * @return 删除结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或策略不存在或删除失败时抛出
     */
    Integer deletePolicy(String policyCode);

    /**
     * <p>批量新增租户策略</p>
     *
     * @param addParamList 批量新增租户策略信息集合
     * @return 成功新增的租户策略数量
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或新增失败时抛出
     */
    Integer batchAddPolicy(List<SysTenantPolicyAddRTO> addParamList);

    /**
     * <p>批量修改租户策略</p>
     *
     * @param updateParamList 批量修改租户策略信息集合
     * @return 成功修改的租户策略数量
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或修改失败时抛出
     */
    Integer batchUpdatePolicy(List<SysTenantPolicyUpdateRTO> updateParamList);

    /**
     * <p>批量更新租户策略状态</p>
     *
     * @param policyCodeList 策略编码集合
     * @param status 策略状态
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或更新失败时抛出
     */
    Integer batchUpdatePolicyStatus(List<String> policyCodeList, String status);

    /**
     * <p>批量删除租户策略</p>
     *
     * @param policyCodeList 策略编码集合
     * @return 删除结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或删除失败时抛出
     */
    Integer batchDeletePolicy(List<String> policyCodeList);

    /**
     * <p>批量绑定部门/角色到租户</p>
     *
     * @param bindParam 批量绑定参数
     * @return 成功绑定的策略数量
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或绑定失败时抛出
     */
    Integer batchBindToTenant(SysTenantPolicyBatchBindRTO bindParam);

    /**
     * <p>批量解绑部门/角色与租户</p>
     *
     * @param unbindParam 批量解绑参数
     * @return 成功解绑的策略数量
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或解绑失败时抛出
     */
    Integer batchUnbindFromTenant(SysTenantPolicyBatchUnbindRTO unbindParam);

    /**
     * <p>查询租户的所有部门绑定</p>
     *
     * @param tenantId 租户ID
     * @return 租户策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或查询失败时抛出
     */
    List<SysTenantPolicyCommonVO> queryTenantDepts(Long tenantId);

    /**
     * <p>查询租户的所有角色绑定</p>
     *
     * @param tenantId 租户ID
     * @return 租户策略列表
     * @throws com.shy.nexusix.common.exception.BusinessException 无权限或查询失败时抛出
     */
    List<SysTenantPolicyCommonVO> queryTenantRoles(Long tenantId);

}
