package com.shy.nexusix.iam.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.dto.LoginUserTenantDTO;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.core.entity.dto.UserPermDetailDTO;
import com.shy.nexusix.iam.entity.*;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.mapper.SysUserPermRelMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.*;
import com.shy.nexusix.tenant.entity.SysTenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 认证服务实现类，负责处理用户登录认证及权限上下文初始化的核心逻辑。
 * </p>
 *
 * <p><b>设计意图：</b></p>
 * <p>本类将登录流程拆分为四个阶段，以减少数据库交互次数并保证权限数据的完整性：</p>
 * <ol>
 *   <li>缓存检查 —— 通过 Sa-Token 判断用户是否已登录，避免重复认证</li>
 *   <li>用户+租户联合查询 —— 通过 MPJ 三表 JOIN 一次性获取用户、租户关系及租户信息</li>
 *   <li>权限策略联合查询 —— 通过 MPJ 三表 JOIN 一次性获取权限、策略及关联关系</li>
 *   <li>权限数据组装 —— 单次遍历完成权限分类、级联禁用判定及字段级权限构建</li>
 * </ol>
 *
 * <p><b>核心职责：</b></p>
 * <ul>
 *   <li>用户身份验证（用户名+密码校验）</li>
 *   <li>租户状态校验（停用/过期拦截）</li>
 *   <li>Sa-Token 会话登录</li>
 *   <li>用户权限上下文（UserContextDTO）的构建与缓存</li>
 * </ul>
 *
 * @author shy
 * @since 2026-04-07
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserPermRelMapper sysUserPermRelMapper;

    /**
     * <p>
     * 用户登录方法，执行完整的认证流程并初始化权限上下文。
     * </p>
     *
     * <p><b>处理流程：</b></p>
     * <ol>
     *   <li>检查 Sa-Token 缓存，若用户已登录则直接返回成功</li>
     *   <li>通过三表 JOIN 查询用户、用户-租户关联、租户信息，验证身份与租户状态</li>
     *   <li>执行 Sa-Token 登录，建立会话</li>
     *   <li>通过三表 JOIN 查询权限、策略、关联关系</li>
     *   <li>单次遍历完成权限分类（有效/无效/级联禁用）及字段级权限构建</li>
     *   <li>组装 UserContextDTO 并写入 Sa-Token Session</li>
     * </ol>
     *
     * @param param 登录请求参数，包含 username（用户名）和 password（密码）
     * @return ApiResponse 登录成功时返回成功响应，数据体为空
     * @throws BusinessException 当用户名或密码不正确时抛出
     * @throws BusinessException 当所属租户已停用时抛出
     * @throws BusinessException 当所属租户已过期时抛出
     */
    @Override
    public ApiResponse login(LoginRTO param) {

        // 缓存检查 若 Sa-Token 中已存在该用户的登录态 则跳过后续认证流程
        if (StpUtil.isLogin(param.getUsername())) {
            return ApiResponse.success();
        }

        // 一次性获取用户基础信息、默认租户关联关系及租户信息 避免多次数据库往返 实现 sys_user sys_tenant sys_user_tenant_rel 三表内连接，
        LoginUserTenantDTO loginData = sysUserMapper.selectJoinOne(LoginUserTenantDTO.class,
                new MPJLambdaWrapper<SysUser>()
                        // 映射 sys_user 表字段
                        .selectAs(SysUser::getId, LoginUserTenantDTO::getUserId)
                        .selectAs(SysUser::getUserCode, LoginUserTenantDTO::getUserCode)
                        .selectAs(SysUser::getUserName, LoginUserTenantDTO::getUserName)
                        .selectAs(SysUser::getPassword, LoginUserTenantDTO::getPassword)
                        .selectAs(SysUser::getNickName, LoginUserTenantDTO::getNickName)
                        .selectAs(SysUser::getEmail, LoginUserTenantDTO::getEmail)
                        .selectAs(SysUser::getPhone, LoginUserTenantDTO::getPhone)
                        .selectAs(SysUser::getAvatar, LoginUserTenantDTO::getAvatar)
                        .selectAs(SysUser::getStatus, LoginUserTenantDTO::getUserStatus)
                        // 映射 sys_user_tenant_rel 关联表字段
                        .selectAs(SysUserTenantRel::getId, LoginUserTenantDTO::getRelId)
                        .selectAs(SysUserTenantRel::getUserId, LoginUserTenantDTO::getRelUserId)
                        .selectAs(SysUserTenantRel::getTenantId, LoginUserTenantDTO::getRelTenantId)
                        .selectAs(SysUserTenantRel::getDeptId, LoginUserTenantDTO::getRelDeptId)
                        .selectAs(SysUserTenantRel::getIsAdmin, LoginUserTenantDTO::getRelIsAdmin)
                        .selectAs(SysUserTenantRel::getIsDefault, LoginUserTenantDTO::getRelIsDefault)
                        // 映射 sys_tenant 租户表字段
                        .selectAs(SysTenant::getId, LoginUserTenantDTO::getTenantId)
                        .selectAs(SysTenant::getTenantCode, LoginUserTenantDTO::getTenantCode)
                        .selectAs(SysTenant::getTenantName, LoginUserTenantDTO::getTenantName)
                        .selectAs(SysTenant::getStatus, LoginUserTenantDTO::getTenantStatus)
                        // 内连接 sys_user_tenant_rel：匹配用户ID，且仅取默认租户关联（isDefault=true），排除已删除记录
                        .innerJoin(SysUserTenantRel.class, on ->
                                on.eq(SysUser::getId, SysUserTenantRel::getUserId)
                                        .eq(SysUserTenantRel::getIsDefault, true)
                                        .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()))
                        // 内连接 sys_tenant：通过关联表的 tenantId 关联租户表，排除已删除租户
                        .innerJoin(SysTenant.class, on ->
                                on.eq(SysUserTenantRel::getTenantId, SysTenant::getId)
                                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()))
                        // 查询条件：按用户名精确匹配，排除已删除用户
                        .eq(SysUser::getUserName, param.getUsername())
                        .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
        );

        // 身份验证：查询结果为空或密码不匹配均视为认证失败，统一提示避免信息泄露
        if (loginData == null || !loginData.getPassword().equals(param.getPassword())) {
            throw new BusinessException("用户名或密码不正确");
        }

        // 租户状态校验：停用状态的租户不允许登录
        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(loginData.getTenantStatus())) {
            throw new BusinessException("所属租户已停用");
        }
        // 租户状态校验：过期状态的租户不允许登录
        if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(loginData.getTenantStatus())) {
            throw new BusinessException("所属租户已过期");
        }

        // 通过 Sa-Token 执行登录，以用户名作为登录标识，建立会话
        StpUtil.login(loginData.getUserName());

        // 一次性获取用户所有权限策略详情，包括权限编码、策略状态、字段级操作配置等 sys_user_perm_rel sys_perm_policy sys_perm 三表内连接
        List<UserPermDetailDTO> permDetails = sysUserPermRelMapper.selectJoinList(UserPermDetailDTO.class,
                new MPJLambdaWrapper<SysUserPermRel>()
                        // 映射 sys_perm 权限表字段
                        .selectAs(SysPerm::getId, UserPermDetailDTO::getPermId)
                        .selectAs(SysPerm::getPermCode, UserPermDetailDTO::getPermCode)
                        .selectAs(SysPerm::getPermName, UserPermDetailDTO::getPermName)
                        // 映射 sys_perm_policy 策略表字段
                        .selectAs(SysPermPolicy::getId, UserPermDetailDTO::getPolicyId)
                        .selectAs(SysPermPolicy::getPermId, UserPermDetailDTO::getPolicyPermId)
                        .selectAs(SysPermPolicy::getStatus, UserPermDetailDTO::getPolicyStatus)
                        .selectAs(SysPermPolicy::getTableName, UserPermDetailDTO::getPolicyTableName)
                        .selectAs(SysPermPolicy::getAccessType, UserPermDetailDTO::getPolicyAccessType)
                        .selectAs(SysPermPolicy::getFieldOperates, UserPermDetailDTO::getPolicyFieldOperates)
                        // 映射 sys_user_perm_rel 关联表字段
                        .selectAs(SysUserPermRel::getId, UserPermDetailDTO::getRelId)
                        // 内连接 sys_perm_policy：通过策略ID关联，排除已删除策略
                        .innerJoin(SysPermPolicy.class, on ->
                                on.eq(SysUserPermRel::getPolicyId, SysPermPolicy::getId)
                                        .eq(SysPermPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()))
                        // 内连接 sys_perm：通过策略中的权限ID关联权限表，排除已删除权限
                        .innerJoin(SysPerm.class, on ->
                                on.eq(SysPermPolicy::getPermId, SysPerm::getId)
                                        .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()))
                        // 查询条件：按用户-租户关联ID匹配，排除已删除关联记录
                        .eq(SysUserPermRel::getUserId, loginData.getRelId())
                        .eq(SysUserPermRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
        );

        // 使用 Set 存储各状态级别的权限ID，用于后续的级联禁用判定
        Set<Long> activePolicyPermIdSet = new HashSet<>();       // 活跃策略对应的权限ID集合
        Set<Long> systemDisabledPermIdSet = new HashSet<>();     // 系统级禁用的权限ID集合
        Set<Long> tenantDisabledPermIdSet = new HashSet<>();     // 租户级禁用的权限ID集合
        Set<Long> roleDisabledPermIdSet = new HashSet<>();       // 角色级禁用的权限ID集合
        Set<Long> userDisabledPermIdSet = new HashSet<>();       // 用户级禁用的权限ID集合

        // 字段级权限映射：key 为数据表名（如 "sys_tenant"），value 为该表的字段可见/不可见配置
        Map<String, UserContextDTO.EntityFieldPerm> queryPermMap = new HashMap<>();   // 查询操作字段权限
        Map<String, UserContextDTO.EntityFieldPerm> createPermMap = new HashMap<>();  // 创建操作字段权限
        Map<String, UserContextDTO.EntityFieldPerm> updatePermMap = new HashMap<>();  // 更新操作字段权限

        // 权限编码列表：用于构建最终的用户上下文权限信息
        List<String> allPermCodeList = new ArrayList<>();        // 全部权限编码（含重复，后续去重）
        List<String> validPermCodeList = new ArrayList<>();      // 有效权限编码（策略状态为 ACTIVE）
        List<String> invalidPermCodeList = new ArrayList<>();    // 无效权限编码（任一级别被禁用）
        List<String> systemDisabledList = new ArrayList<>();     // 系统级禁用的权限编码列表
        List<String> tenantDisabledList = new ArrayList<>();     // 租户级禁用的权限编码列表
        List<String> roleDisabledList = new ArrayList<>();       // 角色级禁用的权限编码列表
        List<String> userDisabledList = new ArrayList<>();       // 用户级禁用的权限编码列表

        for (UserPermDetailDTO detail : permDetails) {
            Long permId = detail.getPermId();
            String permCode = detail.getPermCode();
            String status = detail.getPolicyStatus();

            // 收集所有权限编码（可能存在同一权限对应多条策略的情况，后续统一去重）
            allPermCodeList.add(permCode);

            // 根据策略状态分类：ACTIVE 为有效策略，其余为不同级别的级联禁用
            // 级联禁用优先级：系统级 > 租户级 > 角色级 > 用户级，高级别禁用会覆盖低级别
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

            // 判定权限是否被禁用：任一级别的级联禁用均使该权限失效
            boolean isDisabled = systemDisabledPermIdSet.contains(permId)
                    || tenantDisabledPermIdSet.contains(permId)
                    || roleDisabledPermIdSet.contains(permId)
                    || userDisabledPermIdSet.contains(permId);

            if (isDisabled) {
                invalidPermCodeList.add(permCode);
            } else if (activePolicyPermIdSet.contains(permId)) {
                validPermCodeList.add(permCode);
            }

            // 构建字段级权限：仅当策略配置了 fieldOperates（字段操作列表）时才处理
            String fieldOperates = detail.getPolicyFieldOperates();
            if (fieldOperates == null || fieldOperates.isEmpty()) continue;

            // 根据访问类型（QUERY/CREATE/UPDATE）选择对应的目标权限映射
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

            // 解析字段操作 JSON 数组为字符串列表
            List<String> fields = JSON.parseArray(fieldOperates, String.class);
            if (fields == null || fields.isEmpty()) continue;

            // 按表名获取或创建字段权限对象（computeIfAbsent 保证同一表名只创建一次）
            UserContextDTO.EntityFieldPerm fieldPerm = targetMap.computeIfAbsent(
                    detail.getPolicyTableName(), k -> new UserContextDTO.EntityFieldPerm());

            // 根据策略状态决定字段归属：ACTIVE → 可见字段，其他 → 不可见字段
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

        // 构建级联禁用信息：记录各层级禁用的权限编码，供前端展示禁用原因
        UserContextDTO.CascadeDisabled cascadeDisabled = new UserContextDTO.CascadeDisabled();
        cascadeDisabled.setSystemDisabled(systemDisabledList);
        cascadeDisabled.setTenantDisabled(tenantDisabledList);
        cascadeDisabled.setRoleDisabled(roleDisabledList);
        cascadeDisabled.setUserDisabled(userDisabledList);

        // 构建字段级权限：按操作类型（查询/创建/更新）组织各数据表的字段可见性配置
        UserContextDTO.FieldPerm fieldPerm = new UserContextDTO.FieldPerm();
        fieldPerm.setQuery(queryPermMap);
        fieldPerm.setCreate(createPermMap);
        fieldPerm.setUpdate(updatePermMap);

        // 构建权限信息：汇总全部权限编码、有效/无效权限列表、级联禁用详情及字段级权限
        UserContextDTO.PermInfo permInfo = new UserContextDTO.PermInfo();
        permInfo.setPerms(allPermCodeList.stream().distinct().collect(Collectors.toList()));
        permInfo.setValidPerms(validPermCodeList);
        permInfo.setInvalidPerms(invalidPermCodeList);
        permInfo.setCascadeDisabled(cascadeDisabled);
        permInfo.setFieldPerm(fieldPerm);

        // 构建租户信息：缓存当前用户默认租户的基础信息
        UserContextDTO.TenantInfo tenantInfoCache = new UserContextDTO.TenantInfo();
        tenantInfoCache.setTenantName(loginData.getTenantName());
        tenantInfoCache.setTenantCode(loginData.getTenantCode());

        // 组装完整的用户上下文对象
        UserContextDTO userContext = new UserContextDTO();
        userContext.setTenantInfo(tenantInfoCache);
        userContext.setPermInfo(permInfo);

        // 将用户上下文写入 Sa-Token Session，后续请求可通过 UserContext 工具类直接读取
        StpUtil.getSession().set(GlobalConstant.Session.USER_CONTEXT, userContext);
        return ApiResponse.success();

    }

}
