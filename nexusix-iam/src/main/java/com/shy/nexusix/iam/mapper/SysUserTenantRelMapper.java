package com.shy.nexusix.iam.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.shy.nexusix.iam.dto.UserLoginJoinDTO;
import com.shy.nexusix.iam.entity.SysUserTenantRel;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户与租户的关联关系 Mapper 接口
 * </p>
 *
 * @author shy
 * @since 2026-05-19
 */
public interface SysUserTenantRelMapper extends MPJBaseMapper<SysUserTenantRel> {

    UserLoginJoinDTO queryUserLoginJoin(@Param("username") String username);

}
