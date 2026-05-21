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
import com.shy.nexusix.tenant.converter.SysTenantConverter;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.service.ISysTenantService;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
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

        // 根据用户名查询
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, param.getUsername())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUser loginUserInfo = iSysUserService.getOne(userWrapper);

        if (loginUserInfo == null) {
            throw new BusinessException("用户名或密码不正确");
        }

        // 验证密码
        if (!loginUserInfo.getPassword().equals(param.getPassword())) {
            throw new BusinessException("用户名或密码不正确");
        }

        // 查询租户信息
        LambdaQueryWrapper<SysUserTenantRel> userTenantRelWrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getUserId, loginUserInfo.getId())
                .eq(SysUserTenantRel::getIsDefault, GlobalEnum.DefaultTenant.DEFAULT.getCode())
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserTenantRel userTenantRelInfo = iSysUserTenantRelService.getOne(userTenantRelWrapper);

        if (userTenantRelInfo == null) {
            throw new BusinessException("用户未加入任何租户");
        }

        StpUtil.login(loginUserInfo.getId());

        LambdaQueryWrapper<SysTenant> tenantWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, userTenantRelInfo.getTenantId())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant tenant = iSysTenantService.getOne(tenantWrapper);

        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(tenant.getStatus())) {
            throw new BusinessException("所属租户已停用");
        }
        if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(tenant.getStatus())) {
            throw new BusinessException("所属租户已过期");
        }

        UserContextDTO.TenantInfo tenantInfo = new UserContextDTO.TenantInfo();
        tenantInfo.setTenantName(tenant.getTenantName());
        tenantInfo.setTenantCode(tenant.getTenantCode());

        // 查询用户当前租户下权限信息[有效+无效]
        LambdaQueryWrapper<SysUserPermRel> userPermRelWrapper = new LambdaQueryWrapper<SysUserPermRel>()
                .eq(SysUserPermRel::getUserId, userTenantRelInfo.getId())
                .eq(SysUserPermRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysUserPermRel> userPermRelList = iSysUserPermRelService.list(userPermRelWrapper);
        // 提取权限策略ID
        List<Long> policyIdList = userPermRelList.stream()
                .map(SysUserPermRel::getPolicyId)
                .toList();

        // 查询权限策略信息[有效+无效]
        LambdaQueryWrapper<SysPermPolicy> permPolicyWrapper = new LambdaQueryWrapper<SysPermPolicy>()
                .in(SysPermPolicy::getId, policyIdList)
                .eq(SysPermPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPermPolicy> permPolicyList = iSysPermPolicyService.list(permPolicyWrapper);

        // 权限信息
        UserContextDTO.PermInfo permInfo = new UserContextDTO.PermInfo();
        // 查询权限信息
        List<Long> permIdList = permPolicyList.stream()
                .map(SysPermPolicy::getPermId)
                .toList();
        LambdaQueryWrapper<SysPerm> permWrapper = new LambdaQueryWrapper<SysPerm>()
                .in(SysPerm::getId, permIdList)
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysPerm> permList = iSysPermService.list(permWrapper);
        // 提取所有权限编码
        List<String> permCodeList = permList.stream()
                .map(SysPerm::getPermCode)
                .toList();
        // 存入权限编码列表 [有效/无效]
        permInfo.setPerms(permCodeList);
        // 根据权限策略状态提取有效/无效的 permId
        Set<Long> activePolicyPermIds = permPolicyList.stream()
                .filter(item -> item.getStatus().equals(GlobalEnum.PermPolicyStatus.ACTIVE.getCode()))
                .map(SysPermPolicy::getPermId)
                .collect(Collectors.toSet());
        Set<Long> inactivePolicyPermIds = permPolicyList.stream()
                .filter(item -> item.getStatus().equals(GlobalEnum.PermPolicyStatus.INACTIVE.getCode()))
                .map(SysPermPolicy::getPermId)
                .collect(Collectors.toSet());

        // 提取有效权限编码
        List<String> validPermCodeList = permList.stream()
                .filter(item -> activePolicyPermIds.contains(item.getId()))
                .map(SysPerm::getPermCode)
                .toList();
        // 存入权限编码列表 [有效]
        permInfo.setValidPerms(validPermCodeList);
        // 提取失效权限编码
        List<String> invalidPermCodeList = permList.stream()
                .filter(item -> inactivePolicyPermIds.contains(item.getId()))
                .map(SysPerm::getPermCode)
                .toList();
        // 存入权限编码列表 [无效]
        permInfo.setInvalidPerm(invalidPermCodeList);

        // 查询类 可访问字段权限信息[有效+无效]
        Map<String, UserContextDTO.EntityFieldPerm> queryPermMap = new HashMap<>();
        permPolicyList.stream()
                .filter(item -> item.getAccessType().equals(GlobalEnum.PermPolicyAccessType.QUERY.getCode()))
                .forEach(policy -> {
                    queryPermMap.computeIfAbsent(policy.getTableName(), k -> new UserContextDTO.EntityFieldPerm());
                    UserContextDTO.EntityFieldPerm fieldPerm = queryPermMap.get(policy.getTableName());
                    // 解析 fieldOperates JSON 字符串
                    List<String> fields = JSON.parseArray(policy.getFieldOperates(), String.class);
                    // 根据策略状态区分有效和无效字段
                    if (policy.getStatus().equals(GlobalEnum.PermPolicyStatus.ACTIVE.getCode())) {
                        fieldPerm.setVisibleFields(fields);
                    } else if (policy.getStatus().equals(GlobalEnum.PermPolicyStatus.INACTIVE.getCode())) {
                        fieldPerm.setInvisibleFields(fields);
                    }
                });
        permInfo.setQuery(queryPermMap);

        // 新增类 字段权限信息[有效+无效]
        Map<String, UserContextDTO.EntityFieldPerm> createPermMap = new HashMap<>();
        permPolicyList.stream()
                .filter(item -> item.getAccessType().equals(GlobalEnum.PermPolicyAccessType.CREATE.getCode()))
                .forEach(policy -> {
                    createPermMap.computeIfAbsent(policy.getTableName(), k -> new UserContextDTO.EntityFieldPerm());
                    UserContextDTO.EntityFieldPerm fieldPerm = createPermMap.get(policy.getTableName());
                    // 解析 fieldOperates JSON 字符串
                    List<String> fields = JSON.parseArray(policy.getFieldOperates(), String.class);
                    // 根据策略状态区分有效和无效字段
                    if (policy.getStatus().equals(GlobalEnum.PermPolicyStatus.ACTIVE.getCode())) {
                        fieldPerm.setVisibleFields(fields);
                    } else if (policy.getStatus().equals(GlobalEnum.PermPolicyStatus.INACTIVE.getCode())) {
                        fieldPerm.setInvisibleFields(fields);
                    }
                });
        permInfo.setCreate(createPermMap);

        // 更新类 字段权限信息[有效+无效]
        Map<String, UserContextDTO.EntityFieldPerm> updatePermMap = new HashMap<>();
        permPolicyList.stream()
                .filter(item -> item.getAccessType().equals(GlobalEnum.PermPolicyAccessType.UPDATE.getCode()))
                .forEach(policy -> {
                    updatePermMap.computeIfAbsent(policy.getTableName(), k -> new UserContextDTO.EntityFieldPerm());
                    UserContextDTO.EntityFieldPerm fieldPerm = updatePermMap.get(policy.getTableName());
                    // 解析 fieldOperates JSON 字符串
                    List<String> fields = JSON.parseArray(policy.getFieldOperates(), String.class);
                    // 根据策略状态区分有效和无效字段
                    if (policy.getStatus().equals(GlobalEnum.PermPolicyStatus.ACTIVE.getCode())) {
                        fieldPerm.setVisibleFields(fields);
                    } else if (policy.getStatus().equals(GlobalEnum.PermPolicyStatus.INACTIVE.getCode())) {
                        fieldPerm.setInvisibleFields(fields);
                    }
                });
        permInfo.setUpdate(updatePermMap);

        UserContextDTO userContext = new UserContextDTO();
        userContext.setTenantInfo(tenantInfo);
        userContext.setPermInfo(permInfo);

        // TODO 查询角色信息

        // 存入用户上下文
        StpUtil.getSession().set("userContext", userContext);

        return ApiResponse.success();

    }

}
