package com.shy.nexusix.org.service.impl;

import com.shy.nexusix.org.entity.SysDept;
import com.shy.nexusix.org.mapper.SysDeptMapper;
import com.shy.nexusix.org.service.ISysDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 - 存储租户组织架构，支持无限层级 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

}
