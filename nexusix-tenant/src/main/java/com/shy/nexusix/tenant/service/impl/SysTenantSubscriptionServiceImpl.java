package com.shy.nexusix.tenant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.entity.SysTenantSubscription;
import com.shy.nexusix.tenant.mapper.SysTenantSubscriptionMapper;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantSubscriptionUpdateRTO;
import com.shy.nexusix.tenant.service.ISysTenantService;
import com.shy.nexusix.tenant.service.ISysTenantSubscriptionService;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantSubscriptionDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>租户套餐订阅服务实现类</p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysTenantSubscriptionServiceImpl extends ServiceImpl<SysTenantSubscriptionMapper, SysTenantSubscription> implements ISysTenantSubscriptionService {

    @Autowired
    private ISysTenantService iSysTenantService;


    @Override
    public List<SysTenantSubscriptionCommonVO> querySubscriptionList() {
        return List.of();
    }

    @Override
    public IPage<SysTenantSubscriptionCommonVO> querySubscriptionPage(PageCommonRTO page) {
        return null;
    }

    @Override
    public IPage<SysTenantSubscriptionCommonVO> querySubscription(SysTenantSubscriptionQueryRTO queryParam) {
        return null;
    }

    @Override
    public SysTenantSubscriptionDetailVO querySubscriptionDetail(String id) {
        return null;
    }

    @Override
    public Integer addSubscription(SysTenantSubscriptionAddRTO addParam) {
        return 0;
    }

    @Override
    public Integer updateSubscription(SysTenantSubscriptionUpdateRTO updateParam) {
        return 0;
    }

    @Override
    public Integer updateSubscriptionStatus(String id, String status) {
        return 0;
    }

    @Override
    public Integer deleteSubscription(String id) {
        return 0;
    }

    @Override
    public Integer batchAddSubscription(List<SysTenantSubscriptionAddRTO> addParamList) {
        return 0;
    }

    @Override
    public Integer batchUpdateSubscription(List<SysTenantSubscriptionUpdateRTO> updateParamList) {
        return 0;
    }

    @Override
    public Integer batchUpdateSubscriptionStatus(List<String> ids, String status) {
        return 0;
    }

    @Override
    public Integer batchDeleteSubscription(List<String> ids) {
        return 0;
    }
}
