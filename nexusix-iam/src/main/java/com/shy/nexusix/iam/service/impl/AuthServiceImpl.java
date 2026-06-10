package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.iam.dto.UserLoginJoinDTO;
import com.shy.nexusix.iam.dto.UserPermJoinDTO;
import com.shy.nexusix.iam.dto.UserRoleDTO;
import com.shy.nexusix.iam.dto.UserTenantItemDTO;
import com.shy.nexusix.iam.mapper.SysPermPolicyMapper;
import com.shy.nexusix.iam.mapper.SysRolePolicyMapper;
import com.shy.nexusix.iam.mapper.SysUserPolicyMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>认证服务实现类</p>
 *
 * @author shy
 * @since 2026-06-07
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private SysUserPolicyMapper sysUserPolicyMapper;

    @Autowired
    private SysPermPolicyMapper sysPermPolicyMapper;

    @Autowired
    private SysRolePolicyMapper sysRolePolicyMapper;

    /**
     * <p>用户登录认证</p>
     * <p>登录成功后构建 UserContextDTO 存入 Sa-Token Session，结构如下：</p>
     * <pre>
     * currentTenant  — 当前登录租户（编码/名称/状态）
     * tenants        — 租户分组（全部/有效/无效）
     * permissions    — 权限汇总（全部/有效/无效 + 四级禁用详情 + 字段级操作权限）
     * roles          — 角色分组（当前租户/全部/有效/无效 + 每条角色标注所属租户）
     * </pre>
     *
     * @param param 登录入参对象 封装用户名、明文密码等登录参数
     * @return ApiResponse 统一返回成功结果 用户信息存放于Sa-Token Session中
     * @throws BusinessException 账号密码错误、无默认租户、租户停用/过期时抛出业务异常
     */
    @Override
    public ApiResponse login(LoginRTO param) {

        // 检查当前用户是否已经登录 若已登录则返回缓存中该用户信息
        if (StpUtil.isLogin(param.getUsername())) {
            return ApiResponse.success();
        }

        // 根据用户名查询用户与默认租户的关联登录信息
        UserLoginJoinDTO loginJoinInfo = sysUserPolicyMapper.queryUserLoginJoin(param.getUsername());

        // 校验用户名是否存在以及密码是否匹配 TODO 后续使用加密对比
        if (loginJoinInfo == null || !loginJoinInfo.getPassword().equals(param.getPassword())) {
            throw new BusinessException("用户名或密码不正确");
        }

        // 校验用户是否已关联默认租户 无租户关联则不允许登录
        if (loginJoinInfo.getUserPolicyId() == null) {
            throw new BusinessException("用户未设置任何默认租户，请联系相关租户管理员进行设置");
        }

        // 校验租户是否处于停用状态
        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(loginJoinInfo.getTenantStatus())) {
            throw new BusinessException("所属租户已停用");
        }

        // 校验租户是否已过期
        if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(loginJoinInfo.getTenantStatus())) {
            throw new BusinessException("所属租户已过期");
        }

        // 所有前置校验通过后 执行登录操作
        StpUtil.login(param.getUsername());

        // ==================== 权限计算 ====================
        // 查询当前用户在当前租户下的全部权限策略数据（两路径汇聚：ROLE / USER）
        // 用户权限 = 用户当前登录租户中所属角色具有的权限 + 用户当前登录租户中该用户本身具有的权限
        // TENANT路径（target_type='TENANT'）是租户能力边界约束，不作为用户直接权限参与计算
        List<UserPermJoinDTO> permJoinList = sysPermPolicyMapper.queryUserPermJoin(
                loginJoinInfo.getUserPolicyId(), loginJoinInfo.getTenantId());

        // 权限编码集合（有效/无效）
        Set<String> allPermCodeSet = new LinkedHashSet<>();
        // 权限ID去重
        Set<Long> seenPermIdSet = new HashSet<>();
        // 权限ID到编码映射
        Map<Long, String> permIdToCodeMap = new HashMap<>();
        // 各层级禁用标记
        Map<Long, Boolean> hasSystemDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasTenantDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasRoleDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasUserDisabledMap = new HashMap<>();
        // 有效标记
        Map<Long, Boolean> hasActiveMap = new HashMap<>();

        // 字段权限Map：query / create / update
        Map<String, UserContextDTO.TableFieldPermission> queryFieldMap = new HashMap<>();
        Map<String, UserContextDTO.TableFieldPermission> createFieldMap = new HashMap<>();
        Map<String, UserContextDTO.TableFieldPermission> updateFieldMap = new HashMap<>();

        // 第一轮遍历 收集权限编码 标记各层级状态 解析字段权限
        for (UserPermJoinDTO row : permJoinList) {
            Long permId = row.getPermId();
            String permCode = row.getPermCode();
            String status = row.getPolicyStatus();
            String targetType = row.getTargetType();

            if (permId != null && permCode != null && seenPermIdSet.add(permId)) {
                permIdToCodeMap.put(permId, permCode);
                allPermCodeSet.add(permCode);
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
            } else if (GlobalEnum.PermPolicyStatus.DISABLED.getCode().equals(status)) {
                // 纯DISABLED状态 根据target_type回退判断所属禁用层级
                if (GlobalEnum.PermPolicyTargetType.TENANT.getCode().equals(targetType)) {
                    hasTenantDisabledMap.put(permId, true);
                } else if (GlobalEnum.PermPolicyTargetType.ROLE.getCode().equals(targetType)) {
                    hasRoleDisabledMap.put(permId, true);
                } else if (GlobalEnum.PermPolicyTargetType.USER.getCode().equals(targetType)) {
                    hasUserDisabledMap.put(permId, true);
                }
            }

            // 解析字段权限
            String fieldOperates = row.getFieldOperates();
            String accessType = row.getAccessType();
            String tableName = row.getTableName();

            if (fieldOperates == null || fieldOperates.isEmpty()
                    || accessType == null || tableName == null) {
                continue;
            }

            Map<String, UserContextDTO.TableFieldPermission> targetMap;
            if (GlobalEnum.PermPolicyAccessType.QUERY.getCode().equals(accessType)) {
                targetMap = queryFieldMap;
            } else if (GlobalEnum.PermPolicyAccessType.CREATE.getCode().equals(accessType)) {
                targetMap = createFieldMap;
            } else if (GlobalEnum.PermPolicyAccessType.UPDATE.getCode().equals(accessType)) {
                targetMap = updateFieldMap;
            } else {
                continue;
            }

            List<String> fields = JSON.parseArray(fieldOperates, String.class);
            if (fields == null || fields.isEmpty()) {
                continue;
            }

            UserContextDTO.TableFieldPermission tfp = targetMap.computeIfAbsent(tableName,
                    k -> new UserContextDTO.TableFieldPermission());

            if (GlobalEnum.PermPolicyStatus.ACTIVE.getCode().equals(status)) {
                tfp.getOperable().addAll(fields);
            } else {
                tfp.getInoperable().addAll(fields);
            }
        }

        // 字段权限去重：inoperable字段从operable中移除（禁用优先于启用）
        for (UserContextDTO.TableFieldPermission tfp : queryFieldMap.values()) {
            tfp.getOperable().removeAll(tfp.getInoperable());
        }
        for (UserContextDTO.TableFieldPermission tfp : createFieldMap.values()) {
            tfp.getOperable().removeAll(tfp.getInoperable());
        }
        for (UserContextDTO.TableFieldPermission tfp : updateFieldMap.values()) {
            tfp.getOperable().removeAll(tfp.getInoperable());
        }

        // 第二轮遍历 分类有效/无效权限
        List<String> validPermCodeList = new ArrayList<>();
        List<String> invalidPermCodeList = new ArrayList<>();
        List<String> systemDisabledList = new ArrayList<>();
        List<String> tenantDisabledList = new ArrayList<>();
        List<String> roleDisabledList = new ArrayList<>();
        List<String> userDisabledList = new ArrayList<>();

        for (Long permId : seenPermIdSet) {
            String permCode = permIdToCodeMap.get(permId);
            boolean isSys = hasSystemDisabledMap.getOrDefault(permId, false);
            boolean isTenant = hasTenantDisabledMap.getOrDefault(permId, false);
            boolean isRole = hasRoleDisabledMap.getOrDefault(permId, false);
            boolean isUser = hasUserDisabledMap.getOrDefault(permId, false);

            if (isSys) systemDisabledList.add(permCode);
            if (isTenant) tenantDisabledList.add(permCode);
            if (isRole) roleDisabledList.add(permCode);
            if (isUser) userDisabledList.add(permCode);

            if (isSys || isTenant || isRole || isUser) {
                invalidPermCodeList.add(permCode);
            } else if (hasActiveMap.getOrDefault(permId, false)) {
                validPermCodeList.add(permCode);
            }
        }

        // 构建 PermissionInfo
        UserContextDTO.DisabledDetail disabledDetail = new UserContextDTO.DisabledDetail();
        disabledDetail.setSystem(systemDisabledList);
        disabledDetail.setTenant(tenantDisabledList);
        disabledDetail.setRole(roleDisabledList);
        disabledDetail.setUser(userDisabledList);

        UserContextDTO.FieldPermission fieldPermission = new UserContextDTO.FieldPermission();
        fieldPermission.setQuery(queryFieldMap);
        fieldPermission.setCreate(createFieldMap);
        fieldPermission.setUpdate(updateFieldMap);

        UserContextDTO.PermissionInfo permissionInfo = new UserContextDTO.PermissionInfo();
        permissionInfo.setAll(new ArrayList<>(allPermCodeSet));
        permissionInfo.setValid(validPermCodeList);
        permissionInfo.setInvalid(invalidPermCodeList);
        permissionInfo.setDisabledDetail(disabledDetail);
        permissionInfo.setFieldPermission(fieldPermission);

        // ==================== 角色计算 ====================
        // 当前登录租户下的角色
        List<UserRoleDTO> currentTenantRoleList = sysRolePolicyMapper.queryUserRoleInfo(
                loginJoinInfo.getUserPolicyId(), loginJoinInfo.getTenantId());
        List<UserContextDTO.RoleItem> currentRoles = new ArrayList<>();
        if (currentTenantRoleList != null) {
            for (UserRoleDTO dto : currentTenantRoleList) {
                UserContextDTO.RoleItem item = new UserContextDTO.RoleItem();
                item.setRoleCode(dto.getRoleCode());
                item.setDataScope(dto.getDataScope());
                item.setTenantCode(dto.getTenantCode());
                item.setTenantName(dto.getTenantName());
                currentRoles.add(item);
            }
        }

        // 全部租户下的角色（含有效/无效）
        List<UserRoleDTO> allRoleList = sysRolePolicyMapper.queryUserAllRoleInfo(loginJoinInfo.getUserId());
        List<UserContextDTO.RoleItem> allRoleItems = new ArrayList<>();
        List<UserContextDTO.RoleItem> validRoleItems = new ArrayList<>();
        List<UserContextDTO.RoleItem> invalidRoleItems = new ArrayList<>();
        if (allRoleList != null) {
            for (UserRoleDTO dto : allRoleList) {
                UserContextDTO.RoleItem item = new UserContextDTO.RoleItem();
                item.setRoleCode(dto.getRoleCode());
                item.setDataScope(dto.getDataScope());
                item.setTenantCode(dto.getTenantCode());
                item.setTenantName(dto.getTenantName());
                allRoleItems.add(item);
                if (GlobalEnum.PermPolicyStatus.ACTIVE.getCode().equals(dto.getRolePolicyStatus())) {
                    validRoleItems.add(item);
                } else {
                    invalidRoleItems.add(item);
                }
            }
        }

        UserContextDTO.RoleGroup roleGroup = new UserContextDTO.RoleGroup();
        roleGroup.setCurrent(currentRoles);
        roleGroup.setAll(allRoleItems);
        roleGroup.setValid(validRoleItems);
        roleGroup.setInvalid(invalidRoleItems);

        // ==================== 租户计算 ====================
        // 当前租户
        UserContextDTO.TenantInfo currentTenant = new UserContextDTO.TenantInfo();
        currentTenant.setTenantCode(loginJoinInfo.getTenantCode());
        currentTenant.setTenantName(loginJoinInfo.getTenantName());
        currentTenant.setStatus(loginJoinInfo.getTenantStatus());

        // 所有租户（按 userPolicyStatus + tenantStatus 分类）
        List<UserTenantItemDTO> allTenantList = sysUserPolicyMapper.queryUserAllTenants(loginJoinInfo.getUserId());
        List<UserContextDTO.TenantItem> allTenants = new ArrayList<>();
        List<UserContextDTO.TenantItem> validTenants = new ArrayList<>();
        List<UserContextDTO.TenantItem> invalidTenants = new ArrayList<>();
        for (UserTenantItemDTO item : allTenantList) {
            UserContextDTO.TenantItem tenantItem = new UserContextDTO.TenantItem();
            tenantItem.setTenantCode(item.getTenantCode());
            tenantItem.setTenantName(item.getTenantName());
            tenantItem.setStatus(item.getTenantStatus());
            allTenants.add(tenantItem);
            if (GlobalEnum.TenantStatus.ENABLED.getCode().equals(item.getUserPolicyStatus())
                    && GlobalEnum.TenantStatus.ENABLED.getCode().equals(item.getTenantStatus())) {
                validTenants.add(tenantItem);
            } else {
                invalidTenants.add(tenantItem);
            }
        }

        UserContextDTO.TenantGroup tenantGroup = new UserContextDTO.TenantGroup();
        tenantGroup.setAll(allTenants);
        tenantGroup.setValid(validTenants);
        tenantGroup.setInvalid(invalidTenants);

        // ==================== 组装 UserContext ====================
        UserContextDTO userContext = new UserContextDTO();
        userContext.setCurrentTenant(currentTenant);
        userContext.setTenants(tenantGroup);
        userContext.setPermissions(permissionInfo);
        userContext.setRoles(roleGroup);

        // 存入Session
        StpUtil.getSession().set("userContext", userContext);
        return ApiResponse.success();
    }

}