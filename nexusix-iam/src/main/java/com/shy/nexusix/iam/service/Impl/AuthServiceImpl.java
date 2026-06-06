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

        // 记录每个权限ID在各层级是否存在禁用状态
        Map<Long, Boolean> hasSystemDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasTenantDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasRoleDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasUserDisabledMap = new HashMap<>();
        Map<Long, Boolean> hasActiveMap = new HashMap<>();

        // 权限ID到权限编码的映射 方便后续按ID查找
        Map<Long, String> permIdToCodeMap = new HashMap<>();

        // 按操作类型分类的字段权限  Map：key为表名 value为该表的字段权限配置
        Map<String, UserContextDTO.EntityFieldPerm> queryPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> createPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> updatePermMap = new HashMap<>();

        // 第一轮遍历 收集所有权限编码 记录各权限在不同层级的禁用状态 解析字段级权限
        for (UserPermJoinDTO row : permJoinList) {
            Long permId = row.getPermId();
            String permCode = row.getPermCode();
            String status = row.getPolicyStatus();

            // 对同一权限ID进行去重 确保每个权限编码只在全量列表中出现一次
            if (permId != null && permCode != null && !seenPermIdSet.contains(permId)) {
                seenPermIdSet.add(permId);
                permIdToCodeMap.put(permId, permCode);
                allPermCodeList.add(permCode);
            }

            // 根据策略状态标记该权限在各层级的禁用情况
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

            // 获取字段级权限控制的元数据
            String fieldOperates = row.getFieldOperates();
            String accessType = row.getAccessType();
            String tableName = row.getTableName();

            // 若字段操作、操作类型或表名为空，则跳过该行不处理字段权限
            if (fieldOperates == null || fieldOperates.isEmpty()
                    || accessType == null || tableName == null) {
                continue;
            }

            // 根据操作类型（查询/新增/更新）选择对应的字段权限Map
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

            // 解析JSON数组格式的字段名称列表
            List<String> fields = JSON.parseArray(fieldOperates, String.class);
            if (fields == null || fields.isEmpty()) {
                continue;
            }

            // 获取或创建该表对应的字段权限对象
            UserContextDTO.EntityFieldPerm fieldPerm = targetMap.computeIfAbsent(tableName,
                    k -> new UserContextDTO.EntityFieldPerm());

            // 若策略为激活状态，字段加入可见列表；否则加入不可见列表
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

        // 第二轮遍历：对每个去重后的权限ID，汇总其禁用状态并分类到对应列表
        for (Long permId : seenPermIdSet) {
            String permCode = permIdToCodeMap.get(permId);

            // 获取该权限在各层级的禁用标记
            boolean isSystemDisabled = hasSystemDisabledMap.getOrDefault(permId, false);
            boolean isTenantDisabled = hasTenantDisabledMap.getOrDefault(permId, false);
            boolean isRoleDisabled = hasRoleDisabledMap.getOrDefault(permId, false);
            boolean isUserDisabled = hasUserDisabledMap.getOrDefault(permId, false);

            // 按禁用层级分别归类
            if (isSystemDisabled) systemDisabledList.add(permCode);
            if (isTenantDisabled) tenantDisabledList.add(permCode);
            if (isRoleDisabled) roleDisabledList.add(permCode);
            if (isUserDisabled) userDisabledList.add(permCode);

            // 任一层级禁用则该权限视为无效；只有全层级均为激活状态才视为有效
            if (isSystemDisabled || isTenantDisabled || isRoleDisabled || isUserDisabled) {
                invalidPermCodeList.add(permCode);
            } else if (hasActiveMap.getOrDefault(permId, false)) {
                validPermCodeList.add(permCode);
            }
        }

        // TODO 查询角色信息

        // 构建级联禁用信息：记录各层级分别禁用了哪些权限编码
        UserContextDTO.CascadeDisabled cascadeDisabled = new UserContextDTO.CascadeDisabled();
        cascadeDisabled.setSystemDisabled(systemDisabledList);
        cascadeDisabled.setTenantDisabled(tenantDisabledList);
        cascadeDisabled.setRoleDisabled(roleDisabledList);
        cascadeDisabled.setUserDisabled(userDisabledList);

        // 构建字段权限信息：按查询/新增/更新三类操作分别存储字段可见性规则
        UserContextDTO.FieldPerm fieldPerm = new UserContextDTO.FieldPerm();
        fieldPerm.setQuery(queryPermMap);
        fieldPerm.setCreate(createPermMap);
        fieldPerm.setUpdate(updatePermMap);

        // 构建权限信息对象：聚齐全量、有效、无效权限编码及级联禁用、字段权限
        UserContextDTO.PermInfo permInfo = new UserContextDTO.PermInfo();
        permInfo.setPerms(allPermCodeList);
        permInfo.setValidPerms(validPermCodeList);
        permInfo.setInvalidPerms(invalidPermCodeList);
        permInfo.setCascadeDisabled(cascadeDisabled);
        permInfo.setFieldPerm(fieldPerm);

        // 构建租户上下文信息
        UserContextDTO.TenantInfo tenantInfoCache = new UserContextDTO.TenantInfo();
        tenantInfoCache.setTenantName(loginJoinInfo.getTenantName());
        tenantInfoCache.setTenantCode(loginJoinInfo.getTenantCode());

        // 组装完整的用户上下文对象，包含租户信息和权限信息
        UserContextDTO userContext = new UserContextDTO();
        userContext.setTenantInfo(tenantInfoCache);
        userContext.setPermInfo(permInfo);

        // 将用户上下文存入Session，供后续请求使用
        StpUtil.getSession().set("userContext", userContext);
        return ApiResponse.success();

    }

}