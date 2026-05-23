package com.shy.nexusix.iam.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserContextDTO {

    /**
     * 租户信息
     */
    private TenantInfo tenantInfo;

    /**
     * 权限信息
     */
    private PermInfo permInfo;

    @Data
    public static class TenantInfo {
        /**
         * 租户编码
         */
        private String tenantCode;
        /**
         * 租户名称
         */
        private String tenantName;
    }

    @Data
    public static class PermInfo {
        /**
         * 权限列表
         */
        private List<String> perms;
        /**
         * 有效权限列表
         */
        private List<String> validPerms;
        /**
         * 无效权限映射（permCode → 禁用层级状态码，如 DISABLED_SYSTEM_LEVEL）
         */
        private Map<String, String> invalidPerms;
        /**
         * 可查询的字段
         */
        private Map<String, EntityFieldPerm> query;
        /**
         * 可创建的字段
         */
        private Map<String, EntityFieldPerm> create;
        /**
         * 可修改的字段
         */
        private Map<String, EntityFieldPerm> update;
    }

    @Data
    public static class EntityFieldPerm {
        /**
         * 可访问字段列表
         */
        private List<String> visibleFields;
        /**
         * 不可访问字段映射（fieldName → 禁用层级状态码，如 DISABLED_TENANT_LEVEL）
         */
        private Map<String, String> invisibleFields;
    }

}
