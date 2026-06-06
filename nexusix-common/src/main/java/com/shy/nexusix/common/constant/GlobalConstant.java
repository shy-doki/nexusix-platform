package com.shy.nexusix.common.constant;

/**
 * <p>
 * 全局常量定义 (默认分页大小、状态码常量)
 * </p>
 * <p>
 * 该类定义了系统中通用的常量，包括分页参数、HTTP状态码、通用状态标记、
 * 租户相关常量以及各业务模块的状态码常量。所有常量均采用全大写字母命名，
 * 单词间用下划线分隔，符合Java编码规范。
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public class GlobalConstant {

    private GlobalConstant() {
    }

    /**
     *  分页相关常量 
     */
    public static final class Page {
        
        private Page() {
        }

        /**
         * 默认页码
         * <p>取值范围：正整数，从1开始</p>
         * <p>使用场景：分页查询时未指定页码时的默认值</p>
         */
        public static final int DEFAULT_PAGE_NUM = 1;

        /**
         * 默认每页大小
         * <p>取值范围：正整数，建议10-50之间</p>
         * <p>使用场景：分页查询时未指定每页大小时的默认值</p>
         */
        public static final int DEFAULT_PAGE_SIZE = 10;

        /**
         * 最小每页大小
         * <p>取值范围：正整数，用于防止一次查询过多数据</p>
         * <p>使用场景：限制分页查询时每页最大记录数，防止内存溢出</p>
         */
        public static final int MIN_PAGE_SIZE = 1;

        /**
         * 最大每页大小
         * <p>取值范围：正整数，用于防止一次查询过多数据</p>
         * <p>使用场景：限制分页查询时每页最大记录数，防止内存溢出</p>
         */
        public static final int MAX_PAGE_SIZE = 100;

    }

    /**
     *  HTTP状态码常量 
     */
    public static final class HttpStatus {
        
        private HttpStatus() {
        }

        /**
         * 操作成功
         * <p>取值范围：HTTP标准状态码</p>
         * <p>使用场景：请求处理成功时返回</p>
         */
        public static final int SUCCESS = 200;

        /**
         * 未授权
         * <p>取值范围：HTTP标准状态码</p>
         * <p>使用场景：用户未登录或Token失效时返回</p>
         */
        public static final int UNAUTHORIZED = 401;

        /**
         * 禁止访问
         * <p>取值范围：HTTP标准状态码</p>
         * <p>使用场景：用户无权限访问该资源时返回</p>
         */
        public static final int FORBIDDEN = 403;

        /**
         * 资源不存在
         * <p>取值范围：HTTP标准状态码</p>
         * <p>使用场景：请求的资源不存在时返回</p>
         */
        public static final int NOT_FOUND = 404;

        /**
         * 服务器内部错误
         * <p>取值范围：HTTP标准状态码</p>
         * <p>使用场景：服务器处理请求时发生异常</p>
         */
        public static final int INTERNAL_SERVER_ERROR = 500;

    }

    /**
     *  Sa-Token Session键常量
     */
    public static final class Session {

        private Session() {
        }

        /**
         * 用户上下文信息
         */
        public static final String USER_CONTEXT = "userContext";

    }

    /**
     *  Redis键常量
     */
    public static final class RedisKey {

        private RedisKey() {
        }

    }

    /**
     *  数据表常量
     */
    public static final class Table {

        private Table() {
        }

        /**
         * 租户表
         */
        public static final String TENANT = "sys_tenant";

    }

    /**
     *  角色常量
     */
    public static final  class Role {

        private Role() {
        }

        /**
         * 租户表
         */
        public static final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";

    }
}
