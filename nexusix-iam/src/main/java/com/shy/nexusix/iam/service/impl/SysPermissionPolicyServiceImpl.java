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
 * permName已冗余至策略表，新增/分配时自动填充；
 * createBy和createByName当前硬编码，后续接入登录上下文后替换。
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
     * 返回所有未删除的策略，按优先级降序、创建时间降序排列。
     * permName从Entity冗余字段直接映射，无需额外填充。
     * </p>
     */
    @Override
    public List<SysPermissionPolicyCommonVO> queryPolicyList() {

        /* 构建查询条件：仅查询未删除的策略，按优先级和创建时间降序 */
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysPermissionPolicy::getPriority)
                .orderByDesc(SysPermissionPolicy::getCreateTime);

        List<SysPermissionPolicy> policyList = this.list(wrapper);

        /* 转换为VO，permName从Entity冗余字段自动映射 */
        List<SysPermissionPolicyCommonVO> voList = sysPermissionPolicyConverter.toVoList(policyList);

        return voList;
    }

    /**
     * 分页查询权限策略
     * <p>
     * 返回分页后的策略列表，permName从Entity冗余字段直接映射
     * </p>
     */
    @Override
    public IPage<SysPermissionPolicyCommonVO> queryPolicyPage(PageCommonRTO page) {

        /* 构建分页参数 */
        Page<SysPermissionPolicy> pageParam = new Page<>(page.getPageNum(), page.getPageSize());

        /* 构建查询条件：仅查询未删除的策略，按优先级和创建时间降序 */
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByDesc(SysPermissionPolicy::getPriority)
                .orderByDesc(SysPermissionPolicy::getCreateTime);

        IPage<SysPermissionPolicy> policyPage = this.page(pageParam, wrapper);

        /* 转换为VO分页，permName从Entity冗余字段自动映射 */
        IPage<SysPermissionPolicyCommonVO> voPage = sysPermissionPolicyConverter.toVOPage(policyPage);

        return voPage;
    }

    /**
     * 条件查询权限策略
     * <p>
     * 支持按目标类型、目标ID、权限ID、动作精确筛选
     * </p>
     */
    @Override
    public IPage<SysPermissionPolicyCommonVO> queryPolicy(SysPermissionPolicyQueryRTO queryParam) {

        /* 构建分页参数 */
        Page<SysPermissionPolicy> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        /* 构建动态查询条件 */
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<>();

        /* 按目标类型精确筛选 */
        if (queryParam.getTargetType() != null) {
            wrapper.eq(SysPermissionPolicy::getTargetType, queryParam.getTargetType());
        }

        /* 按目标ID精确筛选 */
        if (queryParam.getTargetId() != null) {
            wrapper.eq(SysPermissionPolicy::getTargetId, queryParam.getTargetId());
        }

        /* 按关联权限ID精确筛选 */
        if (queryParam.getPermissionId() != null) {
            wrapper.eq(SysPermissionPolicy::getPermissionId, queryParam.getPermissionId());
        }

        /* 按动作精确筛选 */
        if (queryParam.getAction() != null) {
            wrapper.eq(SysPermissionPolicy::getAction, queryParam.getAction());
        }

        wrapper.eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        wrapper.orderByDesc(SysPermissionPolicy::getPriority).orderByDesc(SysPermissionPolicy::getCreateTime);

        IPage<SysPermissionPolicy> policyPage = this.page(pageParam, wrapper);

        /* 转换为VO分页，permName从Entity冗余字段自动映射 */
        IPage<SysPermissionPolicyCommonVO> voPage = sysPermissionPolicyConverter.toVOPage(policyPage);

        return voPage;
    }

    /**
     * 查询权限策略详情
     * <p>
     * permName从Entity冗余字段直接映射，permCode通过关联查询手动填充
     * </p>
     */
    @Override
    public SysPermissionPolicyDetailVO queryPolicyDetail(String id) {

        /* 参数校验 */
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(400, "策略ID不能为空");
        }

        /* 查询策略实体 */
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getId, Long.parseLong(id))
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        SysPermissionPolicy policy = this.getOne(wrapper);

        if (policy == null) {
            throw new BusinessException(404, "权限策略不存在");
        }

        /* 转换为详情VO，permName从Entity冗余字段自动映射 */
        SysPermissionPolicyDetailVO detailVO = sysPermissionPolicyConverter.toDetailVO(policy);

        /* 关联查询权限信息，填充permCode */
        SysPermission permission = iSysPermissionService.getById(policy.getPermissionId());
        if (permission != null) {
            detailVO.setPermCode(permission.getPermCode());
        }

        return detailVO;
    }

    /**
     * 新增权限策略
     * <p>
     * 校验目标角色和关联权限的有效性，自动填充permName、createBy、createByName
     * </p>
     */
    @Override
    public Integer addPolicy(SysPermissionPolicyAddRTO addParam) {

        /* 校验策略目标有效性 */
        validatePolicyTarget(addParam.getTargetType(), addParam.getTargetId());

        /* 校验关联权限是否存在 */
        LambdaQueryWrapper<SysPermission> permWrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, addParam.getPermissionId())
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermission permission = iSysPermissionService.getOne(permWrapper);
        if (permission == null) {
            throw new BusinessException(400, "关联权限不存在");
        }

        /* RTO转Entity，枚举自动转编码 */
        SysPermissionPolicy entity = sysPermissionPolicyConverter.toEntityAdd(addParam);

        /* 从关联权限自动填充permName冗余字段 */
        entity.setPermName(permission.getPermName());

        /* TODO: 后续接入登录上下文后，从StpUtil获取当前用户ID和姓名替换硬编码 */
        entity.setCreateBy(1L);
        entity.setCreateByName("系统管理员");

        boolean result = this.save(entity);

        if (!result) {
            throw new BusinessException(500, "新增权限策略失败");
        }

        return 1;
    }

    /**
     * 修改权限策略
     * <p>
     * 校验策略存在性和关联数据有效性，自动更新permName冗余字段
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updatePolicy(SysPermissionPolicyUpdateRTO updateParam) {

        /* 校验策略是否存在 */
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getId, updateParam.getId())
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermissionPolicy existPolicy = this.getOne(wrapper);

        if (existPolicy == null) {
            throw new BusinessException(404, "权限策略不存在");
        }

        /* 校验策略目标有效性 */
        validatePolicyTarget(updateParam.getTargetType(), updateParam.getTargetId());

        /* 校验关联权限是否存在 */
        LambdaQueryWrapper<SysPermission> permWrapper = new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getId, updateParam.getPermissionId())
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermission permission = iSysPermissionService.getOne(permWrapper);
        if (permission == null) {
            throw new BusinessException(400, "关联权限不存在");
        }

        /* RTO转Entity，枚举自动转编码 */
        SysPermissionPolicy entity = sysPermissionPolicyConverter.toEntityUpdate(updateParam);

        /* 从关联权限自动更新permName冗余字段 */
        entity.setPermName(permission.getPermName());

        boolean result = this.updateById(entity);

        if (!result) {
            throw new BusinessException(500, "更新权限策略失败");
        }

        return 1;
    }

    /**
     * 删除权限策略
     * <p>
     * 逻辑删除
     * </p>
     */
    @Override
    public Integer deletePolicy(String id) {

        /* 校验策略是否存在 */
        LambdaQueryWrapper<SysPermissionPolicy> wrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getId, id)
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPermissionPolicy existPolicy = this.getOne(wrapper);

        if (existPolicy == null) {
            throw new BusinessException(400, "权限策略不存在");
        }

        /* 执行逻辑删除 */
        SysPermissionPolicy policy = new SysPermissionPolicy();
        policy.setId(Long.parseLong(id));
        policy.setIsDeleted(GlobalEnum.Deleted.DELETED.getCode());

        boolean result = this.updateById(policy);

        if (!result) {
            throw new BusinessException(500, "删除权限策略失败");
        }

        return 1;
    }

    /**
     * 批量新增权限策略
     * <p>
     * 单次上限100条，校验目标有效性，自动填充permName、createBy、createByName
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddPolicy(List<SysPermissionPolicyAddRTO> addParamList) {

        /* 校验批量上限 */
        if (addParamList.size() > 100) {
            throw new BusinessException(400, "单次批量新增数量不能超过100条");
        }

        /* 逐条校验策略目标有效性 */
        for (SysPermissionPolicyAddRTO param : addParamList) {
            validatePolicyTarget(param.getTargetType(), param.getTargetId());
        }

        /* RTO列表转Entity列表 */
        List<SysPermissionPolicy> entityList = sysPermissionPolicyConverter.toEntityListAdd(addParamList);

        /* 批量填充permName冗余字段和创建人信息 */
        for (int i = 0; i < entityList.size(); i++) {
            SysPermissionPolicy entity = entityList.get(i);
            SysPermissionPolicyAddRTO param = addParamList.get(i);

            /* 根据permissionId查询权限名称并填充 */
            SysPermission permission = iSysPermissionService.getById(param.getPermissionId());
            if (permission != null) {
                entity.setPermName(permission.getPermName());
            }

            /* TODO: 后续接入登录上下文后，从StpUtil获取当前用户ID和姓名替换硬编码 */
            entity.setCreateBy(1L);
            entity.setCreateByName("系统管理员");
        }

        boolean batch = this.saveBatch(entityList);

        if (!batch) {
            throw new BusinessException(500, "批量新增权限策略失败");
        }

        return addParamList.size();
    }

    /**
     * 批量删除权限策略
     * <p>
     * 单次上限100条，逻辑删除
     * </p>
     */
    @Override
    public Integer batchDeletePolicy(List<String> ids) {

        /* 校验批量上限 */
        if (ids.size() > 100) {
            throw new BusinessException(400, "单次批量删除数量不能超过100条");
        }

        /* 收集并校验ID */
        Set<Long> idSet = new HashSet<>();
        for (String id : ids) {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(400, "策略ID不能为空");
            }
            idSet.add(Long.parseLong(id));
        }

        /* 查询存在的策略 */
        LambdaQueryWrapper<SysPermissionPolicy> existWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .in(SysPermissionPolicy::getId, idSet)
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermissionPolicy> existPolicies = this.list(existWrapper);

        if (existPolicies.isEmpty()) {
            throw new BusinessException(404, "未找到可删除的权限策略");
        }

        /* 构建逻辑删除列表 */
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
     * 自动填充permName冗余字段和createBy/createByName。
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignRolePermission(SysRolePermissionAssignRTO assignParam) {

        /* 校验角色是否存在 */
        LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getId, assignParam.getRoleId())
                .eq(SysRole::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        long roleCount = iSysRoleService.count(roleWrapper);
        if (roleCount == 0) {
            throw new BusinessException(400, "角色不存在");
        }

        /* 先清：逻辑删除该角色的所有已有策略 */
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

        /* 若权限ID列表为空，仅清除关联后返回 */
        if (assignParam.getPermissionIds().isEmpty()) {
            return 0;
        }

        /* 后写：批量新增角色权限策略 */
        List<SysPermissionPolicy> newPolicies = new ArrayList<>();
        int priority = 100;

        /* 批量查询权限信息，用于填充permName冗余字段 */
        List<SysPermission> permissions = iSysPermissionService.listByIds(assignParam.getPermissionIds());
        Map<Long, SysPermission> permMap = permissions.stream()
                .collect(Collectors.toMap(SysPermission::getId, p -> p));

        for (Long permissionId : assignParam.getPermissionIds()) {
            SysPermissionPolicy policy = new SysPermissionPolicy();
            policy.setTargetType(GlobalEnum.TargetType.ROLE.getCode());
            policy.setTargetId(assignParam.getRoleId());
            policy.setPermissionId(permissionId);
            policy.setAction(GlobalEnum.Action.ALLOW.getCode());
            policy.setPriority(priority++);
            policy.setInheritanceEnabled(true);

            /* 从关联权限自动填充permName冗余字段 */
            SysPermission perm = permMap.get(permissionId);
            if (perm != null) {
                policy.setPermName(perm.getPermName());
            }

            /* TODO: 后续接入登录上下文后，从StpUtil获取当前用户ID和姓名替换硬编码 */
            policy.setCreateBy(1L);
            policy.setCreateByName("系统管理员");

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

        /* 第一层：查询用户关联的角色ID列表 */
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

        /* 收集允许和拒绝的权限ID */
        Set<Long> allowedPermissionIds = new HashSet<>();
        Set<Long> deniedPermissionIds = new HashSet<>();

        /* 第二层：系统级策略 - 仅收集ALLOW的权限 */
        LambdaQueryWrapper<SysPermissionPolicy> systemWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getTargetType, GlobalEnum.TargetType.SYSTEM.getCode())
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .eq(SysPermissionPolicy::getAction, GlobalEnum.Action.ALLOW.getCode());
        List<SysPermissionPolicy> systemPolicies = this.list(systemWrapper);
        for (SysPermissionPolicy policy : systemPolicies) {
            allowedPermissionIds.add(policy.getPermissionId());
        }

        /* 第三层：租户级策略 - 收集ALLOW和DENY */
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

        /* 第四层：角色级策略 - 按优先级降序，DENY优先于ALLOW */
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

        /* 第五层：用户级策略 - 最高优先级，DENY覆盖所有ALLOW */
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

        /* DENY优先级高于ALLOW：从允许集合中移除被拒绝的权限 */
        allowedPermissionIds.removeAll(deniedPermissionIds);

        /* 若无允许的权限，直接返回空列表 */
        if (allowedPermissionIds.isEmpty()) {
            return Collections.emptyList();
        }

        /* 查询权限详情，过滤已禁用的权限，返回权限标识编码列表 */
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

        /* 参数校验 */
        if (userId == null || StringUtils.isBlank(permCode)) {
            return false;
        }

        /* 获取用户所有权限标识列表 */
        List<String> permissionCodes = queryPermissionCodesByUserId(userId, tenantId);

        /* 精确匹配 */
        if (permissionCodes.contains(permCode)) {
            return true;
        }

        /* 前缀匹配：拥有父权限则自动拥有子权限 */
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

        /* 参数校验 */
        if (roleId == null) {
            throw new BusinessException(400, "角色ID不能为空");
        }

        /* 查询角色关联的ALLOW策略 */
        LambdaQueryWrapper<SysPermissionPolicy> policyWrapper = new LambdaQueryWrapper<SysPermissionPolicy>()
                .eq(SysPermissionPolicy::getTargetType, GlobalEnum.TargetType.ROLE.getCode())
                .eq(SysPermissionPolicy::getTargetId, roleId)
                .eq(SysPermissionPolicy::getAction, GlobalEnum.Action.ALLOW.getCode())
                .eq(SysPermissionPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermissionPolicy> policies = this.list(policyWrapper);

        /* 若无策略，返回空列表 */
        if (policies.isEmpty()) {
            return Collections.emptyList();
        }

        /* 提取权限ID集合 */
        Set<Long> permissionIds = policies.stream()
                .map(SysPermissionPolicy::getPermissionId)
                .collect(Collectors.toSet());

        /* 查询权限详情，仅返回已启用且未删除的权限 */
        LambdaQueryWrapper<SysPermission> permWrapper = new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, permissionIds)
                .eq(SysPermission::getStatus, GlobalEnum.Status.ENABLE.getCode())
                .eq(SysPermission::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByAsc(SysPermission::getPermType)
                .orderByAsc(SysPermission::getId);
        List<SysPermission> permissions = iSysPermissionService.list(permWrapper);

        /* 转换为树形VO并构建树形结构 */
        List<SysPermissionTreeVO> treeVOList = sysPermissionConverter.toTreeVOList(permissions);

        return buildPermissionTree(treeVOList);
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

        /* 按parentId分组，构建父→子映射 */
        Map<Long, List<SysPermissionTreeVO>> parentMap = treeVOList.stream()
                .filter(vo -> vo.getParentId() != null && vo.getParentId() != 0L)
                .collect(Collectors.groupingBy(SysPermissionTreeVO::getParentId));

        /* 为每个节点设置子节点列表 */
        for (SysPermissionTreeVO vo : treeVOList) {
            List<SysPermissionTreeVO> children = parentMap.get(vo.getId());
            vo.setChildPermission(children);
        }

        /* 返回根节点列表（parentId为0或null） */
        return treeVOList.stream()
                .filter(vo -> vo.getParentId() == null || vo.getParentId() == 0L)
                .collect(Collectors.toList());
    }

    /**
     * 校验策略目标有效性
     * <p>
     * 当前仅校验角色类型目标是否存在，租户/用户/系统类型暂不校验
     * </p>
     *
     * @param targetType 目标类型枚举
     * @param targetId   目标ID
     */
    private void validatePolicyTarget(GlobalEnum.TargetType targetType, Long targetId) {

        if (targetType == null || targetId == null) {
            return;
        }

        switch (targetType) {
            case ROLE:
                /* 校验角色是否存在 */
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

}
