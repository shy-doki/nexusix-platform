package com.shy.nexusix.iam.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.dto.UserContextDTO;
import com.shy.nexusix.iam.entity.*;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.*;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysUserTenantRelService iSysUserTenantRelService;

    @Autowired
    private ISysTenantService iSysTenantService;

    @Autowired
    private ISysUserPermRelService iSysUserPermRelService;

    @Autowired
    private ISysPermPolicyService iSysPermPolicyService;

    @Autowired
    private ISysPermService iSysPermService;

    @Override
    public ApiResponse login(LoginRTO param) {

        // 缓存命中直接返回成功
        if (StpUtil.getSession().get("userContext") != null) {
            return ApiResponse.success();
        }

        // 查询用户
        SysUser loginUserInfo = iSysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, param.getUsername())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (loginUserInfo == null || !loginUserInfo.getPassword().equals(param.getPassword())) {
            throw new BusinessException("用户名或密码不正确");
        }

        // 查询默认租户关系
        SysUserTenantRel userTenantRelInfo = iSysUserTenantRelService.getOne(new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getUserId, loginUserInfo.getId())
                .eq(SysUserTenantRel::getIsDefault, GlobalEnum.DefaultTenant.DEFAULT.getCode())
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (userTenantRelInfo == null) {
            throw new BusinessException("用户未设置任何默认租户，请联系租户管理员");
        }

        // 查询租户信息并校验状态
        LambdaQueryWrapper<SysTenant> tenantWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, userTenantRelInfo.getTenantId())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant tenantInfo = iSysTenantService.getOne(tenantWrapper);

        // 租户被管理员停用，禁止登录
        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(tenantInfo.getStatus())) {
            throw new BusinessException("所属租户已停用");
        }
        // 租户已超过有效期，禁止登录
        if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(tenantInfo.getStatus())) {
            throw new BusinessException("所属租户已过期");
        }

        // Sa-Token 登录
        StpUtil.login(loginUserInfo.getId());

        // 查询用户权限策略关联 关系ID作为userId
        List<SysUserPermRel> userPermRelList = iSysUserPermRelService.list(new LambdaQueryWrapper<SysUserPermRel>()
                .eq(SysUserPermRel::getUserId, userTenantRelInfo.getId())
                .eq(SysUserPermRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));

        // 批量查询策略详情
        List<SysPermPolicy> permPolicyList = new ArrayList<>();
        if (!userPermRelList.isEmpty()) {
            List<Long> policyIdList = userPermRelList.stream()
                    .map(SysUserPermRel::getPolicyId)
                    .toList();
            permPolicyList = iSysPermPolicyService.list(new LambdaQueryWrapper<SysPermPolicy>()
                    .in(SysPermPolicy::getId, policyIdList)
                    .eq(SysPermPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        }

        // 批量查询权限资源
        List<SysPerm> permList = new ArrayList<>();
        if (!permPolicyList.isEmpty()) {
            Set<Long> permIdSet = permPolicyList.stream()
                    .map(SysPermPolicy::getPermId)
                    .collect(Collectors.toSet());
            permList = iSysPermService.list(new LambdaQueryWrapper<SysPerm>()
                    .in(SysPerm::getId, permIdSet)
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        }

        // 构建 permId -> permCode 映射
        Map<Long, String> permIdToCodeMap = permList.stream()
                .collect(Collectors.toMap(SysPerm::getId, SysPerm::getPermCode));

        // 初始化禁用层级集合和字段权限Map
        Set<Long> activePolicyPermIdSet = new HashSet<>();
        Set<Long> systemDisabledPermIdSet = new HashSet<>();
        Set<Long> tenantDisabledPermIdSet = new HashSet<>();
        Set<Long> roleDisabledPermIdSet = new HashSet<>();
        Set<Long> userDisabledPermIdSet = new HashSet<>();

        Map<String, UserContextDTO.EntityFieldPerm> queryPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> createPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> updatePermMap = new HashMap<>();

        // 一次流式遍历策略列表：收集禁用层级 + 构建字段权限
        permPolicyList.stream().forEach(policy -> {
            Long permId = policy.getPermId();
            String status = policy.getStatus();
            // 更新禁用层级集合
            if (GlobalEnum.PermPolicyStatus.ACTIVE.getCode().equals(status)) {
                activePolicyPermIdSet.add(permId);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_SYSTEM_LEVEL.getCode().equals(status)) {
                systemDisabledPermIdSet.add(permId);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_TENANT_LEVEL.getCode().equals(status)) {
                tenantDisabledPermIdSet.add(permId);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_ROLE_LEVEL.getCode().equals(status)) {
                roleDisabledPermIdSet.add(permId);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_USER_LEVEL.getCode().equals(status)) {
                userDisabledPermIdSet.add(permId);
            }

            // 构建字段权限 Map（fieldOperates 为空则跳过）
            String fieldOperates = policy.getFieldOperates();
            if (fieldOperates == null || fieldOperates.isEmpty()) return;

            Map<String, UserContextDTO.EntityFieldPerm> targetMap;
            String accessType = policy.getAccessType();
            if (GlobalEnum.PermPolicyAccessType.QUERY.getCode().equals(accessType)) {
                targetMap = queryPermMap;
            } else if (GlobalEnum.PermPolicyAccessType.CREATE.getCode().equals(accessType)) {
                targetMap = createPermMap;
            } else if (GlobalEnum.PermPolicyAccessType.UPDATE.getCode().equals(accessType)) {
                targetMap = updatePermMap;
            } else {
                return;
            }

            List<String> fields = JSON.parseArray(fieldOperates, String.class);
            if (fields == null || fields.isEmpty()) return;

            UserContextDTO.EntityFieldPerm fieldPerm = targetMap.computeIfAbsent(policy.getTableName(),
                    k -> new UserContextDTO.EntityFieldPerm());
            if (GlobalEnum.PermPolicyStatus.ACTIVE.getCode().equals(status)) {
                if (fieldPerm.getVisibleFields() == null) {
                    fieldPerm.setVisibleFields(new ArrayList<>(fields));
                } else {
                    fieldPerm.getVisibleFields().addAll(fields);
                }
            } else {
                if (fieldPerm.getInvisibleFields() == null) {
                    fieldPerm.setInvisibleFields(new ArrayList<>(fields));
                } else {
                    fieldPerm.getInvisibleFields().addAll(fields);
                }
            }
        });

        // 初始化权限编码列表和四级禁用列表
        List<String> allPermCodeList = new ArrayList<>(permList.size());
        List<String> validPermCodeList = new ArrayList<>();
        List<String> invalidPermCodeList = new ArrayList<>();
        List<String> systemDisabledList = new ArrayList<>();
        List<String> tenantDisabledList = new ArrayList<>();
        List<String> roleDisabledList = new ArrayList<>();
        List<String> userDisabledList = new ArrayList<>();

        // 流式遍历权限资源，填充上述列表
        permList.stream().forEach(perm -> {
            Long permId = perm.getId();
            String permCode = perm.getPermCode();
            allPermCodeList.add(permCode);

            boolean isSystemDisabled = systemDisabledPermIdSet.contains(permId);
            boolean isTenantDisabled = tenantDisabledPermIdSet.contains(permId);
            boolean isRoleDisabled = roleDisabledPermIdSet.contains(permId);
            boolean isUserDisabled = userDisabledPermIdSet.contains(permId);

            if (isSystemDisabled) systemDisabledList.add(permCode);
            if (isTenantDisabled) tenantDisabledList.add(permCode);
            if (isRoleDisabled) roleDisabledList.add(permCode);
            if (isUserDisabled) userDisabledList.add(permCode);

            if (isSystemDisabled || isTenantDisabled || isRoleDisabled || isUserDisabled) {
                invalidPermCodeList.add(permCode);
            } else if (activePolicyPermIdSet.contains(permId)) {
                validPermCodeList.add(permCode);
            }
        });

        // 组装 UserContextDTO（与原逻辑完全一致）
        UserContextDTO.CascadeDisabled cascadeDisabled = new UserContextDTO.CascadeDisabled();
        cascadeDisabled.setSystemDisabled(systemDisabledList);
        cascadeDisabled.setTenantDisabled(tenantDisabledList);
        cascadeDisabled.setRoleDisabled(roleDisabledList);
        cascadeDisabled.setUserDisabled(userDisabledList);

        UserContextDTO.FieldPerm fieldPerm = new UserContextDTO.FieldPerm();
        fieldPerm.setQuery(queryPermMap);
        fieldPerm.setCreate(createPermMap);
        fieldPerm.setUpdate(updatePermMap);

        UserContextDTO.PermInfo permInfo = new UserContextDTO.PermInfo();
        permInfo.setPerms(allPermCodeList);
        permInfo.setValidPerms(validPermCodeList);
        permInfo.setInvalidPerms(invalidPermCodeList);
        permInfo.setCascadeDisabled(cascadeDisabled);
        permInfo.setFieldPerm(fieldPerm);

        UserContextDTO.TenantInfo tenantInfoCache = new UserContextDTO.TenantInfo();
        tenantInfoCache.setTenantName(tenantInfo.getTenantName());
        tenantInfoCache.setTenantCode(tenantInfo.getTenantCode());

        UserContextDTO userContext = new UserContextDTO();
        userContext.setTenantInfo(tenantInfoCache);
        userContext.setPermInfo(permInfo);

        // TODO 查询角色信息

        StpUtil.getSession().set("userContext", userContext);
        return ApiResponse.success();

    }

}
