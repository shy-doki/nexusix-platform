package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.iam.dto.UserLoginJoinDTO;
import com.shy.nexusix.iam.dto.UserPermJoinDTO;
import com.shy.nexusix.iam.dto.UserTenantItemDTO;
import com.shy.nexusix.iam.mapper.SysUserPermRelMapper;
import com.shy.nexusix.iam.mapper.SysUserTenantRelMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.*;
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
    private SysUserTenantRelMapper iSysUserTenantRelService;

    @Autowired
    private SysUserPermRelMapper iSysUserPermRelMapper;

    /**
     * <p>用户登录认证</p>
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

        // 根据用户名查询用户与租户的关联登录信息
        UserLoginJoinDTO loginJoinInfo = iSysUserTenantRelService.queryUserLoginJoin(param.getUsername());

        // 校验用户名是否存在以及密码是否匹配 TODO 后续使用加密对比
        if (loginJoinInfo == null || !loginJoinInfo.getPassword().equals(param.getPassword())) {
            throw new BusinessException("用户名或密码不正确");
        }

        // 校验用户是否已关联默认租户 无租户关联则不允许登录
        if (loginJoinInfo.getUserTenantRelId() == null) {
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

        // 查询当前用户租户关系下的所有权限策略关联数据
        List<UserPermJoinDTO> permJoinList = iSysUserPermRelMapper.queryUserPermJoin(loginJoinInfo.getUserTenantRelId());

        // 所有权限编码[有效+失效]
        List<String> allPermCodeList = new ArrayList<>();
        // 所有有效权限编码
        List<String> validPermCodeList = new ArrayList<>();
        // 所有无效权限编码
        List<String> invalidPermCodeList = new ArrayList<>();
        // 无效权限编码[系统级]
        List<String> systemDisabledList = new ArrayList<>();
        // 无效权限编码[租户级]
        List<String> tenantDisabledList = new ArrayList<>();
        // 无效权限编码[角色级]
        List<String> roleDisabledList = new ArrayList<>();
        // 无效权限编码[用户级]
        List<String> userDisabledList = new ArrayList<>();

        // 用于去重 同一个权限ID只需记录一次编码
        Set<Long> seenPermIdSet = new HashSet<>();

        // 权限ID是否存在[系统级]失效
        Map<Long, Boolean> hasSystemDisabledMap = new HashMap<>();
        // 权限ID是否存在[租户级]失效
        Map<Long, Boolean> hasTenantDisabledMap = new HashMap<>();
        // 权限ID是否存在[角色级]失效
        Map<Long, Boolean> hasRoleDisabledMap = new HashMap<>();
        // 权限ID是否存在[用户级]失效
        Map<Long, Boolean> hasUserDisabledMap = new HashMap<>();
        // 权限ID是否为有效状态
        Map<Long, Boolean> hasActiveMap = new HashMap<>();

        // 权限ID到权限编码的映射 方便后续按ID查找
        Map<Long, String> permIdToCodeMap = new HashMap<>();

        // 按操作类型分类的字段权限  Map：key为表名 value为该表的字段权限配置
        Map<String, UserContextDTO.EntityFieldPerm> queryPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> createPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> updatePermMap = new HashMap<>();

        // 第一轮遍历 收集所有权限编码 记录各权限在不同层级的禁用状态 解析字段级权限
        for (UserPermJoinDTO row : permJoinList) {
            // 获取当前权限行的权限ID
            Long permId = row.getPermId();
            // 获取当前权限行的权限编码
            String permCode = row.getPermCode();
            // 获取当前权限行的策略状态
            String status = row.getPolicyStatus();

            // 对同一权限ID进行去重 确保每个权限编码只在全量列表中出现一次
            if (permId != null && permCode != null && !seenPermIdSet.contains(permId)) {
                // 将权限ID加入已处理集合
                seenPermIdSet.add(permId);
                // 权限ID与编码的映射
                permIdToCodeMap.put(permId, permCode);
                // 加入全量权限编码列表
                allPermCodeList.add(permCode);
            }

            // 根据策略状态 标记该权限在各层级的失效情况
            if (GlobalEnum.PermPolicyStatus.ACTIVE.getCode().equals(status)) {
                // 有效状态
                hasActiveMap.put(permId, true);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_SYSTEM_LEVEL.getCode().equals(status)) {
                // 系统级禁用
                hasSystemDisabledMap.put(permId, true);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_TENANT_LEVEL.getCode().equals(status)) {
                // 租户级禁用
                hasTenantDisabledMap.put(permId, true);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_ROLE_LEVEL.getCode().equals(status)) {
                // 角色级禁用
                hasRoleDisabledMap.put(permId, true);
            } else if (GlobalEnum.PermPolicyStatus.DISABLED_USER_LEVEL.getCode().equals(status)) {
                // 用户级禁用
                hasUserDisabledMap.put(permId, true);
            }

            // 允许操作的字段
            String fieldOperates = row.getFieldOperates();
            // 访问类型
            String accessType = row.getAccessType();
            // 表名
            String tableName = row.getTableName();

            // 若字段操作 操作类型或表名为空 则跳过该行不处理字段权限
            if (fieldOperates == null || fieldOperates.isEmpty()
                    || accessType == null || tableName == null) {
                continue;
            }

            // 根据操作类型 选择对应的字段权限Map
            Map<String, UserContextDTO.EntityFieldPerm> targetMap;
            if (GlobalEnum.PermPolicyAccessType.QUERY.getCode().equals(accessType)) {
                // 查询操作
                targetMap = queryPermMap;
            } else if (GlobalEnum.PermPolicyAccessType.CREATE.getCode().equals(accessType)) {
                // 新增操作
                targetMap = createPermMap;
            } else if (GlobalEnum.PermPolicyAccessType.UPDATE.getCode().equals(accessType)) {
                // 更新操作
                targetMap = updatePermMap;
            } else {
                // 未知操作类型 跳过
                continue;
            }

            // 解析JSON数组格式的字段名称列表
            List<String> fields = JSON.parseArray(fieldOperates, String.class);
            // 解析结果为空 跳过
            if (fields == null || fields.isEmpty()) {
                continue;
            }

            // 获取或创建该表对应的字段权限对象
            UserContextDTO.EntityFieldPerm fieldPerm = targetMap.computeIfAbsent(tableName,
                    k -> new UserContextDTO.EntityFieldPerm());

            // 根据策略状态 分配字段到可操作/不可操作列表
            if (GlobalEnum.PermPolicyStatus.ACTIVE.getCode().equals(status)) {
                // 有效字段加入可见列表
                if (fieldPerm.getVisibleFields() == null) {
                    fieldPerm.setVisibleFields(new ArrayList<>(fields));
                } else {
                    fieldPerm.getVisibleFields().addAll(fields);
                }
            } else {
                // 失效字段加入不可见列表
                if (fieldPerm.getInvisibleFields() == null) {
                    fieldPerm.setInvisibleFields(new ArrayList<>(fields));
                } else {
                    fieldPerm.getInvisibleFields().addAll(fields);
                }
            }
        }

        // 第二轮遍历 对每个去重后的权限ID 汇总其禁用状态并分类到对应列表
        for (Long permId : seenPermIdSet) {
            // 通过权限ID获取编码
            String permCode = permIdToCodeMap.get(permId);

            // 获取当前权限ID 失效标记[系统级] 默认false
            boolean isSystemDisabled = hasSystemDisabledMap.getOrDefault(permId, false);
            // 获取当前权限ID 失效标记[租户级] 默认false
            boolean isTenantDisabled = hasTenantDisabledMap.getOrDefault(permId, false);
            // 获取当前权限ID 失效标记[角色级] 默认false
            boolean isRoleDisabled = hasRoleDisabledMap.getOrDefault(permId, false);
            // 获取当前权限ID 失效标记[用户级] 默认false
            boolean isUserDisabled = hasUserDisabledMap.getOrDefault(permId, false);

            // 如果是系统级禁用 将权限编码加入系统级禁用列表
            if (isSystemDisabled) systemDisabledList.add(permCode);
            // 如果是租户级禁用 将权限编码加入租户级禁用列表
            if (isTenantDisabled) tenantDisabledList.add(permCode);
            // 如果是角色级禁用 将权限编码加入角色级禁用列表
            if (isRoleDisabled) roleDisabledList.add(permCode);
            // 如果是用户级禁用 将权限编码加入用户级禁用列表
            if (isUserDisabled) userDisabledList.add(permCode);

            // 判断权限是否无效 任意一个层级禁用 即为无效权限
            if (isSystemDisabled || isTenantDisabled || isRoleDisabled || isUserDisabled) {
                invalidPermCodeList.add(permCode);
            } else if (hasActiveMap.getOrDefault(permId, false)) {
                validPermCodeList.add(permCode);
            }
        }

        // TODO 查询角色信息

        // 实例化[级联禁用]信息对象 用于封装各层级失效的权限数据
        UserContextDTO.CascadeDisabled cascadeDisabled = new UserContextDTO.CascadeDisabled();
        // 为级联禁用对象设置[系统级]失效权限列表
        cascadeDisabled.setSystemDisabled(systemDisabledList);
        // 为级联禁用对象设置[租户级]失效权限列表
        cascadeDisabled.setTenantDisabled(tenantDisabledList);
        // 为级联禁用对象设置[角色级]失效权限列表
        cascadeDisabled.setRoleDisabled(roleDisabledList);
        // 为级联禁用对象设置[用户级]失效权限列表
        cascadeDisabled.setUserDisabled(userDisabledList);

        // 实例化[字段权限]信息对象 用于封装查询/新增/更新的字段权限
        UserContextDTO.FieldPerm fieldPerm = new UserContextDTO.FieldPerm();
        // 为字段权限对象设置[查询操作]的字段权限映射
        fieldPerm.setQuery(queryPermMap);
        // 为字段权限对象设置[新增操作]的字段权限映射
        fieldPerm.setCreate(createPermMap);
        // 为字段权限对象设置[更新操作]的字段权限映射
        fieldPerm.setUpdate(updatePermMap);

        // 实例化[权限信息]对象 用于封装用户所有权限相关数据
        UserContextDTO.PermInfo permInfo = new UserContextDTO.PermInfo();
        // 设置全量权限编码列表[有效+无效]
        permInfo.setPerms(allPermCodeList);
        // 设置有效权限编码列表
        permInfo.setValidPerms(validPermCodeList);
        // 设置无效权限编码列表
        permInfo.setInvalidPerms(invalidPermCodeList);
        // 设置级联禁用信息
        permInfo.setCascadeDisabled(cascadeDisabled);
        // 设置字段权限信息
        permInfo.setFieldPerm(fieldPerm);

        // 实例化[租户上下文信息]对象 用于封装用户所属租户数据
        UserContextDTO.TenantInfo tenantInfoCache = new UserContextDTO.TenantInfo();
        // 设置租户ID
        tenantInfoCache.setTenantId(loginJoinInfo.getTenantId());
        // 设置租户名称
        tenantInfoCache.setTenantName(loginJoinInfo.getTenantName());
        // 设置租户编码
        tenantInfoCache.setTenantCode(loginJoinInfo.getTenantCode());
        // 设置租户状态
        tenantInfoCache.setTenantStatus(loginJoinInfo.getTenantStatus());

        // 查询当前用户关联的所有租户，按状态分类为有效租户和无效租户
        List<UserTenantItemDTO> allTenantList = iSysUserTenantRelService.queryUserAllTenants(loginJoinInfo.getUserId());
        List<UserContextDTO.TenantItemInfo> validTenants = new ArrayList<>();
        List<UserContextDTO.TenantItemInfo> invalidTenants = new ArrayList<>();
        for (UserTenantItemDTO item : allTenantList) {
            UserContextDTO.TenantItemInfo tenantItem = new UserContextDTO.TenantItemInfo();
            tenantItem.setTenantId(item.getTenantId());
            tenantItem.setTenantCode(item.getTenantCode());
            tenantItem.setTenantName(item.getTenantName());
            tenantItem.setTenantStatus(item.getTenantStatus());
            if (GlobalEnum.TenantStatus.ENABLED.getCode().equals(item.getTenantStatus())) {
                validTenants.add(tenantItem);
            } else {
                invalidTenants.add(tenantItem);
            }
        }

        // 实例化[用户上下文信息]对象 整合所有用户登录后的核心信息
        UserContextDTO userContext = new UserContextDTO();
        // 为用户上下文设置租户信息
        userContext.setTenantInfo(tenantInfoCache);
        // 为用户上下文设置有效租户列表
        userContext.setValidTenants(validTenants);
        // 为用户上下文设置无效租户列表
        userContext.setInvalidTenants(invalidTenants);
        // 为用户上下文设置权限信息
        userContext.setPermInfo(permInfo);

        // 将用户上下文存入Session 供后续请求使用
        StpUtil.getSession().set("userContext", userContext);
        return ApiResponse.success();

    }

}