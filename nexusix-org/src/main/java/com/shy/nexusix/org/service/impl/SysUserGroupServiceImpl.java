package com.shy.nexusix.org.service.impl;

import com.shy.nexusix.org.entity.SysUserGroup;
import com.shy.nexusix.org.mapper.SysUserGroupMapper;
import com.shy.nexusix.org.service.ISysUserGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户组表 - 虚拟组，用于跨部门权限分配 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysUserGroupServiceImpl extends ServiceImpl<SysUserGroupMapper, SysUserGroup> implements ISysUserGroupService {

}
