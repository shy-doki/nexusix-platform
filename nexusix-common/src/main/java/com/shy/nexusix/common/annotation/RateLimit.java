package com.shy.nexusix.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>接口限流注解，标记需要进行限流保护的接口</p>
 *
 * @author shy
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * <p>限流键前缀</p>
     *
     * @return 键前缀
     */
    String key() default "rate_limit";

    /**
     * <p>限流时间窗口内允许的最大请求数</p>
     *
     * @return 最大请求数
     */
    int limit() default 100;

    /**
     * <p>限流时间窗口大小</p>
     *
     * @return 时间窗口大小
     */
    long period() default 1;

    /**
     * <p>时间单位</p>
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * <p>限流类型</p>
     *
     * @return 限流类型
     */
    LimitType limitType() default LimitType.IP;

    /**
     * <p>限流提示消息</p>
     *
     * @return 提示消息
     */
    String message() default "访问过于频繁，请稍后再试";

    /**
     * <p>限流类型枚举</p>
     */
    enum LimitType {
        /** 按IP限流 */
        IP("按IP限流"),
        /** 按用户限流 */
        USER("按用户限流"),
        /** 全局限流 */
        GLOBAL("全局限流"),
        /** 按IP和用户组合限流 */
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
