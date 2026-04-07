package com.shy.nexusix.tenant.service.impl;

import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.xml.SysTenantMapper;
import com.shy.nexusix.tenant.service.ISysTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户信息表 - 存储租户基础信息，支持无限层级 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

}
