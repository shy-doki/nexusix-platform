package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.converter.SysRoleConverter;
import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.entity.SysUserRoleRel;
import com.shy.nexusix.iam.mapper.SysRoleMapper;
import com.shy.nexusix.iam.rto.SysRoleAddRTO;
import com.shy.nexusix.iam.rto.SysRoleQueryRTO;
import com.shy.nexusix.iam.rto.SysRoleUpdateRTO;
import com.shy.nexusix.iam.rto.SysRoleUserAssignRTO;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.service.ISysUserRoleRelService;
import com.shy.nexusix.iam.vo.SysRoleCommonVO;
import com.shy.nexusix.iam.vo.SysRoleDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 - 服务实现类
 * </p>
 * <p>
 * 提供角色的CRUD、批量操作、状态切换、用户分配等业务逻辑实现。
 * 所有删除操作均为逻辑删除，新增/修改操作会校验角色编码唯一性。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleConverter sysRoleConverter;

    @Autowired
    private ISysUserRoleRelService iSysUserRoleRelService;

    /**
     * 查询角色列表
     * <p>
     * 返回所有未删除的角色，按排序和ID升序排列
     * </p>
     */
    @Override
    public List<SysRoleCommonVO> queryRoleList() {

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByAsc(SysRole::getSortOrder)
                .orderByAsc(SysRole::getId);

        List<SysRole> roleList = this.list(wrapper);

        return sysRoleConverter.toVoList(roleList);
    }

    /** 分页查询角色 */
    @Override
    public IPage<SysRoleCommonVO> queryRolePage(PageCommonRTO page) {

        Page<SysRole> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByAsc(SysRole::getSortOrder)
                .orderByAsc(SysRole::getId);

        IPage<SysRole> rolePage = this.page(pageParam, wrapper);

        return sysRoleConverter.toVOPage(rolePage);
    }

    /**
     * 条件查询角色
     * <p>
     * 支持按角色名称、编码模糊匹配，按层级、租户、状态精确筛选
     * </p>
     */
    @Override
    public IPage<SysRoleCommonVO> queryRole(SysRoleQueryRTO queryParam) {

        Page<SysRole> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.isNotBlank(queryParam.getRoleName()),
                SysRole::getRoleName, queryParam.getRoleName());

        wrapper.like(StringUtils.isNotBlank(queryParam.getRoleCode()),
                SysRole::getRoleCode, queryParam.getRoleCode());

        if (queryParam.getRoleLevel() != null) {
            wrapper.eq(SysRole::getRoleLevel, queryParam.getRoleLevel());
        }

        if (queryParam.getTenantId() != null) {
            wrapper.eq(SysRole::getTenantId, queryParam.getTenantId());
        }

        if (queryParam.getStatus() != null) {
            wrapper.eq(SysRole::getStatus, queryParam.getStatus());
        }

        wrapper.eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        wrapper.orderByAsc(SysRole::getSortOrder).orderByAsc(SysRole::getId);

        IPage<SysRole> rolePage = this.page(pageParam, wrapper);

        return sysRoleConverter.toVOPage(rolePage);
    }

    /** 查询角色详情 */
    @Override
    public SysRoleDetailVO queryRoleDetail(String id) {

        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "角色ID不能为空");
        }

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, Long.parseLong(id))
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        SysRole role = this.getOne(wrapper);

        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }

        return sysRoleConverter.toDetailVO(role);
    }

    /**
     * 新增角色
     * <p>
     * 校验角色编码唯一性，tenantId为空时默认设为0（系统级）
     * </p>
     */
    @Override
    public Integer addRole(SysRoleAddRTO addParam) {

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, addParam.getRoleCode());

        long count = this.count(wrapper);

        if (count > 0) {
            throw new BusinessException(400, "角色编码已存在");
        }

        SysRole entity = sysRoleConverter.toEntityAdd(addParam);

        if (entity.getTenantId() == null) {
            entity.setTenantId(0L);
        }

        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }

        boolean result = this.save(entity);

        if (!result) {
            throw new BusinessException(500, "新增角色失败");
        }

        return 1;
    }

    /**
     * 修改角色
     * <p>
     * 校验角色存在性和编码唯一性（排除自身）
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateRole(SysRoleUpdateRTO updateParam) {

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, updateParam.getId())
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysRole existRole = this.getOne(wrapper);

        if (existRole == null) {
            throw new BusinessException(404, "角色不存在");
        }

        if (!existRole.getRoleCode().equals(updateParam.getRoleCode())) {
            LambdaQueryWrapper<SysRole> codeWrapper = new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getRoleCode, updateParam.getRoleCode())
                    .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long codeCount = this.count(codeWrapper);
            if (codeCount > 0) {
                throw new BusinessException(400, "角色编码已被其他角色使用");
            }
        }

        SysRole entity = sysRoleConverter.toEntityUpdate(updateParam);

        if (entity.getTenantId() == null) {
            entity.setTenantId(0L);
        }

        boolean result = this.updateById(entity);

        if (!result) {
            throw new BusinessException(500, "更新角色失败");
        }

        return 1;
    }

    /**
     * 更新角色状态
     * <p>
     * 支持数字编码(1/0)和枚举名称(ENABLE/DISABLE)两种格式
     * </p>
     */
    @Override
    public Integer updateRoleStatus(String id, String status) {

        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "角色ID不能为空");
        }

        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        GlobalEnum.Status roleStatus;
        try {
            int statusCode = Integer.parseInt(status);
            roleStatus = GlobalEnum.Status.getByCode(statusCode);
        } catch (NumberFormatException e) {
            roleStatus = GlobalEnum.Status.valueOf(status);
        }
        if (roleStatus == null) {
            throw new BusinessException(400, "状态值不合法，仅支持：1-启用、0-禁用");
        }

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, Long.parseLong(id))
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysRole existRole = this.getOne(wrapper);

        if (existRole == null) {
            throw new BusinessException(400, "角色不存在");
        }

        if (roleStatus.getCode().equals(existRole.getStatus())) {
            throw new BusinessException(400, "角色状态未变更");
        }

        SysRole updateRole = new SysRole();
        updateRole.setId(Long.parseLong(id));
        updateRole.setStatus(roleStatus.getCode());

        boolean result = this.updateById(updateRole);

        if (!result) {
            throw new BusinessException(500, "更新角色状态失败");
        }

        return 1;
    }

    /**
     * 删除角色
     * <p>
     * 逻辑删除，删除前校验是否存在用户关联
     * </p>
     */
    @Override
    public Integer deleteRole(String id) {

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, id)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysRole existRole = this.getOne(wrapper);

        if (existRole == null) {
            throw new BusinessException(400, "角色不存在");
        }

        LambdaQueryWrapper<SysUserRoleRel> userRoleWrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .eq(SysUserRoleRel::getRoleId, Long.parseLong(id));
        long userRelCount = iSysUserRoleRelService.count(userRoleWrapper);
        if (userRelCount > 0) {
            throw new BusinessException(400, "该角色下存在用户关联，请先解除关联");
        }

        SysRole role = new SysRole();
        role.setId(Long.parseLong(id));
        role.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());

        boolean result = this.updateById(role);

        if (!result) {
            throw new BusinessException(500, "删除角色失败");
        }

        return 1;
    }

    /**
     * 批量新增角色
     * <p>
     * 单次上限100条，校验批量内部去重和数据库唯一性
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddRole(List<SysRoleAddRTO> addParamList) {

        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }

        Set<String> codeSet = new HashSet<>();
        for (SysRoleAddRTO param : addParamList) {
            if (codeSet.contains(param.getRoleCode())) {
                throw new BusinessException(400, "批量新增中存在重复的角色编码: " + param.getRoleCode());
            }
            codeSet.add(param.getRoleCode());
        }

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getRoleCode, codeSet);
        long existCount = this.count(wrapper);

        if (existCount > 0) {
            throw new BusinessException(400, "部分角色编码已存在，请检查后重试");
        }

        List<SysRole> entityList = sysRoleConverter.toEntityListAdd(addParamList);
        for (SysRole entity : entityList) {
            if (entity.getTenantId() == null) {
                entity.setTenantId(0L);
            }
            if (entity.getSortOrder() == null) {
                entity.setSortOrder(0);
            }
        }

        boolean batch = this.saveBatch(entityList);

        if (!batch) {
            throw new BusinessException(500, "批量新增角色失败");
        }

        return addParamList.size();
    }

    /**
     * 批量修改角色
     * <p>
     * 单次上限100条，校验ID存在性和编码唯一性（含批量内部去重）
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateRole(List<SysRoleUpdateRTO> updateParamList) {

        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }

        Set<Long> idSet = new HashSet<>();
        Set<String> codeSet = new HashSet<>();
        for (SysRoleUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的角色ID: " + item.getId());
            }
            if (codeSet.contains(item.getRoleCode())) {
                throw new BusinessException(400, "批量修改中存在重复的角色编码: " + item.getRoleCode());
            }
            idSet.add(item.getId());
            codeSet.add(item.getRoleCode());
        }

        LambdaQueryWrapper<SysRole> existWrapper = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getId, idSet)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysRole> existRoles = this.list(existWrapper);

        if (existRoles.size() != idSet.size()) {
            throw new BusinessException(400, "部分角色不存在，请检查后重试");
        }

        List<SysRole> entityList = new ArrayList<>();
        for (SysRoleUpdateRTO rto : updateParamList) {
            SysRole entity = sysRoleConverter.toEntityUpdate(rto);
            if (entity.getTenantId() == null) {
                entity.setTenantId(0L);
            }
            entityList.add(entity);
        }

        boolean batch = this.updateBatchById(entityList);

        if (!batch) {
            throw new BusinessException(500, "批量更新角色失败");
        }

        return updateParamList.size();
    }

    /**
     * 批量删除角色
     * <p>
     * 单次上限100条，逻辑删除，删除前校验是否存在用户关联
     * </p>
     */
    @Override
    public Integer batchDeleteRole(List<String> ids) {

        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "角色ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        LambdaQueryWrapper<SysRole> existWrapper = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getId, idSet)
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysRole> existRoles = this.list(existWrapper);

        if (existRoles.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的角色");
        }

        LambdaQueryWrapper<SysUserRoleRel> userRoleWrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .in(SysUserRoleRel::getRoleId, idSet);
        long userRelCount = iSysUserRoleRelService.count(userRoleWrapper);
        if (userRelCount > 0) {
            throw new BusinessException(400, "部分角色下存在用户关联，请先解除关联");
        }

        List<SysRole> deleteList = new ArrayList<>();
        for (SysRole role : existRoles) {
            SysRole deleteRole = new SysRole();
            deleteRole.setId(role.getId());
            deleteRole.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            deleteList.add(deleteRole);
        }

        boolean batch = this.updateBatchById(deleteList);

        if (!batch) {
            throw new BusinessException(500, "批量删除角色失败");
        }

        return ids.size();
    }

    /**
     * 分配角色用户
     * <p>
     * 采用先清后写模式：先删除该角色在指定租户下的所有用户关联，再批量新增新关联。
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignRoleUsers(SysRoleUserAssignRTO assignParam) {

        LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, assignParam.getRoleId())
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long roleCount = this.count(roleWrapper);
        if (roleCount == 0) {
            throw new BusinessException(400, "角色不存在");
        }

        LambdaQueryWrapper<SysUserRoleRel> deleteWrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .eq(SysUserRoleRel::getRoleId, assignParam.getRoleId())
                .eq(SysUserRoleRel::getTenantId, assignParam.getTenantId());
        iSysUserRoleRelService.remove(deleteWrapper);

        if (assignParam.getUserIds().isEmpty()) {
            return 0;
        }

        List<SysUserRoleRel> newRels = new ArrayList<>();
        for (Long userId : assignParam.getUserIds()) {
            SysUserRoleRel rel = new SysUserRoleRel();
            rel.setUserId(userId);
            rel.setRoleId(assignParam.getRoleId());
            rel.setTenantId(assignParam.getTenantId());
            newRels.add(rel);
        }

        boolean batch = iSysUserRoleRelService.saveBatch(newRels);

        if (!batch) {
            throw new BusinessException(500, "分配角色用户失败");
        }

        return newRels.size();
    }

}
