package com.shy.nexusix.iam.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.core.entity.ColumnPerm;
import com.shy.nexusix.iam.entity.*;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.rto.RegisterRTO;
import com.shy.nexusix.iam.service.IAuthService;
import com.shy.nexusix.iam.service.ISysUserPermRelService;
import com.shy.nexusix.iam.service.ISysUserRoleRelService;
import com.shy.nexusix.iam.service.ISysUserTenantRelService;
import com.shy.nexusix.iam.vo.LoginVO;
import com.shy.nexusix.iam.vo.RegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 认证服务实现类 - 提供登录、注册等认证功能的具体实现
 * </p>
 * <p>
 * 登录流程：验证用户 → Sa-Token登录 → 加载权限角色到Session → 选择默认租户 → 返回Token
 * 注册流程：校验唯一性 → BCrypt加密 → 创建用户记录
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Service
public class IAuthServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements IAuthService {

    @Autowired
    private ISysUserTenantRelService iSysUserTenantRelService;

    @Autowired
    private ISysUserPermRelService iSysUserPermRelService;

    @Autowired
    private ISysUserRoleRelService iSysUserRoleRelService;

    /**
     * <p>
     * 用户登录
     * </p>
     * <p>
     * 完整登录流程：
     * 1. 根据用户名查询用户
     * 2. 校验用户状态（是否启用）
     * 3. BCrypt密码校验
     * 4. Sa-Token执行登录
     * 5. 加载权限和角色信息到Session
     * 6. 自动选择默认租户
     * 7. 更新最后登录信息
     * </p>
     *
     * @param loginRTO 登录请求参数
     * @return 登录响应结果
     * @throws BusinessException 用户不存在、密码错误、用户被禁用
     * @author shy
     * @since 2026-05-17
     */
    @Override
    public String login(LoginRTO loginRTO) {
        SysUser user = getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, loginRTO.getUsername())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        if (!GlobalEnum.UserStatus.ENABLED.getCode().equals(user.getStatus())) {
            throw new BusinessException(403, "用户已被禁用，请联系管理员");
        }

        if (!Objects.equals(loginRTO.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        StpUtil.login(user.getId());

        // 查询用户默认租户关联关系
        Optional<SysUserTenantRel> tenantRelOpt = iSysUserTenantRelService.lambdaQuery()
                .eq(SysUserTenantRel::getUserCode, user.getUserCode())
                .eq(SysUserTenantRel::getIsDefault, true)
                .eq(SysUserTenantRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .oneOpt();

        // 存入 Session 并获取租户编码
        String tenantCode = null;
        if (tenantRelOpt.isPresent()) {
            SysUserTenantRel rel = tenantRelOpt.get();
            tenantCode = rel.getTenantCode();
            StpUtil.getSession().set("tenantId", rel.getTenantCode());
            StpUtil.getSession().set("tenantName", rel.getTenantName());
        }

        // 计算登录用户权限信息 [系统级、租户级、角色级、用户级]
        List<SysUserPermRel> userPermRels = iSysUserPermRelService.lambdaQuery()
                .eq(SysUserPermRel::getUserCode, user.getUserCode())
                .eq(SysUserPermRel::getTenantCode, tenantCode)
                .eq(SysUserPermRel::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .list();

        // 提取所有权限编码并存入 Session
        Set<String> permCodes = userPermRels.stream()
                .map(SysUserPermRel::getPermCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        StpUtil.getSession().set("permCodes", permCodes);

        // 提前有效权限存入 Session
        Set<String> validPerms = userPermRels.stream()
                .filter(rel -> "ALLOW".equals(rel.getAction()))
                .map(SysUserPermRel::getPermCode)
                .collect(Collectors.toSet());
        StpUtil.getSession().set("validPerms", validPerms);

        // 提取无效权限编码并存入 Session
        Set<String> invalidPerms = userPermRels.stream()
                .filter(rel -> "DENY".equals(rel.getAction()))
                .map(SysUserPermRel::getPermCode)
                .collect(Collectors.toSet());
        StpUtil.getSession().set("invalidPerms", invalidPerms);

//        // 提取所有有效操作字段并按表名和操作类型分组存入 Session
//        Map<String, Map<String, Set<String>>> fieldPermissions = userPermRels.stream()
//                .filter(rel -> rel.getFieldOperates() != null && !rel.getFieldOperates().isEmpty())
//                .filter(rel -> "ALLOW".equals(rel.getAction()))
//                .collect(Collectors.groupingBy(
//                        SysUserPermRel::getTableName,
//                        Collectors.groupingBy(
//                                SysUserPermRel::getAccessType,
//                                Collectors.flatMapping(
//                                        rel -> Arrays.stream(rel.getFieldOperates().split(",")),
//                                        Collectors.toSet()
//                                )
//                        )
//                ));
//
//        StpUtil.getSession().set("fieldPermissions", fieldPermissions);
//
//        // 提取所有禁用字段并按表名和操作类型分组存入 Session
//        Map<String, Map<String, Set<String>>> unFieldPermissions = userPermRels.stream()
//                .filter(rel -> rel.getFieldUnOperate() != null && !rel.getFieldUnOperate().isEmpty())
//                .filter(rel -> "DENY".equals(rel.getAction()))
//                .collect(Collectors.groupingBy(
//                        SysUserPermRel::getTableName,
//                        Collectors.groupingBy(
//                                SysUserPermRel::getAccessType,
//                                Collectors.flatMapping(
//                                        rel -> Arrays.stream(rel.getFieldUnOperate().split(",")),
//                                        Collectors.toSet()
//                                )
//                        )
//                ));
//
//        StpUtil.getSession().set("unFieldPermissions", unFieldPermissions);

        // 提取所有有效操作字段并按表名和操作类型分组
        Map<String, Map<String, Set<String>>> fieldPermissionsMap = userPermRels.stream()
                .filter(rel -> rel.getFieldOperates() != null && !rel.getFieldOperates().isEmpty())
                .filter(rel -> "ALLOW".equals(rel.getAction()))
                .collect(Collectors.groupingBy(
                        SysUserPermRel::getTableName,
                        Collectors.groupingBy(
                                SysUserPermRel::getAccessType,
                                Collectors.flatMapping(
                                        rel -> parseJsonArray(rel.getFieldOperates()).stream(),
                                        Collectors.toSet()
                                )
                        )
                ));

        ColumnPerm columnPerm = new ColumnPerm();
        columnPerm.setQuery(extractFieldsByType(fieldPermissionsMap, GlobalConstant.OperableType.QUERY_TYPE));
        columnPerm.setCreate(extractFieldsByType(fieldPermissionsMap, GlobalConstant.OperableType.CREATE_TYPE));
        columnPerm.setUpdate(extractFieldsByType(fieldPermissionsMap, GlobalConstant.OperableType.UPDATE_TYPE));

        StpUtil.getSession().set(GlobalConstant.RedisKey.OPERABLE_COLUMNS, columnPerm);

        // 提取所有禁用字段并按表名和操作类型分组
        Map<String, Map<String, Set<String>>> unFieldPermissionsMap = userPermRels.stream()
                .filter(rel -> rel.getFieldUnOperate() != null && !rel.getFieldUnOperate().isEmpty())
                .filter(rel -> "DENY".equals(rel.getAction()))
                .collect(Collectors.groupingBy(
                        SysUserPermRel::getTableName,
                        Collectors.groupingBy(
                                SysUserPermRel::getAccessType,
                                Collectors.flatMapping(
                                        rel -> parseJsonArray(rel.getFieldUnOperate()).stream(),
                                        Collectors.toSet()
                                )
                        )
                ));

        ColumnPerm unColumnPerm = new ColumnPerm();
        unColumnPerm.setQuery(extractFieldsByType(unFieldPermissionsMap, GlobalConstant.OperableType.QUERY_TYPE));
        unColumnPerm.setCreate(extractFieldsByType(unFieldPermissionsMap, GlobalConstant.OperableType.CREATE_TYPE));
        unColumnPerm.setUpdate(extractFieldsByType(unFieldPermissionsMap, GlobalConstant.OperableType.UPDATE_TYPE));

        StpUtil.getSession().set(GlobalConstant.RedisKey.UN_OPERABLE_COLUMNS, unColumnPerm);

        // 计算登录用户角色信息 [系统级、租户级、用户级] 用户和租户绑定值去查sys_user_role_rel

        return "登录成功";

    }

    /**
     * <p>
     * 用户注册
     * </p>
     * <p>
     * 注册流程：
     * 1. 校验用户名唯一性
     * 2. BCrypt加密密码
     * 3. 创建用户记录
     * </p>
     *
     * @param registerRTO 注册请求参数
     * @return 注册响应结果
     * @throws BusinessException 用户名已存在
     * @author shy
     * @since 2026-05-17
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterVO register(RegisterRTO registerRTO) {

        SysUser user = new SysUser();
        user.setUsername(registerRTO.getUsername());
        user.setPassword(registerRTO.getPassword());
        user.setNickname(registerRTO.getNickname());
        user.setEmail(registerRTO.getEmail());
        user.setPhone(registerRTO.getPhone());
        user.setStatus(GlobalEnum.UserStatus.ENABLED.getCode());
        user.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        save(user);

        RegisterVO vo = new RegisterVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        return vo;
    }

    /**
     * 解析JSONB数组字符串为Set集合
     * PostgreSQL的JSONB字段读取后格式如：["field1","field2","field3"]
     *
     * @param jsonArrayStr JSON数组字符串
     * @return 字段名集合
     */
    private Set<String> parseJsonArray(String jsonArrayStr) {
        if (jsonArrayStr == null || jsonArrayStr.isEmpty()) {
            return Collections.emptySet();
        }
        try {
            // 清理JSON格式符号
            String cleaned = jsonArrayStr.replaceAll("[\\[\\]\"]", "").trim();
            if (cleaned.isEmpty()) {
                return Collections.emptySet();
            }
            // 按逗号分割并去除空白
            return Arrays.stream(cleaned.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    /**
     * 从嵌套Map中提取指定操作类型的字段权限
     * @param permissionsMap 权限Map (表名 -> 操作类型 -> 字段集合)
     * @param operationType 操作类型 (query/create/update)
     * @return 表名到字段集合的映射
     */
    private Map<String, Set<String>> extractFieldsByType(Map<String, Map<String, Set<String>>> permissionsMap, String operationType) {
        return permissionsMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getOrDefault(operationType, Collections.emptySet())
                ));
    }

}
