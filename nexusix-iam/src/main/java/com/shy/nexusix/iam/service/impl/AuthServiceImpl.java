package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.iam.dto.UserDeptDTO;
import com.shy.nexusix.iam.dto.UserPermDTO;
import com.shy.nexusix.iam.dto.UserRoleDTO;
import com.shy.nexusix.iam.dto.UserTenantItemDTO;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.mapper.SysPermPolicyMapper;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.mapper.SysUserPolicyMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>认证服务实现类</p>
 *
 * @author shy
 * @since 2026-06-12
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserPolicyMapper sysUserPolicyMapper;

    @Autowired
    private SysPermPolicyMapper sysPermPolicyMapper;

    /**
     * 用户登录
     * <p>
     * 执行用户认证流程，包括：用户校验、租户验证、权限查询、上下文构建
     * </p>
     *
     * @param param 登录请求参数（用户名、密码）
     * @return 包含用户上下文信息的响应
     * @throws BusinessException 当用户不存在、已禁用、未关联租户或租户已禁用时抛出
     */
    @Override
    public ApiResponse login(LoginRTO param) {

        // 检查当前用户是否已经登录
        if (StpUtil.isLogin(param.getUsername())) {
            return ApiResponse.success("用户已登录");
        }

        // 校验用户名是否存在
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, param.getUsername())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 校验用户状态
        if ("DISABLED".equals(user.getStatus())) {
            throw new BusinessException("用户已被禁用");
        }
        if ("LOCKED".equals(user.getStatus())) {
            throw new BusinessException("用户已被锁定");
        }

        // TODO: 校验密码是否匹配（后续使用加密对比）
        // if (!passwordEncoder.matches(param.getPassword(), user.getPassword())) {
        //     throw new BusinessException("密码错误");
        // }

        // 查询用户所有租户信息
        List<UserTenantItemDTO> tenantList = sysUserPolicyMapper.queryUserAllTenantInfo(user.getId());

        // 校验无租户关联则不允许登录
        if (tenantList == null || tenantList.isEmpty()) {
            throw new BusinessException("用户未关联任何租户，无法登录");
        }

        // 确定当前登录租户（优先主租户）
        UserTenantItemDTO currentTenant = tenantList.stream()
                .filter(t -> Boolean.TRUE.equals(t.getIsPrimary()))
                .findFirst()
                .orElse(tenantList.get(0));

        // 校验当前租户是否处于停用状态
        if ("DISABLED".equals(currentTenant.getStatus())) {
            throw new BusinessException("当前租户已禁用，无法登录");
        }

        // 校验当前租户是否已过期
        if (currentTenant.getExpireTime() != null
            && LocalDateTime.now().isAfter(currentTenant.getExpireTime())) {
            throw new BusinessException("租户服务已过期，无法登录");
        }

        // 执行登录操作
        StpUtil.login(user.getId());

        // 更新最后登录时间和IP
        user.setLastLoginAt(LocalDateTime.now());
        // user.setLastLoginIp(获取真实IP); // 从请求中获取
        sysUserMapper.updateById(user);

        // 查询用户所有权限信息
        List<UserPermDTO> permList = sysPermPolicyMapper.queryUserAllPermInfo(user.getId());

        // 构建权限信息对象
        UserContextDTO.PermissionInfo permissionInfo = new UserContextDTO.PermissionInfo();
        Set<String> allPermCodeSet = new LinkedHashSet<>();
        List<String> validPermCodeList = new ArrayList<>();
        List<String> invalidPermCodeList = new ArrayList<>();

        // 字段级权限映射，按操作类型分组（查询/创建/更新）
        Map<String, UserContextDTO.TableFieldPermission> queryFieldMap = new HashMap<>();
        Map<String, UserContextDTO.TableFieldPermission> createFieldMap = new HashMap<>();
        Map<String, UserContextDTO.TableFieldPermission> updateFieldMap = new HashMap<>();

        // 遍历所有权限记录，提取权限编码和字段级权限
        for (UserPermDTO perm : permList) {
            if (perm.getPermCode() == null) {
                continue;
            }

            // 权限编码去重
            allPermCodeSet.add(perm.getPermCode());

            // 根据权限策略状态分类权限编码
            if ("ACTIVE".equals(perm.getPermPolicyStatus())) {
                validPermCodeList.add(perm.getPermCode());
            } else {
                invalidPermCodeList.add(perm.getPermCode());
            }

            // 解析字段级权限（JSON格式：{"user_name":["READ","CREATE","UPDATE"], "email":["READ","UPDATE"]}）
            if (perm.getFieldPermissions() != null && !perm.getFieldPermissions().isEmpty()
                && perm.getTableName() != null) {
                try {
                    Map<String, List<String>> fieldMap = JSON.parseObject(
                        perm.getFieldPermissions(),
                        Map.class
                    );

                    if (fieldMap != null && !fieldMap.isEmpty()) {
                        // 遍历每个字段及其操作类型
                        for (Map.Entry<String, List<String>> entry : fieldMap.entrySet()) {
                            String fieldName = entry.getKey();
                            List<String> operations = entry.getValue();

                            if (operations == null || operations.isEmpty()) {
                                continue;
                            }

                            boolean isActive = "ACTIVE".equals(perm.getPermPolicyStatus());

                            // 处理READ操作，映射到查询权限
                            if (operations.contains("READ")) {
                                UserContextDTO.TableFieldPermission tableFieldPerm =
                                    queryFieldMap.computeIfAbsent(perm.getTableName(), k -> new UserContextDTO.TableFieldPermission());

                                // 黑名单机制：只要有一个来源是DISABLED，字段就不可操作
                                if (!isActive) {
                                    if (!tableFieldPerm.getInoperable().contains(fieldName)) {
                                        tableFieldPerm.getInoperable().add(fieldName);
                                    }
                                    tableFieldPerm.getOperable().remove(fieldName);
                                } else {
                                    if (!tableFieldPerm.getInoperable().contains(fieldName)
                                        && !tableFieldPerm.getOperable().contains(fieldName)) {
                                        tableFieldPerm.getOperable().add(fieldName);
                                    }
                                }
                            }

                            // 处理CREATE操作，映射到创建权限
                            if (operations.contains("CREATE")) {
                                UserContextDTO.TableFieldPermission tableFieldPerm =
                                    createFieldMap.computeIfAbsent(perm.getTableName(), k -> new UserContextDTO.TableFieldPermission());

                                if (!isActive) {
                                    if (!tableFieldPerm.getInoperable().contains(fieldName)) {
                                        tableFieldPerm.getInoperable().add(fieldName);
                                    }
                                    tableFieldPerm.getOperable().remove(fieldName);
                                } else {
                                    if (!tableFieldPerm.getInoperable().contains(fieldName)
                                        && !tableFieldPerm.getOperable().contains(fieldName)) {
                                        tableFieldPerm.getOperable().add(fieldName);
                                    }
                                }
                            }

                            // 处理UPDATE操作，映射到更新权限
                            if (operations.contains("UPDATE")) {
                                UserContextDTO.TableFieldPermission tableFieldPerm =
                                    updateFieldMap.computeIfAbsent(perm.getTableName(), k -> new UserContextDTO.TableFieldPermission());

                                if (!isActive) {
                                    if (!tableFieldPerm.getInoperable().contains(fieldName)) {
                                        tableFieldPerm.getInoperable().add(fieldName);
                                    }
                                    tableFieldPerm.getOperable().remove(fieldName);
                                } else {
                                    if (!tableFieldPerm.getInoperable().contains(fieldName)
                                        && !tableFieldPerm.getOperable().contains(fieldName)) {
                                        tableFieldPerm.getOperable().add(fieldName);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // 解析失败，忽略该字段权限
                }
            }
        }

        // 设置权限编码列表
        permissionInfo.setAll(new ArrayList<>(allPermCodeSet));
        permissionInfo.setValid(validPermCodeList);
        permissionInfo.setInvalid(invalidPermCodeList);

        // 设置字段级权限
        UserContextDTO.FieldPermission fieldPermission = new UserContextDTO.FieldPermission();
        fieldPermission.setQuery(queryFieldMap);
        fieldPermission.setCreate(createFieldMap);
        fieldPermission.setUpdate(updateFieldMap);
        permissionInfo.setFieldPermission(fieldPermission);

        // 初始化禁用详情（初始化为空）
        UserContextDTO.DisabledDetail disabledDetail = new UserContextDTO.DisabledDetail();
        disabledDetail.setSystem(new ArrayList<>());
        disabledDetail.setTenant(new ArrayList<>());
        disabledDetail.setRole(new ArrayList<>());
        disabledDetail.setUser(new ArrayList<>());
        permissionInfo.setDisabledDetail(disabledDetail);

        // 查询用户所有部门信息
        List<UserDeptDTO> deptList = sysUserPolicyMapper.queryUserAllDeptInfo(user.getId());

        // 构建部门分组信息
        UserContextDTO.DeptGroup deptGroup = new UserContextDTO.DeptGroup();
        List<UserDeptDTO> validDepts = deptList.stream()
                .filter(d -> d.getDeptCode() != null)
                .collect(Collectors.toList());

        // 当前租户的部门
        List<UserContextDTO.DeptItem> currentDepts = validDepts.stream()
                .filter(d -> currentTenant.getTenantCode() != null && currentTenant.getTenantCode().equals(d.getTenantCode()))
                .map(dto -> {
                    UserContextDTO.DeptItem item = new UserContextDTO.DeptItem();
                    item.setDeptCode(dto.getDeptCode());
                    item.setDeptName(dto.getDeptName());
                    item.setPath(dto.getPath());
                    item.setLevel(dto.getLevel());
                    item.setTenantCode(dto.getTenantCode());
                    item.setTenantName(dto.getTenantName());
                    item.setIsPrimary(dto.getIsPrimary());
                    item.setUserPolicyStatus(dto.getUserPolicyStatus());
                    return item;
                })
                .collect(Collectors.toList());
        deptGroup.setCurrent(currentDepts);

        // 全部部门
        List<UserContextDTO.DeptItem> allDepts = validDepts.stream()
                .map(dto -> {
                    UserContextDTO.DeptItem item = new UserContextDTO.DeptItem();
                    item.setDeptCode(dto.getDeptCode());
                    item.setDeptName(dto.getDeptName());
                    item.setPath(dto.getPath());
                    item.setLevel(dto.getLevel());
                    item.setTenantCode(dto.getTenantCode());
                    item.setTenantName(dto.getTenantName());
                    item.setIsPrimary(dto.getIsPrimary());
                    item.setUserPolicyStatus(dto.getUserPolicyStatus());
                    return item;
                })
                .collect(Collectors.toList());
        deptGroup.setAll(allDepts);

        // 有效部门（状态为ACTIVE）
        List<UserContextDTO.DeptItem> validDeptItems = validDepts.stream()
                .filter(d -> "ACTIVE".equals(d.getUserPolicyStatus()))
                .map(dto -> {
                    UserContextDTO.DeptItem item = new UserContextDTO.DeptItem();
                    item.setDeptCode(dto.getDeptCode());
                    item.setDeptName(dto.getDeptName());
                    item.setPath(dto.getPath());
                    item.setLevel(dto.getLevel());
                    item.setTenantCode(dto.getTenantCode());
                    item.setTenantName(dto.getTenantName());
                    item.setIsPrimary(dto.getIsPrimary());
                    item.setUserPolicyStatus(dto.getUserPolicyStatus());
                    return item;
                })
                .collect(Collectors.toList());
        deptGroup.setValid(validDeptItems);

        // 无效部门（状态非ACTIVE）
        List<UserContextDTO.DeptItem> invalidDeptItems = validDepts.stream()
                .filter(d -> !"ACTIVE".equals(d.getUserPolicyStatus()))
                .map(dto -> {
                    UserContextDTO.DeptItem item = new UserContextDTO.DeptItem();
                    item.setDeptCode(dto.getDeptCode());
                    item.setDeptName(dto.getDeptName());
                    item.setPath(dto.getPath());
                    item.setLevel(dto.getLevel());
                    item.setTenantCode(dto.getTenantCode());
                    item.setTenantName(dto.getTenantName());
                    item.setIsPrimary(dto.getIsPrimary());
                    item.setUserPolicyStatus(dto.getUserPolicyStatus());
                    return item;
                })
                .collect(Collectors.toList());
        deptGroup.setInvalid(invalidDeptItems);

        // 查询用户所有角色信息
        List<UserRoleDTO> roleList = sysUserPolicyMapper.queryUserAllRoleInfo(user.getId());

        // 构建角色分组信息
        UserContextDTO.RoleGroup roleGroup = new UserContextDTO.RoleGroup();
        List<UserRoleDTO> validRoles = roleList.stream()
                .filter(r -> r.getRoleCode() != null)
                .collect(Collectors.toList());

        // 当前租户的角色
        List<UserContextDTO.RoleItem> currentRoles = validRoles.stream()
                .filter(r -> currentTenant.getTenantCode() != null && currentTenant.getTenantCode().equals(r.getTenantCode()))
                .map(dto -> {
                    UserContextDTO.RoleItem item = new UserContextDTO.RoleItem();
                    item.setRoleCode(dto.getRoleCode());
                    item.setDataScope(dto.getDataScope() != null ? dto.getDataScope() : "SELF");
                    item.setTenantCode(dto.getTenantCode());
                    item.setTenantName(dto.getTenantName());
                    return item;
                })
                .collect(Collectors.toList());
        roleGroup.setCurrent(currentRoles);

        // 全部角色
        List<UserContextDTO.RoleItem> allRoles = validRoles.stream()
                .map(dto -> {
                    UserContextDTO.RoleItem item = new UserContextDTO.RoleItem();
                    item.setRoleCode(dto.getRoleCode());
                    item.setDataScope(dto.getDataScope() != null ? dto.getDataScope() : "SELF");
                    item.setTenantCode(dto.getTenantCode());
                    item.setTenantName(dto.getTenantName());
                    return item;
                })
                .collect(Collectors.toList());
        roleGroup.setAll(allRoles);

        // 有效角色（状态为ACTIVE）
        List<UserContextDTO.RoleItem> validRoleItems = validRoles.stream()
                .filter(r -> "ACTIVE".equals(r.getRolePolicyStatus()))
                .map(dto -> {
                    UserContextDTO.RoleItem item = new UserContextDTO.RoleItem();
                    item.setRoleCode(dto.getRoleCode());
                    item.setDataScope(dto.getDataScope() != null ? dto.getDataScope() : "SELF");
                    item.setTenantCode(dto.getTenantCode());
                    item.setTenantName(dto.getTenantName());
                    return item;
                })
                .collect(Collectors.toList());
        roleGroup.setValid(validRoleItems);

        // 无效角色（状态非ACTIVE）
        List<UserContextDTO.RoleItem> invalidRoleItems = validRoles.stream()
                .filter(r -> !"ACTIVE".equals(r.getRolePolicyStatus()))
                .map(dto -> {
                    UserContextDTO.RoleItem item = new UserContextDTO.RoleItem();
                    item.setRoleCode(dto.getRoleCode());
                    item.setDataScope(dto.getDataScope() != null ? dto.getDataScope() : "SELF");
                    item.setTenantCode(dto.getTenantCode());
                    item.setTenantName(dto.getTenantName());
                    return item;
                })
                .collect(Collectors.toList());
        roleGroup.setInvalid(invalidRoleItems);

        // 构建租户分组信息
        UserContextDTO.TenantGroup tenantGroup = new UserContextDTO.TenantGroup();
        List<UserContextDTO.TenantItem> currentTenantList = new ArrayList<>();
        List<UserContextDTO.TenantItem> allTenants = new ArrayList<>();
        List<UserContextDTO.TenantItem> validTenants = new ArrayList<>();
        List<UserContextDTO.TenantItem> invalidTenants = new ArrayList<>();

        for (UserTenantItemDTO dto : tenantList) {
            UserContextDTO.TenantItem item = new UserContextDTO.TenantItem();
            item.setTenantCode(dto.getTenantCode());
            item.setTenantName(dto.getTenantName());
            item.setStatus(dto.getStatus());
            item.setExpireTime(dto.getExpireTime() != null ? dto.getExpireTime().toString() : null);
            item.setIsPrimary(dto.getIsPrimary());

            allTenants.add(item);

            // 判断是否是当前租户
            if (dto.getTenantCode().equals(currentTenant.getTenantCode())) {
                currentTenantList.add(item);
            }

            // 分类为有效或无效
            if ("ENABLED".equals(dto.getStatus())) {
                validTenants.add(item);
            } else {
                invalidTenants.add(item);
            }
        }

        tenantGroup.setCurrent(currentTenantList);
        tenantGroup.setAll(allTenants);
        tenantGroup.setValid(validTenants);
        tenantGroup.setInvalid(invalidTenants);

        // 组装用户上下文对象
        UserContextDTO userContext = new UserContextDTO();

        // 设置用户基本信息
        UserContextDTO.UserInfo userInfo = new UserContextDTO.UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setUserCode(user.getUserCode());
        userInfo.setUserName(user.getUserName());
        userInfo.setNickName(user.getNickName());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvatar(user.getAvatarUrl());
        userContext.setUserInfo(userInfo);

        // 设置租户分组
        userContext.setTenantInfo(tenantGroup);

        // 设置权限信息
        userContext.setPermInfo(permissionInfo);

        // 设置角色分组
        userContext.setRoleInfo(roleGroup);

        // 设置部门分组
        userContext.setDeptInfo(deptGroup);

        // 存入Session
        StpUtil.getSession().set(GlobalConstant.Session.USER_CONTEXT, userContext);

        return ApiResponse.success("登录成功", userContext);
    }

}
