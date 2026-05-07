package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysUserRoleRel;
import com.shy.nexusix.iam.rto.SysUserRoleRelAddRTO;
import com.shy.nexusix.iam.rto.SysUserRoleRelQueryRTO;
import com.shy.nexusix.iam.rto.SysUserRoleRelUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserRoleRelCommonVO;
import com.shy.nexusix.iam.vo.SysUserRoleRelDetailVO;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 - 用户与角色的绑定关系 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysUserRoleRelService extends IService<SysUserRoleRel> {

    /**
     * 查询用户角色关联列表
     *
     * @return 关联通用视图对象列表
     */
    List<SysUserRoleRelCommonVO> queryUserRoleRelList();

    /**
     * 分页查询用户角色关联列表
     *
     * @param page 分页参数
     * @return 关联通用视图对象分页
     */
    IPage<SysUserRoleRelCommonVO> queryUserRoleRelPage(PageCommonRTO page);

    /**
     * 条件查询用户角色关联列表
     *
     * @param queryParam 查询条件
     * @return 关联通用视图对象分页
     */
    IPage<SysUserRoleRelCommonVO> queryUserRoleRel(SysUserRoleRelQueryRTO queryParam);

    /**
     * 查询用户角色关联详情
     *
     * @param id 关联记录ID
     * @return 关联详情视图对象
     */
    SysUserRoleRelDetailVO queryUserRoleRelDetail(String id);

    /**
     * 新增用户角色关联
     *
     * @param addParam 新增请求对象
     * @return 影响行数
     */
    Integer addUserRoleRel(SysUserRoleRelAddRTO addParam);

    /**
     * 修改用户角色关联
     *
     * @param updateParam 更新请求对象
     * @return 影响行数
     */
    Integer updateUserRoleRel(SysUserRoleRelUpdateRTO updateParam);

    /**
     * 删除用户角色关联（逻辑删除）
     *
     * @param id 关联记录ID
     * @return 影响行数
     */
    Integer deleteUserRoleRel(String id);

    /**
     * 批量新增用户角色关联
     *
     * @param addParamList 新增请求对象列表
     * @return 影响行数
     */
    Integer batchAddUserRoleRel(List<SysUserRoleRelAddRTO> addParamList);

    /**
     * 批量修改用户角色关联
     *
     * @param updateParamList 更新请求对象列表
     * @return 影响行数
     */
    Integer batchUpdateUserRoleRel(List<SysUserRoleRelUpdateRTO> updateParamList);

    /**
     * 批量删除用户角色关联（逻辑删除）
     *
     * @param ids 关联记录ID列表
     * @return 影响行数
     */
    Integer batchDeleteUserRoleRel(List<String> ids);

}
