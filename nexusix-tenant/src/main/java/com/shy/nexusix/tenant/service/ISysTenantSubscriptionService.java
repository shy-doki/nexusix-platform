package com.shy.nexusix.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.entity.SysTenantSubscription;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionUpdateRTO;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionDetailVO;

import java.util.List;

/**
 * <p>
 * 租户套餐订阅表 - 记录租户购买的套餐及订阅状态 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysTenantSubscriptionService extends IService<SysTenantSubscription> {

    /**
     * 查询订阅列表
     *
     * @return 订阅通用视图对象列表
     */
    List<SysTenantSubscriptionCommonVO> querySubscriptionList();

    /**
     * 分页查询订阅列表
     *
     * @param page 分页参数
     * @return 订阅通用视图对象分页
     */
    IPage<SysTenantSubscriptionCommonVO> querySubscriptionPage(PageCommonRTO page);

    /**
     * 条件查询订阅列表
     *
     * @param queryParam 查询条件
     * @return 订阅通用视图对象分页
     */
    IPage<SysTenantSubscriptionCommonVO> querySubscription(SysTenantSubscriptionQueryRTO queryParam);

    /**
     * 查询订阅详情
     *
     * @param id 订阅ID
     * @return 订阅详情视图对象
     */
    SysTenantSubscriptionDetailVO querySubscriptionDetail(String id);

    /**
     * 新增订阅
     *
     * @param addParam 新增请求对象
     * @return 影响行数
     */
    Integer addSubscription(SysTenantSubscriptionAddRTO addParam);

    /**
     * 修改订阅
     *
     * @param updateParam 更新请求对象
     * @return 影响行数
     */
    Integer updateSubscription(SysTenantSubscriptionUpdateRTO updateParam);

    /**
     * 更新订阅状态
     *
     * @param id     订阅ID
     * @param status 状态值
     * @return 影响行数
     */
    Integer updateSubscriptionStatus(String id, String status);

    /**
     * 删除订阅（逻辑删除）
     *
     * @param id 订阅ID
     * @return 影响行数
     */
    Integer deleteSubscription(String id);

    /**
     * 批量新增订阅
     *
     * @param addParamList 新增请求对象列表
     * @return 影响行数
     */
    Integer batchAddSubscription(List<SysTenantSubscriptionAddRTO> addParamList);

    /**
     * 批量修改订阅
     *
     * @param updateParamList 更新请求对象列表
     * @return 影响行数
     */
    Integer batchUpdateSubscription(List<SysTenantSubscriptionUpdateRTO> updateParamList);

    /**
     * 批量更新订阅状态
     *
     * @param ids    订阅ID列表
     * @param status 状态值
     * @return 影响行数
     */
    Integer batchUpdateSubscriptionStatus(List<String> ids, String status);

    /**
     * 批量删除订阅（逻辑删除）
     *
     * @param ids 订阅ID列表
     * @return 影响行数
     */
    Integer batchDeleteSubscription(List<String> ids);

}
