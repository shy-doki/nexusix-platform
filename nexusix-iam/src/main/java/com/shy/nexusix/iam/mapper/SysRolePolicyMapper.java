package com.shy.nexusix.iam.mapper;

import com.shy.nexusix.iam.dto.UserRoleDTO;
import com.shy.nexusix.iam.entity.SysRolePolicy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色策略表 Mapper 接口
 * </p>
 *
 * @author shy
 * @since 2026-06-10
 */
public interface SysRolePolicyMapper extends BaseMapper<SysRolePolicy> {

    /**
     * <p>查询用户在当前租户下的角色信息</p>
     * <p>从 sys_role_policy(target_type='USER') 联查 sys_role，获取用户的角色编码和数据权限范围</p>
     * <p>sys_role_policy.target_id 为用户策略ID（sys_user_policy.id），通过 sys_role.tenant_id 过滤租户</p>
     *
     * @param userPolicyId 用户策略ID（sys_user_policy.id）
     * @param tenantId     当前登录租户ID，通过 sys_role.tenant_id 过滤
     * @return 用户角色信息列表
     */
    List<UserRoleDTO> queryUserRoleInfo(@Param("userPolicyId") Long userPolicyId, @Param("tenantId") Long tenantId);

    /**
     * <p>查询用户在所有租户下的全部角色信息（含有效/无效）</p>
     * <p>从 sys_user_policy 联查 sys_role_policy → sys_role → sys_tenant，获取用户跨租户的完整角色数据</p>
     * <p>不限制 sys_role_policy.status，以便在 Service 层按 rolePolicyStatus 分类有效/无效角色</p>
     *
     * @param userId 用户ID
     * @return 用户全部角色信息列表（含租户标识和角色策略状态）
     */
    List<UserRoleDTO> queryUserAllRoleInfo(@Param("userId") Long userId);

}
