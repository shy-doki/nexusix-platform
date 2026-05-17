package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.mapper.SysRoleMapper;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表-定义系统/租户/用户级角色 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

}
