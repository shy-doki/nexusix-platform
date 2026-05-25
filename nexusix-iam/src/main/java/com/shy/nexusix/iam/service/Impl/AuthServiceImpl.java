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

        // 有效权限
        Set<Long> activePolicyPermIdSet = new HashSet<>();
        // 失效权限[系统级]
        Set<Long> systemDisabledPermIdSet = new HashSet<>();
        // 失效权限[租户级]
        Set<Long> tenantDisabledPermIdSet = new HashSet<>();
        // 角色级禁用权限
        Set<Long> roleDisabledPermIdSet = new HashSet<>();
        // 用户级禁用权限
        Set<Long> userDisabledPermIdSet = new HashSet<>();
        // 字段权限[查询类]
        Map<String, UserContextDTO.EntityFieldPerm> queryPermMap = new HashMap<>();
        // 字段权限[创建类]
        Map<String, UserContextDTO.EntityFieldPerm> createPermMap = new HashMap<>();
        // 字段权限[更新类]
        Map<String, UserContextDTO.EntityFieldPerm> updatePermMap = new HashMap<>();

        // 一次流式遍历策略列表：收集禁用层级 + 构建字段权限
        permPolicyList.stream().forEach(policy -> {
            // 获取权限ID
            Long permId = policy.getPermId();
            // 获取权限状态
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

            // 构建字段权限
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

        // 所有权限编码
        List<String> allPermCodeList = new ArrayList<>(permList.size());
        // 有效权限编码
        List<String> validPermCodeList = new ArrayList<>();
        // 失效权限编码
        List<String> invalidPermCodeList = new ArrayList<>();
        // 失效权限编码[系统级]
        List<String> systemDisabledList = new ArrayList<>();
        // 失效权限编码[租户级]
        List<String> tenantDisabledList = new ArrayList<>();
        // 失效权限编码[角色级]
        List<String> roleDisabledList = new ArrayList<>();
        // 失效权限编码[用户级]
        List<String> userDisabledList = new ArrayList<>();

        // 流式遍历权限资源，填充上述列表
        permList.stream().forEach(perm -> {
            Long permId = perm.getId();
            String permCode = perm.getPermCode();
            allPermCodeList.add(permCode);

            // 获取失效权限[系统级]
            boolean isSystemDisabled = systemDisabledPermIdSet.contains(permId);
            // 获取失效权限[租户级]
            boolean isTenantDisabled = tenantDisabledPermIdSet.contains(permId);
            // 获取失效权限[角色级]
            boolean isRoleDisabled = roleDisabledPermIdSet.contains(permId);
            // 获取失效权限[用户级]
            boolean isUserDisabled = userDisabledPermIdSet.contains(permId);

            // 填充失效权限[系统级]
            if (isSystemDisabled) systemDisabledList.add(permCode);
            // 填充失效权限[租户级]
            if (isTenantDisabled) tenantDisabledList.add(permCode);
            // 填充失效权限[角色级]
            if (isRoleDisabled) roleDisabledList.add(permCode);
            // 填充失效权限[用户级]
            if (isUserDisabled) userDisabledList.add(permCode);

            // 填充四层级失效权限
            if (isSystemDisabled || isTenantDisabled || isRoleDisabled || isUserDisabled) {
                invalidPermCodeList.add(permCode);
            } else if (activePolicyPermIdSet.contains(permId)) {
                validPermCodeList.add(permCode);
            }
        });

        // 构建失效权限层级
        UserContextDTO.CascadeDisabled cascadeDisabled = new UserContextDTO.CascadeDisabled();
        // 填充失效权限层级[系统级]
        cascadeDisabled.setSystemDisabled(systemDisabledList);
        // 填充失效权限层级[租户级]
        cascadeDisabled.setTenantDisabled(tenantDisabledList);
        // 填充失效权限层级[角色级]
        cascadeDisabled.setRoleDisabled(roleDisabledList);
        // 填充失效权限层级[用户级]
        cascadeDisabled.setUserDisabled(userDisabledList);

        // 构建字段权限
        UserContextDTO.FieldPerm fieldPerm = new UserContextDTO.FieldPerm();
        // 填充字段权限[查询类]
        fieldPerm.setQuery(queryPermMap);
        // 填充字段权限[创建类]
        fieldPerm.setCreate(createPermMap);
        // 填充字段权限[更新类]
        fieldPerm.setUpdate(updatePermMap);

        // 构建权限信息
        UserContextDTO.PermInfo permInfo = new UserContextDTO.PermInfo();
        // 填充所有权限编码
        permInfo.setPerms(allPermCodeList);
        // 填充有效权限编码
        permInfo.setValidPerms(validPermCodeList);
        // 填充失效权限编码
        permInfo.setInvalidPerms(invalidPermCodeList);
        // 填充四层级失效权限
        permInfo.setCascadeDisabled(cascadeDisabled);
        // 填充字段权限
        permInfo.setFieldPerm(fieldPerm);

        // 构建租户信息
        UserContextDTO.TenantInfo tenantInfoCache = new UserContextDTO.TenantInfo();
        // 填充租户名称
        tenantInfoCache.setTenantName(tenantInfo.getTenantName());
        // 填充租户编码
        tenantInfoCache.setTenantCode(tenantInfo.getTenantCode());

        UserContextDTO userContext = new UserContextDTO();
        userContext.setTenantInfo(tenantInfoCache);
        userContext.setPermInfo(permInfo);

        // TODO 查询角色信息

        // 缓存用户上下文信息
        StpUtil.getSession().set("userContext", userContext);
        return ApiResponse.success();

    }

}
