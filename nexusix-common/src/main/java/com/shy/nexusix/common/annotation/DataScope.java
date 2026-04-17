package com.shy.nexusix.common.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 数据权限注解
 * </p>
 * <p>
 * 用于标记需要进行数据权限过滤的方法，支持自定义数据权限范围和别名配置。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 部门表的别名
     *
     * @return 部门表别名
     */
    String deptAlias() default "d";

    /**
     * 用户表的别名
     *
     * @return 用户表别名
     */
    String userAlias() default "u";

    /**
     * 权限字段名称（部门ID字段）
     *
     * @return 权限字段名
     */
    String deptIdField() default "dept_id";

    /**
     * 用户ID字段名称
     *
     * @return 用户ID字段名
     */
    String userIdField() default "user_id";

    /**
     * 是否启用数据权限
     *
     * @return true-启用，false-禁用
     */
    boolean enabled() default true;

    /**
     * 数据权限范围类型
     *
     * @return 数据权限范围
     */
    DataScopeType scopeType() default DataScopeType.AUTO;

    /**
     * 数据权限范围类型枚举
     */
    enum DataScopeType {
        AUTO("自动判断"),
        ALL("全部数据权限"),
        DEPT_AND_CHILD("本部门及以下数据权限"),
        DEPT_ONLY("本部门数据权限"),
        SELF_ONLY("仅本人数据权限"),
        CUSTOM("自定义数据权限");

        private final String description;

        DataScopeType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
