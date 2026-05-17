package com.shy.nexusix.iam.service;

import com.shy.nexusix.iam.entity.SysUserFieldPerm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户字段权限表-存储用户在各表上的字段操作权限（JSONB压缩存储，由权限体系计算后的扁平化结果） 服务类
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
public interface ISysUserFieldPermService extends IService<SysUserFieldPerm> {

}
