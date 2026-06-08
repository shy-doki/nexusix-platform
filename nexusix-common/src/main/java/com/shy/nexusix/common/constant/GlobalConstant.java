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

    /**
     *  字段权限常量
     */
    public static final class FieldPerm {

        private FieldPerm() {
        }

        /**
         * 树形查询业务必需的数据库字段
         * <p>这些字段在构建树形结构时必须从数据库查询，不受用户字段权限限制：</p>
         * <ul>
         *   <li>id - 构建idToCodeMap，将parentId转为tenantCode</li>
         *   <li>tenant_code - 构建idToCodeMap和codeToTreeVOMap，O(1)查找节点</li>
         *   <li>parent_id - 判断根节点，查找父租户关联</li>
         *   <li>path - path前缀匹配查询子节点，排序</li>
         * </ul>
         */
        public static final java.util.Set<String> TREE_MANDATORY_FIELDS = java.util.Set.of(
                "id", "tenant_code", "parent_id", "path"
        );

        /**
         * DB列名到VO字段名的映射关系
         * <p>用于在返回前端时根据用户可见字段过滤VO数据</p>
         * <p>一个DB列可能映射到多个VO字段（如create_by同时映射createByName和createByCode）</p>
         */
        public static final java.util.Map<String, java.util.List<String>> DB_COLUMN_TO_VO_FIELDS = java.util.Map.ofEntries(
                java.util.Map.entry("tenant_code", java.util.List.of("tenantCode")),
                java.util.Map.entry("tenant_name", java.util.List.of("tenantName")),
                java.util.Map.entry("tenant_type", java.util.List.of("tenantType")),
                java.util.Map.entry("parent_name", java.util.List.of("parentName")),
                java.util.Map.entry("contact_name", java.util.List.of("contactName")),
                java.util.Map.entry("contact_phone", java.util.List.of("contactPhone")),
                java.util.Map.entry("status", java.util.List.of("status")),
                java.util.Map.entry("expire_time", java.util.List.of("expireTime")),
                java.util.Map.entry("has_children", java.util.List.of("hasChildren")),
                java.util.Map.entry("package_name", java.util.List.of("packageName")),
                java.util.Map.entry("create_by", java.util.List.of("createByName", "createByCode")),
                java.util.Map.entry("create_at", java.util.List.of("createTime")),
                java.util.Map.entry("update_by", java.util.List.of("updateByName", "updateByCode")),
                java.util.Map.entry("update_at", java.util.List.of("updateTime")),
                java.util.Map.entry("is_deleted", java.util.List.of("isDeleted")),
                java.util.Map.entry("deleted_at", java.util.List.of("deleteTime")),
                java.util.Map.entry("tenant_desc", java.util.List.of("tenantDesc")),
                java.util.Map.entry("tenant_logo_url", java.util.List.of("tenantLogoUrl")),
                java.util.Map.entry("path", java.util.List.of("path")),
                java.util.Map.entry("ext_attributes", java.util.List.of("extAttributes")),
                java.util.Map.entry("parent_id", java.util.List.of("parentCode"))
        );

    }
}
