package com.shy.nexusix.common.enums;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.shy.nexusix.common.exception.BusinessException;

/**
 * <p>
 * 全局统一枚举类
 * </p>
 * <p>
 * 该类整合了系统中所有业务相关的枚举类型，采用内部枚举类的方式进行组织和管理。
 * 每个内部枚举类代表一种特定类型的枚举，确保枚举值的统一编码和规范管理。
 * </p>
 * <p>
 * <b>设计说明：</b>
 * <ul>
 *   <li>所有内部枚举类都实现 BaseEnum 接口，保证统一的规范</li>
 *   <li>使用 @EnumValue 注解标记存储到数据库的字段（code字段）</li>
 *   <li>使用 @JSONField(value = true) 注解支持 JSON 序列化</li>
 *   <li>每个内部枚举类都提供 getByCode() 和 isValidCode() 方法</li>
 * </ul>
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public final class GlobalEnum {

    private GlobalEnum() {
    }

    /**
     * <p>
     * 删除标记枚举
     * </p>
     * <p>
     * 用于表示数据的逻辑删除状态
     * </p>
     */
    public enum Deleted implements BaseEnum {
        NOT_DELETED("NOT_DELETED", "未删除"),
        DELETED("DELETED", "已删除");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        Deleted(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static Deleted getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("删除标记编码不能为空");
            }
            String trimmedCode = code.trim();
            for (Deleted e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的删除标记编码");
        }

        public static Deleted getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("删除标记描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (Deleted e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的删除标记描述");
        }

        public static Deleted getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("删除标记枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的删除标记枚举名称");
            }
        }

        public static Deleted parse(Object value) {
            if (value == null) {
                throw new BusinessException("删除标记值不能为空");
            }
            if (value instanceof Deleted) {
                return (Deleted) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("删除标记值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * <p>
     * 权限状态枚举
     * </p>
     * <p>
     * 用于表示权限的状态
     * </p>
     */
    public enum PermStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "禁用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PermStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static PermStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("权限状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PermStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限状态编码");
        }

        public static PermStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("权限状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PermStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限状态描述");
        }

        public static PermStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("权限状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的权限状态枚举名称");
            }
        }

        public static PermStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("权限状态值不能为空");
            }
            if (value instanceof PermStatus) {
                return (PermStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("权限状态值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * <p>
     * 权限策略状态枚举
     * </p>
     * <p>
     * 用于表示权限策略的服务状态
     * </p>
     */
    public enum PermPolicyStatus implements BaseEnum {
        ACTIVE("ACTIVE", "生效"),
        DISABLED_SYSTEM_LEVEL("DISABLED_SYSTEM_LEVEL", "系统级禁用"),
        DISABLED_TENANT_LEVEL("DISABLED_TENANT_LEVEL", "租户级禁用"),
        DISABLED_ROLE_LEVEL("DISABLED_ROLE_LEVEL", "角色级禁用"),
        DISABLED_USER_LEVEL("DISABLED_USER_LEVEL", "用户级禁用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PermPolicyStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static PermPolicyStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("权限策略状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PermPolicyStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限策略状态编码");
        }

        public static PermPolicyStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("权限策略状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PermPolicyStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限策略状态描述");
        }

        public static PermPolicyStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("权限策略状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的权限策略状态枚举名称");
            }
        }

        public static PermPolicyStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("权限策略状态值不能为空");
            }
            if (value instanceof PermPolicyStatus) {
                return (PermPolicyStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("权限策略状态值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * <p>
     * 权限策略目标类型枚举
     * </p>
     * <p>
     * 用于表示权限策略的目标类型
     * </p>
     */
    public enum PermPolicyTargetType implements BaseEnum {
        TENANT("TENANT", "租户"),
        ROLE("ROLE", "角色"),
        USER("USER", "用户");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PermPolicyTargetType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static PermPolicyTargetType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("权限策略目标类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PermPolicyTargetType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限策略目标类型编码");
        }

        public static PermPolicyTargetType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("权限策略目标类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PermPolicyTargetType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限策略目标类型描述");
        }

        public static PermPolicyTargetType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("权限策略目标类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的权限策略目标类型枚举名称");
            }
        }

        public static PermPolicyTargetType parse(Object value) {
            if (value == null) {
                throw new BusinessException("权限策略目标类型值不能为空");
            }
            if (value instanceof PermPolicyTargetType) {
                return (PermPolicyTargetType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("权限策略目标类型值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * <p>
     * 权限策略字段访问类型
     * </p>
     * <p>
     * 用于表示字段访问类型
     * </p>
     */
    public enum PermPolicyAccessType implements BaseEnum {
        QUERY("QUERY", "查询"),
        CREATE("CREATE", "新增"),
        UPDATE("UPDATE", "更新");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PermPolicyAccessType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static PermPolicyAccessType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("字段访问类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PermPolicyAccessType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的字段访问类型编码");
        }

        public static PermPolicyAccessType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("字段访问类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PermPolicyAccessType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的字段访问类型描述");
        }

        public static PermPolicyAccessType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("字段访问类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的字段访问类型枚举名称");
            }
        }

        public static PermPolicyAccessType parse(Object value) {
            if (value == null) {
                throw new BusinessException("字段访问类型值不能为空");
            }
            if (value instanceof PermPolicyAccessType) {
                return (PermPolicyAccessType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("字段访问类型值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * <p>
     * 租户状态枚举
     * </p>
     * <p>
     * 用于表示租户的服务状态
     * </p>
     */
    public enum TenantStatus implements BaseEnum {
        PENDING("PENDING", "待审核"),
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用"),
        EXPIRED("EXPIRED", "过期");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        TenantStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static TenantStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("租户状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (TenantStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的租户状态编码");
        }

        public static TenantStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("租户状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (TenantStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的租户状态描述");
        }

        public static TenantStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("租户状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的租户状态枚举名称");
            }
        }

        public static TenantStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("租户状态值不能为空");
            }
            if (value instanceof TenantStatus) {
                return (TenantStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("租户状态值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }

        /**
         * 校验状态转换是否合法
         * <p>合法的状态转换规则：</p>
         * <ul>
         *   <li>PENDING → ENABLED（审核通过）</li>
         *   <li>PENDING → DISABLED（审核拒绝）</li>
         *   <li>ENABLED → DISABLED（停用）</li>
         *   <li>DISABLED → ENABLED（恢复/续费）</li>
         *   <li>ENABLED → EXPIRED（过期）</li>
         *   <li>EXPIRED → ENABLED（续费）</li>
         * </ul>
         *
         * @param currentStatus 当前状态编码
         * @param targetStatus  目标状态编码
         * @return 是否为合法的状态转换
         */
        public static boolean isValidTransition(String currentStatus, String targetStatus) {
            if (currentStatus == null || targetStatus == null) return false;
            if (currentStatus.equals(targetStatus)) return false;
            // PENDING → ENABLED 或 DISABLED
            if (PENDING.getCode().equals(currentStatus)) {
                return ENABLED.getCode().equals(targetStatus) || DISABLED.getCode().equals(targetStatus);
            }
            // ENABLED → DISABLED 或 EXPIRED
            if (ENABLED.getCode().equals(currentStatus)) {
                return DISABLED.getCode().equals(targetStatus) || EXPIRED.getCode().equals(targetStatus);
            }
            // DISABLED → ENABLED
            if (DISABLED.getCode().equals(currentStatus)) {
                return ENABLED.getCode().equals(targetStatus);
            }
            // EXPIRED → ENABLED
            if (EXPIRED.getCode().equals(currentStatus)) {
                return ENABLED.getCode().equals(targetStatus);
            }
            return false;
        }
    }

    /**
     * <p>
     * 订阅状态枚举
     * </p>
     * <p>
     * 用于表示租户套餐订阅的状态
     * </p>
     */
    public enum SubscriptionStatus implements BaseEnum {
        ACTIVE("ACTIVE", "生效中"),
        EXPIRED("EXPIRED", "已过期"),
        CANCELLED("CANCELLED", "已取消"),
        PENDING("PENDING", "待生效");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        SubscriptionStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static SubscriptionStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("订阅状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (SubscriptionStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的订阅状态编码");
        }

        public static SubscriptionStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("订阅状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (SubscriptionStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的订阅状态描述");
        }

        public static SubscriptionStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("订阅状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的订阅状态枚举名称");
            }
        }

        public static SubscriptionStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("订阅状态值不能为空");
            }
            if (value instanceof SubscriptionStatus) {
                return (SubscriptionStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("订阅状态值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * <p>
     * 订阅类型枚举
     * </p>
     * <p>
     * 用于表示订阅的来源类型
     * </p>
     */
    public enum SubscriptionType implements BaseEnum {
        NEW("NEW", "新订阅"),
        RENEWAL("RENEWAL", "续费"),
        UPGRADE("UPGRADE", "升级"),
        DOWNGRADE("DOWNGRADE", "降级");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        SubscriptionType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static SubscriptionType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("订阅类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (SubscriptionType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的订阅类型编码");
        }

        public static SubscriptionType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("订阅类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (SubscriptionType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的订阅类型描述");
        }

        public static SubscriptionType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("订阅类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的订阅类型枚举名称");
            }
        }

        public static SubscriptionType parse(Object value) {
            if (value == null) {
                throw new BusinessException("订阅类型值不能为空");
            }
            if (value instanceof SubscriptionType) {
                return (SubscriptionType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("订阅类型值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * <p>
     * 用户默认租户枚举
     * </p>
     * <p>
     * 用于表示用户默认租户
     * </p>
     */
    public enum DefaultTenant implements BaseEnum {
        DEFAULT(true, "默认"),
        SECONDARY_DEFAULT(false, "次默认");

        @EnumValue
        @JSONField(value = true)
        private final Boolean code;
        private final String desc;

        DefaultTenant(Boolean code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code != null ? code.toString() : null;
        }

        public Boolean getBooleanCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static DefaultTenant getByCode(Boolean code) {
            if (code == null) {
                throw new BusinessException("用户默认租户编码不能为空");
            }
            for (DefaultTenant e : values()) {
                if (e.getBooleanCode().equals(code)) {
                    return e;
                }
            }
            throw new BusinessException("无效的用户默认租户编码");
        }

        public static DefaultTenant getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("用户默认租户描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (DefaultTenant e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的用户默认租户描述");
        }

        public static DefaultTenant getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("用户默认租户枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的用户默认租户枚举名称");
            }
        }

        public static DefaultTenant parse(Object value) {
            if (value == null) {
                throw new BusinessException("用户默认租户值不能为空");
            }
            if (value instanceof DefaultTenant) {
                return (DefaultTenant) value;
            }
            if (value instanceof Boolean) {
                return getByCode((Boolean) value);
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("用户默认租户值不能为空字符串");
            }
            try {
                return getByName(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    Boolean boolValue = Boolean.parseBoolean(strValue);
                    return getByCode(boolValue);
                }
            }
        }

        public static boolean isValidCode(Boolean code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

}
