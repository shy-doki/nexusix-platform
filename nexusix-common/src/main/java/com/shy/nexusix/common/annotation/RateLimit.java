package com.shy.nexusix.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 接口限流注解
 * </p>
 * <p>
 * 用于标记需要进行限流保护的接口，支持基于IP、用户、全局限流策略。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流键前缀
     *
     * @return 键前缀
     */
    String key() default "rate_limit";

    /**
     * 限流时间窗口内允许的最大请求数
     *
     * @return 最大请求数
     */
    int limit() default 100;

    /**
     * 限流时间窗口大小
     *
     * @return 时间窗口大小
     */
    long period() default 1;

    /**
     * 时间单位
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 限流类型
     *
     * @return 限流类型
     */
    LimitType limitType() default LimitType.IP;

    /**
     * 限流提示消息
     *
     * @return 提示消息
     */
    String message() default "访问过于频繁，请稍后再试";

    /**
     * 限流类型枚举
     */
    enum LimitType {
        IP("按IP限流"),
        USER("按用户限流"),
        GLOBAL("全局限流"),
        IP_AND_USER("按IP和用户组合限流");

        private final String description;

        LimitType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
