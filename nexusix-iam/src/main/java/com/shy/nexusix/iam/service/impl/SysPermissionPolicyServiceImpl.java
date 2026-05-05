package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.converter.SysPermissionConverter;
import com.shy.nexusix.iam.converter.SysPermissionPolicyConverter;
import com.shy.nexusix.iam.entity.SysPermission;
import com.shy.nexusix.iam.entity.SysPermissionPolicy;
import com.shy.nexusix.iam.entity.SysRole;
import com.shy.nexusix.iam.entity.SysUserRoleRel;
import com.shy.nexusix.iam.mapper.SysPermissionPolicyMapper;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.rto.SysRolePermissionAssignRTO;
import com.shy.nexusix.iam.service.ISysPermissionPolicyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.service.ISysPermissionService;
import com.shy.nexusix.iam.service.ISysRoleService;
import com.shy.nexusix.iam.service.ISysUserRoleRelService;
import com.shy.nexusix.iam.vo.SysPermissionPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyDetailVO;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限策略控制表 - 服务实现类
 * </p>
 * <p>
 * 实现四层权限模型（系统→租户→角色→用户）的策略管理、角色权限分配、
 * 用户权限聚合查询与权限校验等核心业务逻辑。
 * DENY策略优先级高于ALLOW，支持权限继承控制。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@Service
public class SysPermissionPolicyServiceImpl extends ServiceImpl<SysPermissionPolicyMapper, SysPermissionPolicy> implements ISysPermissionPolicyService {

    @Autowired
    private SysPermissionPolicyConverter sysPermissionPolicyConverter;

    @Autowired
    private SysPermissionConverter sysPermissionConverter;

    @Autowired
    private ISysPermissionService iSysPermissionService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private ISysUserRoleRelService iSysUserRoleRelService;

    /**
     * 查询权限策略列表
     * <p>
     * 按优先级降序排列，自动填充关联权限名称和标识
     * </p>
     */
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

    /** 分页查询权限策略 */
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

    /** 条件查询权限策略，支持按目标类型、目标ID、权限ID、动作筛选 */
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

    /** 查询权限策略详情，自动填充关联权限名称和标识 */
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

    /** 新增权限策略，校验目标角色和关联权限的有效性 */
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

    /** 修改权限策略，校验策略存在性和关联数据有效性 */
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

    /** 删除权限策略（逻辑删除） */
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

    /** 批量新增权限策略，单次上限100条 */
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

    /** 批量删除权限策略，单次上限100条，逻辑删除 */
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

    /**
     * 分配角色权限
     * <p>
     * 采用先清后写模式：先逻辑删除该角色的所有已有策略，再批量新增新策略。
     * 所有策略动作默认为允许(ALLOW)，优先级从100递增，默认开启继承。
     * </p>
     */
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

    /**
     * 查询用户权限标识列表
     * <p>
     * 聚合四层权限策略（系统→租户→角色→用户），DENY优先级高于ALLOW，
     * 最终返回该用户在指定租户下拥有的所有权限标识编码。
     * </p>
     */
    @Override
    public List<String> queryPermissionCodesByUserId(Long userId, Long tenantId) {

        Set<Long> roleIds = new HashSet<>();

        LambdaQueryWrapper<SysUserRoleRel> userRoleWrapper = new LambdaQueryWrapper<SysUserRoleRel>()
                .eq(SysUserRoleRel::getUserId, userId);
        if (tenantId != null) {
            userRoleWrapper.eq(SysUserRoleRel::getTenantId, tenantId);
        }
        List<SysUserRoleRel> userRoleRels = iSysUserRoleRelService.list(userRoleWrapper);
        for (SysUserRoleRel rel : userRoleRels) {
            roleIds.add(rel.getRoleId());
        }

        Set<Long> allowedPermissionIds = new HashSet<>();
        Set<Long> deniedPermissionIds = new HashSet<>();

        LambdaQueryWrapper<SysPermissionPolicy> systemWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getTargetType, GlobalEnum.TargetType.SYSTEM.getCode())
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .eq(SysPermissionPolicy::getAction, GlobalEnum.Action.ALLOW.getCode());
        List<SysPermissionPolicy> systemPolicies = this.list(systemWrapper);
        for (SysPermissionPolicy policy : systemPolicies) {
            allowedPermissionIds.add(policy.getPermissionId());
        }

        if (tenantId != null) {
            LambdaQueryWrapper<SysPermissionPolicy> tenantWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                    .eq(SysPermissionPolicy::getTargetType, GlobalEnum.TargetType.TENANT.getCode())
                    .eq(SysPermissionPolicy::getTargetId, tenantId)
                    .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            List<SysPermissionPolicy> tenantPolicies = this.list(tenantWrapper);
            for (SysPermissionPolicy policy : tenantPolicies) {
                if (GlobalEnum.Action.ALLOW.getCode().equals(policy.getAction())) {
                    allowedPermissionIds.add(policy.getPermissionId());
                } else if (GlobalEnum.Action.DENY.getCode().equals(policy.getAction())) {
                    deniedPermissionIds.add(policy.getPermissionId());
                }
            }
        }

        if (!roleIds.isEmpty()) {
            LambdaQueryWrapper<SysPermissionPolicy> roleWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                    .eq(SysPermissionPolicy::getTargetType, GlobalEnum.TargetType.ROLE.getCode())
                    .in(SysPermissionPolicy::getTargetId, roleIds)
                    .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .orderByDesc(SysPermissionPolicy::getPriority);
            List<SysPermissionPolicy> rolePolicies = this.list(roleWrapper);
            for (SysPermissionPolicy policy : rolePolicies) {
                if (GlobalEnum.Action.ALLOW.getCode().equals(policy.getAction())) {
                    allowedPermissionIds.add(policy.getPermissionId());
                } else if (GlobalEnum.Action.DENY.getCode().equals(policy.getAction())) {
                    deniedPermissionIds.add(policy.getPermissionId());
                }
            }
        }

        LambdaQueryWrapper<SysPermissionPolicy> userWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getTargetType, GlobalEnum.TargetType.USER.getCode())
                .eq(SysPermissionPolicy::getTargetId, userId)
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysPermissionPolicy::getPriority);
        List<SysPermissionPolicy> userPolicies = this.list(userWrapper);
        for (SysPermissionPolicy policy : userPolicies) {
            if (GlobalEnum.Action.ALLOW.getCode().equals(policy.getAction())) {
                allowedPermissionIds.add(policy.getPermissionId());
            } else if (GlobalEnum.Action.DENY.getCode().equals(policy.getAction())) {
                deniedPermissionIds.add(policy.getPermissionId());
            }
        }

        allowedPermissionIds.removeAll(deniedPermissionIds);

        if (allowedPermissionIds.isEmpty()) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<SysPermission> permWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, allowedPermissionIds)
                .eq(SysPermission::getStatus, GlobalEnum.Status.ENABLE.getCode())
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermission> permissions = iSysPermissionService.list(permWrapper);

        return permissions.stream()
                .map(SysPermission::getPermCode)
                .collect(Collectors.toList());
    }

    /**
     * 校验用户是否拥有指定权限
     * <p>
     * 支持前缀匹配：若用户拥有 system:user 权限，则 system:user:add 也视为通过
     * </p>
     */
    @Override
    public boolean checkPermission(Long userId, Long tenantId, String permCode) {

        if (userId == null || StringUtils.isBlank(permCode)) {
            return false;
        }

        List<String> permissionCodes = queryPermissionCodesByUserId(userId, tenantId);

        if (permissionCodes.contains(permCode)) {
            return true;
        }

        for (String code : permissionCodes) {
            if (permCode.startsWith(code)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 查询角色已分配的权限树形列表
     * <p>
     * 查询角色关联的ALLOW策略，获取权限ID列表后构建树形结构
     * </p>
     */
    @Override
    public List<SysPermissionTreeVO> queryPermissionsByRoleId(Long roleId) {

        if (roleId == null) {
            throw new BusinessException(400, "角色ID不能为空");
        }

        LambdaQueryWrapper<SysPermissionPolicy> policyWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getTargetType, GlobalEnum.TargetType.ROLE.getCode())
                .eq(SysPermissionPolicy::getTargetId, roleId)
                .eq(SysPermissionPolicy::getAction, GlobalEnum.Action.ALLOW.getCode())
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermissionPolicy> policies = this.list(policyWrapper);

        if (policies.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> permissionIds = policies.stream()
                .map(SysPermissionPolicy::getPermissionId)
                .collect(Collectors.toSet());

        LambdaQueryWrapper<SysPermission> permWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, permissionIds)
                .eq(SysPermission::getStatus, GlobalEnum.Status.ENABLE.getCode())
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByAsc(SysPermission::getPermType)
                .orderByAsc(SysPermission::getId);
        List<SysPermission> permissions = iSysPermissionService.list(permWrapper);

        List<SysPermissionTreeVO> treeVOList = sysPermissionConverter.toTreeVOList(permissions);

        return buildPermissionTree(treeVOList);
    }

    /**
     * 构建权限树形结构
     * <p>
     * 根据parentId将平铺列表组装为父子层级结构
     * </p>
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

    /**
     * 校验策略目标有效性
     * <p>
     * 当前仅校验角色类型目标是否存在，租户/用户/系统类型暂不校验
     * </p>
     */
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

    /**
     * 填充权限名称和标识
     * <p>
     * 根据策略中的permissionId批量查询权限信息，填充到VO的permName和permCode字段
     * </p>
     */
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
