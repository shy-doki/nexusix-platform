package com.shy.nexusix.common.annotation;

import com.shy.nexusix.common.enums.SecurityLevel;

import java.lang.annotation.*;

/**
 * <p>
 * 字段安全级别注解 - 标注在Entity字段上，定义该字段的查看权限级别
 * </p>
 * <p>
 * 优先级：@FieldSecurityLevel注解 > 例外配置 > 默认规则推导
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * public class SysTenant {
 *     // 公开字段（无需注解，默认PUBLIC）
 *     private String tenantName;
 *
 *     // 内部字段（管理员及以上可见）
 *     @FieldSecurityLevel(SecurityLevel.INTERNAL)
 *     private String tenantCode;
 *
 *     // 机密字段（仅超级管理员可见）
 *     @FieldSecurityLevel(SecurityLevel.CONFIDENTIAL)
 *     private String isDeleted;
 * }
 * </pre>
 * </p>
 *
 * @author shy
 * @since 2026-05-14
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldSecurityLevel {

    /**
     * 字段安全级别
     *
     * @return 安全级别枚举，默认PUBLIC
     */
    SecurityLevel value() default SecurityLevel.PUBLIC;

}
