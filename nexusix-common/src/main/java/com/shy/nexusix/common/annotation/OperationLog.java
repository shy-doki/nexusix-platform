package com.shy.nexusix.common.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 操作日志注解
 * </p>
 * <p>
 * 用于标记需要记录操作日志的方法，支持自定义日志标题、业务类型和操作类型。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作模块标题
     *
     * @return 模块标题
     */
    String title() default "";

    /**
     * 业务类型
     *
     * @return 业务类型
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作类型
     *
     * @return 操作类型
     */
    OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求参数
     *
     * @return true-保存，false-不保存
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应参数
     *
     * @return true-保存，false-不保存
     */
    boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     *
     * @return 排除的参数名数组
     */
    String[] excludeParams() default {};

    /**
     * 业务类型枚举
     */
    enum BusinessType {
        OTHER("其他"),
        INSERT("新增"),
        UPDATE("修改"),
        DELETE("删除"),
        GRANT("授权"),
        EXPORT("导出"),
        IMPORT("导入"),
        FORCE("强退"),
        CLEAN("清空"),
        SEARCH("查询");

        private final String description;

        BusinessType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 操作类型枚举
     */
    enum OperatorType {
        OTHER("其他"),
        MANAGE("后台用户"),
        MOBILE("手机端用户"),
        PORTAL("门户用户");

        private final String description;

        OperatorType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
