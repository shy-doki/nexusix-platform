package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysPermissionPolicy;
import com.shy.nexusix.iam.mapper.SysPermissionPolicyMapper;
import com.shy.nexusix.iam.service.ISysPermissionPolicyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限策略控制表 - 实现四层权限及禁用继承逻辑 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysPermissionPolicyServiceImpl extends ServiceImpl<SysPermissionPolicyMapper, SysPermissionPolicy> implements ISysPermissionPolicyService {

}
