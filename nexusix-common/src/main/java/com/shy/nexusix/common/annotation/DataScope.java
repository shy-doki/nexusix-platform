package com.shy.nexusix.common.annotation;

import java.lang.annotation.*;

/**
 * <p>数据权限注解，标记需要进行数据权限过滤的方法</p>
 *
 * @author shy
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * <p>部门表别名</p>
     *
     * @return 部门表别名
     */
    String deptAlias() default "d";

    /**
     * <p>用户表别名</p>
     *
     * @return 用户表别名
     */
    String userAlias() default "u";

    /**
     * <p>权限字段名称（部门ID字段）</p>
     *
     * @return 权限字段名
     */
    String deptIdField() default "dept_id";

    /**
     * <p>用户ID字段名称</p>
     *
     * @return 用户ID字段名
     */
    String userIdField() default "user_id";

    /**
     * <p>是否启用数据权限</p>
     *
     * @return true-启用，false-禁用
     */
    boolean enabled() default true;

    /**
     * <p>数据权限范围类型</p>
     *
     * @return 数据权限范围
     */
    DataScopeType scopeType() default DataScopeType.AUTO;

    /**
     * <p>数据权限范围类型枚举</p>
     */
    enum DataScopeType {
        // 自动判断
        AUTO("自动判断"),
        // 全部数据权限
        ALL("全部数据权限"),
        // 本部门及以下数据权限
        DEPT_AND_CHILD("本部门及以下数据权限"),
        // 本部门数据权限
        DEPT_ONLY("本部门数据权限"),
        // 仅本人数据权限
        SELF_ONLY("仅本人数据权限"),
        // 自定义数据权限
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
