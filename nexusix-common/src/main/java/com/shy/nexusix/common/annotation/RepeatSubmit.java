package com.shy.nexusix.common.annotation;

import java.lang.annotation.*;

/**
 * <p>防重复提交注解，标记需要防止重复提交的接口</p>
 *
 * @author shy
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * <p>间隔时间（毫秒），默认5秒</p>
     *
     * @return 间隔时间
     */
    long interval() default 5000;

    /**
     * <p>提示消息</p>
     *
     * @return 提示消息
     */
    String message() default "请勿重复提交";

    /**
     * <p>防重类型</p>
     *
     * @return 防重类型
     */
    SubmitType submitType() default SubmitType.USER;

    /**
     * <p>是否包含请求参数作为防重键的一部分</p>
     *
     * @return true-包含，false-不包含
     */
    boolean includeParams() default true;

    /**
     * <p>防重类型枚举</p>
     */
    enum SubmitType {
        /** 按IP防重 */
        IP("按IP防重"),
        /** 按用户防重 */
        USER("按用户防重"),
        /** 按IP和用户组合防重 */
        IP_AND_USER("按IP和用户组合防重"),
        /** 按参数防重 */
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
