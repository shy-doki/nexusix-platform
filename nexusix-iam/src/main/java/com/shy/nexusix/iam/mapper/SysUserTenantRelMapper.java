package com.shy.nexusix.iam.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.shy.nexusix.iam.dto.UserLoginJoinDTO;
import com.shy.nexusix.iam.dto.UserTenantItemDTO;
import com.shy.nexusix.iam.entity.SysUserTenantRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查询用户关联的所有租户信息（包含租户状态），用于登录时缓存有效/无效租户列表
     *
     * @param userId 用户ID
     * @return 用户关联的所有租户列表
     */
    List<UserTenantItemDTO> queryUserAllTenants(@Param("userId") Long userId);

}
