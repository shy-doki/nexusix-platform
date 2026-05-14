package com.shy.nexusix.core.permission;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.shy.nexusix.core.user.UserContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 字段权限拦截器 - 自动根据用户角色裁剪SQL查询字段
 * </p>
 * <p>
 * 拦截所有SELECT查询，根据当前用户角色动态修改SQL的SELECT子句，
 * 只查询用户有权查看的字段，从数据库层面控制字段级数据访问。
 * </p>
 * <p>
 * 执行流程：
 * <ol>
 *   <li>从MappedStatement ID推断实体类</li>
 *   <li>从FieldPermissionConfig获取当前用户的可见字段</li>
 *   <li>使用正则替换修改SQL的SELECT子句</li>
 *   <li>通过反射将修改后的SQL写回BoundSql</li>
 * </ol>
 * </p>
 * <p>
 * 不拦截的场景：
 * <ul>
 *   <li>超级管理员（查询所有字段）</li>
 *   <li>无法推断实体类的查询</li>
 *   <li>SQL修改失败时降级为不修改</li>
 * </ul>
 * </p>
 *
 * @author shy
 * @since 2026-05-14
 */
public class FieldPermissionInterceptor implements InnerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FieldPermissionInterceptor.class);

    /**
     * 匹配 SELECT ... FROM 的正则模式（不区分大小写）
     * 支持：SELECT * FROM、SELECT col1, col2 FROM、SELECT t.* FROM
     */
    private static final Pattern SELECT_FROM_PATTERN =
            Pattern.compile("(?i)(SELECT\\s+)(.*?)(\\s+FROM\\s+)", Pattern.DOTALL);

    /**
     * BoundSql.sql 字段的反射引用（用于修改私有字段）
     */
    private static volatile Field boundSqlField;

    /**
     * Mapper类名 → 实体类 的缓存映射
     */
    private final Map<String, Class<?>> mapperEntityCache = new ConcurrentHashMap<>();

    /**
     * 字段权限配置引擎
     */
    private final FieldPermissionConfig fieldPermissionConfig;

    /**
     * 构造方法
     *
     * @param fieldPermissionConfig 字段权限配置引擎
     */
    public FieldPermissionInterceptor(FieldPermissionConfig fieldPermissionConfig) {
        this.fieldPermissionConfig = fieldPermissionConfig;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter,
                            RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {

        // 1. 判断是否需要拦截
        if (!needIntercept()) {
            return;
        }

        // 2. 推断实体类
        Class<?> entityClass = resolveEntityClass(ms.getId());
        if (entityClass == null) {
            return;
        }

        // 3. 获取可见字段
        List<String> visibleFields = fieldPermissionConfig.getVisibleFields(entityClass);
        if (visibleFields == null || visibleFields.isEmpty()) {
            return;
        }

        // 4. 转换为数据库列名
        List<String> visibleColumns = visibleFields.stream()
                .map(this::camelToUnderscore)
                .collect(Collectors.toList());

        // 5. 修改SQL
        String originalSql = boundSql.getSql();
        String modifiedSql = modifySelectColumns(originalSql, visibleColumns);

        if (modifiedSql != null && !modifiedSql.equals(originalSql)) {
            setBoundSql(boundSql, modifiedSql);
            log.debug("[字段权限拦截器] SQL裁剪完成: entity={}, visibleFields={}", entityClass.getSimpleName(), visibleFields);
        }
    }

    /**
     * 判断是否需要拦截
     * 超级管理员不需要拦截（查询所有字段）
     */
    private boolean needIntercept() {
        try {
            if (!UserContext.isLogin()) {
                return true;
            }
            // 超级管理员不限制字段
            return !UserContext.isSuperAdmin() && !UserContext.getCurrentRoles().contains("超级管理员");
        } catch (Exception e) {
            // 未登录时需要拦截（只返回PUBLIC字段）
            return true;
        }
    }

    /**
     * 从MappedStatement ID推断实体类
     * 示例：com.shy.nexusix.tenant.mapper.SysTenantMapper.selectList → SysTenant
     */
    private Class<?> resolveEntityClass(String msId) {
        String mapperClassName = msId.substring(0, msId.lastIndexOf("."));

        Class<?> cached = mapperEntityCache.get(mapperClassName);
        if (cached != null) {
            return cached;
        }

        // 通过命名约定推断实体类：mapper包 → entity包，去掉Mapper后缀
        String entityClassName = mapperClassName
                .replace(".mapper.", ".entity.")
                .replaceAll("Mapper$", "");

        try {
            Class<?> entityClass = Class.forName(entityClassName);
            mapperEntityCache.put(mapperClassName, entityClass);
            return entityClass;
        } catch (ClassNotFoundException e) {
            log.debug("[字段权限拦截器] 无法推断实体类: mapper={}", mapperClassName);
            return null;
        }
    }

    /**
     * 修改SQL的SELECT子句，只保留可见字段
     * 使用正则替换，支持 SELECT * 和 SELECT col1, col2 两种格式
     *
     * @param sql            原始SQL
     * @param visibleColumns 可见的数据库列名列表
     * @return 修改后的SQL，替换失败返回null
     */
    private String modifySelectColumns(String sql, List<String> visibleColumns) {
        if (sql == null || visibleColumns == null || visibleColumns.isEmpty()) {
            return null;
        }

        Matcher matcher = SELECT_FROM_PATTERN.matcher(sql);
        if (!matcher.find()) {
            log.debug("[字段权限拦截器] SQL不匹配SELECT...FROM模式，跳过裁剪");
            return null;
        }

        // 构建新的SELECT列列表
        String newColumns = String.join(", ", visibleColumns);
        String originalColumns = matcher.group(2).trim();

        // 如果原SQL已经是精确列选择，需要与可见列取交集
        if (!"*".equals(originalColumns) && !originalColumns.contains("*")) {
            newColumns = intersectColumns(originalColumns, visibleColumns);
            if (newColumns == null || newColumns.isEmpty()) {
                log.warn("[字段权限拦截器] 列交集为空，降级为可见列: visibleColumns={}", visibleColumns);
                newColumns = String.join(", ", visibleColumns);
            }
        }

        // 替换SELECT子句
        String modifiedSql = matcher.replaceFirst("$1" + newColumns + "$3");

        log.debug("[字段权限拦截器] SELECT替换: [{}] → [{}]", originalColumns, newColumns);
        return modifiedSql;
    }

    /**
     * 将原SQL中的列与可见列取交集
     * 保留原SQL中的列顺序，只过滤掉不可见的列
     *
     * @param originalColumns 原SQL中的列（逗号分隔）
     * @param visibleColumns  可见列集合
     * @return 交集列（逗号分隔）
     */
    private String intersectColumns(String originalColumns, List<String> visibleColumns) {
        Set<String> visibleSet = new HashSet<>(visibleColumns);
        List<String> result = new ArrayList<>();

        for (String col : originalColumns.split(",")) {
            String trimmed = col.trim().toLowerCase();
            // 去掉表别名前缀（如 t.id → id）
            if (trimmed.contains(".")) {
                trimmed = trimmed.substring(trimmed.lastIndexOf(".") + 1);
            }
            if (visibleSet.contains(trimmed)) {
                result.add(col.trim());
            }
        }

        return String.join(", ", result);
    }

    /**
     * 通过反射修改BoundSql中的SQL语句
     * BoundSql.sql是private final字段，需要反射修改
     *
     * @param boundSql   原始BoundSql
     * @param modifiedSql 修改后的SQL
     */
    private void setBoundSql(BoundSql boundSql, String modifiedSql) {
        try {
            if (boundSqlField == null) {
                // 延迟初始化反射字段
                Field field = BoundSql.class.getDeclaredField("sql");
                field.setAccessible(true);
                boundSqlField = field;
            }
            boundSqlField.set(boundSql, modifiedSql);
        } catch (Exception e) {
            // 反射修改失败时记录警告，不影响查询执行
            log.warn("[字段权限拦截器] 修改BoundSql失败，字段权限未生效: {}", e.getMessage());
        }
    }

    /**
     * 驼峰命名转下划线命名
     * 例如：tenantName → tenant_name
     */
    private String camelToUnderscore(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

}
