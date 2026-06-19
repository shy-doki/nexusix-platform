package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysPermPolicy;
import com.shy.nexusix.iam.rto.SysPermPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermPolicyBatchBindRTO;
import com.shy.nexusix.iam.rto.SysPermPolicyBatchUnbindRTO;
import com.shy.nexusix.iam.rto.SysPermPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermPolicyUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermPolicyDetailVO;
import com.shy.nexusix.common.exception.BusinessException;

import java.util.List;

/**
 * <p>权限策略服务接口</p>
 *
 * @author shy
 */
public interface ISysPermPolicyService extends IService<SysPermPolicy> {

    /**
     * <p>查询权限策略列表</p>
     *
     * @return 权限策略通用VO列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    List<SysPermPolicyCommonVO> queryPolicyList();

    /**
     * <p>分页查询权限策略列表</p>
     *
     * @param page 分页参数
     * @return 分页后的权限策略通用VO列表
     * @throws BusinessException 无权限时抛出
     */
    IPage<SysPermPolicyCommonVO> queryPolicyPage(PageCommonRTO page);

    /**
     * <p>条件查询权限策略列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的权限策略分页列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    IPage<SysPermPolicyCommonVO> queryPolicy(SysPermPolicyQueryRTO queryParam);

    /**
     * <p>查询权限策略详情</p>
     *
     * @param policyCode 策略编码
     * @return 权限策略详情VO
     * @throws BusinessException 无权限或查询失败时抛出
     */
    SysPermPolicyDetailVO queryPolicyDetail(String policyCode);

    /**
     * <p>新增权限策略</p>
     *
     * @param addParam 新增权限策略信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    Integer addPolicy(SysPermPolicyAddRTO addParam);

    /**
     * <p>修改权限策略</p>
     *
     * @param updateParam 修改权限策略信息
     * @return 修改结果行数
     * @throws BusinessException 无权限或策略不存在或修改失败时抛出
     */
    Integer updatePolicy(SysPermPolicyUpdateRTO updateParam);

    /**
     * <p>更新权限策略状态</p>
     *
     * @param policyCode 策略编码
     * @param status 策略状态
     * @return 更新结果行数
     * @throws BusinessException 无权限或策略不存在或更新失败时抛出
     */
    Integer updatePolicyStatus(String policyCode, String status);

    /**
     * <p>删除权限策略</p>
     *
     * @param policyCode 策略编码
     * @return 删除结果行数
     * @throws BusinessException 无权限或策略不存在或删除失败时抛出
     */
    Integer deletePolicy(String policyCode);

    /**
     * <p>批量新增权限策略</p>
     *
     * @param addParamList 批量新增权限策略信息集合
     * @return 成功新增的权限策略数量
     * @throws BusinessException 无权限或新增失败时抛出
     */
    Integer batchAddPolicy(List<SysPermPolicyAddRTO> addParamList);

    /**
     * <p>批量修改权限策略</p>
     *
     * @param updateParamList 批量修改权限策略信息集合
     * @return 成功修改的权限策略数量
     * @throws BusinessException 无权限或修改失败时抛出
     */
    Integer batchUpdatePolicy(List<SysPermPolicyUpdateRTO> updateParamList);

    /**
     * <p>批量更新权限策略状态</p>
     *
     * @param policyCodeList 策略编码集合
     * @param status 策略状态
     * @return 更新结果行数
     * @throws BusinessException 无权限或更新失败时抛出
     */
    Integer batchUpdatePolicyStatus(List<String> policyCodeList, String status);

    /**
     * <p>批量删除权限策略</p>
     *
     * @param policyCodeList 策略编码集合
     * @return 删除结果行数
     * @throws BusinessException 无权限或删除失败时抛出
     */
    Integer batchDeletePolicy(List<String> policyCodeList);

    /**
     * <p>批量绑定权限到目标</p>
     *
     * @param bindParam 批量绑定参数
     * @return 成功绑定的策略数量
     * @throws BusinessException 无权限或绑定失败时抛出
     */
    Integer batchBindToTarget(SysPermPolicyBatchBindRTO bindParam);

    /**
     * <p>批量解绑权限与目标</p>
     *
     * @param unbindParam 批量解绑参数
     * @return 成功解绑的策略数量
     * @throws BusinessException 无权限或解绑失败时抛出
     */
    Integer batchUnbindFromTarget(SysPermPolicyBatchUnbindRTO unbindParam);

}
