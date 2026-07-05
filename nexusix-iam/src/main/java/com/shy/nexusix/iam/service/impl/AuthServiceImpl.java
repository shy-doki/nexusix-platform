package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.core.entity.dto.UserContextDTO.CurrentDepts;
import com.shy.nexusix.core.entity.dto.UserContextDTO.CurrentPermissions;
import com.shy.nexusix.core.entity.dto.UserContextDTO.CurrentRoles;
import com.shy.nexusix.core.entity.dto.UserContextDTO.DeptGroup;
import com.shy.nexusix.core.entity.dto.UserContextDTO.DeptItem;
import com.shy.nexusix.core.entity.dto.UserContextDTO.DisabledDetail;
import com.shy.nexusix.core.entity.dto.UserContextDTO.FieldPermission;
import com.shy.nexusix.core.entity.dto.UserContextDTO.PermItem;
import com.shy.nexusix.core.entity.dto.UserContextDTO.PermissionInfo;
import com.shy.nexusix.core.entity.dto.UserContextDTO.RoleGroup;
import com.shy.nexusix.core.entity.dto.UserContextDTO.RoleItem;
import com.shy.nexusix.core.entity.dto.UserContextDTO.TableFieldPermission;
import com.shy.nexusix.core.entity.dto.UserContextDTO.TenantDepts;
import com.shy.nexusix.core.entity.dto.UserContextDTO.TenantGroup;
import com.shy.nexusix.core.entity.dto.UserContextDTO.TenantItem;
import com.shy.nexusix.core.entity.dto.UserContextDTO.TenantPermissions;
import com.shy.nexusix.core.entity.dto.UserContextDTO.TenantRoles;
import com.shy.nexusix.core.entity.dto.UserContextDTO.UserInfo;
import com.shy.nexusix.iam.dto.UserDeptDTO;
import com.shy.nexusix.iam.dto.UserPermDTO;
import com.shy.nexusix.iam.dto.UserRoleDTO;
import com.shy.nexusix.iam.dto.UserTenantItemDTO;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.entity.SysUserPolicy;
import com.shy.nexusix.iam.mapper.SysPermPolicyMapper;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.mapper.SysUserPolicyMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.rto.TenantRegisterRTO;
import com.shy.nexusix.iam.rto.UserRegisterRTO;
import com.shy.nexusix.iam.service.IAuthService;
import com.shy.nexusix.iam.vo.RegisterVO;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.mapper.SysTenantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>认证服务实现类</p>
 *
 * @author shy
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserPolicyMapper sysUserPolicyMapper;

    @Autowired
    private SysPermPolicyMapper sysPermPolicyMapper;

    @Autowired
    private SysTenantMapper sysTenantMapper;

    // 密码编码器 暂无Spring Security Bean配置 直接实例化（与SysUserServiceImpl保持一致）
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

        // 使用 BCrypt 校验密码（数据库存储的是 BCrypt 哈希）
        if (!passwordEncoder.matches(param.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

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
        StpUtil.login(user.getId());

        // 更新最后登录时间和IP
        user.setLastLoginAt(LocalDateTime.now());
        // user.setLastLoginIp(获取真实IP)
        sysUserMapper.updateById(user);

        // 提取当前租户信息
        String currentTenantCode = currentTenant.getTenantCode();
        String currentTenantName = currentTenant.getTenantName();

        // 构建权限信息
        List<UserPermDTO> permList = sysPermPolicyMapper.queryUserAllPermInfo(user.getId());
        PermissionInfo permissionInfo = new PermissionInfo();

        // 按租户分组权限
        Map<String, List<PermItem>> permsByTenant = new LinkedHashMap<>();
        Map<String, String> permTenantNames = new HashMap<>();

        // 当前租户的启用/禁用权限
        List<PermItem> currentEnabledPerms = new ArrayList<>();
        List<PermItem> currentDisabledPerms = new ArrayList<>();

        // 字段级权限（按租户分组）
        Map<String, Map<String, TableFieldPermission>> queryFieldMapByTenant = new HashMap<>();
        Map<String, Map<String, TableFieldPermission>> createFieldMapByTenant = new HashMap<>();
        Map<String, Map<String, TableFieldPermission>> updateFieldMapByTenant = new HashMap<>();

        // 禁用权限详情（按租户分组）
        Map<String, DisabledDetail> disabledDetailByTenant = new HashMap<>();

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

                PermItem item = new PermItem();
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

            // 收集禁用权限的级别信息
            if (!"ACTIVE".equals(perm.getPermPolicyStatus())) {
                // 获取该租户的DisabledDetail（如果不存在则创建）
                DisabledDetail detail = disabledDetailByTenant.get(tenantCode);
                if (detail == null) {
                    detail = new DisabledDetail();
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

                            // 遍历每个操作并去重
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
                                    Map<String, TableFieldPermission> queryFieldMap =
                                        queryFieldMapByTenant.computeIfAbsent(tenantCode, k -> new HashMap<>());
                                    TableFieldPermission tablePerm =
                                        queryFieldMap.computeIfAbsent(tableName, k -> new TableFieldPermission());

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
                                    Map<String, TableFieldPermission> createFieldMap =
                                        createFieldMapByTenant.computeIfAbsent(tenantCode, k -> new HashMap<>());
                                    TableFieldPermission tablePerm =
                                        createFieldMap.computeIfAbsent(tableName, k -> new TableFieldPermission());

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
                                    Map<String, TableFieldPermission> updateFieldMap =
                                        updateFieldMapByTenant.computeIfAbsent(tenantCode, k -> new HashMap<>());
                                    TableFieldPermission tablePerm =
                                        updateFieldMap.computeIfAbsent(tableName, k -> new TableFieldPermission());

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
        CurrentPermissions currentPerms = new CurrentPermissions();
        currentPerms.setEnabled(currentEnabledPerms);
        currentPerms.setDisabled(currentDisabledPerms);
        permissionInfo.setCurrent(currentPerms);

        // 设置所有/有效/无效（按租户分组）
        List<TenantPermissions> allTenantPerms = new ArrayList<>();
        List<TenantPermissions> validTenantPerms = new ArrayList<>();
        List<TenantPermissions> invalidTenantPerms = new ArrayList<>();

        for (Map.Entry<String, List<PermItem>> entry : permsByTenant.entrySet()) {
            String tenantCode = entry.getKey();
            String tenantName = permTenantNames.get(tenantCode);
            List<PermItem> perms = entry.getValue();

            // 所有
            TenantPermissions allGroup = new TenantPermissions();
            allGroup.setTenantCode(tenantCode);
            allGroup.setTenantName(tenantName);
            allGroup.setPermissions(new ArrayList<>(perms));
            allTenantPerms.add(allGroup);

            // 有效/无效分组
            List<PermItem> validPerms = new ArrayList<>();
            List<PermItem> invalidPerms = new ArrayList<>();
            for (PermItem perm : perms) {
                if ("ACTIVE".equals(perm.getPermPolicyStatus())) {
                    validPerms.add(perm);
                } else {
                    invalidPerms.add(perm);
                }
            }

            if (!validPerms.isEmpty()) {
                TenantPermissions validGroup = new TenantPermissions();
                validGroup.setTenantCode(tenantCode);
                validGroup.setTenantName(tenantName);
                validGroup.setPermissions(validPerms);
                validTenantPerms.add(validGroup);
            }

            if (!invalidPerms.isEmpty()) {
                TenantPermissions invalidGroup = new TenantPermissions();
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
        Map<String, FieldPermission> fieldPermByTenant = new HashMap<>();
        for (String tenantCode : permsByTenant.keySet()) {
            FieldPermission fp = new FieldPermission();
            fp.setQuery(queryFieldMapByTenant.getOrDefault(tenantCode, new HashMap<>()));
            fp.setCreate(createFieldMapByTenant.getOrDefault(tenantCode, new HashMap<>()));
            fp.setUpdate(updateFieldMapByTenant.getOrDefault(tenantCode, new HashMap<>()));
            fieldPermByTenant.put(tenantCode, fp);
        }
        permissionInfo.setFieldPermissionByTenant(fieldPermByTenant);

        // 设置禁用详情（确保所有租户都有完整结构）
        for (String tenantCode : permsByTenant.keySet()) {
            if (!disabledDetailByTenant.containsKey(tenantCode)) {
                DisabledDetail detail = new DisabledDetail();
                detail.setSystem(new ArrayList<>());
                detail.setTenant(new ArrayList<>());
                detail.setDept(new ArrayList<>());
                detail.setRole(new ArrayList<>());
                detail.setUser(new ArrayList<>());
                disabledDetailByTenant.put(tenantCode, detail);
            }
        }
        permissionInfo.setDisabledDetailByTenant(disabledDetailByTenant);

        // 构建部门信息
        List<UserDeptDTO> deptList = sysUserPolicyMapper.queryUserAllDeptInfo(user.getId());
        DeptGroup deptGroup = new DeptGroup();

        // 按租户分组部门
        Map<String, List<DeptItem>> deptsByTenant = new LinkedHashMap<>();
        Map<String, String> deptTenantNames = new HashMap<>();

        // 当前租户的启用/禁用部门
        List<DeptItem> currentEnabledDepts = new ArrayList<>();
        List<DeptItem> currentDisabledDepts = new ArrayList<>();

        for (UserDeptDTO dto : deptList) {
            if (dto.getDeptCode() == null || dto.getTenantCode() == null) {
                continue;
            }

            String tenantCode = dto.getTenantCode();
            String tenantName = dto.getTenantName();
            deptTenantNames.putIfAbsent(tenantCode, tenantName);

            DeptItem item = new DeptItem();
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
        CurrentDepts currentDepts = new CurrentDepts();
        currentDepts.setEnabled(currentEnabledDepts);
        currentDepts.setDisabled(currentDisabledDepts);
        deptGroup.setCurrent(currentDepts);

        // 设置所有/有效/无效（按租户分组）
        List<TenantDepts> allTenantDepts = new ArrayList<>();
        List<TenantDepts> validTenantDepts = new ArrayList<>();
        List<TenantDepts> invalidTenantDepts = new ArrayList<>();

        for (Map.Entry<String, List<DeptItem>> entry : deptsByTenant.entrySet()) {
            String tenantCode = entry.getKey();
            String tenantName = deptTenantNames.get(tenantCode);
            List<DeptItem> depts = entry.getValue();

            // 所有
            TenantDepts allGroup = new TenantDepts();
            allGroup.setTenantCode(tenantCode);
            allGroup.setTenantName(tenantName);
            allGroup.setDepts(new ArrayList<>(depts));
            allTenantDepts.add(allGroup);

            // 有效/无效分组
            List<DeptItem> validDepts = new ArrayList<>();
            List<DeptItem> invalidDepts = new ArrayList<>();
            for (DeptItem dept : depts) {
                if ("ACTIVE".equals(dept.getUserPolicyStatus())) {
                    validDepts.add(dept);
                } else {
                    invalidDepts.add(dept);
                }
            }

            if (!validDepts.isEmpty()) {
                TenantDepts validGroup = new TenantDepts();
                validGroup.setTenantCode(tenantCode);
                validGroup.setTenantName(tenantName);
                validGroup.setDepts(validDepts);
                validTenantDepts.add(validGroup);
            }

            if (!invalidDepts.isEmpty()) {
                TenantDepts invalidGroup = new TenantDepts();
                invalidGroup.setTenantCode(tenantCode);
                invalidGroup.setTenantName(tenantName);
                invalidGroup.setDepts(invalidDepts);
                invalidTenantDepts.add(invalidGroup);
            }
        }

        deptGroup.setAll(allTenantDepts);
        deptGroup.setValid(validTenantDepts);
        deptGroup.setInvalid(invalidTenantDepts);

        // 构建角色信息
        List<UserRoleDTO> roleList = sysUserPolicyMapper.queryUserAllRoleInfo(user.getId());
        RoleGroup roleGroup = new RoleGroup();

        // 按租户分组角色
        Map<String, List<RoleItem>> rolesByTenant = new LinkedHashMap<>();
        Map<String, String> roleTenantNames = new HashMap<>();
        Map<String, Map<String, String>> roleStatusByTenant = new HashMap<>();

        // 当前租户的启用/禁用角色
        List<RoleItem> currentEnabledRoles = new ArrayList<>();
        List<RoleItem> currentDisabledRoles = new ArrayList<>();

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

            RoleItem item = new RoleItem();
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
        CurrentRoles currentRoles = new CurrentRoles();
        currentRoles.setEnabled(currentEnabledRoles);
        currentRoles.setDisabled(currentDisabledRoles);
        roleGroup.setCurrent(currentRoles);

        // 设置所有/有效/无效（按租户分组）
        List<TenantRoles> allTenantRoles = new ArrayList<>();
        List<TenantRoles> validTenantRoles = new ArrayList<>();
        List<TenantRoles> invalidTenantRoles = new ArrayList<>();

        for (Map.Entry<String, List<RoleItem>> entry : rolesByTenant.entrySet()) {
            String tenantCode = entry.getKey();
            String tenantName = roleTenantNames.get(tenantCode);
            List<RoleItem> roles = entry.getValue();
            Map<String, String> statusMap = roleStatusByTenant.get(tenantCode);

            // 所有
            TenantRoles allGroup = new TenantRoles();
            allGroup.setTenantCode(tenantCode);
            allGroup.setTenantName(tenantName);
            allGroup.setRoles(new ArrayList<>(roles));
            allTenantRoles.add(allGroup);

            // 有效/无效分组
            List<RoleItem> validRoles = new ArrayList<>();
            List<RoleItem> invalidRoles = new ArrayList<>();
            for (RoleItem role : roles) {
                String status = statusMap.get(role.getRoleCode());
                if ("ACTIVE".equals(status)) {
                    validRoles.add(role);
                } else {
                    invalidRoles.add(role);
                }
            }

            if (!validRoles.isEmpty()) {
                TenantRoles validGroup = new TenantRoles();
                validGroup.setTenantCode(tenantCode);
                validGroup.setTenantName(tenantName);
                validGroup.setRoles(validRoles);
                validTenantRoles.add(validGroup);
            }

            if (!invalidRoles.isEmpty()) {
                TenantRoles invalidGroup = new TenantRoles();
                invalidGroup.setTenantCode(tenantCode);
                invalidGroup.setTenantName(tenantName);
                invalidGroup.setRoles(invalidRoles);
                invalidTenantRoles.add(invalidGroup);
            }
        }

        roleGroup.setAll(allTenantRoles);
        roleGroup.setValid(validTenantRoles);
        roleGroup.setInvalid(invalidTenantRoles);

        // 构建租户信息
        TenantGroup tenantGroup = new TenantGroup();

        List<TenantItem> currentTenantList = new ArrayList<>();
        List<TenantItem> allTenants = new ArrayList<>();
        List<TenantItem> validTenants = new ArrayList<>();
        List<TenantItem> invalidTenants = new ArrayList<>();

        for (UserTenantItemDTO dto : tenantList) {
            TenantItem item = new TenantItem();
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
        UserInfo userInfo = new UserInfo();
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
        StpUtil.getSession().set(GlobalConstant.Session.USER_CONTEXT, userContext);

        return ApiResponse.success("登录成功", userContext);
    }

    /**
     * <p>用户登出，清除当前登录会话</p>
     *
     * @return 登出结果
     */
    @Override
    public ApiResponse logout() {
        StpUtil.logout();
        return ApiResponse.success("登出成功", null);
    }

    /**
     * <p>租户注册（含管理员账户）</p>
     * <p>流程：校验邮箱/手机号唯一性 → 创建租户（含邀请码）→ 创建管理员用户 → 创建用户策略（绑定租户）</p>
     *
     * @param param 租户注册请求参数
     * @return 注册结果，包含登录账号和租户编码
     * @throws BusinessException 当邮箱/手机号已存在或租户创建失败时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse registerTenant(TenantRegisterRTO param) {

        // 校验管理员用户名唯一性（用户名作为登录账号，必须唯一）
        Long userNameCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, param.getAdminUserName())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (userNameCount > 0) {
            throw new BusinessException("该用户名已被注册");
        }

        // 校验管理员邮箱唯一性
        Long emailCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, param.getAdminEmail())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (emailCount > 0) {
            throw new BusinessException("该邮箱已被注册");
        }

        // 校验管理员手机号唯一性
        Long phoneCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, param.getAdminPhone())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (phoneCount > 0) {
            throw new BusinessException("该手机号已被注册");
        }

        // 生成租户编码与邀请码
        String tenantCode = generateTenantCode();
        String inviteCode = generateInviteCode();

        // 构建租户实体
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode(tenantCode);
        tenant.setTenantName(param.getTenantName());
        // tenant_type 同时存储行业类型（数据库已删除 tenant_industry 字段）
        tenant.setTenantType(param.getTenantType());
        tenant.setTenantAddress(param.getTenantAddress());
        tenant.setTenantDesc(param.getTenantDesc());
        tenant.setTenantScale(param.getTenantScale());
        // 租户LOGO路径为可选项，未提供则保留为 null
        tenant.setTenantLogoUrl(param.getTenantLogoUrl());
        // 联系人姓名默认取管理员真实姓名
        tenant.setContactName(param.getAdminRealName());
        tenant.setContactPhone(param.getContactPhone());
        tenant.setContactEmail(param.getAdminEmail());
        tenant.setStatus(GlobalEnum.TenantStatus.ENABLED.getCode());
        tenant.setLevel(1);
        tenant.setParentId(0L);
        tenant.setPath("rootTenant/" + tenantCode);
        tenant.setHasChildren(false);
        tenant.setInviteCode(inviteCode);
        // 套餐与过期时间：注册时默认基础套餐（package_id=1）与一年有效期
        tenant.setPackageId(1L);
        tenant.setExpireTime(LocalDateTime.now().plusYears(1));
        // 审计字段：注册场景无登录用户，createBy 设为 0L 表示系统操作
        tenant.setCreateTenant(0L);
        tenant.setCreateDept(0L);
        tenant.setCreateRole(0L);
        tenant.setCreateBy(0L);
        tenant.setCreateAt(LocalDateTime.now());
        tenant.setUpdateBy(0L);
        tenant.setUpdateAt(LocalDateTime.now());
        tenant.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 插入租户
        int tenantRows = sysTenantMapper.insert(tenant);
        if (tenantRows <= 0 || tenant.getId() == null) {
            throw new BusinessException("租户创建失败");
        }

        // 构建管理员用户实体
        SysUser adminUser = new SysUser();
        adminUser.setUserCode(generateUserCode());
        // 用户名作为登录账号（与 login 接口的 username 字段对应）
        adminUser.setUserName(param.getAdminUserName());
        adminUser.setRealName(param.getAdminRealName());
        adminUser.setNickName(param.getAdminNickName());
        adminUser.setEmail(param.getAdminEmail());
        adminUser.setPhone(param.getAdminPhone());
        // BCrypt 加密密码 + 明文密码同步存储（用户需求：用于密码找回场景）
        adminUser.setPassword(passwordEncoder.encode(param.getAdminPassword()));
        adminUser.setPlainPassword(param.getAdminPassword());
        adminUser.setGender(param.getAdminGender());
        adminUser.setBirthday(LocalDate.parse(param.getAdminBirthday()));
        adminUser.setStatus(GlobalEnum.UserStatus.ENABLED.getCode());
        // 审计字段：注册场景 createBy 设为 0L 表示系统操作
        adminUser.setCreateTenant(0L);
        adminUser.setCreateDept(0L);
        adminUser.setCreateRole(0L);
        adminUser.setCreateBy(0L);
        adminUser.setCreateAt(LocalDateTime.now());
        adminUser.setUpdateBy(0L);
        adminUser.setUpdateAt(LocalDateTime.now());
        adminUser.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 插入管理员用户
        int userRows = sysUserMapper.insert(adminUser);
        if (userRows <= 0 || adminUser.getId() == null) {
            throw new BusinessException("管理员账户创建失败");
        }

        // 构建用户策略（绑定管理员到租户，标记为主租户）
        SysUserPolicy userPolicy = new SysUserPolicy();
        userPolicy.setPolicyCode("UP_" + System.currentTimeMillis());
        userPolicy.setPolicyName(param.getAdminUserName() + "→" + param.getTenantName());
        userPolicy.setUserId(adminUser.getId());
        userPolicy.setTargetType(GlobalEnum.PermPolicyTargetType.TENANT.getCode());
        userPolicy.setTargetId(tenant.getId());
        userPolicy.setIsPrimary(true);
        userPolicy.setStatus(GlobalEnum.PermPolicyStatus.ACTIVE.getCode());
        // 审计字段：注册场景 createBy 设为 0L 表示系统操作
        userPolicy.setCreateTenant(0L);
        userPolicy.setCreateDept(0L);
        userPolicy.setCreateRole(0L);
        userPolicy.setCreateBy(0L);
        userPolicy.setCreateAt(LocalDateTime.now());
        userPolicy.setUpdateBy(0L);
        userPolicy.setUpdateAt(LocalDateTime.now());
        userPolicy.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());

        int policyRows = sysUserPolicyMapper.insert(userPolicy);
        if (policyRows <= 0) {
            throw new BusinessException("用户策略创建失败");
        }

        // 构建响应VO
        RegisterVO vo = new RegisterVO();
        vo.setUserId(adminUser.getId());
        vo.setUserCode(adminUser.getUserCode());
        vo.setLoginAccount(adminUser.getUserName());
        vo.setRealName(adminUser.getRealName());
        vo.setTenantCode(tenant.getTenantCode());
        vo.setTenantName(tenant.getTenantName());

        return ApiResponse.success("租户注册成功", vo);
    }

    /**
     * <p>用户注册（通过邀请码加入租户）</p>
     * <p>流程：校验邀请码有效性 → 校验邮箱/手机号唯一性 → 创建用户 → 创建用户策略（绑定到邀请码对应租户）</p>
     *
     * @param param 用户注册请求参数
     * @return 注册结果，包含登录账号和租户编码
     * @throws BusinessException 当邀请码无效、邮箱/手机号已存在或注册失败时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse registerUser(UserRegisterRTO param) {

        // 根据邀请码查询租户
        SysTenant tenant = sysTenantMapper.selectOne(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getInviteCode, param.getInviteCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (tenant == null) {
            throw new BusinessException("邀请码无效");
        }

        // 校验租户状态
        if (!GlobalEnum.TenantStatus.ENABLED.getCode().equals(tenant.getStatus())) {
            throw new BusinessException("租户已停用，无法注册");
        }

        // 校验邮箱唯一性
        Long emailCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, param.getUserEmail())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (emailCount > 0) {
            throw new BusinessException("该邮箱已被注册");
        }

        // 校验手机号唯一性
        Long phoneCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, param.getUserPhone())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (phoneCount > 0) {
            throw new BusinessException("该手机号已被注册");
        }

        // 构建用户实体
        SysUser user = new SysUser();
        user.setUserCode(generateUserCode());
        // 邮箱作为登录账号
        user.setUserName(param.getUserEmail());
        user.setRealName(param.getUserName());
        user.setNickName(param.getUserName());
        user.setEmail(param.getUserEmail());
        user.setPhone(param.getUserPhone());
        user.setPassword(passwordEncoder.encode(param.getUserPassword()));
        user.setPlainPassword(param.getUserPassword());
        // gender 字段在数据库中为 NOT NULL，注册时未知则设为 UNKNOWN
        user.setGender("UNKNOWN");
        user.setStatus(GlobalEnum.UserStatus.ENABLED.getCode());
        // 审计字段：注册场景 createBy 设为 0L 表示系统操作
        user.setCreateTenant(0L);
        user.setCreateDept(0L);
        user.setCreateRole(0L);
        user.setCreateBy(0L);
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateBy(0L);
        user.setUpdateAt(LocalDateTime.now());
        user.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 插入用户
        int userRows = sysUserMapper.insert(user);
        if (userRows <= 0 || user.getId() == null) {
            throw new BusinessException("用户创建失败");
        }

        // 构建用户策略（绑定用户到邀请码对应的租户）
        SysUserPolicy userPolicy = new SysUserPolicy();
        userPolicy.setPolicyCode("UP_" + System.currentTimeMillis());
        userPolicy.setPolicyName(param.getUserName() + "→" + tenant.getTenantName());
        userPolicy.setUserId(user.getId());
        userPolicy.setTargetType(GlobalEnum.PermPolicyTargetType.TENANT.getCode());
        userPolicy.setTargetId(tenant.getId());
        // 新注册用户暂无其他租户，标记为主租户
        userPolicy.setIsPrimary(true);
        userPolicy.setStatus(GlobalEnum.PermPolicyStatus.ACTIVE.getCode());
        // 审计字段：注册场景 createBy 设为 0L 表示系统操作
        userPolicy.setCreateTenant(0L);
        userPolicy.setCreateDept(0L);
        userPolicy.setCreateRole(0L);
        userPolicy.setCreateBy(0L);
        userPolicy.setCreateAt(LocalDateTime.now());
        userPolicy.setUpdateBy(0L);
        userPolicy.setUpdateAt(LocalDateTime.now());
        userPolicy.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());

        int policyRows = sysUserPolicyMapper.insert(userPolicy);
        if (policyRows <= 0) {
            throw new BusinessException("用户策略创建失败");
        }

        // 构建响应VO
        RegisterVO vo = new RegisterVO();
        vo.setUserId(user.getId());
        vo.setUserCode(user.getUserCode());
        vo.setLoginAccount(user.getEmail());
        vo.setRealName(user.getRealName());
        vo.setTenantCode(tenant.getTenantCode());
        vo.setTenantName(tenant.getTenantName());

        return ApiResponse.success("用户注册成功", vo);
    }

    /**
     * <p>生成租户编码（T + 13位时间戳，保证唯一性）</p>
     *
     * @return 租户编码
     */
    private String generateTenantCode() {
        return "T" + System.currentTimeMillis();
    }

    /**
     * <p>生成用户编码（U + 13位时间戳，保证唯一性）</p>
     *
     * @return 用户编码
     */
    private String generateUserCode() {
        return "U" + System.currentTimeMillis();
    }

    /**
     * <p>生成8位邀请码（大写字母+数字组合，使用SecureRandom保证随机性）</p>
     *
     * @return 8位邀请码
     */
    private String generateInviteCode() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        java.security.SecureRandom random = new java.security.SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }

}
