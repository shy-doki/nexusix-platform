package com.shy.nexusix.iam.service.impl;

import com.shy.nexusix.iam.entity.SysUserFieldPerm;
import com.shy.nexusix.iam.mapper.SysUserFieldPermMapper;
import com.shy.nexusix.iam.service.ISysUserFieldPermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户字段权限表-存储用户在各表上的字段操作权限（JSONB压缩存储，由权限体系计算后的扁平化结果） 服务实现类
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Service
public class SysUserFieldPermServiceImpl extends ServiceImpl<SysUserFieldPermMapper, SysUserFieldPerm> implements ISysUserFieldPermService {

}
