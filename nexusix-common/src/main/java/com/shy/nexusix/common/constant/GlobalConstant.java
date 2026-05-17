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
         * Session键：用户ID
         * <p>使用场景：Sa-Token Session中存储当前用户的ID</p>
         */
        public static final String USER_ID = "userId";

        /**
         * Session键：用户昵称
         * <p>使用场景：Sa-Token Session中存储当前用户的昵称</p>
         */
        public static final String NICK_NAME = "nickName";

        /**
         * Session键：用户名称
         * <p>使用场景：Sa-Token Session中存储当前用户的名称</p>
         */
        public static final String USER_NAME = "userName";

        /**
         * Session键：用户权限
         * <p>使用场景：Sa-Token Session中存储当前用户的权限</p>
         */
        public static final String USER_PERM = "userPerm";

        /**
         * Session键：用户角色
         * <p>使用场景：Sa-Token Session中存储当前用户的角色</p>
         */
        public static final String USER_ROLE = "userRole";

        /**
         * Session键：租户ID
         * <p>使用场景：Sa-Token Session中存储当前用户绑定的租户ID</p>
         */
        public static final String TENANT_ID = "tenantId";

        /**
         * Session键：租户名称
         * <p>使用场景：Sa-Token Session中存储当前用户绑定的租户名称</p>
         */
        public static final String TENANT_NAME = "tenantName";

        /**
         * Session键：有效权限[系统级]
         * <p>使用场景：Sa-Token Session中存储当前用户绑定的系统级有效权限</p>
         */
        public static final String VALID_PERM_SYSTEM = "validPermSystem";

        /**
         * Session键：有效权限[租户级]
         * <p>使用场景：Sa-Token Session中存储当前用户绑定的租户级有效权限</p>
         */
        public static final String VALID_PERM_TENANT = "validPermTenant";

        /**
         * Session键：有效权限[角色级]
         * <p>使用场景：Sa-Token Session中存储当前用户绑定的角色级有效权限</p>
         */
        public static final String VALID_PERM_ROLE = "validPermRole";

        /**
         * Session键：有效权限[用户级]
         * <p>使用场景：Sa-Token Session中存储当前用户绑定的用户级有效权限</p>
         */
        public static final String VALID_PERM_USER = "validPermUser";

        /**
         * Session键：禁用权限[租户级]
         * <p>使用场景：Sa-Token Session中存储当前用户被禁用的租户级权限</p>
         */
        public static final String INVALID_PERM_TENANT = "invalidPermTenant";

        /**
         * Session键：禁用权限[角色级]
         * <p>使用场景：Sa-Token Session中存储当前用户被禁用的角色级权限</p>
         */
        public static final String INVALID_PERM_ROLE = "invalidPermRole";

        /**
         * Session键：禁用权限[用户级]
         * <p>使用场景：Sa-Token Session中存储当前用户被禁用的用户级权限</p>
         */
        public static final String INVALID_PERM_USER = "invalidPermUser";

        /**
         * Session键：有效角色[租户级]
         * <p>使用场景：Sa-Token Session中存储当前用户绑定的租户级有效角色</p>
         */
        public static final String VALID_ROLE_TENANT = "validRoleTenant";

        /**
         * Session键：有效角色[用户级]
         * <p>使用场景：Sa-Token Session中存储当前用户绑定的用户级有效角色</p>
         */
        public static final String VALID_ROLE_USER = "validRoleUser";

        /**
         * Session键：禁用角色[租户级]
         * <p>使用场景：Sa-Token Session中存储当前用户被禁用的租户级角色</p>
         */
        public static final String INVALID_ROLE_TENANT = "invalidRoleTenant";

        /**
         * Session键：禁用角色[用户级]
         * <p>使用场景：Sa-Token Session中存储当前用户被禁用的用户级角色</p>
         */
        public static final String INVALID_ROLE_USER = "invalidRoleUser";

    }

    /**
     *  Redis键常量
     */
    public static final class RedisKey {

        private RedisKey() {
        }

        /**
         * 可操作字段集合
         */
        public static final String OPERABLE_COLUMNS = "operableColumns";

        /**
         * 不可操作字段集合
         */
        public static final String UN_OPERABLE_COLUMNS = "unOperableColumns";

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
     *  字段操作
     */
    public static final class OperableType {

        private OperableType() {
        }

        /**
         * 查询类型
         */
        public static final String QUERY_TYPE = "query";

        /**
         * 新增类型
         */
        public static final String CREATE_TYPE = "create";

        /**
         * 修改类型
         */
        public static final String UPDATE_TYPE = "update";

    }

}
