package com.shy.nexusix.iam.mapper;

import com.shy.nexusix.iam.entity.SysUserFieldPerm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户字段权限表-存储用户在各表上的字段操作权限（JSONB压缩存储，由权限体系计算后的扁平化结果） Mapper 接口
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
public interface SysUserFieldPermMapper extends BaseMapper<SysUserFieldPerm> {

}
