package com.shy.nexusix.tenant.service.impl;

import com.shy.nexusix.tenant.entity.SysTenantSubscription;
import com.shy.nexusix.tenant.xml.SysTenantSubscriptionMapper;
import com.shy.nexusix.tenant.service.ISysTenantSubscriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户套餐订阅表 - 记录租户购买的套餐及订阅状态 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysTenantSubscriptionServiceImpl extends ServiceImpl<SysTenantSubscriptionMapper, SysTenantSubscription> implements ISysTenantSubscriptionService {

}
