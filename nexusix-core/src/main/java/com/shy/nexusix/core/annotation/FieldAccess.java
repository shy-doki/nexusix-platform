package com.shy.nexusix.core.annotation;

import java.lang.annotation.*;

/**
 * 字段访问控制注解
 * 用于标记需要进行字段级权限控制的方法，支持查询、新增、更新场景
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldAccess {

    /**
     * 允许查看该字段的权限标识，默认忽略该校验
     */
    String viewablePerm() default "ignore";

    /**
     * 允许在新增时传入该字段的权限标识，默认忽略该校验
     */
    String creatablePerm() default "ignore";

    /**
     * 允许在更新时传入该字段的权限标识，默认忽略该校验
     */
    String updatablePerm() default "ignore";

}
