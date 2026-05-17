package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysPermPolicy;
import com.shy.nexusix.iam.mapper.SysPermPolicyMapper;
import com.shy.nexusix.iam.service.ISysPermPolicyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限策略控制表-实现四层权限及禁用继承逻辑 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Service
public class SysPermPolicyServiceImpl extends ServiceImpl<SysPermPolicyMapper, SysPermPolicy> implements ISysPermPolicyService {

}
