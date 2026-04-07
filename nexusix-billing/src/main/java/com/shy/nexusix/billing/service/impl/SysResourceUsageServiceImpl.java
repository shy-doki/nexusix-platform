package com.shy.nexusix.billing.service.impl;

import com.shy.nexusix.billing.entity.SysResourceUsage;
import com.shy.nexusix.billing.mapper.SysResourceUsageMapper;
import com.shy.nexusix.billing.service.ISysResourceUsageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资源使用计量表 - 记录租户资源消耗流水 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysResourceUsageServiceImpl extends ServiceImpl<SysResourceUsageMapper, SysResourceUsage> implements ISysResourceUsageService {

}
