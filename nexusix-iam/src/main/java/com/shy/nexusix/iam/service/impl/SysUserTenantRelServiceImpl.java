package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysUserTenantRel;
import com.shy.nexusix.iam.mapper.SysUserTenantRelMapper;
import com.shy.nexusix.iam.service.ISysUserTenantRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-租户关联表-实现用户与多租户绑定 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-05-13
 */
@Service
public class SysUserTenantRelServiceImpl extends ServiceImpl<SysUserTenantRelMapper, SysUserTenantRel> implements ISysUserTenantRelService {

}
