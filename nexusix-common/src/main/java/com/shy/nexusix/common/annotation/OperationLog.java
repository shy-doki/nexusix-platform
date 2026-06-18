package com.shy.nexusix.common.annotation;

import java.lang.annotation.*;

/**
 * <p>操作日志注解，标记需要记录操作日志的方法</p>
 *
 * @author shy
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * <p>操作模块标题</p>
     *
     * @return 模块标题
     */
    String title() default "";

    /**
     * <p>业务类型</p>
     *
     * @return 业务类型
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * <p>操作类型</p>
     *
     * @return 操作类型
     */
    OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * <p>是否保存请求参数</p>
     *
     * @return true-保存，false-不保存
     */
    boolean isSaveRequestData() default true;

    /**
     * <p>是否保存响应参数</p>
     *
     * @return true-保存，false-不保存
     */
    boolean isSaveResponseData() default true;

    /**
     * <p>排除指定的请求参数</p>
     *
     * @return 排除的参数名数组
     */
    String[] excludeParams() default {};

    /**
     * <p>业务类型枚举</p>
     */
    enum BusinessType {
        /** 其他 */
        OTHER("其他"),
        /** 新增 */
        INSERT("新增"),
        /** 修改 */
        UPDATE("修改"),
        /** 删除 */
        DELETE("删除"),
        /** 授权 */
        GRANT("授权"),
        /** 导出 */
        EXPORT("导出"),
        /** 导入 */
        IMPORT("导入"),
        /** 强退 */
        FORCE("强退"),
        /** 清空 */
        CLEAN("清空"),
        /** 查询 */
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
     * <p>操作类型枚举</p>
     */
    enum OperatorType {
        /** 其他 */
        OTHER("其他"),
        /** 后台用户 */
        MANAGE("后台用户"),
        /** 手机端用户 */
        MOBILE("手机端用户"),
        /** 门户用户 */
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
