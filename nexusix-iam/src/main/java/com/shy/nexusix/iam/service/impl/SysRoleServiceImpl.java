package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.mapper.SysRoleMapper;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 - 服务实现类
 * </p>
 * <p>
 * 提供角色的CRUD、批量操作、状态切换、用户分配等业务逻辑实现。
 * 所有删除操作均为逻辑删除，新增/修改操作会校验角色编码唯一性。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

}
