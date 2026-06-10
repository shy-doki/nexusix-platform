package com.shy.nexusix.iam.mapper;

import com.shy.nexusix.iam.dto.UserPermJoinDTO;
import com.shy.nexusix.iam.entity.SysPermPolicy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限策略表 Mapper 接口
 * </p>
 *
 * @author shy
 * @since 2026-06-10
 */
public interface SysPermPolicyMapper extends BaseMapper<SysPermPolicy> {

    /**
     * <p>查询用户在当前租户下的全部权限策略数据（两路径汇聚：ROLE / USER）</p>
     * <p>用户权限 = 角色具有的权限 + 用户本身具有的权限</p>
     * <p>注意：TENANT路径（target_type='TENANT'）是租户能力边界约束，不作为用户直接权限参与计算</p>
     * <p>权限来源：</p>
     * <ul>
     *   <li>ROLE路径：用户在当前租户中所属角色具有的权限策略（target_id = 角色策略ID）</li>
     *   <li>USER路径：用户在当前租户中本身具有的权限策略（target_id = 用户策略ID）</li>
     * </ul>
     *
     * @param userPolicyId 用户策略ID（sys_user_policy.id），用于ROLE和USER路径的target_id匹配
     * @param tenantId     当前登录租户ID，用于ROLE路径中角色租户归属过滤
     * @return 权限策略关联数据列表
     */
    List<UserPermJoinDTO> queryUserPermJoin(@Param("userPolicyId") Long userPolicyId, @Param("tenantId") Long tenantId);

}
