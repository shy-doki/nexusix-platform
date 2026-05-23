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
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

        // 1. 根据用户名查询用户基本信息
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, param.getUsername())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUser loginUserInfo = iSysUserService.getOne(userWrapper);

        if (loginUserInfo == null) {
            // 不区分用户名不存在还是密码错误，防止用户名枚举攻击
            throw new BusinessException("用户名或密码不正确");
        }

        // 2. 验证密码
        if (!loginUserInfo.getPassword().equals(param.getPassword())) {
            // 不区分用户名不存在还是密码错误，防止用户名枚举攻击
            throw new BusinessException("用户名或密码不正确");
        }

        // 3. 查询用户默认租户关系
        LambdaQueryWrapper<SysUserTenantRel> userTenantRelWrapper = new LambdaQueryWrapper<SysUserTenantRel>()
                .eq(SysUserTenantRel::getUserId, loginUserInfo.getId())
                .eq(SysUserTenantRel::getIsDefault, GlobalEnum.DefaultTenant.DEFAULT.getCode())
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysUserTenantRel userTenantRelInfo = iSysUserTenantRelService.getOne(userTenantRelWrapper);

        if (userTenantRelInfo == null) {
            // 用户未绑定默认租户，无法确定登录上下文
            throw new BusinessException("用户未加入任何租户");
        }

        // 4. 查询租户信息并校验状态
        LambdaQueryWrapper<SysTenant> tenantWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, userTenantRelInfo.getTenantId())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant tenant = iSysTenantService.getOne(tenantWrapper);

        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(tenant.getStatus())) {
            // 租户被管理员停用，禁止登录
            throw new BusinessException("所属租户已停用");
        }
        if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(tenant.getStatus())) {
            // 租户已超过有效期，禁止登录
            throw new BusinessException("所属租户已过期");
        }

        // 5. Sa-Token登录
        StpUtil.login(loginUserInfo.getId());

        // 6. 优先从缓存获取用户权限上下文
        UserContextDTO cachedContext = (UserContextDTO) StpUtil.getSession().get("userContext");
        if (cachedContext != null) {
            // 缓存命中，权限上下文已存在，直接返回登录成功
            return ApiResponse.success();
        }

        // 7. 查询用户权限策略关联（基于用户-租户关系ID）
        LambdaQueryWrapper<SysUserPermRel> userPermRelWrapper = new LambdaQueryWrapper<SysUserPermRel>()
                .eq(SysUserPermRel::getUserId, userTenantRelInfo.getId())
                .eq(SysUserPermRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        List<SysUserPermRel> userPermRelList = iSysUserPermRelService.list(userPermRelWrapper);

        // 8. 批量查询权限策略详情
        List<SysPermPolicy> permPolicyList = new ArrayList<>();
        if (!userPermRelList.isEmpty()) {
            // 收集所有策略ID，用于批量查询策略详情
            List<Long> policyIdList = new ArrayList<>(userPermRelList.size());
            for (SysUserPermRel rel : userPermRelList) {
                policyIdList.add(rel.getPolicyId());
            }
            LambdaQueryWrapper<SysPermPolicy> permPolicyWrapper = new LambdaQueryWrapper<SysPermPolicy>()
                    .in(SysPermPolicy::getId, policyIdList)
                    .eq(SysPermPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            permPolicyList = iSysPermPolicyService.list(permPolicyWrapper);
        }

        // 9. 批量查询权限资源详情
        List<SysPerm> permList = new ArrayList<>();
        if (!permPolicyList.isEmpty()) {
            // 使用Set去重，同一权限资源可能被多个策略引用
            Set<Long> permIdSet = new HashSet<>(permPolicyList.size());
            for (SysPermPolicy policy : permPolicyList) {
                permIdSet.add(policy.getPermId());
            }
            LambdaQueryWrapper<SysPerm> permWrapper = new LambdaQueryWrapper<SysPerm>()
                    .in(SysPerm::getId, permIdSet)
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
            permList = iSysPermService.list(permWrapper);
        }

        // 10. 构建权限编码映射（permId -> permCode）
        Map<Long, String> permIdToCodeMap = new HashMap<>(permList.size());
        for (SysPerm perm : permList) {
            permIdToCodeMap.put(perm.getId(), perm.getPermCode());
        }

        // 11. 禁用层级优先级（系统 > 租户 > 角色 > 用户），遍历分离有效/无效策略的permId
        // 数字越大优先级越高，高优先级的禁用状态会覆盖低优先级
        Map<String, Integer> disablePriorityMap = new HashMap<>(4);
        disablePriorityMap.put(GlobalEnum.PermPolicyStatus.DISABLED_SYSTEM_LEVEL.getCode(), 4);
        disablePriorityMap.put(GlobalEnum.PermPolicyStatus.DISABLED_TENANT_LEVEL.getCode(), 3);
        disablePriorityMap.put(GlobalEnum.PermPolicyStatus.DISABLED_ROLE_LEVEL.getCode(), 2);
        disablePriorityMap.put(GlobalEnum.PermPolicyStatus.DISABLED_USER_LEVEL.getCode(), 1);

        Set<Long> activePolicyPermIdSet = new HashSet<>();
        // 记录每个permId被禁用时最高优先级的层级
        Map<Long, String> disabledPermIdLevelMap = new HashMap<>();
        for (SysPermPolicy policy : permPolicyList) {
            String status = policy.getStatus();
            if (GlobalEnum.PermPolicyStatus.ACTIVE.getCode().equals(status)) {
                // 策略生效，记录permId
                activePolicyPermIdSet.add(policy.getPermId());
            } else {
                // 策略被禁用，保留最高优先级的禁用层级
                String existingLevel = disabledPermIdLevelMap.get(policy.getPermId());
                int newPriority = disablePriorityMap.getOrDefault(status, 0);
                int existingPriority = existingLevel == null ? -1 : disablePriorityMap.getOrDefault(existingLevel, 0);
                if (newPriority > existingPriority) {
                    disabledPermIdLevelMap.put(policy.getPermId(), status);
                }
            }
        }

        // 12. 根据策略状态提取有效/无效权限编码（无效权限携带禁用层级）
        List<String> allPermCodeList = new ArrayList<>(permList.size());
        // 有效权限编码列表，用于接口鉴权
        List<String> validPermCodeList = new ArrayList<>();
        // 无效权限映射：permCode -> 禁用层级，用于前端禁用控制
        Map<String, String> invalidPermMap = new HashMap<>();

        for (SysPerm perm : permList) {
            String permCode = perm.getPermCode();
            allPermCodeList.add(permCode);

            if (disabledPermIdLevelMap.containsKey(perm.getId())) {
                // 权限被禁用，记录禁用层级
                invalidPermMap.put(permCode, disabledPermIdLevelMap.get(perm.getId()));
            } else if (activePolicyPermIdSet.contains(perm.getId())) {
                // 权限生效，加入有效列表
                validPermCodeList.add(permCode);
            }
        }

        // 13. 单次遍历构建三种类型的字段权限Map（支持同表多策略字段合并）
        // queryPermMap：查询可见/不可见字段；createPermMap：创建可见/不可见字段；updatePermMap：更新可见/不可见字段
        Map<String, UserContextDTO.EntityFieldPerm> queryPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> createPermMap = new HashMap<>();
        Map<String, UserContextDTO.EntityFieldPerm> updatePermMap = new HashMap<>();

        for (SysPermPolicy policy : permPolicyList) {
            // 跳过已删除或字段为空的策略
            if (policy.getFieldOperates() == null || policy.getFieldOperates().isEmpty()) {
                continue;
            }

            // 根据访问类型选择对应的Map
            Map<String, UserContextDTO.EntityFieldPerm> targetMap = null;
            String accessType = policy.getAccessType();
            if (GlobalEnum.PermPolicyAccessType.QUERY.getCode().equals(accessType)) {
                targetMap = queryPermMap;
            } else if (GlobalEnum.PermPolicyAccessType.CREATE.getCode().equals(accessType)) {
                targetMap = createPermMap;
            } else if (GlobalEnum.PermPolicyAccessType.UPDATE.getCode().equals(accessType)) {
                targetMap = updatePermMap;
            } else {
                // 未知访问类型，跳过
                continue;
            }

            // 获取或创建该表的字段权限对象（key为表名）
            String tableName = policy.getTableName();
            UserContextDTO.EntityFieldPerm fieldPerm = targetMap.computeIfAbsent(tableName, 
                    k -> new UserContextDTO.EntityFieldPerm());

            // 解析字段列表（JSON数组格式）
            List<String> fields = JSON.parseArray(policy.getFieldOperates(), String.class);
            if (fields == null || fields.isEmpty()) {
                continue;
            }

            // 根据策略状态合并到可见或不可见字段列表
            if (GlobalEnum.PermPolicyStatus.ACTIVE.getCode().equals(policy.getStatus())) {
                // 生效策略：字段加入可见列表
                List<String> visibleFields = fieldPerm.getVisibleFields();
                if (visibleFields == null) {
                    fieldPerm.setVisibleFields(new ArrayList<>(fields));
                } else {
                    // 同表多策略字段合并
                    visibleFields.addAll(fields);
                }
            } else {
                // 禁用策略：字段加入不可见列表，保留最高优先级禁用层级
                String disableLevel = policy.getStatus();
                int newPriority = disablePriorityMap.getOrDefault(disableLevel, 0);
                Map<String, String> invisibleFields = fieldPerm.getInvisibleFields();
                if (invisibleFields == null) {
                    invisibleFields = new HashMap<>();
                    fieldPerm.setInvisibleFields(invisibleFields);
                }
                for (String field : fields) {
                    // 同一字段可能被多个策略禁用，保留最高优先级的禁用层级
                    String existingLevel = invisibleFields.get(field);
                    int existingPriority = existingLevel == null ? -1 : disablePriorityMap.getOrDefault(existingLevel, 0);
                    if (newPriority > existingPriority) {
                        invisibleFields.put(field, disableLevel);
                    }
                }
            }
        }

        // 14. 组装用户权限上下文
        // 权限信息：全量权限编码、有效权限编码、无效权限映射、字段级权限
        UserContextDTO.PermInfo permInfo = new UserContextDTO.PermInfo();
        permInfo.setPerms(allPermCodeList);
        permInfo.setValidPerms(validPermCodeList);
        permInfo.setInvalidPerms(invalidPermMap);
        permInfo.setQuery(queryPermMap);
        permInfo.setCreate(createPermMap);
        permInfo.setUpdate(updatePermMap);

        // 租户信息：当前默认租户名称和编码
        UserContextDTO.TenantInfo tenantInfo = new UserContextDTO.TenantInfo();
        tenantInfo.setTenantName(tenant.getTenantName());
        tenantInfo.setTenantCode(tenant.getTenantCode());

        // 组装完整的用户上下文
        UserContextDTO userContext = new UserContextDTO();
        userContext.setTenantInfo(tenantInfo);
        userContext.setPermInfo(permInfo);

        // TODO 查询角色信息

        // 15. 缓存到Sa-Token Session（Redis持久化），后续请求可直接从缓存读取权限上下文
        StpUtil.getSession().set("userContext", userContext);

        return ApiResponse.success();

    }

}
