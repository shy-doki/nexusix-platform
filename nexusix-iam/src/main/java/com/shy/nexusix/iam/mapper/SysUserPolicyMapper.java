package com.shy.nexusix.iam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shy.nexusix.iam.dto.UserDeptDTO;
import com.shy.nexusix.iam.dto.UserRoleDTO;
import com.shy.nexusix.iam.dto.UserTenantItemDTO;
import com.shy.nexusix.iam.entity.SysUserPolicy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户策略表 Mapper 接口
 * </p>
 *
 * @author shy
 * @since 2026-06-12
 */
@Mapper
public interface SysUserPolicyMapper extends BaseMapper<SysUserPolicy> {

    /**
     * 查询用户在所有租户下的部门信息
     * @param userId 系统用户ID
     * @return 用户部门列表
     */
    List<UserDeptDTO> queryUserAllDeptInfo(@Param("userId") Long userId);

    /**
     * 查询用户在所有租户下的角色信息
     * @param userId 系统用户ID
     * @return 用户角色列表
     */
    List<UserRoleDTO> queryUserAllRoleInfo(@Param("userId") Long userId);

    /**
     * 查询用户所有租户信息
     * @param userId 系统用户ID
     * @return 用户租户列表
     */
    List<UserTenantItemDTO> queryUserAllTenantInfo(@Param("userId") Long userId);

}
