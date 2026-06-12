package com.shy.nexusix.iam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shy.nexusix.iam.dto.UserPermDTO;
import com.shy.nexusix.iam.entity.SysPermPolicy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限策略表 Mapper 接口
 * </p>
 *
 * @author shy
 * @since 2026-06-12
 */
@Mapper
public interface SysPermPolicyMapper extends BaseMapper<SysPermPolicy> {

    /**
     * 查询用户在所有租户下的权限信息
     * @param userId 系统用户ID
     * @return 用户权限列表
     */
    List<UserPermDTO> queryUserAllPermInfo(@Param("userId") Long userId);

}
