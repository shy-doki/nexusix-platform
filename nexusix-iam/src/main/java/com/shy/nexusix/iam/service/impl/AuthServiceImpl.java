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
        UserTenantItemDTO currentTenant = null;
        for (UserTenantItemDTO tenant : tenantList) {
            if (Boolean.TRUE.equals(tenant.getIsPrimary())) {
                currentTenant = tenant;
                break;
            }
        }
        if (currentTenant == null) {
            currentTenant = tenantList.get(0);
        }

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
        // StpUtil.login(user.getId());

        // 更新最后登录时间和IP
        user.setLastLoginAt(LocalDateTime.now());
        // user.setLastLoginIp(获取真实IP); // 从请求中获取
        sysUserMapper.updateById(user);

        // 提取当前租户信息
        String currentTenantCode = currentTenant.getTenantCode();
        String currentTenantName = currentTenant.getTenantName();

        // ==================== 构建权限信息 ====================
        List<UserPermDTO> permList = sysPermPolicyMapper.queryUserAllPermInfo(user.getId());
        UserContextDTO.PermissionInfo permissionInfo = new UserContextDTO.PermissionInfo();

        // 按租户分组权限
        Map<String, List<UserContextDTO.PermItem>> permsByTenant = new LinkedHashMap<>();
        Map<String, String> permTenantNames = new HashMap<>();

        // 当前租户的启用/禁用权限
        List<UserContextDTO.PermItem> currentEnabledPerms = new ArrayList<>();
        List<UserContextDTO.PermItem> currentDisabledPerms = new ArrayList<>();

        // 字段级权限（按租户分组）
        Map<String, Map<String, UserContextDTO.TableFieldPermission>> queryFieldMapByTenant = new HashMap<>();
        Map<String, Map<String, UserContextDTO.TableFieldPermission>> createFieldMapByTenant = new HashMap<>();
        Map<String, Map<String, UserContextDTO.TableFieldPermission>> updateFieldMapByTenant = new HashMap<>();

        // 禁用权限详情（按租户分组）
        Map<String, UserContextDTO.DisabledDetail> disabledDetailByTenant = new HashMap<>();

        // 用于去重的集合（基于 permCode + tenantCode）
        Set<String> permDeduplicationSet = new LinkedHashSet<>();

        // 用于字段权限去重（基于 tenantCode + tableName + fieldName + operation）
        Set<String> fieldOpDeduplicationSet = new LinkedHashSet<>();

        // 遍历所有权限记录
        for (UserPermDTO perm : permList) {
            if (perm.getPermCode() == null || perm.getTenantCode() == null) {
                continue;
            }

            String tenantCode = perm.getTenantCode();
            String tenantName = perm.getTenantName();
            permTenantNames.putIfAbsent(tenantCode, tenantName);

            // 构建权限项并去重
            String deduplicationKey = perm.getPermCode() + "_" + tenantCode;
            if (!permDeduplicationSet.contains(deduplicationKey)) {
                permDeduplicationSet.add(deduplicationKey);

                UserContextDTO.PermItem item = new UserContextDTO.PermItem();
                item.setPermCode(perm.getPermCode());
                item.setPermName(perm.getPermName());
                item.setPermType(perm.getPermType());
                item.setTenantCode(tenantCode);
                item.setTenantName(tenantName);
                item.setPermPolicyStatus(perm.getPermPolicyStatus());

                // 按租户分组
                permsByTenant.computeIfAbsent(tenantCode, k -> new ArrayList<>()).add(item);

                // 当前租户的启用/禁用分组
                if (currentTenantCode.equals(tenantCode)) {
                    if ("ACTIVE".equals(perm.getPermPolicyStatus())) {
                        currentEnabledPerms.add(item);
                    } else {
                        currentDisabledPerms.add(item);
                    }
                }
            }

            // 🔴 收集禁用权限的级别信息
            if (!"ACTIVE".equals(perm.getPermPolicyStatus())) {
                // 获取该租户的DisabledDetail（如果不存在则创建）
                UserContextDTO.DisabledDetail detail = disabledDetailByTenant.get(tenantCode);
                if (detail == null) {
                    detail = new UserContextDTO.DisabledDetail();
                    detail.setSystem(new ArrayList<>());
                    detail.setTenant(new ArrayList<>());
                    detail.setDept(new ArrayList<>());
                    detail.setRole(new ArrayList<>());
                    detail.setUser(new ArrayList<>());
                    disabledDetailByTenant.put(tenantCode, detail);
                }

                String permCode = perm.getPermCode();
                String targetType = perm.getTargetType();

                // 根据策略类型分类禁用权限
                if ("TENANT".equals(targetType)) {
                    if (!detail.getTenant().contains(permCode)) {
                        detail.getTenant().add(permCode);
                    }
                } else if ("DEPT".equals(targetType)) {
                    if (!detail.getDept().contains(permCode)) {
                        detail.getDept().add(permCode);
                    }
                } else if ("ROLE".equals(targetType)) {
                    if (!detail.getRole().contains(permCode)) {
                        detail.getRole().add(permCode);
                    }
                } else if ("USER".equals(targetType)) {
                    if (!detail.getUser().contains(permCode)) {
                        detail.getUser().add(permCode);
                    }
                }
            }

            // 处理字段级权限（处理当前用户下的所有租户）
            boolean isActive = "ACTIVE".equals(perm.getPermPolicyStatus());
            if (perm.getFieldPermissions() != null && !perm.getFieldPermissions().isEmpty()
                && perm.getTableName() != null) {
                try {
                    Map<String, List<String>> fieldMap = JSON.parseObject(perm.getFieldPermissions(), Map.class);
                    if (fieldMap != null && !fieldMap.isEmpty()) {
                        String tableName = perm.getTableName();

                        for (Map.Entry<String, List<String>> entry : fieldMap.entrySet()) {
                            String fieldName = entry.getKey();
                            List<String> operations = entry.getValue();

                            if (operations == null || operations.isEmpty()) {
                                continue;
                            }

                            // 🔴 关键修复：遍历每个操作并去重
                            for (String operation : operations) {
                                // 使用 租户:表名:字段名:操作 作为去重key
                                String fieldOpKey = tenantCode + ":" + tableName + ":" + fieldName + ":" + operation;

                                // 如果已经处理过这个字段+操作组合，跳过
                                if (fieldOpDeduplicationSet.contains(fieldOpKey)) {
                                    continue;
                                }
                                fieldOpDeduplicationSet.add(fieldOpKey);

                                // 处理READ操作
                                if ("READ".equals(operation)) {
                                    Map<String, UserContextDTO.TableFieldPermission> queryFieldMap =
                                        queryFieldMapByTenant.computeIfAbsent(tenantCode, k -> new HashMap<>());
                                    UserContextDTO.TableFieldPermission tablePerm =
                                        queryFieldMap.computeIfAbsent(tableName, k -> new UserContextDTO.TableFieldPermission());

                                    if (!isActive) {
                                        if (!tablePerm.getInoperable().contains(fieldName)) {
                                            tablePerm.getInoperable().add(fieldName);
                                        }
                                        tablePerm.getOperable().remove(fieldName);
                                    } else {
                                        if (!tablePerm.getInoperable().contains(fieldName)
                                            && !tablePerm.getOperable().contains(fieldName)) {
                                            tablePerm.getOperable().add(fieldName);
                                        }
                                    }
                                }

                                // 处理CREATE操作
                                if ("CREATE".equals(operation)) {
                                    Map<String, UserContextDTO.TableFieldPermission> createFieldMap =
                                        createFieldMapByTenant.computeIfAbsent(tenantCode, k -> new HashMap<>());
                                    UserContextDTO.TableFieldPermission tablePerm =
                                        createFieldMap.computeIfAbsent(tableName, k -> new UserContextDTO.TableFieldPermission());

                                    if (!isActive) {
                                        if (!tablePerm.getInoperable().contains(fieldName)) {
                                            tablePerm.getInoperable().add(fieldName);
                                        }
                                        tablePerm.getOperable().remove(fieldName);
                                    } else {
                                        if (!tablePerm.getInoperable().contains(fieldName)
                                            && !tablePerm.getOperable().contains(fieldName)) {
                                            tablePerm.getOperable().add(fieldName);
                                        }
                                    }
                                }

                                // 处理UPDATE操作
                                if ("UPDATE".equals(operation)) {
                                    Map<String, UserContextDTO.TableFieldPermission> updateFieldMap =
                                        updateFieldMapByTenant.computeIfAbsent(tenantCode, k -> new HashMap<>());
                                    UserContextDTO.TableFieldPermission tablePerm =
                                        updateFieldMap.computeIfAbsent(tableName, k -> new UserContextDTO.TableFieldPermission());

                                    if (!isActive) {
                                        if (!tablePerm.getInoperable().contains(fieldName)) {
                                            tablePerm.getInoperable().add(fieldName);
                                        }
                                        tablePerm.getOperable().remove(fieldName);
                                    } else {
                                        if (!tablePerm.getInoperable().contains(fieldName)
                                            && !tablePerm.getOperable().contains(fieldName)) {
                                            tablePerm.getOperable().add(fieldName);
                                        }
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

        // 设置current（启用/禁用分组）
        UserContextDTO.CurrentPermissions currentPerms = new UserContextDTO.CurrentPermissions();
        currentPerms.setEnabled(currentEnabledPerms);
        currentPerms.setDisabled(currentDisabledPerms);
        permissionInfo.setCurrent(currentPerms);

        // 设置all/valid/invalid（按租户分组）
        List<UserContextDTO.TenantPermissions> allTenantPerms = new ArrayList<>();
        List<UserContextDTO.TenantPermissions> validTenantPerms = new ArrayList<>();
        List<UserContextDTO.TenantPermissions> invalidTenantPerms = new ArrayList<>();

        for (Map.Entry<String, List<UserContextDTO.PermItem>> entry : permsByTenant.entrySet()) {
            String tenantCode = entry.getKey();
            String tenantName = permTenantNames.get(tenantCode);
            List<UserContextDTO.PermItem> perms = entry.getValue();

            // all
            UserContextDTO.TenantPermissions allGroup = new UserContextDTO.TenantPermissions();
            allGroup.setTenantCode(tenantCode);
            allGroup.setTenantName(tenantName);
            allGroup.setPermissions(new ArrayList<>(perms));
            allTenantPerms.add(allGroup);

            // valid/invalid分组
            List<UserContextDTO.PermItem> validPerms = new ArrayList<>();
            List<UserContextDTO.PermItem> invalidPerms = new ArrayList<>();
            for (UserContextDTO.PermItem perm : perms) {
                if ("ACTIVE".equals(perm.getPermPolicyStatus())) {
                    validPerms.add(perm);
                } else {
                    invalidPerms.add(perm);
                }
            }

            if (!validPerms.isEmpty()) {
                UserContextDTO.TenantPermissions validGroup = new UserContextDTO.TenantPermissions();
                validGroup.setTenantCode(tenantCode);
                validGroup.setTenantName(tenantName);
                validGroup.setPermissions(validPerms);
                validTenantPerms.add(validGroup);
            }

            if (!invalidPerms.isEmpty()) {
                UserContextDTO.TenantPermissions invalidGroup = new UserContextDTO.TenantPermissions();
                invalidGroup.setTenantCode(tenantCode);
                invalidGroup.setTenantName(tenantName);
                invalidGroup.setPermissions(invalidPerms);
                invalidTenantPerms.add(invalidGroup);
            }
        }

        permissionInfo.setAll(allTenantPerms);
        permissionInfo.setValid(validTenantPerms);
        permissionInfo.setInvalid(invalidTenantPerms);

        // 设置字段级权限（按租户分组，包含所有租户）
        Map<String, UserContextDTO.FieldPermission> fieldPermByTenant = new HashMap<>();
        for (String tenantCode : permsByTenant.keySet()) {
            UserContextDTO.FieldPermission fp = new UserContextDTO.FieldPermission();
            fp.setQuery(queryFieldMapByTenant.getOrDefault(tenantCode, new HashMap<>()));
            fp.setCreate(createFieldMapByTenant.getOrDefault(tenantCode, new HashMap<>()));
            fp.setUpdate(updateFieldMapByTenant.getOrDefault(tenantCode, new HashMap<>()));
            fieldPermByTenant.put(tenantCode, fp);
        }
        permissionInfo.setFieldPermissionByTenant(fieldPermByTenant);

        // 设置禁用详情（确保所有租户都有完整结构）
        for (String tenantCode : permsByTenant.keySet()) {
            if (!disabledDetailByTenant.containsKey(tenantCode)) {
                UserContextDTO.DisabledDetail detail = new UserContextDTO.DisabledDetail();
                detail.setSystem(new ArrayList<>());
                detail.setTenant(new ArrayList<>());
                detail.setDept(new ArrayList<>());
                detail.setRole(new ArrayList<>());
                detail.setUser(new ArrayList<>());
                disabledDetailByTenant.put(tenantCode, detail);
            }
        }
        permissionInfo.setDisabledDetailByTenant(disabledDetailByTenant);

        // ==================== 构建部门信息 ====================
        List<UserDeptDTO> deptList = sysUserPolicyMapper.queryUserAllDeptInfo(user.getId());
        UserContextDTO.DeptGroup deptGroup = new UserContextDTO.DeptGroup();

        // 按租户分组部门
        Map<String, List<UserContextDTO.DeptItem>> deptsByTenant = new LinkedHashMap<>();
        Map<String, String> deptTenantNames = new HashMap<>();

        // 当前租户的启用/禁用部门
        List<UserContextDTO.DeptItem> currentEnabledDepts = new ArrayList<>();
        List<UserContextDTO.DeptItem> currentDisabledDepts = new ArrayList<>();

        for (UserDeptDTO dto : deptList) {
            if (dto.getDeptCode() == null || dto.getTenantCode() == null) {
                continue;
            }

            String tenantCode = dto.getTenantCode();
            String tenantName = dto.getTenantName();
            deptTenantNames.putIfAbsent(tenantCode, tenantName);

            UserContextDTO.DeptItem item = new UserContextDTO.DeptItem();
            item.setDeptCode(dto.getDeptCode());
            item.setDeptName(dto.getDeptName());
            item.setPath(dto.getPath());
            item.setLevel(dto.getLevel());
            item.setTenantCode(tenantCode);
            item.setTenantName(tenantName);
            item.setIsPrimary(dto.getIsPrimary());
            item.setUserPolicyStatus(dto.getUserPolicyStatus());

            // 按租户分组
            deptsByTenant.computeIfAbsent(tenantCode, k -> new ArrayList<>()).add(item);

            // 当前租户的启用/禁用分组
            if (currentTenantCode.equals(tenantCode)) {
                if ("ACTIVE".equals(dto.getUserPolicyStatus())) {
                    currentEnabledDepts.add(item);
                } else {
                    currentDisabledDepts.add(item);
                }
            }
        }

        // 设置current
        UserContextDTO.CurrentDepts currentDepts = new UserContextDTO.CurrentDepts();
        currentDepts.setEnabled(currentEnabledDepts);
        currentDepts.setDisabled(currentDisabledDepts);
        deptGroup.setCurrent(currentDepts);

        // 设置all/valid/invalid（按租户分组）
        List<UserContextDTO.TenantDepts> allTenantDepts = new ArrayList<>();
        List<UserContextDTO.TenantDepts> validTenantDepts = new ArrayList<>();
        List<UserContextDTO.TenantDepts> invalidTenantDepts = new ArrayList<>();

        for (Map.Entry<String, List<UserContextDTO.DeptItem>> entry : deptsByTenant.entrySet()) {
            String tenantCode = entry.getKey();
            String tenantName = deptTenantNames.get(tenantCode);
            List<UserContextDTO.DeptItem> depts = entry.getValue();

            // all
            UserContextDTO.TenantDepts allGroup = new UserContextDTO.TenantDepts();
            allGroup.setTenantCode(tenantCode);
            allGroup.setTenantName(tenantName);
            allGroup.setDepts(new ArrayList<>(depts));
            allTenantDepts.add(allGroup);

            // valid/invalid分组
            List<UserContextDTO.DeptItem> validDepts = new ArrayList<>();
            List<UserContextDTO.DeptItem> invalidDepts = new ArrayList<>();
            for (UserContextDTO.DeptItem dept : depts) {
                if ("ACTIVE".equals(dept.getUserPolicyStatus())) {
                    validDepts.add(dept);
                } else {
                    invalidDepts.add(dept);
                }
            }

            if (!validDepts.isEmpty()) {
                UserContextDTO.TenantDepts validGroup = new UserContextDTO.TenantDepts();
                validGroup.setTenantCode(tenantCode);
                validGroup.setTenantName(tenantName);
                validGroup.setDepts(validDepts);
                validTenantDepts.add(validGroup);
            }

            if (!invalidDepts.isEmpty()) {
                UserContextDTO.TenantDepts invalidGroup = new UserContextDTO.TenantDepts();
                invalidGroup.setTenantCode(tenantCode);
                invalidGroup.setTenantName(tenantName);
                invalidGroup.setDepts(invalidDepts);
                invalidTenantDepts.add(invalidGroup);
            }
        }

        deptGroup.setAll(allTenantDepts);
        deptGroup.setValid(validTenantDepts);
        deptGroup.setInvalid(invalidTenantDepts);

        // ==================== 构建角色信息 ====================
        List<UserRoleDTO> roleList = sysUserPolicyMapper.queryUserAllRoleInfo(user.getId());
        UserContextDTO.RoleGroup roleGroup = new UserContextDTO.RoleGroup();

        // 按租户分组角色
        Map<String, List<UserContextDTO.RoleItem>> rolesByTenant = new LinkedHashMap<>();
        Map<String, String> roleTenantNames = new HashMap<>();
        Map<String, Map<String, String>> roleStatusByTenant = new HashMap<>(); // 存储角色状态

        // 当前租户的启用/禁用角色
        List<UserContextDTO.RoleItem> currentEnabledRoles = new ArrayList<>();
        List<UserContextDTO.RoleItem> currentDisabledRoles = new ArrayList<>();

        for (UserRoleDTO dto : roleList) {
            if (dto.getRoleCode() == null || dto.getTenantCode() == null) {
                continue;
            }

            String tenantCode = dto.getTenantCode();
            String tenantName = dto.getTenantName();
            String roleCode = dto.getRoleCode();
            String status = dto.getRolePolicyStatus();

            roleTenantNames.putIfAbsent(tenantCode, tenantName);

            // 存储角色状态
            roleStatusByTenant.computeIfAbsent(tenantCode, k -> new HashMap<>()).put(roleCode, status);

            UserContextDTO.RoleItem item = new UserContextDTO.RoleItem();
            item.setRoleCode(roleCode);
            item.setRoleName(dto.getRoleName());
            item.setDataScope(dto.getDataScope() != null ? dto.getDataScope() : "SELF");
            item.setTenantCode(tenantCode);
            item.setTenantName(tenantName);

            // 按租户分组
            rolesByTenant.computeIfAbsent(tenantCode, k -> new ArrayList<>()).add(item);

            // 当前租户的启用/禁用分组
            if (currentTenantCode.equals(tenantCode)) {
                if ("ACTIVE".equals(status)) {
                    currentEnabledRoles.add(item);
                } else {
                    currentDisabledRoles.add(item);
                }
            }
        }

        // 设置current
        UserContextDTO.CurrentRoles currentRoles = new UserContextDTO.CurrentRoles();
        currentRoles.setEnabled(currentEnabledRoles);
        currentRoles.setDisabled(currentDisabledRoles);
        roleGroup.setCurrent(currentRoles);

        // 设置all/valid/invalid（按租户分组）
        List<UserContextDTO.TenantRoles> allTenantRoles = new ArrayList<>();
        List<UserContextDTO.TenantRoles> validTenantRoles = new ArrayList<>();
        List<UserContextDTO.TenantRoles> invalidTenantRoles = new ArrayList<>();

        for (Map.Entry<String, List<UserContextDTO.RoleItem>> entry : rolesByTenant.entrySet()) {
            String tenantCode = entry.getKey();
            String tenantName = roleTenantNames.get(tenantCode);
            List<UserContextDTO.RoleItem> roles = entry.getValue();
            Map<String, String> statusMap = roleStatusByTenant.get(tenantCode);

            // all
            UserContextDTO.TenantRoles allGroup = new UserContextDTO.TenantRoles();
            allGroup.setTenantCode(tenantCode);
            allGroup.setTenantName(tenantName);
            allGroup.setRoles(new ArrayList<>(roles));
            allTenantRoles.add(allGroup);

            // valid/invalid分组
            List<UserContextDTO.RoleItem> validRoles = new ArrayList<>();
            List<UserContextDTO.RoleItem> invalidRoles = new ArrayList<>();
            for (UserContextDTO.RoleItem role : roles) {
                String status = statusMap.get(role.getRoleCode());
                if ("ACTIVE".equals(status)) {
                    validRoles.add(role);
                } else {
                    invalidRoles.add(role);
                }
            }

            if (!validRoles.isEmpty()) {
                UserContextDTO.TenantRoles validGroup = new UserContextDTO.TenantRoles();
                validGroup.setTenantCode(tenantCode);
                validGroup.setTenantName(tenantName);
                validGroup.setRoles(validRoles);
                validTenantRoles.add(validGroup);
            }

            if (!invalidRoles.isEmpty()) {
                UserContextDTO.TenantRoles invalidGroup = new UserContextDTO.TenantRoles();
                invalidGroup.setTenantCode(tenantCode);
                invalidGroup.setTenantName(tenantName);
                invalidGroup.setRoles(invalidRoles);
                invalidTenantRoles.add(invalidGroup);
            }
        }

        roleGroup.setAll(allTenantRoles);
        roleGroup.setValid(validTenantRoles);
        roleGroup.setInvalid(invalidTenantRoles);

        // ==================== 构建租户信息 ====================
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

            if (dto.getTenantCode().equals(currentTenant.getTenantCode())) {
                currentTenantList.add(item);
            }

            if ("ACTIVE".equals(dto.getStatus())) {
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

        // 设置各分组信息
        userContext.setTenantInfo(tenantGroup);
        userContext.setPermInfo(permissionInfo);
        userContext.setRoleInfo(roleGroup);
        userContext.setDeptInfo(deptGroup);

        // 存入Session
        // StpUtil.getSession().set(GlobalConstant.Session.USER_CONTEXT, userContext);

        return ApiResponse.success("登录成功", userContext);
    }

}
