package com.shy.nexusix.iam.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.dto.LoginUserTenantDTO;
import com.shy.nexusix.iam.dto.UserContextDTO;
import com.shy.nexusix.iam.dto.UserPermDetailDTO;
import com.shy.nexusix.iam.entity.*;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.mapper.SysUserPermRelMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.*;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserPermRelMapper sysUserPermRelMapper;

    @Override
    public ApiResponse login(LoginRTO param) {

        // ========== 第一段：用户 + 租户 三表 JOIN 查询（原 3 次查询 → 1 次） ==========
        LoginUserTenantDTO loginData = sysUserMapper.selectJoinOne(LoginUserTenantDTO.class,
                new MPJLambdaWrapper<SysUser>()
                        .selectAs(SysUser::getId, LoginUserTenantDTO::getUserId)
                        .selectAs(SysUser::getUserCode, LoginUserTenantDTO::getUserCode)
                        .selectAs(SysUser::getUserName, LoginUserTenantDTO::getUserName)
                        .selectAs(SysUser::getPassword, LoginUserTenantDTO::getPassword)
                        .selectAs(SysUser::getNickName, LoginUserTenantDTO::getNickName)
                        .selectAs(SysUser::getEmail, LoginUserTenantDTO::getEmail)
                        .selectAs(SysUser::getPhone, LoginUserTenantDTO::getPhone)
                        .selectAs(SysUser::getAvatar, LoginUserTenantDTO::getAvatar)
                        .selectAs(SysUser::getStatus, LoginUserTenantDTO::getUserStatus)
                        .selectAs(SysUserTenantRel::getId, LoginUserTenantDTO::getRelId)
                        .selectAs(SysUserTenantRel::getUserId, LoginUserTenantDTO::getRelUserId)
                        .selectAs(SysUserTenantRel::getTenantId, LoginUserTenantDTO::getRelTenantId)
                        .selectAs(SysUserTenantRel::getDeptId, LoginUserTenantDTO::getRelDeptId)
                        .selectAs(SysUserTenantRel::getIsAdmin, LoginUserTenantDTO::getRelIsAdmin)
                        .selectAs(SysUserTenantRel::getIsDefault, LoginUserTenantDTO::getRelIsDefault)
                        .selectAs(SysTenant::getId, LoginUserTenantDTO::getTenantId)
                        .selectAs(SysTenant::getTenantCode, LoginUserTenantDTO::getTenantCode)
                        .selectAs(SysTenant::getTenantName, LoginUserTenantDTO::getTenantName)
                        .selectAs(SysTenant::getStatus, LoginUserTenantDTO::getTenantStatus)
                        .innerJoin(SysUserTenantRel.class, on ->
                                on.eq(SysUser::getId, SysUserTenantRel::getUserId)
                                        .eq(SysUserTenantRel::getIsDefault, true)
                                        .eq(SysUserTenantRel::getIsDeleted, "NOT_DELETED"))
                        .innerJoin(SysTenant.class, on ->
                                on.eq(SysUserTenantRel::getTenantId, SysTenant::getId)
                                        .eq(SysTenant::getIsDeleted, "NOT_DELETED"))
                        .eq(SysUser::getUserName, param.getUsername())
                        .eq(SysUser::getIsDeleted, "NOT_DELETED")
        );

        if (loginData == null || !loginData.getPassword().equals(param.getPassword())) {
            throw new BusinessException("用户名或密码不正确");
        }

        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(loginData.getTenantStatus())) {
            throw new BusinessException("所属租户已停用");
        }
        if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(loginData.getTenantStatus())) {
            throw new BusinessException("所属租户已过期");
        }

        StpUtil.login(loginData.getUserId());

        // ========== 第二段：权限 三表 JOIN 查询（原 3 次查询 → 1 次） ==========
        List<UserPermDetailDTO> permDetails = sysUserPermRelMapper.selectJoinList(UserPermDetailDTO.class,
                new MPJLambdaWrapper<SysUserPermRel>()
                        .selectAs(SysPerm::getId, UserPermDetailDTO::getPermId)
                        .selectAs(SysPerm::getPermCode, UserPermDetailDTO::getPermCode)
                        .selectAs(SysPerm::getPermName, UserPermDetailDTO::getPermName)
                        .selectAs(SysPermPolicy::getId, UserPermDetailDTO::getPolicyId)
                        .selectAs(SysPermPolicy::getPermId, UserPermDetailDTO::getPolicyPermId)
                        .selectAs(SysPermPolicy::getStatus, UserPermDetailDTO::getPolicyStatus)
                        .selectAs(SysPermPolicy::getTableName, UserPermDetailDTO::getPolicyTableName)
                        .selectAs(SysPermPolicy::getAccessType, UserPermDetailDTO::getPolicyAccessType)
                        .selectAs(SysPermPolicy::getFieldOperates, UserPermDetailDTO::getPolicyFieldOperates)
                        .selectAs(SysUserPermRel::getId, UserPermDetailDTO::getRelId)
                        .innerJoin(SysPermPolicy.class, on ->
                                on.eq(SysUserPermRel::getPolicyId, SysPermPolicy::getId)
                                        .eq(SysPermPolicy::getIsDeleted, "NOT_DELETED"))
                        .innerJoin(SysPerm.class, on ->
                                on.eq(SysPermPolicy::getPermId, SysPerm::getId)
                                        .eq(SysPerm::getIsDeleted, "NOT_DELETED"))
                        .eq(SysUserPermRel::getUserId, loginData.getRelId())
                        .eq(SysUserPermRel::getIsDeleted, "NOT_DELETED")
        );

        // ========== 第三段：一次遍历构建全部权限数据（原 2 次遍历 → 1 次） ==========
        Set<Long> activePolicyPermIdSet = new HashSet<>();
        Set<Long> systemDisabledPermIdSet = new HashSet<>();
        Set<Long> tenantDisabledPermIdSet = new HashSet<>();
        Set<Long> roleDisabledPermIdSet = new HashSet<>();
        Set<Long> userDisabledPermIdSet = new HashSet<>();
        Map<String, UserContextDTO.EntityFieldPerm> queryPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> createPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> updatePermMap = new HashMap<>();

        List<String> allPermCodeList = new ArrayList<>();
        List<String> validPermCodeList = new ArrayList<>();
        List<String> invalidPermCodeList = new ArrayList<>();
        List<String> systemDisabledList = new ArrayList<>();
        List<String> tenantDisabledList = new ArrayList<>();
        List<String> roleDisabledList = new ArrayList<>();
        List<String> userDisabledList = new ArrayList<>();

        for (UserPermDetailDTO detail : permDetails) {
            Long permId = detail.getPermId();
            String permCode = detail.getPermCode();
            String status = detail.getPolicyStatus();

            allPermCodeList.add(permCode);

            if (GlobalEnum.PermPolicyStatus.ACTIVE.getCode().equals(status)) {
                activePolicyPermIdSet.add(permId);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_SYSTEM_LEVEL.getCode().equals(status)) {
                systemDisabledPermIdSet.add(permId);
                systemDisabledList.add(permCode);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_TENANT_LEVEL.getCode().equals(status)) {
                tenantDisabledPermIdSet.add(permId);
                tenantDisabledList.add(permCode);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_ROLE_LEVEL.getCode().equals(status)) {
                roleDisabledPermIdSet.add(permId);
                roleDisabledList.add(permCode);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_USER_LEVEL.getCode().equals(status)) {
                userDisabledPermIdSet.add(permId);
                userDisabledList.add(permCode);
            }

            boolean isDisabled = systemDisabledPermIdSet.contains(permId)
                    || tenantDisabledPermIdSet.contains(permId)
                    || roleDisabledPermIdSet.contains(permId)
                    || userDisabledPermIdSet.contains(permId);

            if (isDisabled) {
                invalidPermCodeList.add(permCode);
            } else if (activePolicyPermIdSet.contains(permId)) {
                validPermCodeList.add(permCode);
            }

            String fieldOperates = detail.getPolicyFieldOperates();
            if (fieldOperates == null || fieldOperates.isEmpty()) continue;

            Map<String, UserContextDTO.EntityFieldPerm> targetMap;
            String accessType = detail.getPolicyAccessType();
            if (GlobalEnum.PermPolicyAccessType.QUERY.getCode().equals(accessType)) {
                targetMap = queryPermMap;
            } else if (GlobalEnum.PermPolicyAccessType.CREATE.getCode().equals(accessType)) {
                targetMap = createPermMap;
            } else if (GlobalEnum.PermPolicyAccessType.UPDATE.getCode().equals(accessType)) {
                targetMap = updatePermMap;
            } else {
                continue;
            }

            List<String> fields = JSON.parseArray(fieldOperates, String.class);
            if (fields == null || fields.isEmpty()) continue;

            UserContextDTO.EntityFieldPerm fieldPerm = targetMap.computeIfAbsent(
                    detail.getPolicyTableName(), k -> new UserContextDTO.EntityFieldPerm());
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
        }

        // ========== 第四段：组装 UserContextDTO（与原逻辑一致） ==========
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
        permInfo.setPerms(allPermCodeList.stream().distinct().collect(Collectors.toList()));
        permInfo.setValidPerms(validPermCodeList);
        permInfo.setInvalidPerms(invalidPermCodeList);
        permInfo.setCascadeDisabled(cascadeDisabled);
        permInfo.setFieldPerm(fieldPerm);

        UserContextDTO.TenantInfo tenantInfoCache = new UserContextDTO.TenantInfo();
        tenantInfoCache.setTenantName(loginData.getTenantName());
        tenantInfoCache.setTenantCode(loginData.getTenantCode());

        UserContextDTO userContext = new UserContextDTO();
        userContext.setTenantInfo(tenantInfoCache);
        userContext.setPermInfo(permInfo);

        StpUtil.getSession().set("userContext", userContext);
        return ApiResponse.success();

    }

}
