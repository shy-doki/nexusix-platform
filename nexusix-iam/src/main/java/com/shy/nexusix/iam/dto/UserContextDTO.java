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
        private List<String> perms;
        private List<String> validPerms;
        private List<String> invalidPerms;
        private CascadeDisabled cascadeDisabled;
        private FieldPerm fieldPerm;
    }

    @Data
    public static class CascadeDisabled {
        private List<String> systemDisabled;
        private List<String> tenantDisabled;
        private List<String> roleDisabled;
        private List<String> userDisabled;
    }

    @Data
    public static class FieldPerm {
        private Map<String, EntityFieldPerm> query;
        private Map<String, EntityFieldPerm> create;
        private Map<String, EntityFieldPerm> update;
    }

    @Data
    public static class EntityFieldPerm {
        private List<String> visibleFields;
        private List<String> invisibleFields;
    }

}
