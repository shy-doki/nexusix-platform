package com.shy.nexusix.billing.service.impl;

import com.shy.nexusix.billing.entity.BillOrder;
import com.shy.nexusix.billing.mapper.BillOrderMapper;
import com.shy.nexusix.billing.service.IBillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 - 记录租户购买订单 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class BillOrderServiceImpl extends ServiceImpl<BillOrderMapper, BillOrder> implements IBillOrderService {

}
