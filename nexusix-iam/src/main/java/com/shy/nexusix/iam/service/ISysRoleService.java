package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleQueryRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;

import java.util.List;

/**
 * <p>
 * 角色表 - 服务接口
 * </p>
 * <p>
 * 提供角色的CRUD、批量操作、状态切换、用户分配等业务能力。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 查询角色列表
     *
     * @return 角色公共视图对象列表
     */
    List<SysRoleCommonVO> queryRoleList();

    /**
     * 分页查询角色
     *
     * @param page 分页参数
     * @return 分页后的角色公共视图对象
     */
    IPage<SysRoleCommonVO> queryRolePage(PageCommonRTO page);

    /**
     * 条件查询角色
     * <p>
     * 支持按角色名称、编码模糊匹配，按层级、租户、状态、创建人、创建时间筛选
     * </p>
     *
     * @param queryParam 查询条件
     * @return 分页后的角色公共视图对象
     */
    IPage<SysRoleCommonVO> queryRole(SysRoleQueryRTO queryParam);

    /**
     * 查询角色详情
     *
     * @param id 角色ID
     * @return 角色详情视图对象
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色不存在时抛出
     */
    SysRoleDetailVO queryRoleDetail(String id);

    /**
     * 新增角色
     * <p>
     * 会校验角色编码的唯一性，tenantId为空时默认设为0（系统级），
     * 自动填充tenantName、createBy、createByName等冗余字段
     * </p>
     *
     * @param addParam 新增参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色编码已存在时抛出
     */
    Integer addRole(SysRoleAddRTO addParam);

    /**
     * 修改角色
     * <p>
     * 会校验角色编码的唯一性（排除自身），自动更新tenantName、updateBy、updateByName等冗余字段
     * </p>
     *
     * @param updateParam 修改参数
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色不存在或编码已被占用时抛出
     */
    Integer updateRole(SysRoleUpdateRTO updateParam);

    /**
     * 更新角色状态
     *
     * @param id     角色ID
     * @param status 状态值（1-启用 0-禁用）
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色不存在或状态未变更时抛出
     */
    Integer updateRoleStatus(String id, String status);

    /**
     * 删除角色
     * <p>
     * 逻辑删除，删除前会校验是否存在用户关联
     * </p>
     *
     * @param id 角色ID
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当角色不存在或存在用户关联时抛出
     */
    Integer deleteRole(String id);

    /**
     * 批量新增角色
     * <p>
     * 单次上限100条，会校验角色编码的唯一性（含批量内部去重）
     * </p>
     *
     * @param addParamList 新增参数列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当存在重复编码或超过上限时抛出
     */
    Integer batchAddRole(List<SysRoleAddRTO> addParamList);

    /**
     * 批量修改角色
     * <p>
     * 单次上限100条，会校验ID存在性和编码唯一性
     * </p>
     *
     * @param updateParamList 修改参数列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当部分角色不存在或编码冲突时抛出
     */
    Integer batchUpdateRole(List<SysRoleUpdateRTO> updateParamList);

    /**
     * 批量删除角色
     * <p>
     * 单次上限100条，逻辑删除，删除前校验是否存在用户关联
     * </p>
     *
     * @param ids 角色ID列表
     * @return 影响行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当存在用户关联或超过上限时抛出
     */
    Integer batchDeleteRole(List<String> ids);

}
