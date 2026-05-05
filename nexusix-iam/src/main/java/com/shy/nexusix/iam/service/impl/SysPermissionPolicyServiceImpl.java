package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.converter.SysPermissionPolicyConverter;
import com.shy.nexusix.iam.entity.SysPermission;
import com.shy.nexusix.iam.entity.SysPermissionPolicy;
import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.mapper.SysPermissionPolicyMapper;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.rto.SysRolePermissionAssignRTO;
import com.shy.nexusix.iam.service.ISysPermissionPolicyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.service.ISysPermissionService;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.shy.nexusix.iam.vo.SysPermissionPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyDetailVO;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysPermissionPolicyServiceImpl extends ServiceImpl<SysPermissionPolicyMapper, SysPermissionPolicy> implements ISysPermissionPolicyService {

    @Autowired
    private SysPermissionPolicyConverter sysPermissionPolicyConverter;

    @Autowired
    private ISysPermissionService iSysPermissionService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Override
    public List<SysPermissionPolicyCommonVO> queryPolicyList() {

        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysPermissionPolicy::getPriority)
                .orderByDesc(SysPermissionPolicy::getCreateTime);

        List<SysPermissionPolicy> policyList = this.list(wrapper);

        List<SysPermissionPolicyCommonVO> voList = sysPermissionPolicyConverter.toVoList(policyList);

        fillPermissionInfo(voList, policyList);

        return voList;
    }

    @Override
    public IPage<SysPermissionPolicyCommonVO> queryPolicyPage(PageCommonRTO page) {

        Page<SysPermissionPolicy> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysPermissionPolicy::getPriority)
                .orderByDesc(SysPermissionPolicy::getCreateTime);

        IPage<SysPermissionPolicy> policyPage = this.page(pageParam, wrapper);

        IPage<SysPermissionPolicyCommonVO> voPage = sysPermissionPolicyConverter.toVOPage(policyPage);

        fillPermissionInfo(voPage.getRecords(), policyPage.getRecords());

        return voPage;
    }

    @Override
    public IPage<SysPermissionPolicyCommonVO> queryPolicy(SysPermissionPolicyQueryRTO queryParam) {

        Page<SysPermissionPolicy> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<>();

        if (queryParam.getTargetType() != null) {
            wrapper.eq(SysPermissionPolicy::getTargetType, queryParam.getTargetType());
        }

        if (queryParam.getTargetId() != null) {
            wrapper.eq(SysPermissionPolicy::getTargetId, queryParam.getTargetId());
        }

        if (queryParam.getPermissionId() != null) {
            wrapper.eq(SysPermissionPolicy::getPermissionId, queryParam.getPermissionId());
        }

        if (queryParam.getAction() != null) {
            wrapper.eq(SysPermissionPolicy::getAction, queryParam.getAction());
        }

        wrapper.eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        wrapper.orderByDesc(SysPermissionPolicy::getPriority).orderByDesc(SysPermissionPolicy::getCreateTime);

        IPage<SysPermissionPolicy> policyPage = this.page(pageParam, wrapper);

        IPage<SysPermissionPolicyCommonVO> voPage = sysPermissionPolicyConverter.toVOPage(policyPage);

        fillPermissionInfo(voPage.getRecords(), policyPage.getRecords());

        return voPage;
    }

    @Override
    public SysPermissionPolicyDetailVO queryPolicyDetail(String id) {

        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "策略ID不能为空");
        }

        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getId, Long.parseLong(id))
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        SysPermissionPolicy policy = this.getOne(wrapper);

        if (policy == null) {
            throw new BusinessException(404, "权限策略不存在");
        }

        SysPermissionPolicyDetailVO detailVO = sysPermissionPolicyConverter.toDetailVO(policy);

        SysPermission permission = iSysPermissionService.getById(policy.getPermissionId());
        if (permission != null) {
            detailVO.setPermName(permission.getPermName());
            detailVO.setPermCode(permission.getPermCode());
        }

        return detailVO;
    }

    @Override
    public Integer addPolicy(SysPermissionPolicyAddRTO addParam) {

        validatePolicyTarget(addParam.getTargetType(), addParam.getTargetId());

        LambdaQueryWrapper<SysPermission> permWrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, addParam.getPermissionId())
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long permCount = iSysPermissionService.count(permWrapper);
        if (permCount == 0) {
            throw new BusinessException(400, "关联权限不存在");
        }

        SysPermissionPolicy entity = sysPermissionPolicyConverter.toEntityAdd(addParam);

        boolean result = this.save(entity);

        if (!result) {
            throw new BusinessException(500, "新增权限策略失败");
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updatePolicy(SysPermissionPolicyUpdateRTO updateParam) {

        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getId, updateParam.getId())
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermissionPolicy existPolicy = this.getOne(wrapper);

        if (existPolicy == null) {
            throw new BusinessException(404, "权限策略不存在");
        }

        validatePolicyTarget(updateParam.getTargetType(), updateParam.getTargetId());

        LambdaQueryWrapper<SysPermission> permWrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, updateParam.getPermissionId())
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long permCount = iSysPermissionService.count(permWrapper);
        if (permCount == 0) {
            throw new BusinessException(400, "关联权限不存在");
        }

        SysPermissionPolicy entity = sysPermissionPolicyConverter.toEntityUpdate(updateParam);

        boolean result = this.updateById(entity);

        if (!result) {
            throw new BusinessException(500, "更新权限策略失败");
        }

        return 1;
    }

    @Override
    public Integer deletePolicy(String id) {

        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getId, id)
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermissionPolicy existPolicy = this.getOne(wrapper);

        if (existPolicy == null) {
            throw new BusinessException(400, "权限策略不存在");
        }

        SysPermissionPolicy policy = new SysPermissionPolicy();
        policy.setId(Long.parseLong(id));
        policy.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());

        boolean result = this.updateById(policy);

        if (!result) {
            throw new BusinessException(500, "删除权限策略失败");
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddPolicy(List<SysPermissionPolicyAddRTO> addParamList) {

        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }

        for (SysPermissionPolicyAddRTO param : addParamList) {
            validatePolicyTarget(param.getTargetType(), param.getTargetId());
        }

        List<SysPermissionPolicy> entityList = sysPermissionPolicyConverter.toEntityListAdd(addParamList);

        boolean batch = this.saveBatch(entityList);

        if (!batch) {
            throw new BusinessException(500, "批量新增权限策略失败");
        }

        return addParamList.size();
    }

    @Override
    public Integer batchDeletePolicy(List<String> ids) {

        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "策略ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        LambdaQueryWrapper<SysPermissionPolicy> existWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .in(SysPermissionPolicy::getId, idSet)
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermissionPolicy> existPolicies = this.list(existWrapper);

        if (existPolicies.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的权限策略");
        }

        List<SysPermissionPolicy> deleteList = new ArrayList<>();
        for (SysPermissionPolicy policy : existPolicies) {
            SysPermissionPolicy deletePolicy = new SysPermissionPolicy();
            deletePolicy.setId(policy.getId());
            deletePolicy.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
            deleteList.add(deletePolicy);
        }

        boolean batch = this.updateBatchById(deleteList);

        if (!batch) {
            throw new BusinessException(500, "批量删除权限策略失败");
        }

        return ids.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignRolePermission(SysRolePermissionAssignRTO assignParam) {

        LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, assignParam.getRoleId())
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long roleCount = iSysRoleService.count(roleWrapper);
        if (roleCount == 0) {
            throw new BusinessException(400, "角色不存在");
        }

        LambdaQueryWrapper<SysPermissionPolicy> deleteWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getTargetType, GlobalEnum.TargetType.ROLE.getCode())
                .eq(SysPermissionPolicy::getTargetId, assignParam.getRoleId());
        List<SysPermissionPolicy> existPolicies = this.list(deleteWrapper);

        if (!existPolicies.isEmpty()) {
            List<SysPermissionPolicy> deleteList = new ArrayList<>();
            for (SysPermissionPolicy policy : existPolicies) {
                SysPermissionPolicy deletePolicy = new SysPermissionPolicy();
                deletePolicy.setId(policy.getId());
                deletePolicy.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());
                deleteList.add(deletePolicy);
            }
            this.updateBatchById(deleteList);
        }

        if (assignParam.getPermissionIds().isEmpty()) {
            return 0;
        }

        List<SysPermissionPolicy> newPolicies = new ArrayList<>();
        int priority = 100;
        for (Long permissionId : assignParam.getPermissionIds()) {
            SysPermissionPolicy policy = new SysPermissionPolicy();
            policy.setTargetType(GlobalEnum.TargetType.ROLE.getCode());
            policy.setTargetId(assignParam.getRoleId());
            policy.setPermissionId(permissionId);
            policy.setAction(GlobalEnum.Action.ALLOW.getCode());
            policy.setPriority(priority++);
            policy.setInheritanceEnabled(true);
            newPolicies.add(policy);
        }

        boolean batch = this.saveBatch(newPolicies);

        if (!batch) {
            throw new BusinessException(500, "分配角色权限失败");
        }

        return newPolicies.size();
    }

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

    private void validatePolicyTarget(GlobalEnum.TargetType targetType, Long targetId) {

        if (targetType == null || targetId == null) {
            return;
        }

        switch (targetType) {
            case ROLE:
                LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getId, targetId)
                        .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
                long roleCount = iSysRoleService.count(roleWrapper);
                if (roleCount == 0) {
                    throw new BusinessException(400, "目标角色不存在");
                }
                break;
            case TENANT:
            case USER:
            case SYSTEM:
            default:
                break;
        }
    }

    private void fillPermissionInfo(List<SysPermissionPolicyCommonVO> voList, List<SysPermissionPolicy> policyList) {

        if (voList == null || voList.isEmpty()) {
            return;
        }

        Set<Long> permissionIds = policyList.stream()
                .map(SysPermissionPolicy::getPermissionId)
                .collect(Collectors.toSet());

        if (permissionIds.isEmpty()) {
            return;
        }

        List<SysPermission> permissions = iSysPermissionService.listByIds(permissionIds);
        Map<Long, SysPermission> permMap = permissions.stream()
                .collect(Collectors.toMap(SysPermission::getId, p -> p));

        for (int i = 0; i < voList.size(); i++) {
            SysPermissionPolicyCommonVO vo = voList.get(i);
            SysPermissionPolicy policy = policyList.get(i);
            SysPermission permission = permMap.get(policy.getPermissionId());
            if (permission != null) {
                vo.setPermName(permission.getPermName());
                vo.setPermCode(permission.getPermCode());
            }
        }
    }

}
