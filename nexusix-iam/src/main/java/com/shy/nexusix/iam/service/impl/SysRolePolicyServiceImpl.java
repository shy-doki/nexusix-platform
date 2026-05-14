package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysRolePolicy;
import com.shy.nexusix.iam.mapper.SysRolePolicyMapper;
import com.shy.nexusix.iam.service.ISysRolePolicyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色策略控制表-实现角色级联禁用及策略继承 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-05-13
 */
@Service
public class SysRolePolicyServiceImpl extends ServiceImpl<SysRolePolicyMapper, SysRolePolicy> implements ISysRolePolicyService {

}
