package com.shy.nexusix.billing.service.impl;

import com.shy.nexusix.billing.entity.BillInvoice;
import com.shy.nexusix.billing.mapper.BillInvoiceMapper;
import com.shy.nexusix.billing.service.IBillInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 发票管理表 - 记录租户发票申请与开具 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class BillInvoiceServiceImpl extends ServiceImpl<BillInvoiceMapper, BillInvoice> implements IBillInvoiceService {

}
