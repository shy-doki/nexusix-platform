package com.shy.nexusix.common.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 防重复提交注解
 * </p>
 * <p>
 * 用于标记需要防止重复提交的接口，支持基于IP、用户、参数的防重策略。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 间隔时间（毫秒），默认5秒
     *
     * @return 间隔时间
     */
    long interval() default 5000;

    /**
     * 提示消息
     *
     * @return 提示消息
     */
    String message() default "请勿重复提交";

    /**
     * 防重类型
     *
     * @return 防重类型
     */
    SubmitType submitType() default SubmitType.USER;

    /**
     * 是否包含请求参数作为防重键的一部分
     *
     * @return true-包含，false-不包含
     */
    boolean includeParams() default true;

    /**
     * 防重类型枚举
     */
    enum SubmitType {
        IP("按IP防重"),
        USER("按用户防重"),
        IP_AND_USER("按IP和用户组合防重"),
        PARAMS("按参数防重");

        private final String description;

        SubmitType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
