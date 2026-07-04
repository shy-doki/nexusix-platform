package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleGrantPermRTO;
import com.shy.nexusix.iam.rto.SysRoleQueryRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermCommonVO;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;

import java.util.List;

/**
 * <p>系统角色服务接口</p>
 *
 * @author shy
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * <p>查询角色列表</p>
     *
     * @return 角色通用VO列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    List<SysRoleCommonVO> queryRoleList();

    /**
     * <p>分页查询角色列表</p>
     *
     * @param page 分页参数
     * @return 分页后的角色通用VO列表
     * @throws BusinessException 无权限时抛出
     */
    IPage<SysRoleCommonVO> queryRolePage(PageCommonRTO page);

    /**
     * <p>条件查询角色列表</p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的角色分页列表
     * @throws BusinessException 无权限或查询失败时抛出
     */
    IPage<SysRoleCommonVO> queryRole(SysRoleQueryRTO queryParam);

    /**
     * <p>查询角色详情</p>
     *
     * @param roleCode 角色编码
     * @return 角色详情VO
     * @throws BusinessException 无权限或查询失败时抛出
     */
    SysRoleDetailVO queryRoleDetail(String roleCode);

    /**
     * <p>新增角色</p>
     *
     * @param addParam 新增角色信息
     * @return 新增结果行数
     * @throws BusinessException 无权限或新增失败时抛出
     */
    Integer addRole(SysRoleAddRTO addParam);

    /**
     * <p>修改角色</p>
     *
     * @param updateParam 修改角色信息
     * @return 修改结果行数
     * @throws BusinessException 无权限、角色不存在或修改失败时抛出
     */
    Integer updateRole(SysRoleUpdateRTO updateParam);

    /**
     * <p>更新角色状态</p>
     *
     * @param id 角色ID
     * @param status 角色状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、角色不存在或更新失败时抛出
     */
    Integer updateRoleStatus(String id, String status);

    /**
     * <p>删除角色</p>
     *
     * @param id 角色ID
     * @return 删除结果行数
     * @throws BusinessException 无权限、角色不存在或删除失败时抛出
     */
    Integer deleteRole(String id);

    /**
     * <p>批量新增角色</p>
     *
     * @param addParamList 批量新增角色信息集合
     * @return 新增结果行数
     * @throws BusinessException 无权限、参数校验失败或新增失败时抛出
     */
    Integer batchAddRole(List<SysRoleAddRTO> addParamList);

    /**
     * <p>批量修改角色</p>
     *
     * @param updateParamList 批量修改角色信息集合
     * @return 修改结果行数
     * @throws BusinessException 无权限、角色不存在或修改失败时抛出
     */
    Integer batchUpdateRole(List<SysRoleUpdateRTO> updateParamList);

    /**
     * <p>批量更新角色状态</p>
     *
     * @param ids 角色ID集合
     * @param status 角色状态
     * @return 更新结果行数
     * @throws BusinessException 无权限、角色不存在或更新失败时抛出
     */
    Integer batchUpdateRoleStatus(List<String> ids, String status);

    /**
     * <p>批量删除角色</p>
     *
     * @param ids 角色ID集合
     * @return 删除结果行数
     * @throws BusinessException 无权限、角色不存在或删除失败时抛出
     */
    Integer batchDeleteRole(List<String> ids);

    /**
     * <p>查询角色权限</p>
     *
     * @param roleCode 角色编码
     * @return 角色拥有的权限通用VO列表
     * @throws BusinessException 角色不存在时抛出
     */
    List<SysPermCommonVO> queryRolePerms(String roleCode);

    /**
     * <p>为角色授予权限</p>
     *
     * @param roleCode 角色编码
     * @param param 授权请求参数，包含权限编码列表
     * @return 新创建的权限策略数量
     * @throws BusinessException 角色不存在或权限编码不存在时抛出
     */
    Integer grantPermissions(String roleCode, SysRoleGrantPermRTO param);

    /**
     * <p>撤销角色权限</p>
     *
     * @param roleCode 角色编码
     * @param permCodeList 权限编码列表
     * @return 删除的权限策略数量
     * @throws BusinessException 角色不存在时抛出
     */
    Integer revokePermissions(String roleCode, List<String> permCodeList);

}
