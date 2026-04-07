package com.shy.nexusix.billing.service.impl;

import com.shy.nexusix.billing.entity.SysTenantQuotaAdjustment;
import com.shy.nexusix.billing.mapper.SysTenantQuotaAdjustmentMapper;
import com.shy.nexusix.billing.service.ISysTenantQuotaAdjustmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户配额调整表 - 记录套餐外的配额增减 (加油包/补偿) 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysTenantQuotaAdjustmentServiceImpl extends ServiceImpl<SysTenantQuotaAdjustmentMapper, SysTenantQuotaAdjustment> implements ISysTenantQuotaAdjustmentService {

}
