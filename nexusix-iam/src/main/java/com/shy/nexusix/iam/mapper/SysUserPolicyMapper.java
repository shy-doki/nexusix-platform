package com.shy.nexusix.iam.mapper;

import com.shy.nexusix.iam.dto.UserLoginJoinDTO;
import com.shy.nexusix.iam.dto.UserTenantItemDTO;
import com.shy.nexusix.iam.entity.SysUserPolicy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 租户策略表  Mapper 接口
 * </p>
 *
 * @author shy
 * @since 2026-06-10
 */
public interface SysUserPolicyMapper extends BaseMapper<SysUserPolicy> {

    /**
     * <p>根据用户名查询用户登录关联信息</p>
     * <p>联查 sys_user + sys_user_policy(默认租户) + sys_tenant，获取登录校验所需数据</p>
     *
     * @param username 登录用户名
     * @return 用户登录关联信息DTO
     */
    UserLoginJoinDTO queryUserLoginJoin(@Param("username") String username);

    /**
     * <p>查询用户关联的所有租户信息</p>
     * <p>从 sys_user_policy 联查 sys_tenant，获取用户所属全部租户</p>
     *
     * @param userId 用户ID
     * @return 用户关联的租户信息列表
     */
    List<UserTenantItemDTO> queryUserAllTenants(@Param("userId") Long userId);

}
