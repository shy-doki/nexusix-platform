package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysUserPolicy;
import com.shy.nexusix.iam.rto.SysUserPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyBatchBindRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyBatchUnbindRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysUserPolicyUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysUserPolicyDetailVO;
import com.shy.nexusix.common.exception.BusinessException;

import java.util.List;

/**
 * <p>用户策略服务接口</p>
 *
 * @author shy
 */
public interface ISysUserPolicyService extends IService<SysUserPolicy> {

    /**
     * <p>查询用户策略列表</p>
     *
     * @return 用户策略通用VO列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    List<SysUserPolicyCommonVO> queryPolicyList();

    /**
     * <p>分页查询用户策略列表</p>
     *
     * @param page 分页参数
     * @return 分页后的用户策略通用VO列表
     * @throws BusinessException 无权限时抛出
     */
    IPage<SysUserPolicyCommonVO> queryPolicyPage(PageCommonRTO page);

    /**
     * <p>条件查询用户策略列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的用户策略分页列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    IPage<SysUserPolicyCommonVO> queryPolicy(SysUserPolicyQueryRTO queryParam);

    /**
     * <p>查询用户策略详情</p>
     *
     * @param policyCode 策略编码
     * @return 用户策略详情VO
     * @throws BusinessException 无权限或查询失败时抛出
     */
    SysUserPolicyDetailVO queryPolicyDetail(String policyCode);

    /**
     * <p>新增用户策略</p>
     *
     * @param addParam 新增用户策略信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    Integer addPolicy(SysUserPolicyAddRTO addParam);

    /**
     * <p>修改用户策略</p>
     *
     * @param updateParam 修改用户策略信息
     * @return 修改结果行数
     * @throws BusinessException 无权限或策略不存在或修改失败时抛出
     */
    Integer updatePolicy(SysUserPolicyUpdateRTO updateParam);

    /**
     * <p>更新用户策略状态</p>
     *
     * @param policyCode 策略编码
     * @param status 策略状态
     * @return 更新结果行数
     * @throws BusinessException 无权限或策略不存在或更新失败时抛出
     */
    Integer updatePolicyStatus(String policyCode, String status);

    /**
     * <p>删除用户策略</p>
     *
     * @param policyCode 策略编码
     * @return 删除结果行数
     * @throws BusinessException 无权限或策略不存在或删除失败时抛出
     */
    Integer deletePolicy(String policyCode);

    /**
     * <p>批量新增用户策略</p>
     *
     * @param addParamList 批量新增用户策略信息集合
     * @return 成功新增的用户策略数量
     * @throws BusinessException 无权限或新增失败时抛出
     */
    Integer batchAddPolicy(List<SysUserPolicyAddRTO> addParamList);

    /**
     * <p>批量修改用户策略</p>
     *
     * @param updateParamList 批量修改用户策略信息集合
     * @return 成功修改的用户策略数量
     * @throws BusinessException 无权限或修改失败时抛出
     */
    Integer batchUpdatePolicy(List<SysUserPolicyUpdateRTO> updateParamList);

    /**
     * <p>批量更新用户策略状态</p>
     *
     * @param policyCodeList 策略编码集合
     * @param status 策略状态
     * @return 更新结果行数
     * @throws BusinessException 无权限或更新失败时抛出
     */
    Integer batchUpdatePolicyStatus(List<String> policyCodeList, String status);

    /**
     * <p>批量删除用户策略</p>
     *
     * @param policyCodeList 策略编码集合
     * @return 删除结果行数
     * @throws BusinessException 无权限或删除失败时抛出
     */
    Integer batchDeletePolicy(List<String> policyCodeList);

    /**
     * <p>批量绑定用户到目标</p>
     *
     * @param bindParam 批量绑定参数
     * @return 成功绑定的策略数量
     * @throws BusinessException 无权限或绑定失败时抛出
     */
    Integer batchBindToTarget(SysUserPolicyBatchBindRTO bindParam);

    /**
     * <p>批量解绑用户与目标</p>
     *
     * @param unbindParam 批量解绑参数
     * @return 成功解绑的策略数量
     * @throws BusinessException 无权限或解绑失败时抛出
     */
    Integer batchUnbindFromTarget(SysUserPolicyBatchUnbindRTO unbindParam);

}
