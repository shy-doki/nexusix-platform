package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysUserTenantRel;
import com.shy.nexusix.iam.rto.SysUserTenantRelAddRTO;
import com.shy.nexusix.iam.rto.SysUserTenantRelQueryRTO;
import com.shy.nexusix.iam.rto.SysUserTenantRelUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserTenantRelCommonVO;
import com.shy.nexusix.iam.vo.SysUserTenantRelDetailVO;

import java.util.List;

/**
 * <p>
 * 用户 - 租户关联表 - 实现用户与多租户绑定 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysUserTenantRelService extends IService<SysUserTenantRel> {

    /**
     * 查询用户租户关联列表
     */
    List<SysUserTenantRelCommonVO> queryUserTenantRelList();

    /**
     * 分页查询用户租户关联列表
     */
    IPage<SysUserTenantRelCommonVO> queryUserTenantRelPage(PageCommonRTO page);

    /**
     * 条件查询用户租户关联列表
     */
    IPage<SysUserTenantRelCommonVO> queryUserTenantRel(SysUserTenantRelQueryRTO queryParam);

    /**
     * 查询用户租户关联详情
     */
    SysUserTenantRelDetailVO queryUserTenantRelDetail(String id);

    /**
     * 新增用户租户关联
     */
    Integer addUserTenantRel(SysUserTenantRelAddRTO addParam);

    /**
     * 修改用户租户关联
     */
    Integer updateUserTenantRel(SysUserTenantRelUpdateRTO updateParam);

    /**
     * 删除用户租户关联（逻辑删除）
     */
    Integer deleteUserTenantRel(String id);

    /**
     * 批量新增用户租户关联
     */
    Integer batchAddUserTenantRel(List<SysUserTenantRelAddRTO> addParamList);

    /**
     * 批量修改用户租户关联
     */
    Integer batchUpdateUserTenantRel(List<SysUserTenantRelUpdateRTO> updateParamList);

    /**
     * 批量删除用户租户关联（逻辑删除）
     */
    Integer batchDeleteUserTenantRel(List<String> ids);

}
