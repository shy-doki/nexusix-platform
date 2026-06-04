package com.shy.nexusix.iam.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.shy.nexusix.iam.dto.UserPermJoinDTO;
import com.shy.nexusix.iam.entity.SysUserPermRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户与权限策略直接关联表 Mapper 接口
 * </p>
 *
 * @author shy
 * @since 2026-05-19
 */
public interface SysUserPermRelMapper extends MPJBaseMapper<SysUserPermRel> {

    List<UserPermJoinDTO> queryUserPermJoin(@Param("userTenantRelId") Long userTenantRelId);

}
