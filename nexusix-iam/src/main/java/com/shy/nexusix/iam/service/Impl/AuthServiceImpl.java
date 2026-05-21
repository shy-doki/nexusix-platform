package com.shy.nexusix.iam.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.dto.UserContextDTO;
import com.shy.nexusix.iam.entity.*;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.*;
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

        // 查询用户当前租户下权限信息
        LambdaQueryWrapper<SysUserPermRel> userPermRelWrapper = new LambdaQueryWrapper<SysUserPermRel>()
                .eq(SysUserPermRel::getUserId, userTenantRelInfo.getId())
                .eq(SysUserPermRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysUserPermRel> userPermRelList = iSysUserPermRelService.list(userPermRelWrapper);
        // 提取权限策略ID
        List<Long> policyIdList = userPermRelList.stream()
                .map(SysUserPermRel::getPolicyId)
                .toList();

        // 查询权限策略信息 分为两部分 1. 生效 2. 失效
        List<SysPermPolicy> permPolicyList = iSysPermPolicyService.listByIds(policyIdList);
        // 提取生效权限策略
        List<SysPermPolicy> validPermPolicyList = permPolicyList.stream()
                .filter(item -> item.getStatus().equals(GlobalEnum.PermPolicyStatus.ACTIVE.getCode()))
                .toList();
        // 提取失效权限策略
        List<SysPermPolicy> invalidPermPolicyList = permPolicyList.stream()
                .filter(item -> item.getStatus().equals(GlobalEnum.PermPolicyStatus.INACTIVE.getCode()))
                .toList();
        // 下面根据 validPermPolicyList invalidPermPolicyList的权限编码进行操作
        // TODO 查询角色信息
        // TODO 发消息 用队列获取到租户信息

        // 权限信息
        UserContextDTO.PermInfo permInfo = new UserContextDTO.PermInfo();
        // 查询权限信息
        List<Long> permIdList = permPolicyList.stream()
                .map(SysPermPolicy::getPermId)
                .distinct()
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
        // 提取有效权限编码
        List<String> validPermCodeList = permList.stream()
                .filter(item -> item.getStatus().equals(GlobalEnum.PermStatus.ENABLED.getCode()))
                .map(SysPerm::getPermCode)
                .toList();
        // 存入权限编码列表 [有效]
        permInfo.setValidPerms(validPermCodeList);
        // 提取失效权限编码
        List<String> invalidPermCodeList = permList.stream()
                .filter(item -> item.getStatus().equals(GlobalEnum.PermStatus.DISABLED.getCode()))
                .map(SysPerm::getPermCode)
                .toList();
        // 存入权限编码列表 [无效]
        permInfo.setInvalidPerm(invalidPermCodeList);

        // 查询类型字段
        Map<String, List<SysPermPolicy>> queryField = validPermPolicyList.stream()
                .filter(item -> item.getAccessType().equals(GlobalEnum.PermPolicyAccessType.QUERY.getCode()))
                .collect(Collectors.groupingBy(SysPermPolicy::getTableName));
        // 转换结构
        Map<String, UserContextDTO.EntityFieldPerm> queryPermField = queryField.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<String> visibleFields = entry.getValue().stream()
                                    .map(SysPermPolicy::getFieldOperates)
                                    .flatMap(item -> {
                                        try {
                                            return new ObjectMapper()
                                                    .readValue(item, new TypeReference<List<String>>() {})
                                                    .stream();
                                        } catch (Exception e) {
                                            // 降级：按逗号分割并清理符号
                                            return Arrays.stream(item.replaceAll("[\\[\\]\"\\s]", "").split(","))
                                                    .map(String::trim)
                                                    .filter(s -> !s.isEmpty());
                                        }
                                    })
                                    .distinct()
                                    .toList();
                            List<String> invisibleFields = entry.getValue().stream()
                                    .map(SysPermPolicy::getFieldUnOperates)
                                    .flatMap(item -> {
                                        try {
                                            return new ObjectMapper()
                                                    .readValue(item, new TypeReference<List<String>>() {})
                                                    .stream();
                                        } catch (Exception e) {
                                            // 降级：按逗号分割并清理符号
                                            return Arrays.stream(item.replaceAll("[\\[\\]\"\\s]", "").split(","))
                                                    .map(String::trim)
                                                    .filter(s -> !s.isEmpty());
                                        }
                                    })
                                    .distinct()
                                    .toList();
                            UserContextDTO.EntityFieldPerm fieldPerm = new UserContextDTO.EntityFieldPerm();
                            fieldPerm.setVisibleFields(visibleFields);
                            fieldPerm.setInvisibleFields(invisibleFields);
                            return fieldPerm;
                        }
                ));
        permInfo.setQuery(queryPermField);

        // 新增类型字段
        Map<String, List<SysPermPolicy>> createField = validPermPolicyList.stream()
                .filter(item -> item.getAccessType().equals(GlobalEnum.PermPolicyAccessType.CREATE.getCode()))
                .collect(Collectors.groupingBy(SysPermPolicy::getTableName));
        // 转换结构
        Map<String, UserContextDTO.EntityFieldPerm> createPermField = createField.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<String> visibleFields = entry.getValue().stream()
                                    .map(SysPermPolicy::getFieldOperates)
                                    .toList();
                            List<String> invisibleFields = entry.getValue().stream()
                                    .map(SysPermPolicy::getFieldUnOperates)
                                    .toList();
                            UserContextDTO.EntityFieldPerm fieldPerm = new UserContextDTO.EntityFieldPerm();
                            fieldPerm.setVisibleFields(visibleFields);
                            fieldPerm.setInvisibleFields(invisibleFields);
                            return fieldPerm;
                        }
                ));
        permInfo.setCreate(createPermField);

        // 更新类型字段
        Map<String, List<SysPermPolicy>> updateField = validPermPolicyList.stream()
                .filter(item -> item.getAccessType().equals(GlobalEnum.PermPolicyAccessType.UPDATE.getCode()))
                .collect(Collectors.groupingBy(SysPermPolicy::getTableName));
        // 转换结构
        Map<String, UserContextDTO.EntityFieldPerm> updatePermField = updateField.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<String> visibleFields = entry.getValue().stream()
                                    .map(SysPermPolicy::getFieldOperates)
                                    .toList();
                            List<String> invisibleFields = entry.getValue().stream()
                                    .map(SysPermPolicy::getFieldUnOperates)
                                    .toList();
                            UserContextDTO.EntityFieldPerm fieldPerm = new UserContextDTO.EntityFieldPerm();
                            fieldPerm.setVisibleFields(visibleFields);
                            fieldPerm.setInvisibleFields(invisibleFields);
                            return fieldPerm;
                        }
                ));
        permInfo.setUpdate(updatePermField);

        // 存入用户上下文
        StpUtil.getSession().set("userContext", permInfo);

        return ApiResponse.success();

    }

}
