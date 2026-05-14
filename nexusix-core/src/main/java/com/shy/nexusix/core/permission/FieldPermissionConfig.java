package com.shy.nexusix.core.permission;

import com.shy.nexusix.common.annotation.FieldSecurityLevel;
import com.shy.nexusix.common.enums.SecurityLevel;
import com.shy.nexusix.core.user.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 字段权限配置引擎 - 混合方案（注解 > 例外配置 > 默认规则）
 * </p>
 * <p>
 * 三级优先级：
 * <ol>
 *   <li>注解配置：Entity字段上的 @FieldSecurityLevel 注解（最高优先级）</li>
 *   <li>例外配置：通过 registerException() 手动注册的例外规则</li>
 *   <li>默认规则：基于字段名模式自动推导安全级别（最低优先级）</li>
 * </ol>
 * </p>
 * <p>
 * 安全级别与角色映射：
 * <ul>
 *   <li>PUBLIC → 所有角色可见</li>
 *   <li>INTERNAL → 管理员及以上可见</li>
 *   <li>CONFIDENTIAL → 仅超级管理员可见</li>
 * </ul>
 * </p>
 *
 * @author shy
 * @since 2026-05-14
 */
@Component
public class FieldPermissionConfig {

    private static final Logger log = LoggerFactory.getLogger(FieldPermissionConfig.class);

    /**
     * 字段安全级别缓存
     * Key: 实体类全限定名
     * Value: 字段名 → 安全级别映射
     */
    private final Map<String, Map<String, SecurityLevel>> fieldSecurityCache = new ConcurrentHashMap<>();

    /**
     * 例外配置（手动覆盖）
     * Key: 实体类全限定名
     * Value: 字段名 → 安全级别映射
     */
    private final Map<String, Map<String, SecurityLevel>> exceptionConfig = new ConcurrentHashMap<>();

    /**
     * 获取当前用户对指定实体类的可见字段列表
     *
     * @param entityClass 实体类
     * @return 可见字段名列表（驼峰命名），null表示不限制（查询所有字段）
     */
    public List<String> getVisibleFields(Class<?> entityClass) {
        SecurityLevel userLevel = getUserSecurityLevel();

        // 超级管理员不限制字段
        if (userLevel == SecurityLevel.CONFIDENTIAL) {
            return null;
        }

        Map<String, SecurityLevel> fieldLevels = getFieldSecurityLevels(entityClass);

        return fieldLevels.entrySet().stream()
                .filter(entry -> entry.getValue().ordinal() <= userLevel.ordinal())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 获取指定实体类的字段安全级别映射（带缓存）
     * 优先级：注解 > 例外配置 > 默认规则
     *
     * @param entityClass 实体类
     * @return 字段名 → 安全级别映射
     */
    public Map<String, SecurityLevel> getFieldSecurityLevels(Class<?> entityClass) {
        return fieldSecurityCache.computeIfAbsent(
                entityClass.getName(),
                name -> scanFieldSecurityLevels(entityClass)
        );
    }

    /**
     * 注册例外配置（手动覆盖指定字段的安全级别）
     * 优先级高于默认规则，低于注解配置
     *
     * @param entityClass 实体类
     * @param fieldName   字段名
     * @param level       安全级别
     */
    public void registerException(Class<?> entityClass, String fieldName, SecurityLevel level) {
        exceptionConfig.computeIfAbsent(entityClass.getName(), k -> new HashMap<>())
                .put(fieldName, level);
        // 清除缓存，使下次查询时重新计算
        fieldSecurityCache.remove(entityClass.getName());
        log.info("[字段权限] 注册例外配置: entity={}, field={}, level={}", entityClass.getSimpleName(), fieldName, level);
    }

    /**
     * 批量注册例外配置
     *
     * @param entityClass 实体类
     * @param exceptions  字段名 → 安全级别映射
     */
    public void registerExceptions(Class<?> entityClass, Map<String, SecurityLevel> exceptions) {
        exceptionConfig.computeIfAbsent(entityClass.getName(), k -> new HashMap<>())
                .putAll(exceptions);
        fieldSecurityCache.remove(entityClass.getName());
        log.info("[字段权限] 批量注册例外配置: entity={}, count={}", entityClass.getSimpleName(), exceptions.size());
    }

    /**
     * 清除指定实体类的缓存
     *
     * @param entityClass 实体类
     */
    public void invalidateCache(Class<?> entityClass) {
        fieldSecurityCache.remove(entityClass.getName());
        log.info("[字段权限] 清除缓存: entity={}", entityClass.getSimpleName());
    }

    /**
     * 清除所有缓存
     */
    public void invalidateAllCache() {
        fieldSecurityCache.clear();
        log.info("[字段权限] 清除所有缓存");
    }

    /**
     * 扫描实体类的字段安全级别
     * 优先级：注解 > 例外配置 > 默认规则
     */
    private Map<String, SecurityLevel> scanFieldSecurityLevels(Class<?> entityClass) {
        Map<String, SecurityLevel> levels = new LinkedHashMap<>();
        Class<?> currentClass = entityClass;

        while (currentClass != null && currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                // 跳过静态字段和serialVersionUID
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                String fieldName = field.getName();

                // 优先级1：注解配置
                FieldSecurityLevel annotation = field.getAnnotation(FieldSecurityLevel.class);
                if (annotation != null) {
                    levels.put(fieldName, annotation.value());
                    log.debug("[字段权限扫描] 注解配置: {}.{}={}", entityClass.getSimpleName(), fieldName, annotation.value());
                    continue;
                }

                // 优先级2：例外配置
                Map<String, SecurityLevel> entityExceptions = exceptionConfig.get(entityClass.getName());
                if (entityExceptions != null && entityExceptions.containsKey(fieldName)) {
                    levels.put(fieldName, entityExceptions.get(fieldName));
                    log.debug("[字段权限扫描] 例外配置: {}.{}={}", entityClass.getSimpleName(), fieldName, entityExceptions.get(fieldName));
                    continue;
                }

                // 优先级3：默认规则推导
                SecurityLevel inferred = inferSecurityLevel(fieldName);
                levels.put(fieldName, inferred);
                log.debug("[字段权限扫描] 默认规则: {}.{}={}", entityClass.getSimpleName(), fieldName, inferred);
            }

            currentClass = currentClass.getSuperclass();
        }

        log.info("[字段权限扫描] 实体类={} 共扫描 {} 个字段", entityClass.getSimpleName(), levels.size());
        return levels;
    }

    /**
     * 基于字段名模式推导安全级别（默认规则）
     *
     * @param fieldName 字段名
     * @return 推导的安全级别
     */
    private SecurityLevel inferSecurityLevel(String fieldName) {
        String name = fieldName.toLowerCase();

        // CONFIDENTIAL：仅超级管理员可见
        if (name.contains("deleted") || name.contains("password") || name.contains("pwd")
                || name.contains("salt") || name.contains("secret") || name.contains("token")
                || name.contains("credential")) {
            return SecurityLevel.CONFIDENTIAL;
        }

        // 默认：PUBLIC，所有角色可见
        return SecurityLevel.PUBLIC;
    }

    /**
     * 获取当前用户的安全级别
     *
     * @return 用户安全级别
     */
    private SecurityLevel getUserSecurityLevel() {
        try {
            if (!UserContext.isLogin()) {
                return SecurityLevel.PUBLIC;
            }

            List<String> roles = UserContext.getCurrentRoles();

            // 超级管理员
            if (UserContext.isSuperAdmin() || roles.contains("超级管理员")) {
                return SecurityLevel.CONFIDENTIAL;
            }

            // 管理员
            if (roles.contains("管理员")) {
                return SecurityLevel.INTERNAL;
            }

            // 普通用户
            return SecurityLevel.PUBLIC;
        } catch (Exception e) {
            // 未登录或获取用户信息失败，返回最低权限
            log.debug("[字段权限] 获取用户安全级别失败，使用最低权限: {}", e.getMessage());
            return SecurityLevel.PUBLIC;
        }
    }

}
