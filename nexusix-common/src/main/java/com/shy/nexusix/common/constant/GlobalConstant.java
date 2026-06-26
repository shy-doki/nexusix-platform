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

        // 用户表
        public static final String USER = "sys_user";

        // 角色表
        public static final String ROLE = "sys_role";

        // 权限表
        public static final String PERM = "sys_perm";

        // 权限策略表
        public static final String PERM_POLICY = "sys_perm_policy";

        // 用户策略表
        public static final String USER_POLICY = "sys_user_policy";

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
         * 租户树形查询业务必需的数据库字段（不受用户字段权限限制）
         * <p>id/tenant_code/parent_id/path</p>
         */
        public static final Set<String> TREE_MANDATORY_FIELDS = Set.of(
                "id", "tenant_code", "parent_id", "path"
        );

        /**
         * 权限树形查询业务必需的数据库字段（不受用户字段权限限制）
         * <p>id/perm_code/parent_id/path</p>
         */
        public static final Set<String> PERM_TREE_MANDATORY_FIELDS = Set.of(
                "id", "perm_code", "parent_id", "path"
        );

    }
}
