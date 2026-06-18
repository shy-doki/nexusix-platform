package com.shy.nexusix.common.constant;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>全局常量定义 - 分页、状态码、Session键、Redis键、数据表、角色、字段权限</p>
 *
 * @author shy
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

        // 默认页码
        public static final int DEFAULT_PAGE_NUM = 1;

        // 默认每页大小
        public static final int DEFAULT_PAGE_SIZE = 10;

        // 最小每页大小
        public static final int MIN_PAGE_SIZE = 1;

        // 最大每页大小
        public static final int MAX_PAGE_SIZE = 100;

    }

    /**
     *  HTTP状态码常量 
     */
    public static final class HttpStatus {

        private HttpStatus() {
        }

        // 操作成功
        public static final int SUCCESS = 200;

        // 未授权
        public static final int UNAUTHORIZED = 401;

        // 禁止访问
        public static final int FORBIDDEN = 403;

        // 资源不存在
        public static final int NOT_FOUND = 404;

        // 服务器内部错误
        public static final int INTERNAL_SERVER_ERROR = 500;

    }

    /**
     *  Sa-Token Session键常量
     */
    public static final class Session {

        private Session() {
        }

        // 用户上下文信息
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

        // 租户表
        public static final String TENANT = "sys_tenant";

        // 租户策略表
        public static final String TENANT_POLICY = "sys_tenant_policy";

    }

    /**
     *  角色常量
     */
    public static final  class Role {

        private Role() {
        }

        // 超级管理员角色编码
        public static final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";

    }

    /**
     *  字段权限常量
     */
    public static final class FieldPerm {

        private FieldPerm() {
        }

        /**
         * 树形查询业务必需的数据库字段（不受用户字段权限限制）
         * <p>id/tenant_code/parent_id/path</p>
         */
        public static final Set<String> TREE_MANDATORY_FIELDS = Set.of(
                "id", "tenant_code", "parent_id", "path"
        );

        /**
         * DB列名到VO字段名的映射关系
         * <p>用于返回前端时根据用户可见字段过滤VO数据，一个DB列可映射多个VO字段</p>
         */
        public static final Map<String, List<String>> DB_COLUMN_TO_VO_FIELDS = Map.ofEntries(
                Map.entry("tenant_code", List.of("tenantCode")),
                Map.entry("tenant_name", List.of("tenantName")),
                Map.entry("tenant_type", List.of("tenantType")),
                Map.entry("parent_name", List.of("parentName")),
                Map.entry("contact_name", List.of("contactName")),
                Map.entry("contact_phone", List.of("contactPhone")),
                Map.entry("status", List.of("status")),
                Map.entry("expire_time", List.of("expireTime")),
                Map.entry("has_children", List.of("hasChildren")),
                Map.entry("package_name", List.of("packageName")),
                Map.entry("create_by", List.of("createByName", "createByCode")),
                Map.entry("create_at", List.of("createTime")),
                Map.entry("update_by", List.of("updateByName", "updateByCode")),
                Map.entry("update_at", List.of("updateTime")),
                Map.entry("is_deleted", List.of("isDeleted")),
                Map.entry("deleted_at", List.of("deleteTime")),
                Map.entry("tenant_desc", List.of("tenantDesc")),
                Map.entry("tenant_logo_url", List.of("tenantLogoUrl")),
                Map.entry("path", List.of("path")),
                Map.entry("ext_attributes", List.of("extAttributes")),
                Map.entry("parent_id", List.of("parentCode")),
                Map.entry("disable_reason", List.of("disableReason"))
        );

    }
}
