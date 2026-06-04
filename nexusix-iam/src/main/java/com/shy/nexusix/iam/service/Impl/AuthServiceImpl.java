package com.shy.nexusix.iam.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.iam.dto.UserLoginJoinDTO;
import com.shy.nexusix.iam.dto.UserPermJoinDTO;
import com.shy.nexusix.iam.mapper.SysUserPermRelMapper;
import com.shy.nexusix.iam.mapper.SysUserTenantRelMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private SysUserTenantRelMapper iSysUserTenantRelService;

    @Autowired
    private SysUserPermRelMapper iSysUserPermRelMapper;

    @Override
    public ApiResponse login(LoginRTO param) {

        if (StpUtil.isLogin(param.getUsername())) {
            return ApiResponse.success();
        }

        UserLoginJoinDTO loginJoinInfo = iSysUserTenantRelService.queryUserLoginJoin(param.getUsername());

        // 校验1：用户名或密码不正确（保留原有异常提示）
        if (loginJoinInfo == null || !loginJoinInfo.getPassword().equals(param.getPassword())) {
            throw new BusinessException("用户名或密码不正确");
        }

        if (loginJoinInfo.getUserTenantRelId() == null) {
            throw new BusinessException("用户未设置任何默认租户，请联系租户管理员");
        }

        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(loginJoinInfo.getTenantStatus())) {
            throw new BusinessException("所属租户已停用");
        }

        if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(loginJoinInfo.getTenantStatus())) {
            throw new BusinessException("所属租户已过期");
        }

        StpUtil.login(param.getUsername());

        List<UserPermJoinDTO> permJoinList = iSysUserPermRelMapper.queryUserPermJoin(loginJoinInfo.getUserTenantRelId());

        List<String> allPermCodeList = new ArrayList<>();
        List<String> validPermCodeList = new ArrayList<>();
        List<String> invalidPermCodeList = new ArrayList<>();
        List<String> systemDisabledList = new ArrayList<>();
        List<String> tenantDisabledList = new ArrayList<>();
        List<String> roleDisabledList = new ArrayList<>();
        List<String> userDisabledList = new ArrayList<>();

        Set<Long> seenPermIdSet = new HashSet<>();

        Map<Long, Boolean> hasSystemDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasTenantDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasRoleDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasUserDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasActiveMap = new HashMap<>();

        Map<Long, String> permIdToCodeMap = new HashMap<>();

        Map<String, UserContextDTO.EntityFieldPerm> queryPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> createPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> updatePermMap = new HashMap<>();

        for (UserPermJoinDTO row : permJoinList) {
            Long permId = row.getPermId();
            String permCode = row.getPermCode();
            String status = row.getPolicyStatus();

            if (permId != null && permCode != null && !seenPermIdSet.contains(permId)) {
                seenPermIdSet.add(permId);
                permIdToCodeMap.put(permId, permCode);
                allPermCodeList.add(permCode);
            }

            if (GlobalEnum.PermPolicyStatus.ACTIVE.getCode().equals(status)) {
                hasActiveMap.put(permId, true);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_SYSTEM_LEVEL.getCode().equals(status)) {
                hasSystemDisabledMap.put(permId, true);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_TENANT_LEVEL.getCode().equals(status)) {
                hasTenantDisabledMap.put(permId, true);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_ROLE_LEVEL.getCode().equals(status)) {
                hasRoleDisabledMap.put(permId, true);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_USER_LEVEL.getCode().equals(status)) {
                hasUserDisabledMap.put(permId, true);
            }

            String fieldOperates = row.getFieldOperates();
            String accessType = row.getAccessType();
            String tableName = row.getTableName();

            if (fieldOperates == null || fieldOperates.isEmpty()
                    || accessType == null || tableName == null) {
                continue;
            }

            Map<String, UserContextDTO.EntityFieldPerm> targetMap;
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
            if (fields == null || fields.isEmpty()) {
                continue;
            }

            UserContextDTO.EntityFieldPerm fieldPerm = targetMap.computeIfAbsent(tableName,
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
        }

        for (Long permId : seenPermIdSet) {
            String permCode = permIdToCodeMap.get(permId);

            boolean isSystemDisabled = hasSystemDisabledMap.getOrDefault(permId, false);
            boolean isTenantDisabled = hasTenantDisabledMap.getOrDefault(permId, false);
            boolean isRoleDisabled = hasRoleDisabledMap.getOrDefault(permId, false);
            boolean isUserDisabled = hasUserDisabledMap.getOrDefault(permId, false);

            if (isSystemDisabled) systemDisabledList.add(permCode);
            if (isTenantDisabled) tenantDisabledList.add(permCode);
            if (isRoleDisabled) roleDisabledList.add(permCode);
            if (isUserDisabled) userDisabledList.add(permCode);

            if (isSystemDisabled || isTenantDisabled || isRoleDisabled || isUserDisabled) {
                invalidPermCodeList.add(permCode);
            } else if (hasActiveMap.getOrDefault(permId, false)) {
                validPermCodeList.add(permCode);
            }
        }

        // TODO 查询角色信息

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
        tenantInfoCache.setTenantName(loginJoinInfo.getTenantName());
        tenantInfoCache.setTenantCode(loginJoinInfo.getTenantCode());

        UserContextDTO userContext = new UserContextDTO();
        userContext.setTenantInfo(tenantInfoCache);
        userContext.setPermInfo(permInfo);

        StpUtil.getSession().set("userContext", userContext);
        return ApiResponse.success();

    }

}