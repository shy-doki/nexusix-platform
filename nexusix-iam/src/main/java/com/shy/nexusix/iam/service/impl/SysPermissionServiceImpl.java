package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.converter.SysPermissionConverter;
import com.shy.nexusix.iam.entity.SysPermission;
import com.shy.nexusix.iam.mapper.SysPermissionMapper;
import com.shy.nexusix.iam.rto.SysPermissionAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionUpdateRTO;
import com.shy.nexusix.iam.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.vo.SysPermissionCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionDetailVO;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限资源表 - 服务实现类
 * </p>
 * <p>
 * 提供权限资源的CRUD、树形结构构建、批量操作、状态切换等业务逻辑实现。
 * 所有删除操作均为逻辑删除，新增/修改操作会校验权限标识唯一性。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private SysPermissionConverter sysPermissionConverter;

    /**
     * 查询权限列表
     * <p>
     * 返回所有未删除的权限，按权限类型和ID升序排列
     * </p>
     */
    @Override
    public List<SysPermissionCommonVO> queryPermissionList() {

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByAsc(SysPermission::getPermType)
                .orderByAsc(SysPermission::getId);

        List<SysPermission> permissionList = this.list(wrapper);

        return sysPermissionConverter.toVoList(permissionList);
    }

    /**
     * 分页查询权限
     */
    @Override
    public IPage<SysPermissionCommonVO> queryPermissionPage(PageCommonRTO page) {

        Page<SysPermission> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByAsc(SysPermission::getPermType)
                .orderByAsc(SysPermission::getId);

        IPage<SysPermission> permissionPage = this.page(pageParam, wrapper);

        return sysPermissionConverter.toVOPage(permissionPage);
    }

    /**
     * 查询权限树形列表
     * <p>
     * 查询所有未删除权限后，按parentId构建父子层级关系
     * </p>
     */
    @Override
    public List<SysPermissionTreeVO> queryPermissionTreeList() {

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByAsc(SysPermission::getPermType)
                .orderByAsc(SysPermission::getId);

        List<SysPermission> permissionList = this.list(wrapper);

        List<SysPermissionTreeVO> treeVOList = sysPermissionConverter.toTreeVOList(permissionList);

        return buildPermissionTree(treeVOList);
    }

    /**
     * 条件查询权限
     * <p>
     * 支持按权限名称、标识模糊匹配，按类型和状态精确筛选
     * </p>
     */
    @Override
    public IPage<SysPermissionCommonVO> queryPermission(SysPermissionQueryRTO queryParam) {

        Page<SysPermission> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.isNotBlank(queryParam.getPermName()),
                SysPermission::getPermName, queryParam.getPermName());

        wrapper.like(StringUtils.isNotBlank(queryParam.getPermCode()),
                SysPermission::getPermCode, queryParam.getPermCode());

        if (queryParam.getPermType() != null) {
            wrapper.eq(SysPermission::getPermType, queryParam.getPermType());
        }

        if (queryParam.getStatus() != null) {
            wrapper.eq(SysPermission::getStatus, queryParam.getStatus());
        }

        wrapper.eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        wrapper.orderByAsc(SysPermission::getPermType).orderByAsc(SysPermission::getId);

        IPage<SysPermission> permissionPage = this.page(pageParam, wrapper);

        return sysPermissionConverter.toVOPage(permissionPage);
    }

    /**
     * 查询权限详情
     */
    @Override
    public SysPermissionDetailVO queryPermissionDetail(String id) {

        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "权限ID不能为空");
        }

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, Long.parseLong(id))
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        SysPermission permission = this.getOne(wrapper);

        if (permission == null) {
            throw new BusinessException(404, "权限不存在");
        }

        return sysPermissionConverter.toDetailVO(permission);
    }

    /**
     * 新增权限
     * <p>
     * 校验权限标识唯一性，parentId为空时默认设为0（顶级权限）
     * </p>
     */
    @Override
    public Integer addPermission(SysPermissionAddRTO addParam) {

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getPermCode, addParam.getPermCode());

        long count = this.count(wrapper);

        if (count > 0) {
            throw new BusinessException(400, "权限标识已存在");
        }

        SysPermission entity = sysPermissionConverter.toEntityAdd(addParam);

        if (entity.getParentId() == null) {
            entity.setParentId(0L);
        }

        boolean result = this.save(entity);

        if (!result) {
            throw new BusinessException(500, "新增权限失败");
        }

        return 1;
    }

    /**
     * 修改权限
     * <p>
     * 校验权限存在性和标识唯一性（排除自身），parentId为空时默认设为0
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updatePermission(SysPermissionUpdateRTO updateParam) {

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, updateParam.getId())
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermission existPermission = this.getOne(wrapper);

        if (existPermission == null) {
            throw new BusinessException(404, "权限不存在");
        }

        if (!existPermission.getPermCode().equals(updateParam.getPermCode())) {
            LambdaQueryWrapper<SysPermission> codeWrapper = new LambdaQueryWrapper<SysPermission>()
                    .eq(SysPermission::getPermCode, updateParam.getPermCode())
                    .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            long codeCount = this.count(codeWrapper);
            if (codeCount > 0) {
                throw new BusinessException(400, "权限标识已被其他权限使用");
            }
        }

        SysPermission entity = sysPermissionConverter.toEntityUpdate(updateParam);

        if (entity.getParentId() == null) {
            entity.setParentId(0L);
        }

        boolean result = this.updateById(entity);

        if (!result) {
            throw new BusinessException(500, "更新权限失败");
        }

        return 1;
    }

    /**
     * 更新权限状态
     * <p>
     * 支持数字编码(1/0)和枚举名称(ENABLE/DISABLE)两种格式
     * </p>
     */
    @Override
    public Integer updatePermissionStatus(String id, String status) {

        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "权限ID不能为空");
        }

        if (StringUtils.isBlank(status)) {
            throw new BusinessException(400, "状态不能为空");
        }

        GlobalEnum.Status permStatus;
        try {
            int statusCode = Integer.parseInt(status);
            permStatus = GlobalEnum.Status.getByCode(statusCode);
        } catch (NumberFormatException e) {
            permStatus = GlobalEnum.Status.valueOf(status);
        }
        if (permStatus == null) {
            throw new BusinessException(400, "状态值不合法，仅支持：1-启用、0-禁用");
        }

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, Long.parseLong(id))
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermission existPermission = this.getOne(wrapper);

        if (existPermission == null) {
            throw new BusinessException(400, "权限不存在");
        }

        if (permStatus.getCode().equals(existPermission.getStatus())) {
            throw new BusinessException(400, "权限状态未变更");
        }

        SysPermission updatePermission = new SysPermission();
        updatePermission.setId(Long.parseLong(id));
        updatePermission.setStatus(permStatus.getCode());

        boolean result = this.updateById(updatePermission);

        if (!result) {
            throw new BusinessException(500, "更新权限状态失败");
        }

        return 1;
    }

    /**
     * 删除权限
     * <p>
     * 逻辑删除，删除前校验是否存在子权限
     * </p>
     */
    @Override
    public Integer deletePermission(String id) {

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, id)
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermission existPermission = this.getOne(wrapper);

        if (existPermission == null) {
            throw new BusinessException(400, "权限不存在");
        }

        LambdaQueryWrapper<SysPermission> childWrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getParentId, Long.parseLong(id))
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long childCount = this.count(childWrapper);
        if (childCount > 0) {
            throw new BusinessException(400, "该权限下存在子权限，请先删除子权限");
        }

        SysPermission permission = new SysPermission();
        permission.setId(Long.parseLong(id));
        permission.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());

        boolean result = this.updateById(permission);

        if (!result) {
            throw new BusinessException(500, "删除权限失败");
        }

        return 1;
    }

    /**
     * 批量新增权限
     * <p>
     * 单次上限100条，校验批量内部去重和数据库唯一性
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddPermission(List<SysPermissionAddRTO> addParamList) {

        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }

        Set<String> codeSet = new HashSet<>();
        for (SysPermissionAddRTO param : addParamList) {
            if (codeSet.contains(param.getPermCode())) {
                throw new BusinessException(400, "批量新增中存在重复的权限标识: " + param.getPermCode());
            }
            codeSet.add(param.getPermCode());
        }

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getPermCode, codeSet);
        long existCount = this.count(wrapper);

        if (existCount > 0) {
            throw new BusinessException(400, "部分权限标识已存在，请检查后重试");
        }

        List<SysPermission> entityList = sysPermissionConverter.toEntityListAdd(addParamList);
        for (SysPermission entity : entityList) {
            if (entity.getParentId() == null) {
                entity.setParentId(0L);
            }
        }

        boolean batch = this.saveBatch(entityList);

        if (!batch) {
            throw new BusinessException(500, "批量新增权限失败");
        }

        return addParamList.size();
    }

    /**
     * 批量修改权限
     * <p>
     * 单次上限100条，校验ID存在性和标识唯一性（含批量内部去重）
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdatePermission(List<SysPermissionUpdateRTO> updateParamList) {

        if (updateParamList.size() > 100) {
            throw new BusinessException(400, "单次批量修改数量不能超过100条");
        }

        Set<Long> idSet = new HashSet<>();
        Set<String> codeSet = new HashSet<>();
        for (SysPermissionUpdateRTO item : updateParamList) {
            if (item.getId() == null) {
                throw new BusinessException(400, "批量修改中存在ID为空的记录");
            }
            if (idSet.contains(item.getId())) {
                throw new BusinessException(400, "批量修改中存在重复的权限ID: " + item.getId());
            }
            if (codeSet.contains(item.getPermCode())) {
                throw new BusinessException(400, "批量修改中存在重复的权限标识: " + item.getPermCode());
            }
            idSet.add(item.getId());
            codeSet.add(item.getPermCode());
        }

        LambdaQueryWrapper<SysPermission> existWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, idSet)
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermission> existPermissions = this.list(existWrapper);

        if (existPermissions.size() != idSet.size()) {
            throw new BusinessException(400, "部分权限不存在，请检查后重试");
        }

        List<SysPermission> entityList = new ArrayList<>();
        for (SysPermissionUpdateRTO rto : updateParamList) {
            SysPermission entity = sysPermissionConverter.toEntityUpdate(rto);
            if (entity.getParentId() == null) {
                entity.setParentId(0L);
            }
            entityList.add(entity);
        }

        boolean batch = this.updateBatchById(entityList);

        if (!batch) {
            throw new BusinessException(500, "批量更新权限失败");
        }

        return updateParamList.size();
    }

    /**
     * 批量删除权限
     * <p>
     * 单次上限100条，逻辑删除，删除前校验是否存在子权限
     * </p>
     */
    @Override
    public Integer batchDeletePermission(List<String> ids) {

        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "权限ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        LambdaQueryWrapper<SysPermission> existWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, idSet)
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermission> existPermissions = this.list(existWrapper);

        if (existPermissions.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的权限");
        }

        LambdaQueryWrapper<SysPermission> childWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getParentId, idSet)
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long childCount = this.count(childWrapper);
        if (childCount > 0) {
            throw new BusinessException(400, "部分权限下存在子权限，请先删除子权限");
        }

        List<SysPermission> deleteList = new ArrayList<>();
        for (SysPermission perm : existPermissions) {
            SysPermission deletePerm = new SysPermission();
            deletePerm.setId(perm.getId());
            deletePerm.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            deleteList.add(deletePerm);
        }

        boolean batch = this.updateBatchById(deleteList);

        if (!batch) {
            throw new BusinessException(500, "批量删除权限失败");
        }

        return ids.size();
    }

    /**
     * 构建权限树形结构
     * <p>
     * 根据parentId将平铺列表组装为父子层级结构，
     * parentId为0或null的节点作为根节点
     * </p>
     *
     * @param treeVOList 平铺的权限树形VO列表
     * @return 根节点列表（含子节点）
     */
    private List<SysPermissionTreeVO> buildPermissionTree(List<SysPermissionTreeVO> treeVOList) {

        Map<Long, List<SysPermissionTreeVO>> parentMap = treeVOList.stream()
                .filter(vo -> vo.getParentId() != null && vo.getParentId() != 0L)
                .collect(Collectors.groupingBy(SysPermissionTreeVO::getParentId));

        for (SysPermissionTreeVO vo : treeVOList) {
            List<SysPermissionTreeVO> children = parentMap.get(vo.getId());
            vo.setChildPermission(children);
        }

        return treeVOList.stream()
                .filter(vo -> vo.getParentId() == null || vo.getParentId() == 0L)
                .collect(Collectors.toList());
    }

}
