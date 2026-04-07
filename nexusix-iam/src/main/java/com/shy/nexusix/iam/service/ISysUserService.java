package com.shy.nexusix.iam.service;

import com.shy.nexusix.iam.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户基础表 - 存储全局用户信息 (不区分租户) 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysUserService extends IService<SysUser> {

}
